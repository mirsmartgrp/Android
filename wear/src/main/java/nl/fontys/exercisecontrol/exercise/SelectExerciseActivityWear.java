package nl.fontys.exercisecontrol.exercise;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SelectExerciseActivityWear extends Activity implements WearableListView.ClickListener  {

    private WearableListView mListView;

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
    }


    private static ArrayList<String> listItems;
    static {
        listItems = new ArrayList<String>();
        listItems.add("Exercise I");
        listItems.add("Exercise II");
        listItems.add("Run");
        listItems.add("Jumping Jack");
        listItems.add("Exercise v");
    }

    private ArrayList<String> loadExerciseList(String filePath) throws IOException
{
   listItems = new ArrayList<String>();
    File file = new File(filePath);
    FileReader fr = new FileReader(file);
    BufferedReader br = new BufferedReader(fr);
    String line;
    while((line = br.readLine()) != null){
       listItems.add(line);
    }
    br.close();
    fr.close();
    return listItems;
}

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        Log.d("WEAR", "you selected nr "+viewHolder.getPosition()+1);
        Log.d("WEAR", "you selected "+listItems.get(viewHolder.getPosition()));
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
