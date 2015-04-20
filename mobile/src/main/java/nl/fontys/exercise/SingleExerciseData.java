package nl.fontys.exercise;

import java.util.Date;

/**
 * Created by root on 08.04.15.
 * <p/>
 * stores data is collected at a specific timestamp from an exercise
 */
public class SingleExerciseData
{
    private Date                timeStamp;
    private PartialExerciseData acceleratorData;
    private PartialExerciseData gyroscopeData;

    public SingleExerciseData(Date timeStamp,
                              PartialExerciseData acceleratorData,
                              PartialExerciseData gyroscopeData)
    {
        if (timeStamp != null)
        {
            this.timeStamp = timeStamp;
        }
        else
        {
            throw new NullPointerException("timeStamp is null");
        }

        this.acceleratorData = acceleratorData;
        this.gyroscopeData = gyroscopeData;
    }

    public Date getTimeStamp()
    {
        return this.timeStamp;
    }

    public PartialExerciseData getAcceleratorData()
    {
        return acceleratorData;
    }

    public PartialExerciseData getGyroscopeData()
    {
        return gyroscopeData;
    }
}
