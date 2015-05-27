package nl.fontys.exercisecontrol.exercise;

import java.util.List;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;

/**
 * Created by Ron Gebauer on 20.04.15.
 * <p/>
 * the exercise itself.
 */
public class Exercise
{
    private final String                 GUID;
    private final String                 NAME;
    private final String                 DESCRIPTION;
    private final String                 VIDEO_IMAGE;
    private final ExerciseData           EXERCISE_DATA;
    private       Hmm<ObservationVector> hmm;
    private       List<String>           sequences;


    /**
     * Constructs an exercise object with given NAME.
     *
     * @param guid        the GUID of the exercise
     * @param name        the NAME of the exercise
     * @param description the DESCRIPTION of the exercise
     * @param videoImage  the VIDEO_IMAGE of the exercise
     */
    Exercise(String guid,
             String name,
             String description,
             String videoImage)
    {
        this.GUID = guid;
        this.NAME = name;
        this.DESCRIPTION = description;
        this.VIDEO_IMAGE = videoImage;
        this.EXERCISE_DATA = new ExerciseData();
        this.hmm = null;
        this.sequences = null;
    }

    public String getGUID()
    {
        return GUID;
    }

    public Hmm<ObservationVector> getHmm()
    {
        return hmm;
    }

    public void setHmm(Hmm<ObservationVector> hmm)
    {
        this.hmm = hmm;
    }

    public List<String> getSequences()
    {
        return sequences;
    }

    public void setSequences(List<String> sequences)
    {
        this.sequences = sequences;
    }

    public String getNAME()
    {
        return NAME;
    }

    public String getDESCRIPTION()
    {
        return DESCRIPTION;
    }

    public String getVIDEO_IMAGE()
    {
        return VIDEO_IMAGE;
    }

    public ExerciseData getEXERCISE_DATA()
    {
        return EXERCISE_DATA;
    }

    @Override
    public String toString()
    {
        return NAME;
    }
}
