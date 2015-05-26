package nl.fontys.exercisecontrol.exercise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
    public final static String FEEDBACK="nl.fontys.exercisecontrol.exercise.Feedback";

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
                if (isFeedback(messageEvent)) {
                    handleFeedback(messageEvent);
                } else if (isDownload(messageEvent)) {
                    handleDownload(messageEvent);
                } else {
                    Log.d(this.getClass().getName(), "unknown message " + messageEvent);
                }
            }
        });
    }

    /**
     * Download exercise list
     * or just copy string ?
     */
    private void handleDownload(MessageEvent event) {
            BufferedInputStream in = null;
            FileOutputStream fout = null;
            String urlString="";
            String filename="/raw/exerciseist";
            try {
                in = new BufferedInputStream(new URL(urlString).openStream());
                fout = new FileOutputStream(filename);

                final byte data[] = new byte[1024];
                int count;
                while ((count = in.read(data, 0, 1024)) != -1) {
                    fout.write(data, 0, count);
                }
            } catch (MalformedURLException e) {
                Log.e(this.getClass().getName(), "no valid url "+e);
            } catch (FileNotFoundException e) {
               Log.e(this.getClass().getName(),"file not found "+e);
            } catch (IOException e) {
                Log.e(this.getClass().getName(),"error reading file "+e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        Log.e(this.getClass().getName(), "error closing BufferedInputStream "+e);

                    }
                }
                if (fout != null) {
                    try {
                        fout.close();
                    } catch (IOException e) {
                        Log.e(this.getClass().getName(), "error closing FileOutputStream " + e);

                    }
                }
            }
    }

    /**
     * show feedback message
     * @param messageEvent
     */
    private void handleFeedback(MessageEvent messageEvent) {
        //TODO impl
        Intent i=new Intent(context,MessageViewActivity.class);
        i.putExtra(FEEDBACK, messageEvent.getData() );
        context.startActivity(i);
    }

    /**
     * TODO: determine how to check if feedback
     * @param messageEvent message received
     * @return true if message indicates to download exercises
     */
    private boolean isDownload(MessageEvent messageEvent) {
        return messageEvent.getData().toString().contains("feedback");

    }

    /**
     * @param messageEvent messsage received
     * @return true if messae contains feedback for exercises
     */
    private boolean isFeedback(MessageEvent messageEvent) {
        return messageEvent.getData().toString().contains("exercise");
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


    public boolean isConnected() {
        return mApiClient.isConnected();
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
