package nl.fontys.exercisecontrol.exercise.recorder;

import android.hardware.SensorEvent;

/**
 * Adaptor between SensorEventListener and MeasurementCollector
 */
public class MeasurementAdaptor {

    private final MeasurementSensorData sensorData;
    private final MeasurementCollector collector;
    private final long startTime;
    private final int samplingDelayUs;
    private final double samplingInterval;
    private long lastFired = -1;

    public MeasurementAdaptor(MeasurementSensorData sensorData, MeasurementCollector collector, long startTime) {
        this.sensorData = sensorData;
        this.collector = collector;
        this.startTime = startTime;
        samplingDelayUs = 1000000000 / sensorData.getSamplingRate();
        samplingInterval = 1.0 / (double)sensorData.getSamplingRate();
    }

    public void sensorEvent(long timestamp, SensorEvent event) throws MeasurementException {
        if (lastFired >= 0) {
            long passed = timestamp - lastFired;
            if (passed < samplingDelayUs) return;
        }

        collector.collectMeasurement(event.sensor, relativeToStart(timestamp), event.values, event.accuracy, samplingInterval);
        lastFired = timestamp;
    }

    private double relativeToStart(long timestamp) {
        long diff = timestamp - startTime;
        return (double)diff / 1000000000.0;
    }
}
