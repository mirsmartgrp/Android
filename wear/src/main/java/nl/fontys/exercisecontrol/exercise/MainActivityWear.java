package nl.fontys.exercisecontrol.exercise;

import android.app.Activity;
import android.hardware.Sensor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import nl.fontys.exercisecontrol.exercise.collector.DataEntry;
import nl.fontys.exercisecontrol.exercise.collector.ExerciseData;
import nl.fontys.exercisecontrol.exercise.collector.JsonMeasurementCollector;
import nl.fontys.exercisecontrol.exercise.recorder.MeasurementCollector;
import nl.fontys.exercisecontrol.exercise.recorder.MeasurementException;
import nl.fontys.exercisecontrol.exercise.recorder.MeasurementRecorder;

public class MainActivityWear extends Activity {

    private ConnectionHandler handler;
    private MeasurementRecorder recorder;
    private MeasurementCollector collector;
    private Chronometer chronometer;
    private TextView headerLbl;
    private String exerciseName="cycling";
    private final static String TAG="LOG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new ConnectionHandler(this);

        collector = new JsonMeasurementCollectorImpl();

        recorder = new MeasurementRecorder(this,initSensors(), 1, collector, exerciseName);
        recorder.initialize();
       // initView();
    }


    /**
     * setup the view
     */
    private void initView() {
        headerLbl = (TextView ) findViewById(R.id.headerLbl);
        headerLbl.setText(exerciseName);
    }
    /**
     * create sensor data
     */
    private int[] initSensors() {
        int[] sensors = new int[2];
        sensors[0]=Sensor.TYPE_GYROSCOPE;
        sensors[1]=Sensor.TYPE_LINEAR_ACCELERATION;
        return sensors;
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
     * start chronometer
     * @param view
     */
    public void start(View view) {
        initView();
        if(!handler.isConnected()) {
            handler.connectGogleClient();
        }
        if(handler.isConnected()) {
            chronometer = (Chronometer) findViewById(R.id.chronometer);
            chronometer.start();
            recorder.start();
        }
        else {
            showToast("could not establish connection to phone", Toast.LENGTH_LONG);
        }

    }

    /**
     * stop the measurement
     * stop chronometer
     * @param view
     */
    public void stop(View view) {
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.stop();
        recorder.stop();
    }

    /**
     * display a toast message
     * @param message text to display
     * @param length time how long the display is show
     */
    private void showToast(String message, int length) {
        Toast toast = Toast.makeText(this, message, length);
        toast.show();
    }
    private class JsonMeasurementCollectorImpl extends JsonMeasurementCollector {

        @Override
        public void startCollecting(String exerciseName) throws MeasurementException {
            super.startCollecting(exerciseName);
            Log.d(TAG, "Started collecting measurements.");
        }

        @Override
        public void collectMeasurement(Sensor sensor, double time, float[] values, int accuracy, double interval) throws MeasurementException {
            super.collectMeasurement(sensor, time, values, accuracy, interval);
        }


            @Override
        public void collectionComplete(ExerciseData data) {

            Gson gson = new Gson();
                Log.d(TAG,"name: "+data.getName());
                for(DataEntry d:data.getData()) {
                    Log.d(TAG,"dataEntry: "+d);
                }
            Log.d(TAG, "collecting measurements complete: "+gson.toString());
            sendMessage(gson.toString());
        }

        /**
         * send message with text and show toast
         * @param text text of message
         **/
        private void sendMessage(String text) {
            handler.connectGogleClient();
            if(handler.isConnected()) {
                handler.sendMessage(text);
                showToast("data send to phone", Toast.LENGTH_LONG);
            }
            else {
                showToast("no connection to phone.", Toast.LENGTH_LONG);
            }
        }
        @Override
        public void collectionFailed(MeasurementException ex) {
            Log.d(TAG, "Measurement failed: " + ex.getMessage());
            showToast("Measurement failed", Toast.LENGTH_LONG);
        }
    }
}
