package source.util.structures;

/**
 * A 3 component Vector
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Vector3 implements DistanceComparable<Vector3> {

    /** X */
    public final double x;
    /** Y */
    public final double y;
    /** Z */
    public final double z;
    /** X (float) */
    public final float xf;
    /** Y (float) */
    public final float yf;
    /** Z (float) */
    public final float zf;
    /** Magnitude (length) of this vector */
    public final double magnitude;

    /** Global Forward Vector */
    public static final Vector3 FORWARD = new Vector3(0,0,-1);
    /** Global Up Vector */
    public static final Vector3 UP = new Vector3(0,1,0);
    /** Global Right Vector */
    public static final Vector3 RIGHT = new Vector3(1,0,0);

    /**
     * Constructs a vector of magnitude 0
     */
    public Vector3() {
        this(0, 0, 0);
    }

    /**
     * Constructs vector &lt;x,y,z&gt;
     * @param x x component
     * @param y y component
     * @param z z component
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xf = (float) x;
        this.yf = (float )y;
        this.zf = (float) z;

        this.magnitude = Math.sqrt(x*x+y*y+z*z);
    }

    /**
     * Converts this vector to a two component vector by truncating off z
     * @return a Vector2 cast of this object
     */
    public Vector2 toVector2() {
        return new Vector2(x,y);
    }

    /**
     * Converts this vector to a four component vector by appending 0 as w
     * @return a Vector4 cast of this object
     */
    public Vector4 toVector4() {
        return new Vector4(x,y,z,0);
    }

    /**
     * Adds a vector to this vector
     * @param vector vector to add
     * @return this + vector
     */
    public Vector3 add(Vector3 vector) {
        return new Vector3(x + vector.x, y + vector.y, z + vector.z);
    }

    /**
     * Subtracts a vector from this vector
     * @param vector vector to subtract
     * @return this - vector
     */
    public Vector3 subtract(Vector3 vector) {
        return new Vector3(x - vector.x, y - vector.y, z - vector.z);
    }

    /**
     * Uniformly scales this vector
     * @param s scale
     * @return scaled vector
     */
    public Vector3 scale(double s) {
        return new Vector3(x * s, y * s, z * s);
    }

    /**
     * Applies a component-wise scale to this vector
     * @param vector vector comprised of component scales
     * @return scaled vector
     */
    public Vector3 scale(Vector3 vector) {
        return new Vector3(x * vector.x, y * vector.y, z * vector.z);
    }

    /**
     * Normalizes this vector to a magnitude of 1
     * @return a vector of this direction with magnitude 1
     */
    public Vector3 normalize() {
        return this.scale(1 / magnitude);
    }

    /**
     * The vector dot product
     * @param vector vector to apply
     * @return this*vector
     */
    public double dot(Vector3 vector) {
        return x * vector.x + y * vector.y + z * vector.z;
    }

    /**
     * Returns the angle separating this and another vector
     * @param vector other vector
     * @return angle between the vectors in radians
     */
    public double angle(Vector3 vector) {
        return Math.acos(this.dot(vector) / (this.magnitude * vector.magnitude));
    }

    /**
     * Returns the cross product of this vector and another
     * @param vector vector to evaluate with
     * @return vector normal to this and vector
     */
    public Vector3 cross(Vector3 vector) {
        return new Vector3(
                y * vector.z - z * vector.y,
                z * vector.x - x * vector.z,
                x * vector.y - y * vector.x
        );
    }

    /**
     * Gets the distance between two vectors
     * @param vector vector to check against
     * @return distance between this and vector
     */
    public double distance(Vector3 vector) {
        return subtract(vector).magnitude;
    }

    /**
     * @see Object#equals(Object obj)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector3 vector3 = (Vector3) o;

        if (Double.compare(vector3.x, x) != 0) return false;
        if (Double.compare(vector3.y, y) != 0) return false;
        return Double.compare(vector3.z, z) == 0;
    }
    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "<" + x + ", " + y + ", " + z + ">";
    }

}