package nl.fontys.exercise;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by root on 08.04.15.
 */
public class ConnectionListener implements ConnectionCallbacks,MessageApi.MessageListener {

    private static final String WEAR_MESSAGE_PATH = "/system/exercise";
    private static final String TAG = "sensorValues";
    private GoogleApiClient mApiClient;
    private ArrayAdapter<String> mAdapter;
    private ListView mListView;
    private BackendReceiver backendReceiver;

    public ConnectionListener(BackendReceiver receiver, Context context)
    {
        initGoogleApiClient(context);
        backendReceiver = receiver;

    }

    private void initGoogleApiClient(Context context)
    {
        mApiClient = new GoogleApiClient.Builder(context)
                .addApi( Wearable.API ).build();
        if( mApiClient != null && !( mApiClient.isConnected() || mApiClient.isConnecting() ) )
        {
            mApiClient.connect();
            Wearable.MessageApi.addListener(mApiClient,this);
            Log.d("ConnectionTest",Boolean.toString(mApiClient.isConnectionCallbacksRegistered(this)));
        }
    }


    /**
     * Is called when the connection is successfully created.
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle)
    {
        //mApiClient.connect();
        Log.i(TAG, "Connected in ConnectionListener");
    }

    /**
     * Is called when the connection is suspended.
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i)
    {
        Log.i(TAG, "ConnectionSuspended in ConnectionListener");
    }



    /**
     * Is called when a message is received and sends the Message to the parser
     * @param messageEvent
     */
    public void onMessageReceived(MessageEvent messageEvent)
    {
       // if( messageEvent.getPath().equalsIgnoreCase( WEAR_MESSAGE_PATH ) )
       // {
            Log.d("CLTest", "Received Text: " + new String(messageEvent.getData()));
            backendReceiver.parseToExerciseData(new String(messageEvent.getData()));

    //    }
    }


}
