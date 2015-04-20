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

public class MeasurementRecorder {

    private final int[] sensorTypes;
    private final SensorManager sensorManager;
    private final List<Sensor> sensors;
    private final SensorMeasurementAdaptor adaptor;
    private final MeasurementListenerThread listenerThread;

    public MeasurementRecorder(Context context, int[] sensorTypes, MeasurementCollector collector) {
        this.sensorTypes = sensorTypes.clone();
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

        private SensorMeasurementAdaptor(MeasurementCollector collector) {
            this.collector = collector;
        }

        public void onRecordingStart() {
            collector.startCollecting();
        }

        public void onRecordingStop() {
            collector.stopCollecting();
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            collector.collectMeasurement(event.sensor, event.timestamp, event.values, event.accuracy);
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
                sync.wait();
            } catch (InterruptedException e) { }
        }

        public void terminate() {
            sendMessage(MeasurementListenerThreadMessage.QUIT);
            try {
                sync.wait();
            } catch (InterruptedException e) { }
        }

        @Override
        public void run() {
            // associate messaging loop to this thread
            Looper.prepare();

            // create messaging handler
            handler = new MeasurementListenerMessageHandler(this);

            // notify that thread has started
            sync.notifyAll();

            // start looping
            Looper.loop();

            // reset handler
            handler = null;

            // notify that thread has stopped
            sync.notifyAll();
        }

        public void sendMessage(MeasurementListenerThreadMessage msg) {
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
                    throw new RuntimeException("Already recording!");

                for (Sensor sensor : sensors)
                    sensorManager.registerListener(adaptor, sensor, SensorManager.SENSOR_DELAY_UI, handler);
                recording = true;
            }

            public void stop() {
                if (!recording)
                    throw new RuntimeException("Not recording!");

                sensorManager.unregisterListener(adaptor);
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
                            if (recording)
                                stop();
                            getLooper().quit();
                            break;
                    }
                }
            }
        }
    }

    private static enum MeasurementListenerThreadMessage {
        START, STOP, QUIT
    }
}
