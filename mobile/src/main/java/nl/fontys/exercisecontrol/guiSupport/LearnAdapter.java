package nl.fontys.exercisecontrol.guiSupport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import nl.fontys.exercisecontrol.exercise.Exercise;
import nl.fontys.exercisecontrol.exercise.R;

/**
 * Created by sascha on 21.05.15.
 */
public class LearnAdapter extends BaseAdapter
{
    private final Context context;
    private List<Exercise> exeList;
    LayoutInflater inflater;

    public LearnAdapter(Context context, List<Exercise> exeList)
    {
        this.exeList = exeList;
        this.context = context;
        inflater =  LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return exeList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return exeList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return new Long(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {

            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.learn_list_layout, parent, false);

            holder.exeName = (TextView) convertView.findViewById(R.id.sequence_textView);
            holder.delButton = (Button) convertView.findViewById(R.id.deleteSeq_Button);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Exercise exercise = exeList.get(position);
        holder.exeName.setText(exercise.getNAME() + " " + position + 1);
        holder.delButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                exeList.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
    private class ViewHolder
    {
        public TextView exeName;
        public Button delButton;
    }
}
