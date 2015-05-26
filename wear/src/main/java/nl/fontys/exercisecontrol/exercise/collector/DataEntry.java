package nl.fontys.exercisecontrol.exercise.collector;

public class DataEntry {

    private final double secondsSinceStart;
    private Vector accelerometer;
    private Vector gyroscope;

    public DataEntry(double secondsSinceStart) {
        this.secondsSinceStart = secondsSinceStart;
        accelerometer = new Vector(0.0, 0.0, 0.0);
        gyroscope = new Vector(0.0, 0.0, 0.0);
    }

    @Override
    public String toString() {
        return "DataEntry{" +
                "secondsSinceStart=" + secondsSinceStart +
                ", accelerometer=" + accelerometer +
                ", gyroscope=" + gyroscope +
                '}';
    }

    public double getSecondsSinceStart() {
        return secondsSinceStart;
    }

    public Vector getAccelerometer() {
        return accelerometer;
    }

    public void setAccelerometer(Vector accelerometer) {
        this.accelerometer = accelerometer;
    }

    public Vector getGyroscope() {
        return gyroscope;
    }

    public void setGyroscope(Vector gyroscope) {
        this.gyroscope = gyroscope;
    }

    public static class Vector {
        private final double x, y, z;

        public Vector(float[] v) {
            this(v[0], v[1], v[2]);
        }

        public Vector(double[] v) {
            this(v[0], v[1], v[2]);
        }

        @Override
        public String toString() {
            return "Vector{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }

        public Vector(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }
    }
}
