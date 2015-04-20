package nl.fontys;

import android.app.Application;
import android.content.Context;

import nl.fontys.storage.FileStorage;

public class MyApplication
        extends Application
{

    private static Context     context;
    private static FileStorage fileStorage;

    public static Context getContext()
    {
        return MyApplication.context;
    }

    public static FileStorage getFileStorage()
    {
        return MyApplication.fileStorage;
    }

    public void onCreate()
    {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        MyApplication.fileStorage = new FileStorage(MyApplication.context);
    }
}
