package nl.fontys.exercise;

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
    public Exercise(String name)
    {
        this.name = name;
        this.exerciseData = new ExerciseData();
    }

    /**
     * Constructs an exercise object with given name.
     *
     * @param name the name of the exercise
     * @param exerciseData the name of the exercise
     */
    public Exercise(String name, ExerciseData exerciseData)
    {
        this.name = name;
        this.exerciseData = exerciseData;
    }
}
