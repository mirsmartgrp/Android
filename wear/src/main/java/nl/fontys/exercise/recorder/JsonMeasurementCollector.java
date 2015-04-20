package nl.fontys.exercise.recorder;

import android.hardware.Sensor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JsonMeasurementCollector implements MeasurementCollector {

    private static final String LOG_TAG = "JSON_COLLECTOR";

    private final Map<String, JSONArray> dataMap;

    public JsonMeasurementCollector() {
        dataMap = new HashMap<String, JSONArray>();
    }

    public JSONObject getData() throws JSONException {
        JSONObject obj = new JSONObject();

        for (Map.Entry<String, JSONArray> dataEntry : dataMap.entrySet()) {
            obj.put(dataEntry.getKey(), dataEntry.getValue());
        }

        return obj;
    }

    @Override
    public void startCollecting() {
        dataMap.clear();
    }

    @Override
    public void stopCollecting(double time) { }

    @Override
    public void collectMeasurement(Sensor sensor, double time, float[] values, int accuracy) {
        JSONArray dataArray;

        // obtain right array to store data in
        if ((dataArray = dataMap.get(sensor.getName())) == null)
            dataArray = dataMap.put(sensor.getName(), new JSONArray());

        try {
            JSONObject value = new JSONObject();
            switch (sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                case Sensor.TYPE_GYROSCOPE:
                case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                    value.put("x", values[0]);
                    value.put("y", values[1]);
                    value.put("z", values[2]);
                    break;
                default:
                    throw new JSONException("Not implemented yet: " + sensor.getName());
            }

            JSONObject measurement = new JSONObject();
            measurement.put("time", time);
            measurement.put("value", value);
            dataArray.put(measurement);
        } catch (JSONException ex) {
            Log.d(LOG_TAG, ex.getMessage());
        }
    }
}
