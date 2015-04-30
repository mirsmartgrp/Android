package nl.fontys.exercisecontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nl.fontys.exercisecontrol.connection.ConnectionHandlerBackend;
import nl.fontys.exercisecontrol.exercise.R;
import nl.fontys.exercisecontrol.listener.Listener;


public class MainActivityMobile extends Activity
{
    private ConnectionHandlerBackend connectionHandlerBackend;
    private TextView textView;
    private static int exercsieRequestCode = 1001;
    private static int historyRequestCode = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        connectionHandlerBackend = new ConnectionHandlerBackend(this);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        Button button = (Button) findViewById(R.id.button);
        Button btn =(Button) findViewById(R.id.exerciseButton);
        Intent intent = new Intent(this, SelectExerciseActivity.class);
        View.OnClickListener listnr=new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                StartExerciseActivity();
            }
        };
        btn.setOnClickListener(listnr);
        button.setOnClickListener(
                new Button.OnClickListener()
                {
                    public void onClick(View v)
                    {

                        connectionHandlerBackend.sendExerciseData("Hallo Welt!");

                    }
                }

        );
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
        textView.setText(text);
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

    private void StartExerciseActivity()
    {
        Intent intent = new Intent(this, SelectExerciseActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        startActivityForResult(intent, exercsieRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        Log.d("RESULTREST", "RequestCode: " +requestCode);
        Log.d("RESULTREST", "ResultCode: " +resultCode);
       // Log.d("RESULTREST", "Data: " + data.toString());
    }
}
