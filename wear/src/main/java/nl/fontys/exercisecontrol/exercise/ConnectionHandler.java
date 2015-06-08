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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import nl.fontys.exercisecontrol.exercise.exlist.Exercise;
import nl.fontys.exercisecontrol.exercise.exlist.ExerciseList;

import static android.support.v4.app.ActivityCompat.startActivity;

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
            if(mApiClient.isConnected()){
                onConnected(null);
            }else {
                connectGogleClient();
            }
            Log.d(this.getClass().getName(), Boolean.toString(mApiClient.isConnectionCallbacksRegistered(this)));
    }
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(this.getClass().getName(), "onConnected");
        Wearable.MessageApi.addListener(mApiClient, new MessageApi.MessageListener() {
            @Override
            public void onMessageReceived(MessageEvent messageEvent) {
                String data = new String(messageEvent.getData());
                Log.d(this.getClass().getName(), "message received " + data);
                if (isFeedback(data)) {
                    handleFeedback(data);
                } else if (isDownload(data)) {
                    handleDownload(data);
                } else if(isSelect(data)) {
                    handleSelect(data);
                }else{
                    Log.d(this.getClass().getName(), "unknown message " + messageEvent.getData());
                }
            }
        });
    }

    private void handleSelect(String data){

        try {
            JSONObject json = new JSONObject(data);
            String guid = (String) json.get("selected");
            List<Exercise> exerciseList = SelectExerciseActivityWear.getExerciseList();
            if(exerciseList != null){

                Exercise exercise = null;
                for(Exercise ex : exerciseList){
                    if(ex.getGuid().equals(guid)){
                        exercise = ex;
                        break;
                    }
                }
                if(exercise != null){
                    Intent i = new Intent(context, MainActivityWear.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle bundle = new Bundle();
                    bundle.putString(SelectExerciseActivityWear.EXERCISE_NAME, exercise.getName());
                    bundle.putString(SelectExerciseActivityWear.EXERCISE_GUID, exercise.getGuid());
                    i.putExtras(bundle);
                    context.startActivity(i);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * Download exercise list
     * or just copy string ?
     */
    private void handleDownload(String jsonData) {
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
     *
     */
    private void handleFeedback(String data) {
        //TODO impl
        Intent i=new Intent(context,MessageViewActivity.class);
        i.putExtra(FEEDBACK, data);
        context.startActivity(i);
    }

    /**
     * TODO: determine how to check if feedback
     *
     * @return true if message indicates to download exercises
     */
    private boolean isDownload(String data) {
        return data.contains("exercise");

    }

    private boolean isSelect(String data){
        return data.contains("selected");
    }


    /**
     *
     * @return true if messae contains feedback for exercises
     */
    private boolean isFeedback(String data) {
        return data.contains("feedback");
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
