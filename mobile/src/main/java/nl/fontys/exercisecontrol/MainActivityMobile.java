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

import org.json.JSONException;
import org.json.JSONObject;

import nl.fontys.exercisecontrol.connection.ConnectionHandlerBackend;
import nl.fontys.exercisecontrol.exercise.Exercise;
import nl.fontys.exercisecontrol.exercise.ExerciseList;
import nl.fontys.exercisecontrol.exercise.ObjectHelper;
import nl.fontys.exercisecontrol.exercise.R;
import nl.fontys.exercisecontrol.exercise.analysis.Analysis;
import nl.fontys.exercisecontrol.exercise.analysis.AnalysisError;
import nl.fontys.exercisecontrol.guiSupport.LearnAdapter;
import nl.fontys.exercisecontrol.listener.Listener;


public class MainActivityMobile
        extends Activity {
    private TextView textView;
    public static Context context;
    private ConnectionHandlerBackend connectionHandlerBackend;
    private TextView helloWorldTextView;
    private static int exerciseRequestCode = 1001;
    private static int historyRequestCode = 1002;
    private static boolean learn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getBaseContext();

        super.onCreate(savedInstanceState);


        connectionHandlerBackend = new ConnectionHandlerBackend(this);
        setContentView(R.layout.activity_main);
        helloWorldTextView = (TextView) findViewById(R.id.testTextView);
        Button exerciseButton = (Button) findViewById(R.id.exerciseButton);
        Button historyButton = (Button) findViewById(R.id.historyButton);
        Button androidButton = (Button) findViewById(R.id.androidButton);
        final Button tizenButton = (Button) findViewById(R.id.tizenButton);
        tizenButton.setText("Learn");

        androidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hmm.printHMM();
            }
        });


        Intent intent = new Intent(this, SelectExerciseActivity.class);
        View.OnClickListener listnr = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StartExerciseActivity();
            }
        };

        exerciseButton.setOnClickListener(listnr);

        exerciseButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent exerciseIntent = new Intent(MainActivityMobile.this, SelectExerciseActivity.class);
                        startActivityForResult(exerciseIntent, exerciseRequestCode);
                    }
                });

        tizenButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

  /*                      if(learn){
                            learn = false;
                            tizenButton.setText("Test");
                        }else{
                            learn = true;
                            tizenButton.setText("Learn");
                        }

                        //connectionHandlerBackend.sendExerciseData("Hallo Welt!");
*/
                        Intent intent = new Intent(MainActivityMobile.this, TrainActivity.class);
                        startActivity(intent);
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
        connectionHandlerBackend.addListener(new Listener() {
            @Override
            public void onNotify(String data) {

                try {
                    JSONObject json = new JSONObject(data);
                    Log.d("JSON", json.toString());
                    Exercise ex = ObjectHelper.getInstance(context).getExerciseList().getExerciseByMovementData(json);
                    ObjectHelper.getInstance(context).setActualExecercise(ex);
                    if (learn) {
                        ObjectHelper.getInstance(context).getAnalysis().addLearnSequence(ex);
                        LearnAdapter learnAdapter = ObjectHelper.getInstance(context).getLearnAdapter();
                        if (learnAdapter != null) {
                            ObjectHelper.getInstance(context).getTrainActivity().getHandler().sendMessage(ObjectHelper.getInstance(context).getTrainActivity().getHandler().obtainMessage());
                        }
                        Log.d("Learn", "Leanr");
                    } else {
                        try {
                            ObjectHelper.getInstance(context).getAnalysis().testExercise(ex);
                        } catch (AnalysisError analysisError) {
                            analysisError.printStackTrace();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void updateText(String text) {
        helloWorldTextView.setText(text);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main,
                menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void StartExerciseActivity() {
        Intent intent = new Intent(this, SelectExerciseActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        startActivityForResult(intent, exerciseRequestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.d("RESULTREST", "RequestCode: " + requestCode);
        Log.d("RESULTREST", "ResultCode: " + resultCode);
        // Log.d("RESULTREST", "Data: " + data.toString());
    }
}
