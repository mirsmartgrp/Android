package nl.fontys.exercisecontrol.exercise;

import nl.fontys.exercisecontrol.TrainActivity;
import nl.fontys.exercisecontrol.exercise.analysis.Analysis;
import nl.fontys.exercisecontrol.guiSupport.LearnAdapter;

/**
 * Created by techlogic on 26.05.15.
 */
public class ObjectHelper {

    private static ObjectHelper instance = new ObjectHelper();

    private Analysis analysis;
    private Exercise actualExecercise;
    private LearnAdapter learnAdapter;
    private TrainActivity trainActivity;

    private ObjectHelper(){
    this.analysis = new Analysis();
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

    public static ObjectHelper getInstance(){
        return  instance;
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
