package nl.fontys.exercisecontrol.exercise;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import be.ac.ulg.montefiore.run.jahmm.Opdf;
import be.ac.ulg.montefiore.run.jahmm.OpdfFactory;
import be.ac.ulg.montefiore.run.jahmm.OpdfGaussian;
import be.ac.ulg.montefiore.run.jahmm.OpdfGaussianFactory;
import be.ac.ulg.montefiore.run.jahmm.OpdfMultiGaussianFactory;
import be.ac.ulg.montefiore.run.jahmm.draw.GenericHmmDrawerDot;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;
import be.ac.ulg.montefiore.run.jahmm.learn.KMeansLearner;

/**
 * Created by techlogic on 30.04.15.
 */
public class HMM {

    private Hmm<ObservationVector> hmm;
    private BaumWelchLearner bwl;
    private KMeansLearner meansLearner;

    public  HMM(){

        OpdfFactory factory = new OpdfMultiGaussianFactory(3);
        hmm = new Hmm<ObservationVector>(5,factory);

    }

    private List<List<ObservationVector>> kMeanSeq = new ArrayList<List<ObservationVector>>();

    public void learnExercise(List<SingleExerciseData> data){

        List<List<ObservationVector>> sequences = new ArrayList<List<ObservationVector>>();
        List<ObservationVector> seq = new ArrayList<ObservationVector>();
        for(SingleExerciseData single : data){

            double x;
            double y;
            double z;

            PartialExerciseData accel = single.getAcceleratorData();
            PartialExerciseData gyro = single.getGyroscopeData();
            x = gyro.getX() * accel.getX();
            y = gyro.getY() * accel.getY();
            z = gyro.getZ() * accel.getZ();

            double[] values = {x,y,z};
           ObservationVector vector =  new ObservationVector(values);
            seq.add(vector);
        }
        kMeanSeq.add(seq);

    }

    public void testExercise(List<SingleExerciseData> data){
        List<List<ObservationVector>> sequences = new ArrayList<List<ObservationVector>>();
        List<ObservationVector> seq = new ArrayList<ObservationVector>();
        for(SingleExerciseData single : data){

            double x;
            double y;
            double z;

            PartialExerciseData accel = single.getAcceleratorData();
            PartialExerciseData gyro = single.getGyroscopeData();

            x = accel.getX() * gyro.getX();
            y = accel.getY() * gyro.getY();
            z = accel.getZ() * gyro.getZ();

            double[] values = {x,y,z};
            ObservationVector vector =  new ObservationVector(values);
            seq.add(vector);
        }
        int[] probSeq = filterSequences(hmm.mostLikelyStateSequence(seq));
        String seqString = "";
        for (int i : probSeq) {

            seqString = seqString + " " + i;
        }
        Log.d("KMEAN TEST",seqString);
        Log.d("KMEAN TEST","% : "+ Double.toString(hmm.probability(seq)));

    }

    public void printHMM(){
        try {
            File path;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            } else {
                path = Environment.getDataDirectory();
            }
            Log.d("HMM", "Printed HMM ");
            meansLearner = new KMeansLearner(5, new OpdfMultiGaussianFactory(3), kMeanSeq);
            Hmm kmean = meansLearner.learn();
            String kMeanPath = path.getAbsolutePath() + "/mean/" + (new Date()) + ".dot";
            new GenericHmmDrawerDot().write(kmean, kMeanPath);

            List<ObservationVector> seq = kMeanSeq.get(0);

            double prob = kmean.probability(seq);
            Log.d("KMEAN TEST", Double.toString(prob));

            int count = -1;
            for (List<ObservationVector> sequence : kMeanSeq) {
                int[] probSeq = filterSequences(kmean.mostLikelyStateSequence(sequence));
                Log.d("Length",Integer.toString(sequence.size()));
                String seqString = "";
                for (int i : probSeq) {

                    seqString = seqString + " " + i;
                }
                count++;
                Log.d("KMEAN TEST", count + ": " + seqString);
                seqString = "";
                hmm =kmean;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public int[] filterSequences(int[] seq){

        ArrayList<Integer>  result = new ArrayList<Integer>();
        for(int j : seq){
            result.add(new Integer(j));
        }

        int aktValue = seq[0];
        int removeCount = 0;
        for(int i = 1;i<seq.length;i++){
            if(seq[i] == aktValue){
                   result.remove(i-removeCount);
                    removeCount++;
            }else{
                aktValue = seq[i];
            }
        }

        int[] values = new int[result.size()];
        for(int j = 0; j<result.size();j++){
            values[j] = result.get(j).intValue();
        }

        return values;
    }





}
