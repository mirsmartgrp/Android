package nl.fontys.exercisecontrol.exercise.recorder;

import android.hardware.Sensor;

public interface MeasurementCollector {
    /**
     * Starts the collecting process. Time is zero.
     * @param name name of the exercise
     * @throws MeasurementException when an error occurred
     */
    void startCollecting(String guid) throws MeasurementException;

    /**
     * Stops the collecting process
     * @param time time since start
     * @throws MeasurementException when an error occurred
     */
    void stopCollecting(double time) throws MeasurementException;

    /**
     * Collects a measurement
     * @param sensor Measurement source sensor
     * @param time Time since collecting started
     * @param values Values array
     * @param accuracy Accuracy reading
     * @throws MeasurementException when an error occurred
     */
    void collectMeasurement(Sensor sensor, double time, float[] values, int accuracy, double interval) throws MeasurementException;

    /**
     * Fails the measurement collection. This completely invalidates the current measurement collection.
     * This method is automatically called when one of the other methods throw an exception.
     * @param ex The failing exception
     */
    void collectionFailed(MeasurementException ex);
}
