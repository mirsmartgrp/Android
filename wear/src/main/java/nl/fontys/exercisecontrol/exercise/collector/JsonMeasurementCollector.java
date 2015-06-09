package nl.fontys.exercisecontrol.exercise.collector;

import android.hardware.Sensor;
import android.util.Log;

import java.util.ListIterator;

import nl.fontys.exercisecontrol.exercise.recorder.MeasurementCollector;
import nl.fontys.exercisecontrol.exercise.recorder.MeasurementException;

public abstract class JsonMeasurementCollector implements MeasurementCollector {

    private ExerciseData exerciseData = null;
    private DataEntry.Vector accelerometer = null;
    private DataEntry.Vector gyroscope = null;

    @Override
    public void startCollecting(String guid) throws MeasurementException {
        accelerometer = null;
        gyroscope = null;
        exerciseData = new ExerciseData(guid);
    }

    @Override
    public void stopCollecting(double time) throws MeasurementException {
        if (exerciseData == null)
            throw new MeasurementException("No exercise data available!");

        collectionComplete(exerciseData);
    }

    @Override
    public void collectMeasurement(Sensor sensor, double time, float[] values, int accuracy, double interval) throws MeasurementException {
        DataEntry dataEntry = null;

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

        ListIterator<DataEntry> iter = exerciseData.getSensorData().listIterator(exerciseData.getSensorData().size());
        while (iter.hasPrevious()) {
            DataEntry entry = iter.previous();
            if (entry.getSecondsSinceStart() >= time - interval) {
                dataEntry = entry;
                break;
            }
        }

        if (dataEntry == null) {
            dataEntry = new DataEntry(time);
            exerciseData.getSensorData().add(dataEntry);
        }

        if (accelerometer != null)
            dataEntry.setAccelerometer(accelerometer);
        if (gyroscope != null)
            dataEntry.setGyroscope(gyroscope);
    }

    public abstract void collectionComplete(ExerciseData data);
}
