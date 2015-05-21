package nl.fontys.exercisecontrol.exercise.exlist;

import java.util.ArrayList;
import java.util.Collection;

public class ExerciseList extends ArrayList<Exercise> {

    public ExerciseList() {
    }

    public ExerciseList(int capacity) {
        super(capacity);
    }

    public ExerciseList(Collection<? extends Exercise> collection) {
        super(collection);
    }
}
