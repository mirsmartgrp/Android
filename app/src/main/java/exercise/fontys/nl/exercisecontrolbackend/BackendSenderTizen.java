package exercise.fontys.nl.exercisecontrolbackend;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Binder;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SAAuthenticationToken;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;

import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

/**
 * Created by sascha on 14.04.15.
 */
public class BackendSenderTizen extends SAAgent
{
    public static final int SERVICE_CONNECTION_RESULT_OK = 0;
    public static final int HELLOACCESSORY_CHANNEL_ID = 104;
    public static final String TAG = "BackendSenderTizen";
    public Boolean isAuthentication = false;
    public Context mContext = null;
    private static int connectionID;
    //
    private final IBinder mBinder = new LocalBinder();
    private int authCount = 1;
    private static Map<Integer, BackendSenderTizenSocket> mConnectionsMap = null;

    public BackendSenderTizen()
    {
        super(TAG, BackendSenderTizenSocket.class);
    }

    public static Map<Integer, BackendSenderTizenSocket> getConnectionMap()
    {
        return mConnectionsMap;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        SA mAccessory = new SA();
        try
        {
            mAccessory.initialize(this);
        } catch (SsdkUnsupportedException e)
        {
// Error Handling
        } catch (Exception e1)
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
    protected void onFindPeerAgentResponse(SAPeerAgent arg0, int arg1)
    {
// TODO Auto-generated method stub
    }

    protected void onAuthenticationResponse(SAPeerAgent uPeerAgent,
                                            SAAuthenticationToken authToken, int error)
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
                } else
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
        } else if (authToken.getAuthenticationType() == SAAuthenticationToken.AUTHENTICATION_TYPE_NONE)
            Log.e(TAG, "onAuthenticationResponse : CERT_TYPE(NONE)");
    }

    @Override
    protected void onServiceConnectionResponse(SAPeerAgent peerAgent, SASocket thisConnection, int result)
    {
        if (result == CONNECTION_SUCCESS)
        {
            if (thisConnection != null)
            {
                BackendSenderTizenSocket myConnection =
                        (BackendSenderTizenSocket) thisConnection;
                if (mConnectionsMap == null)
                {
                    mConnectionsMap = new HashMap<Integer, BackendSenderTizenSocket>();
                }
                myConnection.setMConnectionId((int) (System.currentTimeMillis() & 255));
                connectionID = myConnection.getMConnectionId();
                mConnectionsMap.put(myConnection.getMConnectionId(), myConnection);
                Log.d(TAG, Integer.toString(connectionID));

            }
        } else if (result == CONNECTION_ALREADY_EXIST)

        {
            Log.e(TAG, "onServiceConnectionResponse, CONNECTION_ALREADY_EXIST");
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
            isAuthentication = false;
        else
            isAuthentication = true;
        authCount++;
        isAuthentication = false;
        if (isAuthentication)
        {
            Toast.makeText(getBaseContext(), "Authentication On!", Toast.LENGTH_SHORT).show();
            authenticatePeerAgent(peerAgent);
        } else
        {
            Toast.makeText(getBaseContext(), "Authentication Off!", Toast.LENGTH_SHORT).show();
            acceptServiceConnectionRequest(peerAgent);
        }
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
                        context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
                if (pkgInfo == null)
                {
                    return null;
                }
                Signature[] sigs = pkgInfo.signatures;
                if (sigs == null)
                {
                } else
                {
                    X509Certificate x509cert = X509Certificate.getInstance(sigs[0].toByteArray());
                    cert = x509cert.getPublicKey().getEncoded();
                }
            } catch (PackageManager.NameNotFoundException e)
            {
                e.printStackTrace();

            } catch (CertificateException e)
            {
                e.printStackTrace();
            }
        }
        return cert;
    }

    public static void sendString(String text) throws IOException
    {
       BackendSenderTizenSocket cHandler = mConnectionsMap.get(connectionID);
        cHandler.send(HELLOACCESSORY_CHANNEL_ID, text.getBytes());
        Log.d(TAG, "Send Message "+ text);
    }

    public class LocalBinder extends Binder
    {
        public BackendSenderTizen getService()
        {
            return BackendSenderTizen.this;
        }
    }


}
