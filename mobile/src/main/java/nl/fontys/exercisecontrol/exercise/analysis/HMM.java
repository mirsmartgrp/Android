package nl.fontys.exercisecontrol.exercise.analysis;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import be.ac.ulg.montefiore.run.jahmm.OpdfFactory;
import be.ac.ulg.montefiore.run.jahmm.OpdfMultiGaussianFactory;
import be.ac.ulg.montefiore.run.jahmm.draw.GenericHmmDrawerDot;
import be.ac.ulg.montefiore.run.jahmm.learn.KMeansLearner;
import nl.fontys.exercisecontrol.exercise.PartialExerciseData;
import nl.fontys.exercisecontrol.exercise.SingleExerciseData;

/**
 * Created by techlogic on 30.04.15.
 */
public class HMM {

    private Hmm<ObservationVector> hmm;
    private List<String> sequences = new ArrayList<String>();
    private List<List<ObservationVector>> kMeanSeq = new ArrayList<List<ObservationVector>>();
    private static final int FAILURE_TOLERANCE = 4;


    public HMM() {
        OpdfFactory factory = new OpdfMultiGaussianFactory(3);
        hmm = new Hmm<ObservationVector>(5, factory);
    }


    public void addLearnSequence(List<SingleExerciseData> data) {
        List<ObservationVector> seq = createSequence(data);
        kMeanSeq.add(seq);
    }

    public boolean testExercise(List<SingleExerciseData> data) {
        List<ObservationVector> seq = createSequence(data);
        int[] probSeq = AnalysisUtil.FILTER_SEQUENCES(hmm.mostLikelyStateSequence(seq));
        String seqString = "";
        for (int i : probSeq) {

            seqString = seqString + i;
        }
        int min = -1;
        for (String s : sequences) {
            int distance = AnalysisUtil.CALCULATE_LEVENSHTEIN_DISTANCE(s, seqString);
            if (min > distance || min < 0) {
                min = distance;
            }
        }
        return min <= FAILURE_TOLERANCE;
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


    public void learnHMM() {

        KMeansLearner<ObservationVector> meansLearner = new KMeansLearner(5, new OpdfMultiGaussianFactory(3), kMeanSeq);
        hmm = meansLearner.learn();
    }


    public void printHMM() {
        try {
            File path;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            } else {
                path = Environment.getDataDirectory();
            }
            String kMeanPath = path.getAbsolutePath() + "/mean/" + (new Date()) + ".dot";
            Log.d("HMM", "Printed HMM ");
            new GenericHmmDrawerDot().write(hmm, kMeanPath);
            int count = -1;
            for (List<ObservationVector> sequence : kMeanSeq) {
                int[] probSeq = AnalysisUtil.FILTER_SEQUENCES(hmm.mostLikelyStateSequence(sequence));
                Log.d("Length", Integer.toString(sequence.size()));
                String seqString = "";
                for (int i : probSeq) {

                    seqString = seqString + i;
                }
                count++;
                Log.d("KMEAN TEST", count + ": " + seqString);
                sequences.add(seqString);
                seqString = "";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
