package exercise.fontys.nl.exercisecontrolbackend;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;

/**
 * Created by root on 08.04.15.
 */
public class BackendSender implements GoogleApiClient.ConnectionCallbacks
{
    private ConnectionListener listener;
    private ExerciseData exerciseData;
    private GoogleApiClient mApiClient;
    private static final String WEAR_MESSAGE_PATH = "/system/exercise";
    private boolean connectetAndroid;


    public BackendSender(Context context)

    {
        initGoogleApiClient(context);
    }

    /**
     * Initialize the Goold Api Client and connect it, is called in the constructor.
     */
    private void initGoogleApiClient(Context context) {
        mApiClient = new GoogleApiClient.Builder( context )
                    .addApi(Wearable.API)
                    .build();
         mApiClient.connect();
    }

    /**
     * Send Data to the connected Smartwatch
     * @param exData The data that is send to the smartwatch
     * @return Returns Null, because no data is expected to be gathered.
     */
    public ExerciseData sendExerciseData(String exData) {
        if(connectetAndroid)
        {
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
            for (Node node : nodes.getNodes())
            {
                MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                        mApiClient, node.getId(), WEAR_MESSAGE_PATH, exData.getBytes()).await();
            }
        }
        else
        {
            try
            {
                BackendSenderTizen.sendString(exData);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onConnected(Bundle bundle)
    {

    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    public void setReciverAndroid()
    {
        connectetAndroid = true;
    }

    public void setReciverTizen()
    {
        connectetAndroid = false;
    }

    //TODO Find alternative or fix to "this" in builder.
}
