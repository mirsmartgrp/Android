package nl.fontys.connection;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import nl.fontys.listener.Listener;

/**
 * Created by root on 08.04.15.
 */
public class ConnectionHandlerBackend
{

    private ConnectionHandlerAndroid connectionHandlerAndroid;


    public ConnectionHandlerBackend(Context context)

    {
        connectionHandlerAndroid=new ConnectionHandlerAndroid(context);

    }



    /**
     * Send Data to the connected Smart-watch
     * @param exData The data that is send to the smart-watch
     */
    public void sendExerciseData(final String  exData) {
    if(connectionHandlerAndroid.isConnected()) {
        connectionHandlerAndroid.sendMessage(exData);
    }
        if (ConnectionHandlerTizen.isConnected()) {
              Log.d("BackendSenderTizen","is connected");
            try {
                ConnectionHandlerTizen.sendString(exData);
            } catch (IOException e) {
                Log.e(this.getClass().getName(),"can not send message to tizen",e);
            }
        }else{
            Log.d("BackendSenderTizen"," is not connected");
        }
    }


    public void addListener(Listener listener){
        connectionHandlerAndroid.addListener(listener);
        ConnectionHandlerTizen.addListener(listener);
    }

    public void removeListener(Listener listener){
        connectionHandlerAndroid.removeListener(listener);
        ConnectionHandlerTizen.removeListener(listener);
    }

}
