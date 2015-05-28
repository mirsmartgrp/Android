package nl.fontys.exercisecontrol.exercise.collector;

import java.util.ArrayList;
import java.util.List;

public class ExerciseData {

    private String guid;
    private final List<DataEntry> sensorData;

    public ExerciseData(String guid) {
        this.guid = guid;
        sensorData = new ArrayList<DataEntry>();
    }

    @Override
    public String toString() {
        return "ExerciseData{" +
                "guid='" + guid + '\'' +
                ", sensorData=" + sensorData.toString() +
                '}';
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public List<DataEntry> getSensorData() {
        return sensorData;
    }
}
