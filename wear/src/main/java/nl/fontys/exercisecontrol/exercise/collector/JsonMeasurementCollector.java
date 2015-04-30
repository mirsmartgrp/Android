package nl.fontys.exercisecontrol.exercise.collector;

import android.hardware.Sensor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import nl.fontys.exercisecontrol.exercise.recorder.MeasurementCollector;
import nl.fontys.exercisecontrol.exercise.recorder.MeasurementException;

public abstract class JsonMeasurementCollector implements MeasurementCollector {

    private ExerciseData exerciseData = null;
    private final DataEntry[] buffer;
    private int index = 0;

    public JsonMeasurementCollector() {
        buffer = new DataEntry[32];
    }

    @Override
    public void startCollecting() throws MeasurementException {
        index = 0;
        for (int i = 0, n = buffer.length; i < n; i++)
            buffer[i] = null;

        exerciseData = new ExerciseData("Exercise Data");
    }

    @Override
    public void stopCollecting(double time) throws MeasurementException {
        if (exerciseData == null)
            throw new MeasurementException("No exercise data available!");

        while (buffer[index] != null) {
            exerciseData.getData().add(buffer[index++]);
            if (index == buffer.length)
                index = 0;
        }

        collectionComplete(exerciseData);
    }

    @Override
    public void collectMeasurement(Sensor sensor, double time, float[] values, int accuracy) throws MeasurementException {
        DataEntry dataEntry = null;

        // find data entry
        for (int i = index; buffer[i] != null; --i) {
            if (buffer[i].getTime() >= time)
                dataEntry = buffer[i];
            if (i == 0)
                i = buffer.length;
        }

        // add new data entry
        if (dataEntry == null) {
            dataEntry = new DataEntry(time);
            if (buffer[index] != null)
                exerciseData.getData().add(buffer[index]);
            buffer[index] = dataEntry;
            if (index == buffer.length)
                index = 0;
        }

        // assign data to entry object
        switch (sensor.getType()) {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                dataEntry.setAccelerometer(new DataEntry.Vector(values));
                break;
            case Sensor.TYPE_GYROSCOPE:
                dataEntry.setGyroscope(new DataEntry.Vector(values));
                break;
            default:
                throw new MeasurementException(String.format("Sensor %s not implemented.", sensor.getName()));
        }
    }

    public abstract void collectionComplete(ExerciseData data);
}
