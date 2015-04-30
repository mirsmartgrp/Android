package nl.fontys.exercisecontrol.exercise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    Exercise(String name)
    {
        this.NAME = name;
        this.EXERCISE_DATA = new ExerciseData();
    }

    public static Exercise parseExercise(JSONObject jsonObject)
    {
        try
        {
            jsonObject = new JSONObject("{'name': 'test exercise'}");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        if (jsonObject.length() <= 0)
        {
            throw new RuntimeException("The jsonObject is empty.");
        }

        Exercise exercise = null;

        try
        {
                exercise = new Exercise(jsonObject.getString("name"));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
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
