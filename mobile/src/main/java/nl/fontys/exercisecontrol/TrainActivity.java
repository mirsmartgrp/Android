package nl.fontys.exercisecontrol;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import nl.fontys.exercisecontrol.exercise.Exercise;
import nl.fontys.exercisecontrol.exercise.R;
import nl.fontys.exercisecontrol.guiSupport.LearnAdapter;

public class TrainActivity extends Activity
{
    private List<Exercise> mockExerciseList = new ArrayList<Exercise>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        mockExerciseList.add(new Exercise("Exercise 1"));
        mockExerciseList.add(new Exercise("Exercise 2"));
        mockExerciseList.add(new Exercise("Exercise 3"));
        mockExerciseList.add(new Exercise("Exercise 4"));

        ListView listViewExersie = (ListView) findViewById(R.id.exeListView);
        LearnAdapter adapter = new LearnAdapter(this, mockExerciseList);
        listViewExersie.setAdapter(adapter);
    }


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
}
