package nl.fontys.exercise.recorder;

import android.hardware.Sensor;

public interface MeasurementCollector {
    /**
     * Starts the collecting process. Time is zero.
     */
    void startCollecting();

    /**
     * Stops the collecting process
     * @param time time since start
     */
    void stopCollecting(double time);

    /**
     * Collects a measurement
     * @param sensor Measurement source sensor
     * @param time Time since collecting started
     * @param values Values array
     * @param accuracy Accuracy reading
     */
    void collectMeasurement(Sensor sensor, double time, float[] values, int accuracy);
}
