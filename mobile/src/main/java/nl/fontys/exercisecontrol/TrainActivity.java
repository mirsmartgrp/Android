package nl.fontys.exercisecontrol;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import nl.fontys.exercisecontrol.exercise.Exercise;
import nl.fontys.exercisecontrol.exercise.ObjectHelper;
import nl.fontys.exercisecontrol.exercise.R;
import nl.fontys.exercisecontrol.exercise.analysis.Analysis;
import nl.fontys.exercisecontrol.guiSupport.LearnAdapter;

public class TrainActivity extends Activity
{

    private LearnAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
    }


    public Handler getHandler() {
        return handler;
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            adapter.notifyDataSetChanged();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_train, menu);
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

    @Override
    protected void onStart() {
        super.onStart();

        ListView listViewExersie = (ListView) findViewById(R.id.exeListView);
        Exercise exercise = ObjectHelper.getInstance().getActualExecercise();
        Analysis analysis = ObjectHelper.getInstance().getAnalysis();
        List<List<ObservationVector>> list =analysis.getLearnSequences(exercise);
        adapter = new LearnAdapter(this,list,exercise);
        listViewExersie.setAdapter(adapter);
        ObjectHelper.getInstance().setLearnAdapter(adapter);
        ObjectHelper.getInstance().setTrainActivity(this);



    }

    @Override
    protected void onStop() {
        super.onStop();
        ObjectHelper.getInstance().setTrainActivity(null);
        ObjectHelper.getInstance().setLearnAdapter(null);
    }
}
