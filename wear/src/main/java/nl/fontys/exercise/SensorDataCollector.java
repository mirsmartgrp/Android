package nl.fontys.exercise;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class to collect sensor data for sensors on watch
 * Created by max on 09.04.15.
 */
public class SensorDataCollector  {

    private String exerciseData;
    private SensorManager sensorManager;
    //inital / reseted value of exercise data
    private static String START_DATA = "";
    //miliseconds of when the sensors should return its value
    //private static int SENSOR_DELAY = SensorManager.SENSOR_DELAY_NORMAL
    private static int SENSOR_DELAY = 100;
    //start time of the sensor measurement
    private static long START_TIME;
    private List<Sensor> sensorList;
    private Map<Sensor, SensorEventListener> sensorEvents;
    private Map<Sensor, JSONArray> sensorJsons;
    private JSONObject json;
    private Context context;

    public SensorDataCollector(Context context) {
        this.context=context;
        exerciseData = START_DATA;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorList = new ArrayList<>();
        sensorEvents = new HashMap<>();
        initSensorList();
        initSensorListeners();
        registerSensors();
        initJsons();
        START_TIME = new Date().getTime();

    }
    /**
     * init the json objects
     */
    private void initJsons() {
        json = new JSONObject();
        sensorJsons = new HashMap<>();

        for (Sensor currentSensor : sensorList) {
            //add jsons to map
            JSONArray currentArr = new JSONArray();
            sensorJsons.put(currentSensor, currentArr);
            try {
                //add sensor specific json to the 'big' json
                json.put(currentSensor.getName(), currentArr);
            } catch (JSONException ex) {
                Log.d(this.getClass().getName(), "error creating json for " + currentSensor.getName() + ". " + ex);
                //TODO ExceptionHandling properly

            }
        }

    }


    /**
     * add sensors to the list of sensors
     * Sensor.TYPE_LINEAR_ACCELERATION = without gravity
     */
    private void initSensorList() {
        sensorList.add(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        sensorList.add(sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));
        Log.d(this.getClass().getName(), "initSensorList");
    }

    /**
     * add a sensorListener for every
     */
    private void initSensorListeners() {
        for (Sensor currentSensor : sensorList) {
            SensorEventListener currentListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    Log.d(this.getClass().getName(), event.sensor.getName() + " changed: " + event.values);

                    addNewData(event);

                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    Log.d(this.getClass().getName(), sensor.getName() + " accuracy changed to: " + accuracy);

                }
            };
            sensorEvents.put(currentSensor, currentListener);
            Log.d(this.getClass().getName(), "initSensorListeners");

        }
    }

    /**
     * add sensorlisteners to the senormanager
     */
    public void registerSensors() {
        for (Sensor currentSensor : sensorList) {
            SensorEventListener currentListener = sensorEvents.get(currentSensor);
            sensorManager.registerListener(currentListener, currentSensor, SENSOR_DELAY);
        }
        Log.d(this.getClass().getName(), "register sensors");

    }

    /**
     * unregister sensorlisteners from sensormanager
     */
    public void unRegisterSensors() {
        for (SensorEventListener currentListener : sensorEvents.values()) {
            sensorManager.unregisterListener(currentListener);
        }
    }

    public String getExerciseData() {
        return exerciseData;
    }

    public void setExerciseData(String exerciseData) {
        exerciseData = START_DATA;
    }


    /**
     * adding the exercise data from the change event
     *
     * @param exerciseDataEvent
     */
    private void addNewData(SensorEvent exerciseDataEvent) {
        Log.d(this.getClass().getName(), "adding new data");

        Long timeStamp = new Long((new Date().getTime() - START_TIME));
        double timeStampLong = timeStamp * 0.001;
        JSONObject valueObj = new JSONObject();
        JSONObject mainObj = new JSONObject();
        if(exerciseDataEvent.values.length>=3) { //otherwise its not processable
            try {
                valueObj.put("x", exerciseDataEvent.values[0]);
                valueObj.put("y", exerciseDataEvent.values[1]);
                valueObj.put("z", exerciseDataEvent.values[2]);
                mainObj.put("value", valueObj);
                mainObj.put("time", timeStampLong);
                JSONArray sensorObject = sensorJsons.get(exerciseDataEvent.sensor);
                sensorObject.put(mainObj);
            } catch (JSONException ex) {
                Log.e(this.getClass().getName(), "can not create json . " + ex);
                //TODO handleException properly

            }

            exerciseData = json.toString();
            Log.d(this.getClass().getName(), "exercise data: " + exerciseData);
        }
    }

    /**
     * set exerciseData to Starting data
     */
    public void resetData() {
        exerciseData = START_DATA;
    }

}
