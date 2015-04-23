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

    private static final String LOG_TAG = "JSON_COLLECTOR";

    private ExerciseData exerciseData = null;

    @Override
    public void startCollecting() throws MeasurementException {
        exerciseData = new ExerciseData("Exercise Data");
    }

    @Override
    public void stopCollecting(double time) throws MeasurementException {
        if (exerciseData == null)
            throw new MeasurementException("No exercise data available!");

        collectionComplete(exerciseData);
    }

    @Override
    public void collectMeasurement(Sensor sensor, double time, float[] values, int accuracy) throws MeasurementException {
        //TODO actually find data entry with same time
        DataEntry dataEntry = new DataEntry(time);
        exerciseData.getData().add(dataEntry);

        // assign data to entry object
        switch (sensor.getType()) {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                dataEntry.setAccelerometer(new DataEntry.Vector(values[0], values[1], values[2]));
                break;
            case Sensor.TYPE_GYROSCOPE:
                dataEntry.setGyroscope(new DataEntry.Vector(values[0], values[1], values[2]));
                break;
            default:
                throw new MeasurementException(String.format("Sensor %s not implemented.", sensor.getName()));
        }
    }

    public abstract void collectionComplete(ExerciseData data);
}
