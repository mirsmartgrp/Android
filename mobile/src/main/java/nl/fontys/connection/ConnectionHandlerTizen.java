package nl.fontys.connection;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SAAuthenticationToken;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import nl.fontys.listener.Listener;

/**
 * Created by sascha on 14.04.15.
 */
public class ConnectionHandlerTizen
        extends SAAgent
{
    public static final int                                 SERVICE_CONNECTION_RESULT_OK = 0;
    public static final int                                 HELLOACCESSORY_CHANNEL_ID    = 104;
    public static final String                              TAG                          = "BackendSenderTizen";
    private static      int                                 connectionID                 = -1;
    private static      Map<Integer, ConnectionSocketTizen> mConnectionsMap              = new HashMap<Integer, ConnectionSocketTizen>();
    private static      boolean                             isConnected                  = false;
    private static List<Listener> listeners = new ArrayList<Listener>();
    //
<<<<<<< HEAD:mobile/src/main/java/nl/fontys/exercise/ConnectionHandlerTizen.java
    private final IBinder mBinder = new LocalBinder();
    private int authCount = 1;
    private static Map<Integer, ConnectionSocketTizen> mConnectionsMap = new HashMap<>();
    private static List<Listener> listeners = new ArrayList<Listener>();
=======
    private final       IBinder                             mBinder                      = new LocalBinder();
    public              Boolean                             isAuthentication             = false;
    public              Context                             mContext                     = null;
    private             int                                 authCount                    = 1;

>>>>>>> 65bcb7061b38b12aaa8912561ba21f558e3d3f07:mobile/src/main/java/nl/fontys/connection/ConnectionHandlerTizen.java
    public ConnectionHandlerTizen()
    {
        super(TAG,
              ConnectionSocketTizen.class);
    }

    public static Map<Integer, ConnectionSocketTizen> getConnectionMap()
    {
        return mConnectionsMap;
    }

    public static boolean isConnected()
    {
        return !mConnectionsMap.isEmpty();
    }

    private static byte[] getApplicationCertificate(Context context)
    {
        if (context == null)
        {
            return null;
        }
        byte[] cert = null;
        String packageName = context.getPackageName();
        if (context != null)
        {
            try
            {
                PackageInfo pkgInfo =
                        context.getPackageManager().getPackageInfo(packageName,
                                                                   PackageManager.GET_SIGNATURES);
                if (pkgInfo == null)
                {
                    return null;
                }
                Signature[] sigs = pkgInfo.signatures;
                if (sigs == null)
                {
                }
                else
                {
                    X509Certificate x509cert = X509Certificate.getInstance(sigs[0].toByteArray());
                    cert = x509cert.getPublicKey().getEncoded();
                }
            }
            catch (PackageManager.NameNotFoundException e)
            {
                e.printStackTrace();

            }
            catch (CertificateException e)
            {
                e.printStackTrace();
            }
        }
        return cert;
    }

    /**
     * Sends the given String to the Smartwatch
     *
     * @param text The String that will be send
     * @throws java.io.IOException
     */
    public static void sendString(String text)
            throws IOException
    {
        ConnectionSocketTizen cHandler = mConnectionsMap.get(connectionID);
        cHandler.send(HELLOACCESSORY_CHANNEL_ID,
                      text.getBytes());
        Log.d(TAG,
              "Send Message " + text);
    }

    public static List<Listener> getListenerList()
    {
        return listeners;
    }

    public static void addListener(Listener listener)
    {
        listeners.add(listener);
    }

    public static void removeListener(Listener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Is called when the BackendSender is Created
     */
    @Override
    public void onCreate()
    {
        super.onCreate();
        SA mAccessory = new SA();
        try
        {
            mAccessory.initialize(this);
        }
        catch (SsdkUnsupportedException e)
        {
            // Error Handling
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
/*
* Your application can not use Samsung Accessory SDK.
* You application should work smoothly without using this SDK, or you may want
* to notify user and close your application gracefully (release resources,
* stop Service threads, close UI thread, etc.)
*/
            stopSelf();
        }
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return mBinder;
    }

    @Override
    protected void onFindPeerAgentResponse(SAPeerAgent arg0,
                                           int arg1)
    {
        // TODO Auto-generated method stub
    }

    protected void onAuthenticationResponse(SAPeerAgent uPeerAgent,
                                            SAAuthenticationToken authToken,
                                            int error)
    {
        if (authToken.getAuthenticationType() == SAAuthenticationToken.AUTHENTICATION_TYPE_CERTIFICATE_X509)
        {
            mContext = getApplicationContext();
            byte[] myAppKey = getApplicationCertificate(mContext);

            if (authToken.getKey() != null)
            {
                boolean matched = true;
                if (authToken.getKey().length != myAppKey.length)
                {
                    matched = false;
                }
                else
                {
                    for (int i = 0; i < authToken.getKey().length; i++)
                    {
                        if (authToken.getKey()[i] != myAppKey[i])
                        {
                            matched = false;
                        }
                    }
                }
                if (matched)
                {
                    acceptServiceConnectionRequest(uPeerAgent);
                }
            }
        }
        else if (authToken.getAuthenticationType() == SAAuthenticationToken.AUTHENTICATION_TYPE_NONE)
        {
            Log.e(TAG,
                  "onAuthenticationResponse : CERT_TYPE(NONE)");
        }
    }

    @Override
    protected void onServiceConnectionResponse(SAPeerAgent peerAgent,
                                               SASocket thisConnection,
                                               int result)
    {
        Log.d(TAG,
              "onServiceConnectionResponse");
        if (result == CONNECTION_SUCCESS)
        {
            if (thisConnection != null)
            {
                ConnectionSocketTizen myConnection =
                        (ConnectionSocketTizen) thisConnection;

                myConnection.setMConnectionId((int) (System.currentTimeMillis() & 255));
                connectionID = myConnection.getMConnectionId();
                mConnectionsMap.put(myConnection.getMConnectionId(),
                                    myConnection);

                isConnected = true;
                Log.d(TAG,
                      Integer.toString(connectionID));
                Log.d(TAG,
                      Boolean.toString(isConnected));

            }
        }
        else if (result == CONNECTION_ALREADY_EXIST)

        {
            Log.e(TAG,
                  "onServiceConnectionResponse, CONNECTION_ALREADY_EXIST");
        }
    }

    @Override
    protected void onServiceConnectionRequested(SAPeerAgent peerAgent)
    {
/*
* The authenticatePeerAgent(peerAgent) API may not be working properly depending on the firmware
* version of accessory device. Recommend to upgrade accessory device firmware if possible.
*/
        //
        //
        //
        //
        //
        if (authCount % 2 == 1)
        {
            isAuthentication = false;
        }
        else
        {
            isAuthentication = true;
        }
        authCount++;
        isAuthentication = false;
        if (isAuthentication)
        {
            Toast.makeText(getBaseContext(),
                           "Authentication On!",
                           Toast.LENGTH_SHORT).show();
            authenticatePeerAgent(peerAgent);
        }
        else
        {
            Toast.makeText(getBaseContext(),
                           "Authentication Off!",
                           Toast.LENGTH_SHORT).show();
            acceptServiceConnectionRequest(peerAgent);
        }
    }

    public class LocalBinder
            extends Binder
    {
        public ConnectionHandlerTizen getService()
        {
            return ConnectionHandlerTizen.this;
        }
    }


<<<<<<< HEAD:mobile/src/main/java/nl/fontys/exercise/ConnectionHandlerTizen.java


    public static List<Listener> getListenerList(){
        return listeners;
    }

    public void addListener(Listener listener){
        listeners.add(listener);
    }

    public void removeListener(Listener listener){
        listeners.remove(listener);
    }


=======
>>>>>>> 65bcb7061b38b12aaa8912561ba21f558e3d3f07:mobile/src/main/java/nl/fontys/connection/ConnectionHandlerTizen.java
}
