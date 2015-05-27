package nl.fontys.exercisecontrol.exercise;

import android.content.Context;

import nl.fontys.exercisecontrol.TrainActivity;
import nl.fontys.exercisecontrol.exercise.analysis.Analysis;
import nl.fontys.exercisecontrol.guiSupport.LearnAdapter;

/**
 * Created by techlogic on 26.05.15.
 */
public class ObjectHelper {

    private static ObjectHelper instance = null;

    private Analysis analysis;
    private Exercise actualExecercise;
    private LearnAdapter learnAdapter;
    private TrainActivity trainActivity;
    private ExerciseList exerciseList;
    private Context context;

    private ObjectHelper(Context context){

    this.analysis = new Analysis();

        try
        {
            exerciseList = new ExerciseList(context);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public ExerciseList getExerciseList()
    {
        return exerciseList;
    }

    public void setActualExecercise(Exercise actualExecercise) {
        this.actualExecercise = actualExecercise;
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public Exercise getActualExecercise() {
        return actualExecercise;
    }

    public static ObjectHelper getInstance(Context context){
        if(instance == null)
        {
            instance = new ObjectHelper(context);
        }
        return instance;
    }

    public void setLearnAdapter(LearnAdapter learnAdapter) {
        this.learnAdapter = learnAdapter;
    }

    public LearnAdapter getLearnAdapter() {
        return learnAdapter;
    }

    public void setTrainActivity(TrainActivity trainActivity) {
        this.trainActivity = trainActivity;
    }

    public TrainActivity getTrainActivity() {
        return trainActivity;
    }
}
