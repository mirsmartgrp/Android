package nl.fontys.exercisecontrol.exercise.recorder;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
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
    private final MeasurementListenerThread listenerThread;

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
        adaptor = new SensorMeasurementAdaptor();
        listenerThread = new MeasurementListenerThread();

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

        listenerThread.start();
    }

    /**
     * Terminates the measurement recorder.
     */
    public void terminate() {
        listenerThread.terminate();
    }

    /**
     * Starts recording of measurements.
     */
    public void start() {
        listenerThread.sendMessage(MeasurementListenerThreadMessage.START);
    }

    /**
     * Stops the recording of measurements.
     */
    public void stop() {
        listenerThread.sendMessage(MeasurementListenerThreadMessage.STOP);
    }

    private class SensorMeasurementAdaptor implements SensorEventListener {

        private final Map<Sensor, MeasurementAdaptor> adaptorMap = new HashMap<Sensor, MeasurementAdaptor>();
        private long startTime = 0;
        private long delay = -1;

        public void onRecordingStart() throws MeasurementException {
            startTime = System.nanoTime();
            delay = -1;
            collector.startCollecting();

            for (MeasurementSensorData data : sensorData)
                adaptorMap.put(data.getSensor(), new MeasurementAdaptor(data, collector, startTime));
        }

        public void onRecordingStop() throws MeasurementException {
            adaptorMap.clear();
            collector.stopCollecting((double)(System.nanoTime() - startTime - delay) / 1000000000);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            MeasurementAdaptor adaptor;

            if ((adaptor = adaptorMap.get(event.sensor)) == null)
                return;

            if (delay < 0)
                delay = event.timestamp - startTime;
            event.timestamp -= delay;

            try {
                adaptor.sensorEvent(event);
            } catch (MeasurementException ex) {
                listenerThread.sendMessage(ex);
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

    private class MeasurementListenerThread extends Thread {

        private volatile MeasurementListenerMessageHandler handler = null;
        private final Object sync = new Object();

        @Override
        public void start() {
            super.start();
            try {
                synchronized (sync) { sync.wait(); }
            } catch (InterruptedException e) { }
        }

        public void terminate() {
            sendMessage(MeasurementListenerThreadMessage.QUIT);
            try {
                synchronized (sync) {
                    synchronized (sync) { sync.wait(); }
                }
            } catch (InterruptedException e) { }
        }

        @Override
        public void run() {
            // associate messaging loop to this thread
            Looper.prepare();

            // create messaging handler
            handler = new MeasurementListenerMessageHandler(this);

            // notify that thread has started
            synchronized (sync) { sync.notifyAll(); }

            // start looping
            Looper.loop();

            // reset handler
            handler = null;

            // notify that thread has stopped
            synchronized (sync) { sync.notifyAll(); }
        }

        public void sendMessage(Object msg) {
            if (handler == null)
                throw new RuntimeException("Can not send message.");

            Message message = handler.obtainMessage();
            message.obj = msg;
            handler.sendMessage(message);
        }

        private class MeasurementListenerMessageHandler extends Handler {

            private final MeasurementListenerThread parentThread;
            boolean recording = false;

            public MeasurementListenerMessageHandler(MeasurementListenerThread parentThread) {
                this.parentThread = parentThread;
            }

            public void start() {
                if (recording)
                    return;

                try {
                    adaptor.onRecordingStart();
                    register();
                    recording = true;
                } catch (MeasurementException ex) {
                    adaptor.onRecordingFailed(ex);
                }
            }

            public void stop() {
                if (!recording)
                    return;

                unregister();
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

                unregister();
                adaptor.onRecordingFailed(ex);
                recording = false;
            }

            public void register() {
                for (MeasurementSensorData data : sensorData)
                    sensorManager.registerListener(adaptor, data.getSensor(), 1000000 / data.getSamplingRate(), handler);
            }

            public void unregister() {
                sensorManager.unregisterListener(adaptor);
            }

            @Override
            public void handleMessage(Message message) {
                if (message.obj instanceof MeasurementListenerThreadMessage) {
                    MeasurementListenerThreadMessage msg = (MeasurementListenerThreadMessage)message.obj;

                    switch (msg) {
                        case START: start();                    break;
                        case STOP:  stop();                     break;
                        case QUIT:  stop(); getLooper().quit();
                    }
                } else if (message.obj instanceof MeasurementException) {
                    MeasurementException ex = (MeasurementException)message.obj;
                    fail(ex);
                }
            }
        }
    }

    private static enum MeasurementListenerThreadMessage {
        START, STOP, QUIT
    }
}
