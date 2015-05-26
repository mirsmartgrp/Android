package nl.fontys.exercisecontrol.exercise.exlist;

import com.google.gson.Gson;

/**
 * Created by Vincent on 21.05.15.
 */
public class ExercisesObjectParser {

    public ExercisesObjectParser() {
    }

    public ExercisesObject parse(String json) {
        Gson gson = new Gson();

        ExercisesObject obj = gson.fromJson(json, ExercisesObject.class);
        return obj;
    }
}
