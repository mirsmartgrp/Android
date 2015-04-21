package nl.fontys.exercise;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import nl.fontys.exercise.recorder.JsonMeasurementCollector;
import nl.fontys.exercise.recorder.MeasurementCollector;
import nl.fontys.exercise.recorder.MeasurementException;
import nl.fontys.exercise.recorder.MeasurementRecorder;

public class MainActivityWear extends Activity {

    private ConnectionHandler handler;
    private String messageToSendToPhone="undefined";
    private MeasurementRecorder recorder;
    private MeasurementCollector collector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new ConnectionHandler(this);

        int[] sensors = new int[2];
        sensors[0]=Sensor.TYPE_GYROSCOPE;
        sensors[1]=Sensor.TYPE_LINEAR_ACCELERATION;

        collector = new JsonMeasurementCollectorImpl();
        recorder = new MeasurementRecorder(this,sensors, 10, collector);
        recorder.initialize();
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume(){
        super.onResume();
      //  recorder.start();
    }
    @Override
    public void onDestroy() {
    super.onDestroy();
    handler.disconnectGoogleClient();
    recorder.stop();
       recorder.terminate();
    }

    /**
     * start the measurement
     * @param view
     */
    public void start(View view) {

    recorder.start();

    }

    /**
     * stop the measurement
     * @param view
     */
    public void stop(View view) {
    recorder.stop();
        recorder.terminate();
    }


    /**
     * send collected data to phone
     * and inform user
     * @param view
     */
    public void sendMessage(View view) {

            String resultText="data send to phone";
            handler.sendMessage(messageToSendToPhone);
            Log.d(this.getClass().getName(), resultText);
            Toast toast = Toast.makeText(this, resultText, Toast.LENGTH_SHORT);
            toast.show();
    }

    private class JsonMeasurementCollectorImpl extends JsonMeasurementCollector {

        @Override
        public void collectionComplete(JSONObject data) {
            Log.d("WEAR", "Measurement complete: " + data.toString());
        }

        @Override
        public void collectionFailed(MeasurementException ex) {
            Log.d("WEAR", "Measurement failed: " + ex.getMessage());
        }
    }
}
