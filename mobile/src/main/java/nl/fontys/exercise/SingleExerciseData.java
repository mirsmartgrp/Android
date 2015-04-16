package nl.fontys.exercise;

import java.util.Date;

/**
 * Created by root on 08.04.15.
 * <p/>
 * stores the data that is collected from the exercises
 */
public class SingleExerciseData {
    private Date timeStamp;
    private PartialExerciseData accelData;
    private PartialExerciseData gyroData;

    public SingleExerciseData(Date timeStamp, PartialExerciseData accelData, PartialExerciseData gyroData) {
        this.timeStamp = timeStamp;
        this.accelData = accelData;
        this.gyroData = gyroData;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public PartialExerciseData getAccelData() {
        return accelData;
    }

    public void setAccelData(PartialExerciseData accelData) {
        this.accelData = accelData;
    }

    public PartialExerciseData getGyroData() {
        return gyroData;
    }

    public void setGyroData(PartialExerciseData gyroData) {
        this.gyroData = gyroData;
    }
}
