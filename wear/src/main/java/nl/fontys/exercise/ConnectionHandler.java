package nl.fontys.exercise;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by root on 16.04.15.
 */
public class ConnectionHandler implements GoogleApiClient.ConnectionCallbacks {

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

            mApiClient.connect();
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
}
