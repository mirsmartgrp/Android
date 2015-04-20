package nl.fontys.exercise;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ron Gebauer on 08.04.15.
 * <p/>
 * stores whole data that is collected from the exercise
 */
public class ExerciseData
{
    private List<SingleExerciseData> lSingleExerciseData;

    /**
     * Constructs a empty ExerciseData object.
     */
    ExerciseData()
    {
        this.lSingleExerciseData = new ArrayList<>();
    }

    public void addSingleExerciseData(Date timeStamp,
                                      Double[][] coordinates)
    {
        PartialExerciseData acceleratorData = new PartialExerciseData(coordinates[0][0],
                                                                      coordinates[0][1],
                                                                      coordinates[0][2]);
        PartialExerciseData gyroscopeData = new PartialExerciseData(coordinates[1][0],
                                                                    coordinates[1][1],
                                                                    coordinates[1][2]);
        SingleExerciseData singleExerciseData = new SingleExerciseData(timeStamp,
                                                                       acceleratorData,
                                                                       gyroscopeData);

        this.lSingleExerciseData.add(singleExerciseData);
    }

    public void clearlSingleExerciseDataByIndex(Integer index)
    {
        this.lSingleExerciseData.clear();
    }

    public void removeSingleExerciseDataByIndex(Integer index)
    {
        this.lSingleExerciseData.remove(index.intValue());
    }

    public void removeSingleExerciseDataByTimeStamp(Date timeStamp)
    {
        Integer index = null;
        for (int i = 0; i < lSingleExerciseData.size(); i++)
        {
            if (timeStamp.equals(lSingleExerciseData.get(i).getTimeStamp()))
            {
                index = i;
            }
        }

        if (index == null)
        {
            return;
        }

        this.lSingleExerciseData.remove(index.intValue());
    }

    /**
     * @return a SingleExerciseData from index position.
     */
    public SingleExerciseData getSingleExerciseData(Integer index)
    {
        return lSingleExerciseData.get(index.intValue());
    }

    /**
     * @return all SingleExerciseData are collect until now as List object.
     */
    public List<SingleExerciseData> getListOfSingleExerciseData()
    {
        return lSingleExerciseData;
    }
}
