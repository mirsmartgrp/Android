package nl.fontys.exercisecontrol.exercise;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;

/**
 * Created by Ron Gebauer on 20.04.15.
 * <p/>
 * the exercise itself.
 */
public class Exercise {
    private final String GUID;
    private final String NAME;
    private final String DESCRIPTION;
    private final ExerciseData EXERCISE_DATA;
    private Hmm<ObservationVector> hmm;
    private List<String> sequences;


    /**
     * Constructs an exercise object with given NAME.
     *
     * @param name the NAME of the exercise
     */


    Exercise(String guid) {
        this.GUID = guid;
        this.NAME = "name";
        this.DESCRIPTION = "";
        this.EXERCISE_DATA = new ExerciseData();
        this.hmm = null;
        this.sequences = null;
    }

    public String getGUID() {
        return this.GUID;
    }


    public void setHmm(Hmm<ObservationVector> hmm) {
        this.hmm = hmm;
    }

    public void setSequences(List<String> sequences) {
        this.sequences = sequences;
    }

    public Hmm<ObservationVector> getHmm() {
        return hmm;
    }

    public List<String> getSequences() {
        return sequences;
    }


    public static Exercise parseExercise(JSONObject jsonObject) {
        Exercise exercise = null;

        try {
            if (jsonObject.length() <= 0) {
                throw new NullPointerException("jsonObject is empty");
            }

            exercise = new Exercise(jsonObject.getString("guid"));

            JSONArray jsonArray = jsonObject.getJSONArray("sensorData");
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject tmpJsonObject = jsonArray.getJSONObject(index);
                Double time = tmpJsonObject.getDouble("time");
                JSONObject accelerometer = tmpJsonObject.getJSONObject("accelerometer");
                JSONObject gyroscope = tmpJsonObject.getJSONObject("gyroscope");
                Double[][] coordinates = new Double[][]{
                        {
                                Double.parseDouble(accelerometer.get("x").toString()),
                                Double.parseDouble(accelerometer.get("y").toString()),
                                Double.parseDouble(accelerometer.get("z").toString())
                        },
                        {
                                Double.parseDouble(gyroscope.get("x").toString()),
                                Double.parseDouble(gyroscope.get("y").toString()),
                                Double.parseDouble(gyroscope.get("z").toString())
                        }
                };

                exercise.getEXERCISE_DATA().addSingleExerciseData(time,
                        coordinates);
            }
        } catch (JSONException e) {
            Log.e("parseExercise",
                    e.getMessage());
        }

        return exercise;
    }


    public String getNAME() {
        return NAME;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public ExerciseData getEXERCISE_DATA() {
        return EXERCISE_DATA;
    }
}
