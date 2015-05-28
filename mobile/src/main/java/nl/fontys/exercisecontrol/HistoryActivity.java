package nl.fontys.exercisecontrol;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import nl.fontys.exercisecontrol.exercise.R;

public class HistoryActivity
        extends ListActivity
{

    ListView listView;
    String[] testHistory = new String[]
            {
                    "Exercise 12.5 12:22",
                    "Exercise II",
                    "Run",
                    "Jumping Jack",
                    "Exercsie V",
                    "Fuu",
                    "Blah",
                    "Just for a long testlist",
                    "...",
                    "...---...",
                    "Out of ideads"
            };
    private int RETURNRESULTLESS = -2;
    private int RETURNERROR      = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>
                (this,
                 android.R.layout.simple_list_item_1,
                 testHistory);

        setListAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id)
            {
                FinishActivityWithClick(parent,
                                        view,
                                        position,
                                        id);
            }
        });

        Button returnButton = (Button) findViewById(R.id.returnButton);
        View.OnClickListener listnr = new View.OnClickListener()
        {
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
        getMenuInflater().inflate(R.menu.menu_history,
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

    private void FinishActivityWithClick(AdapterView<?> parent,
                                         View view,
                                         int position,
                                         long id)
    {
        setResult(position);
        finish();
    }
}
