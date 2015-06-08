package nl.fontys.exercisecontrol.exercise;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import nl.fontys.exercisecontrol.exercise.recorder.MeasurementCollector;
import nl.fontys.exercisecontrol.exercise.recorder.MeasurementRecorder;

public class TrainingActivity extends Activity implements SensorEventListener {

    private ConnectionHandler handler;
    private MeasurementRecorder recorder;
    private MeasurementCollector collector;
    private Chronometer chronometer;
    private TextView headerLbl;
    private String exerciseName="unknown exercise";
    private String exerciseGUID ="";
    private final static String TAG="LOG";
    private Button startBtn;
    private Button stopBtn;
    private SensorManager sensorManager;
    private long startTime;
    private JSONObject mainObj=new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = ObjectHelper.getInstance(getApplicationContext()).getConnectionHandler();


        final WatchViewStub stub = (WatchViewStub)findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub watchViewStub) {
                TextView textView = (TextView) findViewById(R.id.headerLbl);
                Bundle bundle = getIntent().getExtras();
                String name = bundle.getString(SelectExerciseActivityWear.EXERCISE_NAME);
                textView.setText(name);

            }
        });

    }
    /**
     * start the measurement
     * start chronometer
     * set the Exercise nameS
     * @param view
     */
    public void start(View view) {
        if(isConnectedToPhone()) {
            exerciseGUID = getExerciseGUID();
            chronometer = (Chronometer) findViewById(R.id.chronometer);
            chronometer.setBase(SystemClock.elapsedRealtime()); //reset to 0
            registerSensors();
            startTime = System.nanoTime();
            chronometer.start();
            setExerciseNameToHeaderLbl();
            toogleButtons();
        }
        else {
            showToast(getString(R.string.errorConnectionToPhone), Toast.LENGTH_LONG);
        }

    }

    private void registerSensors() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_UI);

    }

    private void unregisterSensors() {
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));

    }
    /**
     * TODO: enable, disabled during debug
     * check if phone and watch are connected
     * @return
     */
    private boolean isConnectedToPhone() {
     /*
        if(!handler.isConnected()) {
            handler.connectGogleClient();
        }
        return handler.isConnected();
        */
        return true;
    }
    /**
     * stop the measurement
     * stop chronometer
     * @param view
     */
    public void stop(View view) {
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.stop();
     unregisterSensors();
        toogleButtons();
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
    private void toogleButtons() {
        startBtn = (Button) findViewById(R.id.startBtn);
        stopBtn = (Button) findViewById(R.id.stopBtn);
        if(startBtn.isEnabled()) {
            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);
        }
        else {
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
        }
    }
    private String getExerciseName() {
        Intent intent = getIntent();
        return intent.getStringExtra(SelectExerciseActivityWear.EXERCISE_NAME);
    }
    private String getExerciseGUID() {
        Intent intent = getIntent();
        return intent.getStringExtra(SelectExerciseActivityWear.EXERCISE_GUID);
    }

    /**
     * setting the name of the exerccise to the header
     */
    private void setExerciseNameToHeaderLbl() {
        headerLbl = (TextView ) findViewById(R.id.headerLbl);
        exerciseName = getExerciseName();
        headerLbl.setText(exerciseName);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "sensor changed");
        if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE) {

        }
        else if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE) {

        }
        else {
            //nothing
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
