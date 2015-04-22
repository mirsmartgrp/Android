package nl.fontys.exercise.recorder;

import android.hardware.SensorEvent;

/**
 * Adaptor between SensorEventListener and MeasurementCollector
 */
public class MeasurementAdaptor {

    private final MeasurementSensorData sensorData;
    private final MeasurementCollector collector;
    private final long startTime;
    private long lastFired = -1;

    public MeasurementAdaptor(MeasurementSensorData sensorData, MeasurementCollector collector, long startTime) {
        this.sensorData = sensorData;
        this.collector = collector;
        this.startTime = startTime;
    }

    public void sensorEvent(SensorEvent event) throws MeasurementException {
        //TODO: For now, just limit samples. Later, interpolate measurements maybe?
        if (lastFired >= 0) {
            long passed = event.timestamp - lastFired;

            if (passed >= 1000000 / sensorData.getSamplingRate())
                return;
        }

        collector.collectMeasurement(event.sensor, relativeToStart(event.timestamp), event.values, event.accuracy);
        lastFired = event.timestamp;
    }

    private double relativeToStart(long timestamp) {
        return (double)(timestamp - startTime) / 1000000000;
    }
}
