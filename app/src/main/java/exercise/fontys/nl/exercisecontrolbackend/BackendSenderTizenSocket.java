package exercise.fontys.nl.exercisecontrolbackend;

import android.text.format.Time;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.samsung.android.sdk.accessory.SASocket;

import java.io.IOException;
import java.util.Map;

/**
 * Created by sascha on 14.04.15.
 */
public class BackendSenderTizenSocket extends SASocket
{
    private int mConnectionId;
    private BackendReceiver backendReceiver;

    public BackendSenderTizenSocket()
    {
        super(BackendSenderTizenSocket.class.getName());

    }

    /**
     * Set the backend receiver that is called when a message is received
     * @param receiver the BackendReceiver that is called when a message is received
     */
    public void setBackendReceiver(BackendReceiver receiver)
    {
        backendReceiver = receiver;
    }

    @Override
    public void onError(int channelId, String errorString, int error)
    {
    }

    public int getMConnectionId()
    {
        return mConnectionId;
    }

    public void setMConnectionId(int i)
    {
        mConnectionId = i;
    }

    /**
     * Is called when a message from the Smartwatch is recived. Should not be called by the Backend
     * The data is then given to the reciver to transform the data into ExerciseData
     * @param channelId the Id of the channel
     * @param data the data that is recived
     */
    @Override
    public void onReceive(int channelId, byte[] data)
    {
        Log.d("BSTSocket", "Recived text: " + new String(data));
        if(backendReceiver != null)
        {
            backendReceiver.parseToExerciseData(new String(data));
        }
        else
        {
            Log.d("BSTSocket", "No Receiver specified, could not proccess data");
        }

    }

    /**
     * Is called when the connection is lost
     * @param errorCode the code of the error
     */
    @Override
    protected void onServiceConnectionLost(int errorCode)
    {
        if (BackendSenderTizen.getConnectionMap() != null)
        {
            BackendSenderTizen.getConnectionMap().remove(mConnectionId);
        }
    }
}
