package nl.fontys.exercise;

import com.samsung.android.sdk.accessory.SASocket;

/**
 * Created by sascha on 14.04.15.
 */
public class ConnectionSocketTizen extends SASocket
{
    private int mConnectionId;

   public ConnectionSocketTizen()
    {
        super(ConnectionSocketTizen.class.getName());
    }

    /**
     * Set the backend receiver that is called when a message is received
     */
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

    /**
     * Is called when a message from the Smartwatch is recived. Should not be called by the Backend
     * The data is then given to the reciver to transform the data into ExerciseData
     * @param channelId the Id of the channel
     * @param data the data that is recived
     */
    @Override
    public void onReceive(int channelId, byte[] data)
    {
        String dataStr = new String (data);
        for(Listener l: ConnectionHandlerTizen.getListenerList()){
            l.onNotify(dataStr);
        }
    }

    /**
     * Is called when the connection is lost
     * @param errorCode the code of the error
     */
    @Override
    protected void onServiceConnectionLost(int errorCode)
    {
        if (ConnectionHandlerTizen.getConnectionMap() != null)
        {
            ConnectionHandlerTizen.getConnectionMap().remove(mConnectionId);
        }
    }
}
