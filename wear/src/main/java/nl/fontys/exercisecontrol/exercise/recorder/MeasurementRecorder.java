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
    private final HandlerThread handlerThread;
    private MeasurementRecorderMessageHandler messageHandler = null;

    /**
     * Instantiate a new measurement recorder.
     * @param context Android context
     * @param sensorTypes Array of sensor types recorded
     * @param samplingRate Desired measurements per second
     * @param collector Instance of a measurement collector
     */
    public MeasurementRecorder(Context context, int[] sensorTypes, int samplingRate, MeasurementCollector collector) {
        this.context = context;
        this.collector = collector;
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensorData = new ArrayList<MeasurementSensorData>();
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
        messageHandler.initialize();
    }

    /**
     * Terminates the measurement recorder.
     */
    public void terminate() {
        Message message = new Message();
        message.obj = MeasurementRecorderMessageObject.quit();
        messageHandler.sendMessage(message);
    }

    /**
     * Starts recording of measurements.
     */
    public void start(String name) {
        Message message = new Message();
        message.obj = MeasurementRecorderMessageObject.start(name);
        messageHandler.sendMessage(message);
    }

    /**
     * Stops the recording of measurements.
     */
    public void stop() {
        Message message = new Message();
        message.obj = MeasurementRecorderMessageObject.stop();
        messageHandler.sendMessage(message);
    }


    private class MeasurementRecorderMessageHandler extends Handler {

        private final MeasurementCollectorAdaptor adaptor;
        private boolean recording = false;

        public MeasurementRecorderMessageHandler(Looper looper) {
            super(looper);
            adaptor = new MeasurementCollectorAdaptor(collector, sensorData, this);
        }

        public void initialize() {
            for (MeasurementSensorData data : sensorData) {
                sensorManager.registerListener(adaptor, data.getSensor(), 1000000 / data.getSamplingRate(), messageHandler);
            }
        }

        public void quit() {
            if (recording)
                stop();
            sensorManager.unregisterListener(adaptor);
            getLooper().quitSafely();
        }

        public void start(String name) {
            if (recording)
                return;

            try {
                adaptor.onRecordingStart(name);
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
            if (!(message.obj instanceof MeasurementRecorderMessageObject))
                return;

            MeasurementRecorderMessageObject msg = (MeasurementRecorderMessageObject)message.obj;

            switch (msg.getAction()) {
                case START: start(msg.getName()); break;
                case STOP:  stop();  break;
                case FAIL:  fail(msg.getException()); break;
                case QUIT:  quit();  break;
            }
        }
    }
}
