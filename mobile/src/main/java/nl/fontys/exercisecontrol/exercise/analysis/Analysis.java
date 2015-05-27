package nl.fontys.exercisecontrol.exercise.analysis;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import be.ac.ulg.montefiore.run.jahmm.OpdfMultiGaussianFactory;
import be.ac.ulg.montefiore.run.jahmm.draw.GenericHmmDrawerDot;
import be.ac.ulg.montefiore.run.jahmm.learn.KMeansLearner;
import nl.fontys.exercisecontrol.exercise.Exercise;
import nl.fontys.exercisecontrol.exercise.PartialExerciseData;
import nl.fontys.exercisecontrol.exercise.SingleExerciseData;

/**
 * Created by techlogic on 30.04.15.
 */
public class Analysis {


    private Map<String, List<List<ObservationVector>>> map = new HashMap<String, List<List<ObservationVector>>>();
    private static final int FAILURE_TOLERANCE = 4;


    public Analysis() {
    }


    public List<List<ObservationVector>> getLearnSequences(Exercise exercise){
        return map.get(exercise.getGUID());
    }

    public void addLearnSequence(Exercise exercise) {
        if (exercise.getEXERCISE_DATA() != null && exercise.getEXERCISE_DATA().getListOfSingleExerciseData() != null) {
            List<ObservationVector> seq = createSequence(exercise.getEXERCISE_DATA().getListOfSingleExerciseData());
            List<List<ObservationVector>> kMeanSeq = map.get(exercise.getGUID());
            if (kMeanSeq == null) {
                kMeanSeq = new ArrayList<List<ObservationVector>>();
                map.put(exercise.getGUID(), kMeanSeq);
            }
            kMeanSeq.add(seq);
        }else{
            Log.d("Analsysis","exerciseData exmpty!");
        }
    }

    public boolean testExercise(Exercise exercise) throws AnalysisError {

        if (exercise.getHmm() == null || exercise.getSequences() == null) {
            throw new AnalysisError("Exerise " + exercise.getNAME() + " not trained!");
        }

        if (exercise.getEXERCISE_DATA() != null && exercise.getEXERCISE_DATA().getListOfSingleExerciseData() != null) {
            List<ObservationVector> seq = createSequence(exercise.getEXERCISE_DATA().getListOfSingleExerciseData());
            int[] probSeq = AnalysisUtil.FILTER_SEQUENCES(exercise.getHmm().mostLikelyStateSequence(seq));
            String seqString = "";
            for (int i : probSeq) {

                seqString = seqString + i;
            }
            int min = -1;
            for (String s : exercise.getSequences()) {
                int distance = AnalysisUtil.CALCULATE_LEVENSHTEIN_DISTANCE(s, seqString);
                if (min > distance || min < 0) {
                    min = distance;
                }
            }
            return min <= FAILURE_TOLERANCE;
        } else {
            throw new AnalysisError("Exercise Data is empty!");
        }
    }

    private List<ObservationVector> createSequence(List<SingleExerciseData> data) {
        List<ObservationVector> seq = new ArrayList<ObservationVector>();
        for (SingleExerciseData single : data) {

            double x;
            double y;
            double z;

            PartialExerciseData accel = single.getAcceleratorData();
            PartialExerciseData gyro = single.getGyroscopeData();

            x = accel.getX() * gyro.getX();
            y = accel.getY() * gyro.getY();
            z = accel.getZ() * gyro.getZ();

            double[] values = {x, y, z};
            ObservationVector vector = new ObservationVector(values);
            seq.add(vector);
        }
        return seq;
    }


    public void learnHMM(Exercise exercise) {

        List<List<ObservationVector>> kMeanSeq = map.get(exercise.getGUID());

        if (kMeanSeq != null) {
            KMeansLearner<ObservationVector> meansLearner = new KMeansLearner(5, new OpdfMultiGaussianFactory(3), kMeanSeq);
            Hmm<ObservationVector> hmm = meansLearner.learn();
            List<String> sequences = new ArrayList<String>();


            for (List<ObservationVector> sequence : kMeanSeq) {
                int[] probSeq = AnalysisUtil.FILTER_SEQUENCES(hmm.mostLikelyStateSequence(sequence));
                String seqString = "";
                for (int i : probSeq) {

                    seqString = seqString + i;
                }
                sequences.add(seqString);
            }
            exercise.setHmm(hmm);
            exercise.setSequences(sequences);
        }
    }


    public void printHMM(Exercise exercise) {
        try {
            if (exercise.getHmm() != null) {
                File path;
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                } else {
                    path = Environment.getDataDirectory();
                }
                String kMeanPath = path.getAbsolutePath() + "/mean/" + (new Date()) + ".dot";
                Log.d("HMM", "Printed HMM ");
                new GenericHmmDrawerDot().write(exercise.getHmm(), kMeanPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
