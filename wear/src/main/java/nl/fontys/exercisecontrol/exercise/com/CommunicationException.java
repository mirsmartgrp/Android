package nl.fontys.exercisecontrol.exercise.com;

public class CommunicationException extends Exception {

    public CommunicationException(String detailMessage) {
        super(detailMessage);
    }

    public CommunicationException(Throwable throwable) {
        super(throwable);
    }
}
