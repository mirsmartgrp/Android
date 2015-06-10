package nl.fontys.exercisecontrol.exercise.collector;

import android.hardware.Sensor;
import java.util.List;
import java.util.ListIterator;

import nl.fontys.exercisecontrol.exercise.recorder.MeasurementCollector;
import nl.fontys.exercisecontrol.exercise.recorder.MeasurementException;

public abstract class JsonMeasurementCollector implements MeasurementCollector {

    private ExerciseData exerciseData = null;
    private List<DataEntry> sensorData = null;
    private DataEntry.Vector accelerometer = null;
    private DataEntry.Vector gyroscope = null;

    @Override
    public void startCollecting(String guid) throws MeasurementException {
        accelerometer = null;
        gyroscope = null;
        exerciseData = new ExerciseData(guid);
        sensorData = exerciseData.getSensorData();
    }

    @Override
    public void stopCollecting(double time) throws MeasurementException {
        if (exerciseData == null)
            throw new MeasurementException("No exercise data available!");

        collectionComplete(exerciseData);
    }

    @Override
    public void collectMeasurement(Sensor sensor, double time, float[] values, int accuracy, double interval) throws MeasurementException {
        // assign data to entry object
        switch (sensor.getType()) {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                accelerometer = new DataEntry.Vector(values);
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyroscope = new DataEntry.Vector(values);
                break;
            default:
                throw new MeasurementException(String.format("Sensor %s not implemented.", sensor.getName()));
        }

        DataEntry entry;
        if ((sensorData.size() == 0) || ((entry = sensorData.get(sensorData.size() - 1)) == null) ||
            (entry.getSecondsSinceStart() < time - interval)) {
            entry = new DataEntry(time);
            exerciseData.getSensorData().add(entry);
        }

        if (accelerometer != null)
            entry.setAccelerometer(accelerometer);
        if (gyroscope != null)
            entry.setGyroscope(gyroscope);
    }

    public abstract void collectionComplete(ExerciseData data);
}
