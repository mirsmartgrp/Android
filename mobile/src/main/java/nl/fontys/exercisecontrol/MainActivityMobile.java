package nl.fontys.exercisecontrol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import nl.fontys.exercisecontrol.connection.ConnectionHandlerBackend;
import nl.fontys.exercisecontrol.exercise.Exercise;
import nl.fontys.exercisecontrol.exercise.R;
import nl.fontys.exercisecontrol.listener.Listener;


public class MainActivityMobile
        extends Activity
{
    public static Context                  context;
    private       ConnectionHandlerBackend connectionHandlerBackend;
    private       TextView                 helloWorldTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        context = getBaseContext();

        super.onCreate(savedInstanceState);
        connectionHandlerBackend = new ConnectionHandlerBackend(this);
        setContentView(R.layout.activity_main);
        helloWorldTextView = (TextView) findViewById(R.id.helloWorldTextView);
        Button exerciseButton = (Button) findViewById(R.id.exerciseButton);
        Button historyButton = (Button) findViewById(R.id.historyButton);
        Button androidButton = (Button) findViewById(R.id.androidButton);
        Button tizenButton = (Button) findViewById(R.id.tizenButton);
        Button parseExerciseButton = (Button) findViewById(R.id.parseExerciseButton);
        Log.d("BUTTONTEST",
              "Blah");

        exerciseButton.setOnClickListener(
                new Button.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        Log.d("BUTTONTEST",
                              "Button clicked");
                        Intent i = new Intent("SelectExerciseActivity");
                        startActivity(i);
                    }
                });

        androidButton.setOnClickListener(
                new Button.OnClickListener()
                {
                    public void onClick(View v)
                    {

                        connectionHandlerBackend.sendExerciseData("Hallo Welt!");
                    }
                });

        connectionHandlerBackend.addListener(new Listener()
        {
            @Override
            public void onNotify(String data)
            {
                updateText(data);
            }
        });
    }

    public void updateText(String text)
    {
        helloWorldTextView.setText(text);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main,
                                  menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
