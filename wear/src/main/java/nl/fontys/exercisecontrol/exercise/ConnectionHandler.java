package nl.fontys.exercisecontrol.exercise;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by max on 16.04.15.
 *
 * class to read from and send to phone
 */
public class ConnectionHandler implements GoogleApiClient.ConnectionCallbacks {

    //to identify phone-watch pair
    private static final String WEAR_MESSAGE_PATH = "/system/exercise";
    private GoogleApiClient mApiClient ;
    private Context context;

    public ConnectionHandler(Context context) {
        this.context=context;
        initGoogleApiClient(context);
    }
    private void initGoogleApiClient(Context context)
    {
        mApiClient = new GoogleApiClient.Builder(context)
                .addApi( Wearable.API ).build();
        mApiClient.registerConnectionCallbacks(this);

            connectGogleClient();
            Log.d(this.getClass().getName(), Boolean.toString(mApiClient.isConnectionCallbacksRegistered(this)));
    }
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(this.getClass().getName(), "onConnected");
        Wearable.MessageApi.addListener(mApiClient, new MessageApi.MessageListener() {
            @Override
            public void onMessageReceived(MessageEvent messageEvent) {
            Log.d(this.getClass().getName(), "message received " + messageEvent.getData().toString());
            }
        });
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.d(this.getClass().getName(), "onConnectionSuspended: " + i);
    }

    public void disconnectGoogleClient() {
        mApiClient.disconnect();
        if(!mApiClient.isConnected()) {
            Log.d(this.getClass().getName(),"google client disconnected");
        }
        else {
            Log.d(this.getClass().getName(),"error disconnecting google client");
        }
    }

    public void connectGogleClient() {
        mApiClient.connect();
        if(mApiClient.isConnected()) {
            Log.d(this.getClass().getName(),"google client connected");
        }
        else {
            Log.d(this.getClass().getName(),"error connecting google client");
        }
    }

    /**
     * sending a string to the phone
     * @param message to be send to phone
     */
    public void sendMessage(final String message) {
        if(mApiClient.isConnected()) {
            Wearable.NodeApi.getConnectedNodes(mApiClient).setResultCallback(
                    new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                        @Override
                        public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                            for (Node node : getConnectedNodesResult.getNodes()) {
                                Wearable.MessageApi.sendMessage(
                                        mApiClient, node.getId(), WEAR_MESSAGE_PATH, message.getBytes()).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                                    @Override
                                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {

                                        Log.d(this.getClass().getName(), sendMessageResult.getStatus().toString());
                                    }
                                });
                            }
                        }
                    });
        }
        else {
            Log.d(this.getClass().getName(),"can NOT send message. Google client not connected");
        }
    }
}
