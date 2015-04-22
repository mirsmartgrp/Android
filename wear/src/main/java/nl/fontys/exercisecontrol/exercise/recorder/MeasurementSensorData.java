package nl.fontys.exercisecontrol.exercise.recorder;

import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * Measurement sensor information holder
 */
public class MeasurementSensorData {

    private final int sensorType;
    private final int samplingRate;

    private Sensor sensor = null;

    /**
     * Instantiate new sensor information holder object
     * @param sensorType Numerical sensor type
     * @param samplingRate Desired samples per second
     */
    public MeasurementSensorData(int sensorType, int samplingRate) {
        this.sensorType = sensorType;
        this.samplingRate = samplingRate;
    }

    /**
     * Gets numerical sensor type
     * @return numerical sensor type
     */
    public int getSensorType() {
        return sensorType;
    }

    /**
     * Gets the desired samples per second rate
     * @return samples per second
     */
    public int getSamplingRate() {
        return samplingRate;
    }

    /**
     * Gets the attached sensor
     * @return sensor object
     */
    public Sensor getSensor() {
        return sensor;
    }

    /**
     * Sets the attached sensor
     * @param sensor sensor object
     */
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    /**
     * Sets the default sensor for this numerical sensor type
     * @param sensorManager
     */
    public void setDefaultSensor(SensorManager sensorManager) {
        sensor = sensorManager.getDefaultSensor(sensorType);
    }
}
