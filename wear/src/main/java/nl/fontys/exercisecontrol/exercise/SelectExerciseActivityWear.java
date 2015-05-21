package nl.fontys.exercisecontrol.exercise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SelectExerciseActivityWear extends Activity implements WearableListView.ClickListener  {

    private WearableListView mListView;
    public final static String EXERCISE_NAME="nl.fontys.exercisecontrol.exercise.ExerciseName";
    private  List<String> listItems;
    public SelectExerciseActivityWear() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_exercise_activity_wear);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mListView = (WearableListView) findViewById(R.id.listView1);
                mListView.setAdapter(new MyAdapter(SelectExerciseActivityWear.this));
                mListView.setClickListener(SelectExerciseActivityWear.this);
            }
        });
        InputStream inputStream = getResources().openRawResource(R.raw.exerciseist);
        listItems=loadExerciseList(inputStream);

    }


    /**
     * loading exercise names from json file
     * @param inputStream stream with the file as input
     */
    private List<String> loadExerciseList(InputStream inputStream) {
        List names =new ArrayList<>();
        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        final String ELEMENT_TO_GET="name";
        String line;
        try {
            while (( line = buffreader.readLine()) != null) {
                Log.d("log", line);
                JSONObject jsonObj = new JSONObject(line);
                JSONObject exercise = jsonObj.getJSONObject("Exercise");
                String name = exercise.getString(ELEMENT_TO_GET);
                names.add(name);
            }
            return names;
        }
        catch (Exception e) {
            Log.e("log", "error reading exercise list. " + e);
            //TODO try to download from server ?
            return null;
        }
    }
    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        Log.d("WEAR", "selected nr " + viewHolder.getPosition() + 1);
        Log.d("WEAR", " selected " + listItems.get(viewHolder.getPosition()));
        startMainActivity(listItems.get(viewHolder.getPosition()));
        
    }

    /**
     * starts the main activity
     * @param exerciseName name of the selected exercise
     */
    private void startMainActivity(String exerciseName) {
        Intent i = new Intent(getApplicationContext(), MainActivityWear.class);
        i.putExtra(EXERCISE_NAME, exerciseName);
        startActivity(i);
    }
    @Override
    public void onTopEmptyRegionClick() {

    }

    private class MyAdapter extends WearableListView.Adapter {
        private final LayoutInflater mInflater;

        private MyAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WearableListView.ViewHolder(
                    mInflater.inflate(R.layout.row_simple_item_layout, null));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
            TextView view = (TextView) holder.itemView.findViewById(R.id.textView);
            view.setText(listItems.get(position).toString());
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }
    }
}
