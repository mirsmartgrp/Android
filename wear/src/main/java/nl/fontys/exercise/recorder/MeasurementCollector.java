package nl.fontys.exercise.recorder;

import android.hardware.Sensor;

public interface MeasurementCollector {
    void startCollecting();
    void stopCollecting();
    void collectMeasurement(Sensor sensor, long timestamp, float[] values, int accuracy);
}
