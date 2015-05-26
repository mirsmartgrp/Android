package nl.fontys.exercisecontrol.exercise;

import android.util.Log;

import com.google.android.gms.games.internal.constants.TimeSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.LinkedHashMap;

import nl.fontys.exercisecontrol.MainActivityMobile;

/**
 * Created by Ron Gebauer on 20.04.15.
 * <p/>
 * the exercise itself.
 */
public class Exercise
{
    private final String       NAME;
    private final ExerciseData EXERCISE_DATA;

    /**
     * Constructs an exercise object with given NAME.
     *
     * @param name the NAME of the exercise
     */
    public Exercise(String name)
    {
        this.NAME = name;
        this.EXERCISE_DATA = new ExerciseData();
    }

    public static Exercise parseExercise(JSONObject jsonObject)
    {
        Exercise exercise = null;

        try
        {
            if (jsonObject.length() <= 0)
            {
                throw new NullPointerException("jsonObject is empty");
            }

            exercise = new Exercise(jsonObject.getString("name"));

            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int index = 0; index < jsonArray.length(); index++)
            {
                JSONObject tmpJsonObject = jsonArray.getJSONObject(index);
                Double time = tmpJsonObject.getDouble("time");
                JSONObject accelerometer = tmpJsonObject.getJSONObject("accelerometer");
                JSONObject gyroscope = tmpJsonObject.getJSONObject("gyroscope");
                Double[][] coordinates = new Double[][]{
                        {
                                Double.parseDouble(accelerometer.get("x").toString()),
                                Double.parseDouble(accelerometer.get("y").toString()),
                                Double.parseDouble(accelerometer.get("z").toString())
                        },
                        {
                                Double.parseDouble(gyroscope.get("x").toString()),
                                Double.parseDouble(gyroscope.get("y").toString()),
                                Double.parseDouble(gyroscope.get("z").toString())
                        }
                };

                exercise.getEXERCISE_DATA().addSingleExerciseData(time,
                                                                  coordinates);
            }
        }
        catch (JSONException e)
        {
            Log.e("parseExercise",
                  e.getMessage());
        }

        return exercise;
    }


    public String getNAME()
    {
        return NAME;
    }

    public ExerciseData getEXERCISE_DATA()
    {
        return EXERCISE_DATA;
    }
}
