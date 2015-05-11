package nl.fontys.exercisecontrol.exercise.collector;

import android.hardware.Sensor;

import java.util.ListIterator;

import nl.fontys.exercisecontrol.exercise.recorder.MeasurementCollector;
import nl.fontys.exercisecontrol.exercise.recorder.MeasurementException;

public abstract class JsonMeasurementCollector implements MeasurementCollector {

    private ExerciseData exerciseData = null;
    private DataEntry.Vector accelerometer = null;
    private DataEntry.Vector gyroscope = null;

    @Override
    public void startCollecting() throws MeasurementException {
        accelerometer = null;
        gyroscope = null;
        exerciseData = new ExerciseData("Exercise Data");
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
        boolean isFirst = false;

        ListIterator<DataEntry> iter = exerciseData.getData().listIterator(exerciseData.getData().size());
        while (iter.hasPrevious()) {
            DataEntry entry = iter.previous();
            if (entry.getTime() >= time - interval) {
                dataEntry = entry;
                break;
            }
        }
        if (dataEntry == null)
            dataEntry = new DataEntry(time);

        // assign data to entry object
        switch (sensor.getType()) {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                isFirst = (accelerometer == null);
                accelerometer = new DataEntry.Vector(values);
                break;
            case Sensor.TYPE_GYROSCOPE:
                isFirst = (gyroscope == null);
                gyroscope = new DataEntry.Vector(values);
                break;
            default:
                throw new MeasurementException(String.format("Sensor %s not implemented.", sensor.getName()));
        }

        if (isFirst) {
            for (DataEntry entry : exerciseData.getData()) {
                if (entry.getAccelerometer() == null)
                    entry.setAccelerometer(accelerometer);
                if (entry.getGyroscope() == null)
                    entry.setGyroscope(gyroscope);
            }
        }

        dataEntry.setAccelerometer(accelerometer);
        dataEntry.setGyroscope(gyroscope);
        exerciseData.getData().add(dataEntry);


    }

    public abstract void collectionComplete(ExerciseData data);
}
