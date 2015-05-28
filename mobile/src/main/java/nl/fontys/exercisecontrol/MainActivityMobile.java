package nl.fontys.exercisecontrol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import nl.fontys.exercisecontrol.connection.ConnectionHandlerBackend;
import nl.fontys.exercisecontrol.exercise.Exercise;
import nl.fontys.exercisecontrol.exercise.ObjectHelper;
import nl.fontys.exercisecontrol.exercise.R;
import nl.fontys.exercisecontrol.exercise.analysis.AnalysisError;
import nl.fontys.exercisecontrol.guiSupport.LearnAdapter;
import nl.fontys.exercisecontrol.listener.Listener;


public class MainActivityMobile
        extends Activity
{
    public static Context                  context;
    private static int     exerciseRequestCode = 1001;
    private static int     historyRequestCode  = 1002;
    public static boolean learn               = false;
    private       ConnectionHandlerBackend connectionHandlerBackend;
    private       TextView                 helloWorldTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = getBaseContext();

        setContentView(R.layout.activity_main);

        connectionHandlerBackend = new ConnectionHandlerBackend(this);
        helloWorldTextView = (TextView) findViewById(R.id.testTextView);
        Button exerciseButton = (Button) findViewById(R.id.exerciseButton);
        Button historyButton = (Button) findViewById(R.id.historyButton);
        Button learnButton = (Button) findViewById(R.id.learnButton);

        View.OnClickListener listnr = new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                StartExerciseActivity();
            }
        };

        exerciseButton.setOnClickListener(listnr);

        exerciseButton.setOnClickListener(
                new Button.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        Intent exerciseIntent = new Intent(MainActivityMobile.this,
                                                           SelectExerciseActivity.class);
                        startActivityForResult(exerciseIntent,
                                               exerciseRequestCode);
                    }
                });

        learnButton.setOnClickListener(
                new Button.OnClickListener()
                {
                    public void onClick(View v)
                    {

  /*                      if(learn){
                            learn = false;
                            learnButton.setText("Test");
                        }else{
                            learn = true;
                            learnButton.setText("Learn");
                        }

                        //connectionHandlerBackend.sendExerciseData("Hallo Welt!");
*/
                        Intent learnIntent = new Intent(MainActivityMobile.this,
                                                        SelectLearnActivity.class);
                        startActivity(learnIntent);

                    }
                }

                                      );
     /*   connectionHandlerBackend.addListener(new Listener()
        {
            @Override
            public void onNotify(String data)
            {
                updateText(data);
            }
        });*/
        connectionHandlerBackend.addListener(new Listener()
        {
            @Override
            public void onNotify(String data)
            {

                try
                {
                    JSONObject json = new JSONObject(data);
                    Log.d("JSON",
                          json.toString());
                    Exercise ex = ObjectHelper.getInstance(context).getExerciseList().getExerciseByMovementData(json);
                    ObjectHelper.getInstance(context).setActualExecercise(ex);
                    if (learn)
                    {
                        ObjectHelper.getInstance(context).getAnalysis().addLearnSequence(ex);
                        LearnAdapter learnAdapter = ObjectHelper.getInstance(context).getLearnAdapter();
                        if (learnAdapter != null)
                        {
                            ObjectHelper.getInstance(context).getTrainActivity().getHandler().sendMessage(ObjectHelper.getInstance(context).getTrainActivity().getHandler().obtainMessage());
                        }
                        Log.d("Learn",
                              "Leanr");
                    }
                    else
                    {
                        try
                        {
                           boolean result =  ObjectHelper.getInstance(context).getAnalysis().testExercise(ex);
                            CharSequence text;
                            if(result){
                                text = "Exercise done right!";

                           }else{
                                text = "Exercise done wrong!";
                           }
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        catch (AnalysisError analysisError)
                        {
                            CharSequence text = "Exercise not trained!";
                            Toast toast = Toast.makeText(context,text,Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            analysisError.printStackTrace();
                        }
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

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
//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void StartExerciseActivity()
    {
        Intent intent = new Intent(this,
                                   SelectExerciseActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        startActivityForResult(intent,
                               exerciseRequestCode);

    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {
        super.onActivityResult(requestCode,
                               resultCode,
                               data);


        Log.d("RESULTREST",
              "RequestCode: " + requestCode);
        Log.d("RESULTREST",
              "ResultCode: " + resultCode);
        // Log.d("RESULTREST", "Data: " + data.toString());
    }
}
