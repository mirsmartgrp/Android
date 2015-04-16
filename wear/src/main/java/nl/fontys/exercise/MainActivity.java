package nl.fontys.exercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTextView;
    private ConnectionHandler handler;
    private SensorDataCollector collector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new ConnectionHandler(this);
        collector = new SensorDataCollector(this);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });

    }

    public void sendMessage(View view) {
        String msg = collector.getExerciseData();
        handler.sendMessage(msg);
        Intent intent = new Intent(getApplicationContext(), SensorDataCollector.class);
        startActivity(intent);
    }


}
