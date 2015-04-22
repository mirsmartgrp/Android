package nl.fontys.exercise;

import junit.framework.TestCase;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExerciseTest
        extends TestCase
{

    private String     name;
    private Exercise   exercise;
    private JSONObject jsonObject;

    @Before
    public void setUp()
            throws Exception
    {
        name = "test exercise";

        String jsonString = null;

        // TODO read json file into jsonstring.

        jsonObject = new JSONObject(jsonString);
    }

    @After
    public void tearDown()
            throws Exception
    {
        name = null;
        exercise = null;
        jsonObject = null;
    }

    @Test
    public void testParseExercise()
            throws Exception
    {
//        exercise = Exercise.parseExercise(jsonObject);
//
//        assertEquals(name,
//                     exercise.getNAME());
    }
}