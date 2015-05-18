package nl.fontys.exercisecontrol.exercise.recorder;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Measurement recording worker class.
 * All actions are non-blocking for the most part except initialize() and terminate() which wait
 * for the start-up and tear-down of the worker thread.
 */
public class MeasurementRecorder {

    private final Context context;
    private final SensorManager sensorManager;
    private final MeasurementCollector collector;
    private final List<MeasurementSensorData> sensorData;
    private final SensorMeasurementAdaptor adaptor;
    private final HandlerThread handlerThread;
    private MeasurementRecorderMessageHandler messageHandler = null;
    private String exerciseName;

    /**
     * Instantiate a new measurement recorder.
     * @param context Android context
     * @param sensorTypes Array of sensor types recorded
     * @param samplingRate Desired measurements per second
     * @param collector Instance of a measurement collector
     */
    public MeasurementRecorder(Context context, int[] sensorTypes, int samplingRate, MeasurementCollector collector, String exerciseName) {
        this.context = context;
        this.collector = collector;
        this.exerciseName=exerciseName;
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensorData = new ArrayList<MeasurementSensorData>();
        adaptor = new SensorMeasurementAdaptor();
        handlerThread = new HandlerThread(getClass().getSimpleName());

        for (int sensorType : sensorTypes) {
            MeasurementSensorData data = new MeasurementSensorData(sensorType, samplingRate);
            sensorData.add(data);
        }
    }

    /**
     * Initializes the measurement recorder.
     */
    public void initialize() {
        // attach default sensors to data holders
        for (MeasurementSensorData data : sensorData)
            data.setDefaultSensor(sensorManager);

        handlerThread.start();
        messageHandler = new MeasurementRecorderMessageHandler(handlerThread.getLooper());

        for (MeasurementSensorData data : sensorData) {
            if (!sensorManager.registerListener(adaptor, data.getSensor(), 1000000 / data.getSamplingRate(), messageHandler))
                throw new RuntimeException("Sensor registration failed.");
        }
    }

    /**
     * Terminates the measurement recorder.
     */
    public void terminate() {
        Message message = new Message();
        message.obj = MeasurementRecorderMessage.QUIT;
        messageHandler.sendMessage(message);
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    /**
     * Starts recording of measurements.
     */
    public void start() {
        Message message = new Message();
        message.obj = MeasurementRecorderMessage.START;
        messageHandler.sendMessage(message);
    }

    /**
     * Stops the recording of measurements.
     */
    public void stop() {
        Message message = new Message();
        message.obj = MeasurementRecorderMessage.STOP;
        messageHandler.sendMessage(message);
    }

    private class SensorMeasurementAdaptor implements SensorEventListener {

        private final Map<Sensor, MeasurementAdaptor> adaptorMap = new HashMap<Sensor, MeasurementAdaptor>();
        private long startTime = 0;

        public void onRecordingStart() throws MeasurementException {
            startTime = System.nanoTime();
            collector.startCollecting(exerciseName);

            for (MeasurementSensorData data : sensorData)
                adaptorMap.put(data.getSensor(), new MeasurementAdaptor(data, collector, startTime));
        }

        public void onRecordingStop() throws MeasurementException {
            adaptorMap.clear();
            collector.stopCollecting((double)(System.nanoTime() - startTime) / 1000000000.0);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            MeasurementAdaptor adaptor;

            if ((adaptor = adaptorMap.get(event.sensor)) == null)
                return;

            try {
                adaptor.sensorEvent(System.nanoTime(), event);
            } catch (MeasurementException ex) {
                Message message = new Message();
                message.obj = ex;
                messageHandler.sendMessage(message);
                adaptorMap.clear();
            }
        }

        public void onRecordingFailed(MeasurementException ex) {
            adaptorMap.clear();
            collector.collectionFailed(ex);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }
    }

    private class MeasurementRecorderMessageHandler extends Handler {

        boolean recording = false;

        public MeasurementRecorderMessageHandler(Looper looper) {
            super(looper);
        }

        public void quit() {
            if (recording)
                stop();
            sensorManager.unregisterListener(adaptor);
            getLooper().quitSafely();
        }

        public void start() {
            if (recording)
                return;

            try {
                adaptor.onRecordingStart();
                recording = true;
            } catch (MeasurementException ex) {
                adaptor.onRecordingFailed(ex);
            }
        }

        public void stop() {
            if (!recording)
                return;

            try {
                adaptor.onRecordingStop();
            } catch (MeasurementException ex) {
                adaptor.onRecordingFailed(ex);
            } finally {
                recording = false;
            }
        }

        public void fail(MeasurementException ex) {
            if (!recording)
                return;

            adaptor.onRecordingFailed(ex);
            recording = false;
        }

        @Override
        public void handleMessage(Message message) {
            if (message.obj instanceof MeasurementRecorderMessage) {
                MeasurementRecorderMessage msg = (MeasurementRecorderMessage)message.obj;

                switch (msg) {
                    case START: start(); break;
                    case STOP:  stop();  break;
                    case QUIT:  quit();  break;
                }
            } else if (message.obj instanceof MeasurementException) {
                MeasurementException ex = (MeasurementException)message.obj;
                fail(ex);
            }
        }
    }

    private enum MeasurementRecorderMessage {
        START, STOP, QUIT
    }
}
