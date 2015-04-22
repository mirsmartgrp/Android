package nl.fontys.exercisecontrol.exercise;

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

    public String getNAME()
    {
        return NAME;
    }

    public ExerciseData getEXERCISE_DATA()
    {
        return EXERCISE_DATA;
    }

    public static Exercise parseExercise(JSONObject jsonObject)
    {
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
}
