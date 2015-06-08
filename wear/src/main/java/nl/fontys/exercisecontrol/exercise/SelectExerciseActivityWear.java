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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nl.fontys.exercisecontrol.exercise.exlist.Exercise;
import nl.fontys.exercisecontrol.exercise.exlist.ExercisesObject;
import nl.fontys.exercisecontrol.exercise.exlist.ExercisesObjectParser;

public class SelectExerciseActivityWear extends Activity implements WearableListView.ClickListener  {

    private WearableListView mListView;
    public final static String EXERCISE_NAME="nl.fontys.exercisecontrol.exercise.ExerciseName";
    public final static String EXERCISE_GUID="nl.fontys.exercisecontrol.exercise.ExerciseGUID";

    private static  List<Exercise> listItems;
    private static final String TAG="LOG";

    public SelectExerciseActivityWear() {
    }

    public static List<Exercise> getExerciseList(){
        return  listItems;
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
        ObjectHelper.getInstance(getApplicationContext());

    }


    /**
     * loading exercise names from json file
     * @param inputStream stream with the file as input
     */
    private List<Exercise> loadExerciseList(InputStream inputStream) {
        ExercisesObjectParser parser = new ExercisesObjectParser();

        List exerciseList =new ArrayList<>();
        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while (( line = buffreader.readLine()) != null) {
                stringBuilder.append(line);
            }
            Log.d(TAG, stringBuilder.toString());
            ExercisesObject obj = parser.parse(stringBuilder.toString());
            for(Exercise ex:obj.getExercises()) {
                exerciseList.add(ex);
            }
            return exerciseList;
        } catch (IOException e) {
            showToast(getString(R.string.errorLoadingList), Toast.LENGTH_SHORT);
           Log.e(TAG, getString(R.string.errorLoadingList) +e);
        return exerciseList;
        }
    }
    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        Exercise exercise = listItems.get(viewHolder.getPosition());
        startMainActivity(exercise);
        
    }

    /**
     * starts the main activity
     * @param exercise selected exercise
     */
    private void startMainActivity(Exercise exercise) {
        Intent i = new Intent(getApplicationContext(), MainActivityWear.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXERCISE_NAME, exercise.getName());
        bundle.putString(EXERCISE_GUID, exercise.getGuid());
        i.putExtras(bundle);
        startActivity(i);
    }
    /**
     * display a toast message
     * @param message text to display
     * @param length time how long the display is show
     */
    private void showToast(String message, int length) {
        Toast toast = Toast.makeText(this, message, length);
        toast.show();
    }
    @Override
    public void onTopEmptyRegionClick() {
        showToast(getString(R.string.listViewHint),Toast.LENGTH_LONG);
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
