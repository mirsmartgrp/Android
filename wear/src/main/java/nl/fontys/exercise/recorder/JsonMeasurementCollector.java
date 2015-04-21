package nl.fontys.exercise.recorder;

import android.hardware.Sensor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class JsonMeasurementCollector implements MeasurementCollector {

    private static final String LOG_TAG = "JSON_COLLECTOR";

    private final Map<String, JSONArray> dataMap;

    public JsonMeasurementCollector() {
        dataMap = new HashMap<String, JSONArray>();
    }

    @Override
    public void startCollecting() throws MeasurementException {
        dataMap.clear();
    }

    @Override
    public void stopCollecting(double time) throws MeasurementException {
        JSONObject obj = new JSONObject();

        try {
            for (Map.Entry<String, JSONArray> dataEntry : dataMap.entrySet()) {
                obj.put(dataEntry.getKey(), dataEntry.getValue());
            }
            collectionComplete(obj);
        } catch (JSONException ex) {
            throw new MeasurementException(ex);
        }
    }

    @Override
    public void collectMeasurement(Sensor sensor, double time, float[] values, int accuracy) throws MeasurementException {
        JSONArray dataArray;

        // obtain right array to store data in
        if ((dataArray = dataMap.get(sensor.getName())) == null) {
            dataArray = new JSONArray();
            dataMap.put(sensor.getName(), dataArray);
        }

        try {
            JSONObject value = new JSONObject();
            switch (sensor.getType()) {
                case Sensor.TYPE_LINEAR_ACCELERATION:
                case Sensor.TYPE_GYROSCOPE:
                    value.put("x", values[0]);
                    value.put("y", values[1]);
                    value.put("z", values[2]);
                    break;
                default:
                    throw new MeasurementException(String.format("Sensor %s not implemented.", sensor.getName()));
            }

            JSONObject measurement = new JSONObject();
            measurement.put("time", time);
            measurement.put("value", value);
            dataArray.put(measurement);
        } catch (JSONException ex) {
            throw new MeasurementException(ex);
        }
    }

    public abstract void collectionComplete(JSONObject data);
}
