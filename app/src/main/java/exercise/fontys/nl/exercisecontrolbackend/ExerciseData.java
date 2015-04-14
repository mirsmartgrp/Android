package exercise.fontys.nl.exercisecontrolbackend;

/**
 * Created by root on 08.04.15.
 * <p/>
 * stores the data that is collected from the exercises
 */
public class ExerciseData
{
    private String typeOfExercise;
    private SingleExerciseData singleExerciseData;

    public ExerciseData(String typeOfExercise, SingleExerciseData singleExerciseData)
    {
        this.typeOfExercise = typeOfExercise;
        this.singleExerciseData = singleExerciseData;
    }

    public String getTypeOfExercise()
    {
        return typeOfExercise;
    }

    public void setTypeOfExercise(String typeOfExercise)
    {
        this.typeOfExercise = typeOfExercise;
    }

    public SingleExerciseData singleExerciseData()
    {
        return singleExerciseData;
    }

    public void setGyroData(SingleExerciseData singleExerciseData)
    {
        this.singleExerciseData = singleExerciseData;
    }
}
