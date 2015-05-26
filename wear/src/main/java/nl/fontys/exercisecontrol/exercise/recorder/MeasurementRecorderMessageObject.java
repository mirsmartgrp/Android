package nl.fontys.exercisecontrol.exercise.recorder;

/**
 * CMessage object for listener thread data exchange.
 */
public class MeasurementRecorderMessageObject {

    private final MessageAction action;
    private final String guid;
    private final MeasurementException exception;

    private MeasurementRecorderMessageObject(MessageAction action, String guid, MeasurementException exception) {
        this.action = action;
        this.guid = guid;
        this.exception = exception;
    }

    public MessageAction getAction() {
        return action;
    }

    public String getGuid() {
        return guid;
    }

    public MeasurementException getException() {
        return exception;
    }

    public static MeasurementRecorderMessageObject start(String guid) {
        return new MeasurementRecorderMessageObject(MessageAction.START, guid, null);
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
