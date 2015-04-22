package nl.fontys.exercisecontrol.exercise.recorder;

public class MeasurementException extends Exception {

    public MeasurementException(String message) {
        super(message);
    }

    public MeasurementException(Throwable throwable) {
        super(throwable);
    }
}
