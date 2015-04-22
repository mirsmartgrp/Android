package nl.fontys.exercisecontrol.connection;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

import nl.fontys.exercisecontrol.listener.Listener;

/**
 * Created by root on 08.04.15.
 */
public class ConnectionHandlerAndroid
        implements ConnectionCallbacks,
                   MessageApi.MessageListener
{

    private static final String WEAR_MESSAGE_PATH = "/system/exercise";
    private static final String TAG               = "sensorValues";
    private GoogleApiClient      mApiClient;
    private ArrayAdapter<String> mAdapter;
    private ListView mListView;
 private List<Listener> listeners = new ArrayList<Listener>();

    public ConnectionHandlerAndroid(Context context)
    {
        initGoogleApiClient(context);

    }

    public boolean isConnected()
    {
        return mApiClient.isConnected();
    }

    private void initGoogleApiClient(Context context)
    {
        mApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API).build();
        if (mApiClient != null && !(mApiClient.isConnected() || mApiClient.isConnecting()))
        {
            mApiClient.connect();
            Wearable.MessageApi.addListener(mApiClient,
                                            this);
            Log.d("ConnectionTest",
                  Boolean.toString(mApiClient.isConnectionCallbacksRegistered(this)));
        }
    }

    public void sendMessage(final String message)
    {
        if (mApiClient.isConnected())
        {
            Wearable.NodeApi.getConnectedNodes(mApiClient).setResultCallback(
                    new ResultCallback<NodeApi.GetConnectedNodesResult>()
                    {
                        @Override
                        public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult)
                        {
                            for (Node node : getConnectedNodesResult.getNodes())
                            {
                                Wearable.MessageApi.sendMessage(
                                        mApiClient,
                                        node.getId(),
                                        WEAR_MESSAGE_PATH,
                                        message.getBytes()).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>()
                                {
                                    @Override
                                    public void onResult(MessageApi.SendMessageResult sendMessageResult)
                                    {

                                        Log.d(this.getClass().getName(),
                                              sendMessageResult.getStatus().toString());
                                    }
                                });
                            }
                        }
                    });
        }
        else
        {
            Log.d(this.getClass().getName(),
                  "can NOT send message. Google client not connected");
        }
    }

    /**
     * Is called when the connection is successfully created.
     *
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle)
    {
        //mApiClient.connect();
        Log.i(TAG,
              "Connected in ConnectionListener");
    }

    /**
     * Is called when the connection is suspended.
     *
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i)
    {
        Log.i(TAG,
              "ConnectionSuspended in ConnectionListener");
    }

    public void addListener(Listener listener)
    {
        listeners.add(listener);
    }

    public void removeListener(Listener listener)
    {
        listeners.remove(listener);
    }


    /**
     * Is called when a message is received and sends the Message to the parser
     *
     * @param messageEvent
     */
    public void onMessageReceived(MessageEvent messageEvent)
    {

        if (messageEvent.getPath().equalsIgnoreCase(WEAR_MESSAGE_PATH))
        {
            String data = new String(messageEvent.getData());
            for (Listener l : listeners)
            {
                l.onNotify(data);
            }
        }
    }


}
