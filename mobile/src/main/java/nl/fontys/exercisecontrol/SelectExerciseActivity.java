package nl.fontys.exercisecontrol;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Collection;

import nl.fontys.exercisecontrol.exercise.Exercise;
import nl.fontys.exercisecontrol.exercise.ExerciseHashMap;
import nl.fontys.exercisecontrol.exercise.ExerciseList;
import nl.fontys.exercisecontrol.exercise.ObjectHelper;
import nl.fontys.exercisecontrol.exercise.R;


public class SelectExerciseActivity
        extends ListActivity
{

    ListView listView;
    private int RETURNRESULTLESS = -2;
    private int RETURNERROR      = -1;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        ExerciseList exerciseList = ObjectHelper.getInstance(context).getExerciseList();
        ExerciseHashMap<String, Exercise> exerciseHashMap = exerciseList.getEXERCISE_HASH_MAP();
        Collection<Exercise> exerciseCollection = exerciseHashMap.valuesSortedByName();

        Exercise[] exercises = exerciseCollection.toArray(new Exercise[exerciseList.getEXERCISE_HASH_MAP().size()]);

        ArrayAdapter<Exercise> listAdapter = new ArrayAdapter<>
                (this,
                 android.R.layout.simple_list_item_1,
                 exercises);
        setContentView(R.layout.activity_exercise_overview);
        setListAdapter(listAdapter);
        listView = getListView();

        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id)
            {
                Intent exerciseIntent = new Intent(SelectExerciseActivity.this,
                                                   ExerciseDescriptionActivity.class);
                startActivityForResult(exerciseIntent, position);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_exercise,
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode);
        finish();
    }
}
