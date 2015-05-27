package nl.fontys.exercisecontrol.exercise;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ron on 27.05.15.
 */
public class ExerciseHashMap<T, T1>
        extends HashMap<T, T1>
{
    public Collection<T1> valuesSortedByName()
    {
        List<T1> exerciseList = new ArrayList<>(super.values());
        Collections.sort(exerciseList,
                         new Comparator<T1>()
                         {
                             @Override
                             public int compare(T1 lhs,
                                                T1 rhs)
                             {
                                 return lhs.toString().compareToIgnoreCase(rhs.toString());
                             }
                         });

        return exerciseList;
    }
}
