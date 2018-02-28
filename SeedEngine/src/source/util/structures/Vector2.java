package source.util.structures;


/**
 * A 2 component Vector
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Vector2 implements DistanceComparable<Vector2> {

    /** X */
    public final double x;
    /** Y */
    public final double y;
    /** X (float) */
    public final float xf;
    /** Y (float) */
    public final float yf;
    /** Magnitude (length) of this vector */
    public final double magnitude;

    /** Global Up Vector */
    public static final Vector2 UP = new Vector2(0,1);
    /** Global Right Vector */
    public static final Vector2 RIGHT = new Vector2(1,0);

    /**
     * Constructs a vector of magnitude 0
     */
    public Vector2() {
        this(0, 0);
    }

    /**
     * Constructs vector &lt;x,y&gt;
     * @param x x component
     * @param y y component
     */
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
        this.xf = (float) x;
        this.yf = (float )y;
        this.magnitude = Math.sqrt(x*x+y*y);
    }

    /**
     * Converts this vector to a two component vector by appending 0 as z
     * @return a Vector3 cast of this object
     */
    public Vector3 toVector3() {
        return new Vector3(x,y, 0);
    }

    /**
     * Converts this vector to a four component vector by appending 0 as z and w
     * @return a Vector4 cast of this object
     */
    public Vector4 toVector4() {
        return new Vector4(x,y,0,0);
    }

    /**
     * Adds a vector to this vector
     * @param vector vector to add
     * @return this + vector
     */
    public Vector2 add(Vector2 vector) {
        return new Vector2(x + vector.x, y + vector.y);
    }

    /**
     * Subtracts a vector from this vector
     * @param vector vector to subtract
     * @return this - vector
     */
    public Vector2 subtract(Vector2 vector) {
        return new Vector2(x - vector.x, y - vector.y);
    }

    /**
     * Uniformly scales this vector
     * @param s scale
     * @return scaled vector
     */
    public Vector2 scale(double s) {
        return new Vector2(x * s, y * s);
    }

    /**
     * Applies a component-wise scale to this vector
     * @param vector vector comprised of component scales
     * @return scaled vector
     */
    public Vector2 scale(Vector2 vector) {
        return new Vector2(x * vector.x, y * vector.y);
    }

    /**
     * Normalizes this vector to a magnitude of 1
     * @return a vector of this direction with magnitude 1
     */
    public Vector2 normalize() {
        return this.scale(1 / magnitude);
    }

    /**
     * The vector dot product
     * @param vector vector to apply
     * @return this*vector
     */
    public double dot(Vector2 vector) {
        return x * vector.x + y * vector.y;
    }

    /**
     * Returns the angle separating this and another vector
     * @param vector other vector
     * @return angle between the vectors in radians
     */
    public double angle(Vector2 vector) {
        return Math.acos(this.dot(vector) / (this.magnitude * vector.magnitude));
    }

    /**
     * Gets the distance between two vectors
     * @param vector vector to check against
     * @return distance between this and vector
     */
    public double distance(Vector2 vector) {
        return subtract(vector).magnitude;
    }

    /**
     * @see Object#equals(Object obj)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2 vector2 = (Vector2) o;

        if (Double.compare(vector2.x, x) != 0) return false;
        return Double.compare(vector2.y, y) == 0;
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
        return result;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "<" + x + ", " + y + ">";
    }

}