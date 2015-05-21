package nl.fontys.exercisecontrol.exercise.recorder;

/**
 * CMessage object for listener thread data exchange.
 */
public class MeasurementRecorderMessageObject {

    private final MessageAction action;
    private final String name;
    private final MeasurementException exception;

    private MeasurementRecorderMessageObject(MessageAction action, String name, MeasurementException exception) {
        this.action = action;
        this.name = name;
        this.exception = exception;
    }

    public MessageAction getAction() {
        return action;
    }

    public String getName() {
        return name;
    }

    public MeasurementException getException() {
        return exception;
    }

    public static MeasurementRecorderMessageObject start(String name) {
        return new MeasurementRecorderMessageObject(MessageAction.START, name, null);
    }

    public static MeasurementRecorderMessageObject stop() {
        return new MeasurementRecorderMessageObject(MessageAction.STOP, null, null);
    }

    public static MeasurementRecorderMessageObject fail(MeasurementException exception) {
        return new MeasurementRecorderMessageObject(MessageAction.FAIL, null, exception);
    }

    public static MeasurementRecorderMessageObject quit() {
        return new MeasurementRecorderMessageObject(MessageAction.QUIT, null, null);
    }

    public enum MessageAction {
        START, STOP, FAIL, QUIT
    }
}
