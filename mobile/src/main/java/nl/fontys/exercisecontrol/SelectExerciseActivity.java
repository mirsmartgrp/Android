package nl.fontys.exercisecontrol;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import nl.fontys.exercisecontrol.exercise.R;


public class SelectExerciseActivity extends ListActivity
{

    ListView listView;
    private int RETURNRESULTLESS = -2;
    private int RETURNERROR = -1;

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

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1,testExercises);
        setContentView(R.layout.activity_exercise_overview);
        setListAdapter(listAdapter);
        //listView.setAdapter(listAdapter);
        listView = getListView();

        listView.setOnItemClickListener( new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //FinishActivityWithClick(parent, view, position, id);
                Intent exerciseIntent = new Intent(SelectExerciseActivity.this, ExerciseDescriptionActivity.class);
                startActivity(exerciseIntent);
            }
        });
        Button returnButton =(Button) findViewById(R.id.returnButton);
        View.OnClickListener listnr=new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                setResult(RETURNRESULTLESS);
                finish();
            }
        };
        returnButton.setOnClickListener(listnr);
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

    private void FinishActivityWithClick(AdapterView<?> parent, View view, int position, long id)
    {
        setResult(position);
        finish();
    }
}
