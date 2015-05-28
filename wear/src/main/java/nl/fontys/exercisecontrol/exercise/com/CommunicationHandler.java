package nl.fontys.exercisecontrol.exercise.com;

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
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import nl.fontys.exercisecontrol.exercise.collector.ExerciseData;
import nl.fontys.exercisecontrol.exercise.exlist.ExercisesObject;
import nl.fontys.exercisecontrol.exercise.feedback.Feedback;

/**
 * Communication handler for communication with mobile application.
 */
public abstract class CommunicationHandler {

    private static final String WEAR_MESSAGE_PATH = "/system/exercise";

    private final Context context;
    private final Gson gson;
    private GoogleApiClient apiClient;
    private final CommunicationHandlerConnectionCallbacks callbacks;

    public CommunicationHandler(Context context) {
        this.context = context;
        gson = new Gson();
        callbacks = new CommunicationHandlerConnectionCallbacks();
    }

    public void initialize() {
        apiClient = new GoogleApiClient.Builder(context).addApi(Wearable.API).build();
        apiClient.registerConnectionCallbacks(callbacks);
        Wearable.MessageApi.addListener(apiClient, callbacks);
    }

    public void terminate() {
        if (apiClient.isConnected()) {
            apiClient.disconnect();
        }

        Wearable.MessageApi.removeListener(apiClient, callbacks);
        apiClient.unregisterConnectionCallbacks(callbacks);
    }

    public void ensureConnection() throws CommunicationException {
        if (!apiClient.isConnected()) {
            apiClient.connect();
        }

        if (!apiClient.isConnected())
            throw new CommunicationException("Failed to connect.");
    }

    public void sendExerciseData(ExerciseData obj) throws CommunicationException {
        sendMessage(gson.toJson(obj).getBytes());
    }

    public void sendMessage(final byte[] message) throws CommunicationException {
        ensureConnection();

        Wearable.NodeApi.getConnectedNodes(apiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {

            @Override
            public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                for (Node node : getConnectedNodesResult.getNodes()) {
                    Wearable.MessageApi.sendMessage(apiClient, node.getId(), WEAR_MESSAGE_PATH, message).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            if (sendMessageResult.getStatus().isSuccess()) {
                                sendSuccess();
                            } else {
                                sendFailure();
                            }
                        }
                    });
                }
            }
        });
    }

    public abstract void sendSuccess();
    public abstract void sendFailure();

    public abstract void receivedFeedback(Feedback obj);
    public abstract void receivedExercises(ExercisesObject obj);

    private class CommunicationHandlerConnectionCallbacks implements GoogleApiClient.ConnectionCallbacks, MessageApi.MessageListener {

        @Override
        public void onConnected(Bundle bundle) {

        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onMessageReceived(MessageEvent messageEvent) {
            JsonElement json = gson.fromJson(messageEvent.getData().toString(), JsonElement.class);

            if (!json.isJsonObject()) return;

            if (json.getAsJsonObject().has("exercises")) {
                ExercisesObject obj = gson.fromJson(json, ExercisesObject.class);
                receivedExercises(obj);
            }

            if (json.getAsJsonObject().has("feedback")) {
                Feedback obj = gson.fromJson(json, Feedback.class);
                receivedFeedback(obj);
            }
        }
    }
}
