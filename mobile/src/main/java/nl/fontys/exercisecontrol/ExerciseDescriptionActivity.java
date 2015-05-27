package nl.fontys.exercisecontrol;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import nl.fontys.exercisecontrol.exercise.Exercise;
import nl.fontys.exercisecontrol.exercise.ObjectHelper;
import nl.fontys.exercisecontrol.exercise.R;

public class ExerciseDescriptionActivity
        extends Activity
{
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_description);
        Bundle b = getIntent().getExtras();
        String GUID = b.getString("GUID");
        context = getApplicationContext();
        Exercise exercise = ObjectHelper.getInstance(context).getExerciseList().getEXERCISE_HASH_MAP().get(GUID);

        TextView titleText = (TextView) findViewById(R.id.nameTextView);
        titleText.setText(exercise.getNAME());

        TextView descText = (TextView) findViewById(R.id.descriptionTextView);
        descText.setText(exercise.getDESCRIPTION());
        //Add the exercise description and title to the activity
        //Implement OK button with returnvalue equal to the id in the list.

        VideoView vidView = (VideoView) findViewById(R.id.exerciseVideo);

        //Displaying the video, needs an mp4 links afaik.
        String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
        Uri vidUri = Uri.parse(vidAddress);
        vidView.setVideoURI(vidUri);

        //Implementing Video controlls
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);

        vidView.start();
        //Video autostarts, may only on a seperate button click event.
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_description,
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
}
