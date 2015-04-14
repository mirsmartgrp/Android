package exercise.fontys.nl.exercisecontrolbackend;

import android.text.format.Time;

import com.samsung.android.sdk.accessory.SASocket;

import java.io.IOException;
import java.util.Map;

/**
 * Created by sascha on 14.04.15.
 */
public class BackendSenderTizenSocket extends SASocket
{
    private int mConnectionId;

    public BackendSenderTizenSocket()
    {
        super(BackendSenderTizenSocket.class.getName());
    }

    @Override
    public void onError(int channelId, String errorString, int error)
    {
    }

    public int getMConnectionId()
    {
        return mConnectionId;
    }

    public void setMConnectionId(int i)
    {
        mConnectionId = i;
    }

    @Override
    public void onReceive(int channelId, byte[] data)
    {
        Map connectionMap;
        Time time = new Time();
        time.set(System.currentTimeMillis());
        String timeStr = " " + String.valueOf(time.minute) + ":" + String.valueOf(time.second);
        String strToUpdateUI = new String(data);
        final String message = strToUpdateUI.concat(timeStr);
        final BackendSenderTizenSocket uHandler;

        connectionMap = BackendSenderTizen.getConnectionMap();
        if(connectionMap == null)
        {
            return;
        }
        else
        {
            uHandler = BackendSenderTizen.getConnectionMap().get(Integer.parseInt(String.valueOf(mConnectionId)));
        }
        if (uHandler == null)
        {
            return;
        }
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    uHandler.send(BackendSenderTizen.HELLOACCESSORY_CHANNEL_ID, message.getBytes());
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onServiceConnectionLost(int errorCode)
    {
        if (BackendSenderTizen.getConnectionMap() != null)
        {
            BackendSenderTizen.getConnectionMap().remove(mConnectionId);
        }
    }
}
