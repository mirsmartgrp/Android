package exercise.fontys.nl.exercisecontrolbackend;

import java.util.Date;

/**
 * Created by root on 08.04.15.
 *
 * stores the data that is collected from the excercises
 */
public class ExerciseData {
    private Date timeStamp;
    private String typeOfExercise;
    private PartialExerciseData accelData;
    private PartialExerciseData gyroData;

    public ExerciseData(Date timeStamp, String typeOfExercise, PartialExerciseData accelData, PartialExerciseData gyroData) {
        this.timeStamp = timeStamp;
        this.typeOfExercise = typeOfExercise;
        this.accelData = accelData;
        this.gyroData = gyroData;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTypeOfExercise() {
        return typeOfExercise;
    }

    public void setTypeOfExercise(String typeOfExercise) {
        this.typeOfExercise = typeOfExercise;
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
