package nl.fontys.exercisecontrol.exercise.collector;

import java.util.ArrayList;
import java.util.List;

public class ExerciseData {

    private String name;
    private final List<DataEntry> data;

    public ExerciseData(String name) {
        this.name = name;
        data = new ArrayList<DataEntry>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DataEntry> getData() {
        return data;
    }
}
