package nl.fontys.exercise.recorder;

import android.hardware.Sensor;
import android.util.Log;

import java.util.Arrays;

public class JsonMeasurementCollector implements MeasurementCollector {

    private final String TAG = "COLLECTOR";

    @Override
    public void startCollecting() {
        Log.d(TAG, "Collecting started.");
    }

    @Override
    public void stopCollecting() {
        Log.d(TAG, "Collecting stopped.");
    }

    @Override
    public void collectMeasurement(Sensor sensor, long timestamp, float[] values, int accuracy) {
        Log.d(TAG, String.format("[%s] %s %s (%s)", sensor.getName(), Long.toString(timestamp), Arrays.toString(values), Integer.toString(accuracy)));
    }
}
