package nl.fontys.exercisecontrol.exercise;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class SelectExerciseActivity extends ListActivity
{

    ListView listView;

    String[] testExercises = new String[]
            {
                    "Exercise I",
                    "Exercise II",
                    "Run",
                    "Jumping Jack",
                    "Exercsie V",
            };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        listView = (ListView) findViewById(R.id.exerListView);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1,testExercises);
        setContentView(R.layout.activity_select_exercise);

        listView.setAdapter(listAdapter);


        AdapterView.OnItemClickListener
                mMessageClickedHandler =
                new AdapterView.OnItemClickListener()
                {
                    public void onItemClick(AdapterView parent,
                                            View v,
                                            int position,
                                            long id)

                    {
                        Log.d("ExerciseActivity" , "Clicked item: " + id);
                    }
                };
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_exercise, menu);
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
