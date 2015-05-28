package nl.fontys.exercisecontrol;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import nl.fontys.exercisecontrol.exercise.Exercise;
import nl.fontys.exercisecontrol.exercise.ObjectHelper;
import nl.fontys.exercisecontrol.exercise.R;
import nl.fontys.exercisecontrol.exercise.analysis.Analysis;
import nl.fontys.exercisecontrol.guiSupport.LearnAdapter;

public class TrainActivity
        extends Activity {

    private LearnAdapter adapter;
    private Context context;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        context = getApplicationContext();
        Button androidButton = (Button) findViewById(R.id.learnExercise);

        androidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ObjectHelper.getInstance(context).getAnalysis().learnHMM(ObjectHelper.getInstance(context).getActualExecercise());
                    CharSequence text = "Excercise succesfully trained!";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.setGravity(Gravity.CENTER, 0, 0);

                    toast.show();

                } catch (Exception e) {
                    CharSequence text = "Excercise could not be trained!";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_train,
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

    @Override
    protected void onStart() {
        super.onStart();

        ListView listViewExersie = (ListView) findViewById(R.id.exeListView);
        Exercise exercise = ObjectHelper.getInstance(context).getActualExecercise();
        Analysis analysis = ObjectHelper.getInstance(context).getAnalysis();
        List<List<ObservationVector>> list = analysis.getLearnSequences(exercise);
        adapter = new LearnAdapter(this,
                list,
                exercise);
        listViewExersie.setAdapter(adapter);
        ObjectHelper.getInstance(context).setLearnAdapter(adapter);
        ObjectHelper.getInstance(context).setTrainActivity(this);


    }

    @Override
    protected void onStop() {
        super.onStop();
        ObjectHelper.getInstance(context).setTrainActivity(null);
        ObjectHelper.getInstance(context).setLearnAdapter(null);
    }
}
