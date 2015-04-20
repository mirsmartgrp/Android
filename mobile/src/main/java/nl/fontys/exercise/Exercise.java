package nl.fontys.exercise;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ron Gebauer on 20.04.15.
 * <p/>
 * the exercise itself.
 */
public class Exercise
{
    private final String       name;
    private final ExerciseData exerciseData;

    /**
     * Constructs an exercise object with given name.
     *
     * @param name the name of the exercise
     */
    Exercise(String name)
    {
        this.name = name;
        this.exerciseData = new ExerciseData();
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
