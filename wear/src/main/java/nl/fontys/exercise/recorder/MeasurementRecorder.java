package nl.fontys.exercise.recorder;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Measurement recording worker class.
 * All actions are non-blocking for the most part except initialize() and terminate() which wait
 * for the start-up and tear-down of the worker thread.
 */
public class MeasurementRecorder {

    private final int[] sensorTypes;
    private final int measurementInterval;
    private final SensorManager sensorManager;
    private final List<Sensor> sensors;
    private final SensorMeasurementAdaptor adaptor;
    private final MeasurementListenerThread listenerThread;

    /**
     * Instantiate a new measurement recorder.
     * @param context Android context
     * @param sensorTypes Array of sensor types recorded
     * @param measurementInterval Interval of measurement in milliseconds
     * @param collector Instance of a measurement collector
     */
    public MeasurementRecorder(Context context, int[] sensorTypes, int measurementInterval, MeasurementCollector collector) {
        this.sensorTypes = sensorTypes.clone();
        this.measurementInterval = measurementInterval;
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensors = new ArrayList<Sensor>();
        adaptor = new SensorMeasurementAdaptor(collector);
        listenerThread = new MeasurementListenerThread();
    }

    /**
     * Initializes the measurement recorder.
     */
    public void initialize() {
        for (int sensorType : sensorTypes)
            sensors.add(sensorManager.getDefaultSensor(sensorType));
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

        private final MeasurementCollector collector;
        private long startTime = 0;
        private boolean blockMeasurements;

        private SensorMeasurementAdaptor(MeasurementCollector collector) {
            this.collector = collector;
        }

        public void onRecordingStart() throws MeasurementException {
            startTime = System.nanoTime();
            blockMeasurements = true;
            collector.startCollecting();
            blockMeasurements = false;
        }

        public void onRecordingStop() throws MeasurementException {
            blockMeasurements = true;
            collector.stopCollecting(reltime(System.nanoTime()));
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (blockMeasurements)
                return;

            try {
                collector.collectMeasurement(event.sensor, reltime(event.timestamp), event.values, event.accuracy);
            } catch (MeasurementException ex) {
                blockMeasurements = true;
                listenerThread.sendMessage(ex);
            }
        }

        public void onRecordingFailed(MeasurementException ex) {
            collector.collectionFailed(ex);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }

        private double reltime(long timestamp) {
            return (double)(timestamp - startTime) / 1000000000;
        }
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

            private MeasurementListenerThread parentThread;
            boolean recording = false;

            public MeasurementListenerMessageHandler(MeasurementListenerThread parentThread) {
                this.parentThread = parentThread;
            }

            public void start() {
                if (recording)
                    return;

                try {
                    adaptor.onRecordingStart();

                    for (Sensor sensor : sensors)
                        sensorManager.registerListener(adaptor, sensor, measurementInterval, handler);
                    recording = true;
                } catch (MeasurementException ex) {
                    adaptor.onRecordingFailed(ex);
                }
            }

            public void stop() {
                if (!recording)
                    return;

                sensorManager.unregisterListener(adaptor);
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

                sensorManager.unregisterListener(adaptor);
                adaptor.onRecordingFailed(ex);
                recording = false;
            }

            @Override
            public void handleMessage(Message message) {
                if (message.obj instanceof MeasurementListenerThreadMessage) {
                    MeasurementListenerThreadMessage msg = (MeasurementListenerThreadMessage)message.obj;

                    switch (msg) {
                        case START:
                            start();
                            break;
                        case STOP:
                            stop();
                            break;
                        case QUIT:
                            stop();
                            getLooper().quit();
                            break;
                    }
                } else if (message.obj instanceof MeasurementException) {
                    MeasurementException ex = (MeasurementException)message.obj;
                    fail(ex);
                }
            }
        }
    }

    private static enum MeasurementListenerThreadMessage {
        START, STOP, FAIL, QUIT
    }
}
