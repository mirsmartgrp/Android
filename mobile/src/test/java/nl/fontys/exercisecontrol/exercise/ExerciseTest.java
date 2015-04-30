package nl.fontys.exercisecontrol.exercise;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class ExerciseTest
        extends TestCase
{

    private String     exerciseName;
    private JSONObject jsonObject;

    @Before
    public void setUp()
            throws Exception
    {
        // initialize exerciseName
        exerciseName = "test exercise";

        // initialize jsonObject
        // TODO read json file into jsonString.
        String jsonString = null;
        URL url = getClass().getResource("ExerciseTest.class");
        Integer end1 = url.getPath().toString().indexOf("build");
        Integer start = url.getPath().toString().indexOf("nl");
        Integer end2 = url.getPath().toString().indexOf("ExerciseTest.class");
        String pathString = url.getPath().substring(0,
                                                    end1)
                + "src/test/java/"
                + url.getPath().substring(start,
                                          end2)
                + "test_data.json";
        InputStream inputStream = new FileInputStream(pathString);

        if (inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null)
            {
                    stringBuilder.append(receiveString.trim());
            }

            inputStream.close();
            jsonString = stringBuilder.toString();

            jsonObject = new JSONObject(jsonString);
        }
        else
        {
            jsonObject = null;
        }
    }

    @After
    public void tearDown()
            throws Exception
    {
        exerciseName = null;
        jsonObject = null;
    }

    @Test
    public void testParseExercise()
            throws Exception
    {
//        Exercise exercise = Exercise.parseExercise(jsonObject);
//
//        assertEquals(exerciseName,
//                     exercise.getNAME());
    }
}