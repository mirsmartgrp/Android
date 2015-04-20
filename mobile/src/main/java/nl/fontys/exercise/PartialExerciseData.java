package nl.fontys.exercise;

/**
 * Created by Ron Gebauer on 08.04.15.
 * <p/>
 * stores movement data with x,y,z
 */
public class PartialExerciseData
{
    private Double x;
    private Double y;
    private Double z;

    /**
     * @param x x coordinate as Double object
     * @param y y coordinate as Double object
     * @param z z coordinate as Double object
     */
    PartialExerciseData(Double x,
                        Double y,
                        Double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return x coordinate as Double object.
     */
    public Double getX()
    {
        return x;
    }

    /**
     * @return y coordinate as Double object.
     */
    public Double getY()
    {
        return y;
    }

    /**
     * @return z coordinate as Double object.
     */
    public Double getZ()
    {
        return z;
    }
}
