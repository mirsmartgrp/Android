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
        String guid = b.getString("guid");
        context = getApplicationContext();
        Exercise exercise = ObjectHelper.getInstance(context).getExerciseList().getEXERCISE_HASH_MAP().get(guid);

        TextView exerciseNameTextView = (TextView) findViewById(R.id.ExerciseNameTextView);
        exerciseNameTextView.setText(exercise.getNAME());

        TextView exerciseDescriptionTextView = (TextView) findViewById(R.id.ExerciseDescriptionTextView);
        exerciseDescriptionTextView.setText(exercise.getDESCRIPTION());

        VideoView exerciseVideoView = (VideoView) findViewById(R.id.exerciseVideoView);

        //Displaying the video, needs an mp4 links afaik.
        String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
        Uri vidUri = Uri.parse(vidAddress);
        exerciseVideoView.setVideoURI(vidUri);

        //Implementing Video controlls
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(exerciseVideoView);
        exerciseVideoView.setMediaController(vidControl);

        exerciseVideoView.start();
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
//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
