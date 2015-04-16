package nl.fontys.exercise;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

/**
 * Created by root on 08.04.15.
 */
public class ConnectionHandlerBackend
{

    private ConnectionHandlerAndroid connectionHandlerAndroid;
    private  ConnectionHandlerTizen connectionHandlerTizen;

    public ConnectionHandlerBackend(Context context)

    {
        connectionHandlerAndroid=new ConnectionHandlerAndroid(context);
        connectionHandlerTizen = new ConnectionHandlerTizen();
    }



    /**
     * Send Data to the connected Smart-watch
     * @param exData The data that is send to the smart-watch
     */
    public void sendExerciseData(final String  exData) {
    if(connectionHandlerAndroid.isConnected()) {
        connectionHandlerAndroid.sendMessage(exData);
    }
        if (connectionHandlerTizen.isConnected()) {
            try {
                ConnectionHandlerTizen.sendString(exData);
            } catch (IOException e) {
                Log.e(this.getClass().getName(),"can not send message to tizen",e);
            }
        }
    }


    public void addListener(Listener listener){
        connectionHandlerAndroid.addListener(listener);
        connectionHandlerTizen.addListener(listener);
    }

    public void removeListener(Listener listener){
        connectionHandlerAndroid.removeListener(listener);
        connectionHandlerTizen.removeListener(listener);
    }

}
