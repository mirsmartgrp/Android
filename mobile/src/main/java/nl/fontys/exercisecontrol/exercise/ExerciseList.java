package nl.fontys.exercisecontrol.exercise;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by ron on 21.05.15.
 * <p/>
 * a Hashmap<guid,exercise> for reading all exercises
 */
public class ExerciseList
{
    private final HashMap<String, Exercise> EXERCISE_HASH_MAP;

    public ExerciseList(Context context)
            throws Exception
    {
        EXERCISE_HASH_MAP = new HashMap<>();

        if (!fillEXERCISE_HASHMAP(context,
                                  EXERCISE_HASH_MAP))
        {
            throw new Exception("exerciseListJsonObject is empty");
        }
    }

    private Boolean fillEXERCISE_HASHMAP(Context context,
                                         HashMap<String, Exercise> exerciseHashMap)
    {
        Boolean retVal = Boolean.FALSE;

        //TODO read the json exerciselist file and generate an jsonobject
        Integer ressourceID = context.getResources().getIdentifier("exercise_list",
                                                                   "raw",
                                                                   context.getPackageName());
        InputStream inputStream = context.getResources().openRawResource(ressourceID.intValue());
        String jsonString = new Scanner(inputStream).useDelimiter("\\A").next();

        try
        {
            JSONObject exerciseListJsonObject = new JSONObject(jsonString);

            if (exerciseListJsonObject.length() < 1)
            {
                throw new NullPointerException("exerciseListJsonObject is empty");
            }

            JSONArray exercisesJsonArray = exerciseListJsonObject.getJSONArray("exercises");

            for (int index = 0; index < exercisesJsonArray.length(); index++)
            {
                JSONObject exerciseJsonObject = exercisesJsonArray.getJSONObject(index);
                Exercise exercise = new Exercise(exerciseJsonObject.getString("guid"),
                                                 exerciseJsonObject.getString("name"),
                                                 exerciseJsonObject.getString("description"),
                                                 exerciseJsonObject.getString("video_image"));

                exerciseHashMap.put(exercise.getGUID(),
                                    exercise);
            }

            retVal = Boolean.TRUE;
        }
        catch (JSONException e)
        {
            Log.e("FILL_EXERCISE_HASH_MAP",
                  e.getMessage());
        }

        return retVal;
    }

    public HashMap<String, Exercise> getEXERCISE_HASH_MAP()
    {
        return EXERCISE_HASH_MAP;
    }

    public Exercise getExerciseByMovementData(JSONObject jsonObject)
    {
        Exercise exercise = null;

        try
        {
            if (jsonObject.length() <= 0)
            {
                throw new NullPointerException("jsonObject is empty");
            }

            exercise = getEXERCISE_HASH_MAP().get(jsonObject.getString("guid"));

            JSONArray jsonArray = jsonObject.getJSONArray("sensorData");
            for (int index = 0; index < jsonArray.length(); index++)
            {
                JSONObject tmpJsonObject = jsonArray.getJSONObject(index);
                Double time = tmpJsonObject.getDouble("secondsSinceStart");
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
}
