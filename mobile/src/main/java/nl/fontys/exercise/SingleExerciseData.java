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

    /**
     * Constructs a new object from SingleExerciseData with a specific timeStamp.
     *
     * @param timeStamp
     * @param acceleratorData
     * @param gyroscopeData
     */
    SingleExerciseData(Date timeStamp,
                       PartialExerciseData acceleratorData,
                       PartialExerciseData gyroscopeData)
    {
        // timeStamp may not be null
        if (timeStamp != null)
        {
            this.timeStamp = timeStamp;
        }
        else
        {
            throw new NullPointerException("timeStamp may not be null");
        }

        this.acceleratorData = acceleratorData;
        this.gyroscopeData = gyroscopeData;
    }

    /**
     * @return the timeStamp as Date object.
     */
    public Date getTimeStamp()
    {
        return this.timeStamp;
    }

    /**
     * @return the accelerator data as PartialExerciseData object.
     */
    public PartialExerciseData getAcceleratorData()
    {
        return acceleratorData;
    }

    /**
     * @return the gyroscope data as PartialExerciseData object.
     */
    public PartialExerciseData getGyroscopeData()
    {
        return gyroscopeData;
    }
}
