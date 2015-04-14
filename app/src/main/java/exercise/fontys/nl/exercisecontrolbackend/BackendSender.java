package exercise.fontys.nl.exercisecontrolbackend;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by root on 08.04.15.
 */
public class BackendSender implements GoogleApiClient.ConnectionCallbacks
{
    private ConnectionListener listener;
    private ExerciseData exerciseData;
    private GoogleApiClient mApiClient;
    private static final String WEAR_MESSAGE_PATH = "/message";
    private static final String androidPath = "path";
    private boolean connectetAndroid;


    public BackendSender()
    {
        initGoogleApiClient();
    }

    /**
     * Initialize the Goold Api Client and connect it, is called in the constructor.
     */
    private void initGoogleApiClient() {
       // mApiClient = new GoogleApiClient.Builder( this )
       //         .addApi( Wearable.API )
      //          .build();
      //  mApiClient.connect();
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
                        mApiClient, node.getId(), androidPath, exData.getBytes()).await();
            }
        }
        else
        {
            //TODO Implement sending to Tizen.
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
