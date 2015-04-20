package nl.fontys.exercise.recorder;

import android.hardware.Sensor;
import android.util.Log;

import java.util.Arrays;

public class JsonMeasurementCollector implements MeasurementCollector {

    private final String TAG = "COLLECTOR";

    @Override
    public void startCollecting() {
        Log.d(TAG, String.format("[%.3f] Collecting started.", 0));
    }

    @Override
    public void stopCollecting(double time) {
        Log.d(TAG, String.format("[%.3f] Collecting stopped.", time));
    }

    @Override
    public void collectMeasurement(Sensor sensor, double time, float[] values, int accuracy) {
        Log.d(TAG, String.format("[%.3f] %s: %s (%s)", time, sensor.getName(), Arrays.toString(values), Integer.toString(accuracy)));
    }
}
