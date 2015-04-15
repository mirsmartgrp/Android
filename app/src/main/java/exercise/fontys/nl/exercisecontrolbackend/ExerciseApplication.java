package exercise.fontys.nl.exercisecontrolbackend;

import android.app.Application;
import android.content.Context;

public class ExerciseApplication extends Application {

    private static Context context;
    private static FileStorage fileStorage;

    public void onCreate(){
        super.onCreate();
        ExerciseApplication.context = getApplicationContext();
        ExerciseApplication.fileStorage = new FileStorage(ExerciseApplication.context);
    }

    public static Context getContext() {
        return ExerciseApplication.context;
    }

    public static FileStorage getFileStorage() {
        return ExerciseApplication.fileStorage;
    }
}
