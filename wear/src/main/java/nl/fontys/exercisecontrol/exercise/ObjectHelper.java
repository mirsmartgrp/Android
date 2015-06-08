package nl.fontys.exercisecontrol.exercise;

import android.content.Context;

/**
 * Created by root on 08.06.15.
 */
public class ObjectHelper {

    private static ObjectHelper instance = null;


    private  ObjectHelper(Context context){
        connectionHandler = new ConnectionHandler(context);
    }

    private static  ConnectionHandler connectionHandler;


    public  ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public static ObjectHelper getInstance(Context context){
    if(instance == null){
        instance = new ObjectHelper(context);
    }
    return instance;
}


}
