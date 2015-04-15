package exercise.fontys.nl.exercisecontrolbackend;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
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
    private boolean connectedAndroid;


    public BackendSender(Context context)

    {
        initGoogleApiClient(context);
    }

    /**
     * Initialize the Google Api Client and connect it, is called in the constructor.
     */
    private void initGoogleApiClient(Context context) {
        mApiClient = new GoogleApiClient.Builder( context )
                    .addApi(Wearable.API).addConnectionCallbacks(this)
                    .build();
         mApiClient.connect();
    }

    /**
     * Send Data to the connected Smart-watch
     * @param exData The data that is send to the smart-watch
     * @return Returns Null, because no data is expected to be gathered.
     */
    public ExerciseData sendExerciseData(final String  exData) {
        if(connectedAndroid)
        {
              if(mApiClient.isConnected()) {
                  Wearable.NodeApi.getConnectedNodes(mApiClient).setResultCallback(
                          new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                              @Override
                              public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                                  for (Node node : getConnectedNodesResult.getNodes()) {
                                      Wearable.MessageApi.sendMessage(
                                              mApiClient, node.getId(), WEAR_MESSAGE_PATH, exData.getBytes()).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                                          @Override
                                          public void onResult(MessageApi.SendMessageResult sendMessageResult) {

                                              Log.d("AndroidSend", sendMessageResult.getStatus().toString());
                                          }
                                      });
                                  }
                              }
                          });
              }else{
                  Log.e("AndroidSend","Not connected");
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
        Log.d("sensorValues","Sender connected");
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    public void setReceiverAndroid()
    {
        connectedAndroid = true;
    }

    public void setReceiverTizen()
    {
        connectedAndroid = false;
    }

    //TODO Find alternative or fix to "this" in builder.
}
