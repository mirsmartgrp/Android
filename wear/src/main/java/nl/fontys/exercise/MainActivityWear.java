package nl.fontys.exercise;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivityWear extends Activity {

    private TextView mTextView;
    private ConnectionHandler handler;
    private SensorDataCollector collector;
    private String messageToSendToPhone="undefined";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new ConnectionHandler(this);
        //collector = new SensorDataCollector(this);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });

    }

    @Override
    public void onDestroy() {
    super.onDestroy();
    handler.disconnectGoogleClient();
    }

    /**
     * start the measurement
     * @param view
     */
    public void start(View view) {

                collector = new SensorDataCollector(this);
        collector.start();
      //  collector.registerSensors();
    }

    /**
     * stop the measurement
     * @param view
     */
    public void stop(View view) {
        Log.d(this.getClass().getName(), "stoping....");
        if (collector != null) {
            //collector.unRegisterSensors();
            collector.stop();
            messageToSendToPhone = collector.getExerciseData();
            collector.resetData();
            Log.d(this.getClass().getName(), "...stopped");
        } else {
            Log.d(this.getClass().getName(), "click start before stop");
        }
    }


    /**
     * send message to phone
     * @param view
     */
    public void sendMessage(View view) {
        if (collector != null) {
            String msg = collector.getExerciseData();
            handler.sendMessage(msg);
            Log.d(this.getClass().getName(), "message send");

        } else {
            Log.d(this.getClass().getName(), "click start then stop then send");
        }

    }


    public void sendMessageToPhone(View view) {
        sendMessage(view);
    }
}
