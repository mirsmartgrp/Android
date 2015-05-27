package nl.fontys.exercisecontrol.exercise;

import android.content.Context;

import nl.fontys.exercisecontrol.TrainActivity;
import nl.fontys.exercisecontrol.exercise.analysis.Analysis;
import nl.fontys.exercisecontrol.guiSupport.LearnAdapter;

/**
 * Created by techlogic on 26.05.15.
 */
public class ObjectHelper
{

    private static ObjectHelper instance = null;

    private Analysis      analysis;
    private Exercise      actualExecercise;
    private LearnAdapter  learnAdapter;
    private TrainActivity trainActivity;
    private ExerciseList  exerciseList;
    private Context       context;

    private ObjectHelper(Context context)
    {

        this.analysis = new Analysis();

        try
        {
            exerciseList = new ExerciseList(context);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static ObjectHelper getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new ObjectHelper(context);
        }
        return instance;
    }

    public ExerciseList getExerciseList()
    {
        return exerciseList;
    }

    public Analysis getAnalysis()
    {
        return analysis;
    }

    public Exercise getActualExecercise()
    {
        return actualExecercise;
    }

    public void setActualExecercise(Exercise actualExecercise)
    {
        this.actualExecercise = actualExecercise;
    }

    public LearnAdapter getLearnAdapter()
    {
        return learnAdapter;
    }

    public void setLearnAdapter(LearnAdapter learnAdapter)
    {
        this.learnAdapter = learnAdapter;
    }

    public TrainActivity getTrainActivity()
    {
        return trainActivity;
    }

    public void setTrainActivity(TrainActivity trainActivity)
    {
        this.trainActivity = trainActivity;
    }
}
