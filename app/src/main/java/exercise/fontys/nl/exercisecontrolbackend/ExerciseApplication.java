package exercise.fontys.nl.exercisecontrolbackend;

import android.app.Application;
import android.content.Context;

public class ExerciseApplication extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        ExerciseApplication.context = getApplicationContext();
    }

    public static Context getContext() {
        return ExerciseApplication.context;
    }
}
