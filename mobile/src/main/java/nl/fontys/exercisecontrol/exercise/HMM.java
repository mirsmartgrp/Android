package nl.fontys.exercisecontrol.exercise;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
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
        bwl = new BaumWelchLearner();
        bwl.setNbIterations(100);

    }

    private List<List<ObservationVector>> kMeanSeq = new ArrayList<List<ObservationVector>>();

    public void learnExercise(List<PartialExerciseData> data){

        List<List<ObservationVector>> sequences = new ArrayList<List<ObservationVector>>();
        List<ObservationVector> seq = new ArrayList<ObservationVector>();
        for(PartialExerciseData single : data){
            double[] values = {single.getX(),single.getY(),single
            .getZ()};
           ObservationVector vector =  new ObservationVector(values);
            seq.add(vector);
        }
        sequences.add(seq);
        kMeanSeq.add(seq);

        hmm = bwl.learn(hmm,sequences);

    }

    public void printHMM(){
        try {
          File path;
           String state =  Environment.getExternalStorageState();
             if (Environment.MEDIA_MOUNTED.equals(state)) {
                 path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            }else{
                 path = Environment.getDataDirectory();
            }

            String filepath = path.getAbsolutePath()+"/hmm/" + (new Date())+ ".dot";
            (new GenericHmmDrawerDot()).write(hmm,filepath);

            Log.d("HMM","Printed HMM ");

            meansLearner = new KMeansLearner(5,new OpdfMultiGaussianFactory(3),kMeanSeq);
            Hmm kmean = meansLearner.learn();


            String kMeanPath = path.getAbsolutePath()+"/mean/"+(new Date())+".dot";
            new GenericHmmDrawerDot().write(kmean,kMeanPath);

            List<ObservationVector> seq = kMeanSeq.get(new Random().nextInt(kMeanSeq.size()));
            double prob = kmean.probability(seq);
            double prob2 = hmm.probability(seq);

            Log.d("KMEAN TEST",Double.toString(prob));
            Log.d("BWELC TEST",Double.toString(prob2));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
