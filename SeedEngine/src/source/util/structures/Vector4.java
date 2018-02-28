package source.util.structures;

/**
 * A 4 component Vector
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Vector4 implements DistanceComparable<Vector4> {

    /** X */
    public final double x;
    /** Y */
    public final double y;
    /** Z */
    public final double z;
    /** W */
    public final double w;
    /** X (float) */
    public final float xf;
    /** Y (float) */
    public final float yf;
    /** Z (float) */
    public final float zf;
    /** W (float) */
    public final float wf;
    /** Magnitude (length) of this vector */
    public final double magnitude;

    /**
     * Constructs a vector of magnitude 0
     */
    public Vector4() {
        this(0, 0, 0, 0);
    }

    /**
     * Constructs vector &lt;x,y,z,w&gt;
     * @param x x component
     * @param y y component
     * @param z z component
     * @param z w component
     */
    public Vector4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.xf = (float) x;
        this.yf = (float )y;
        this.zf = (float) z;
        this.wf = (float) w;

        this.magnitude = Math.sqrt(x*x+y*y+z*z+w*w);
    }

    /**
     * Converts this vector to a two component vector by truncating off z and w
     * @return a Vector2 cast of this object
     */
    public Vector2 toVector2() {
        return new Vector2(x,y);
    }

    /**
     * Converts this vector to a three component vector by truncating off w
     * @return a Vector3 cast of this object
     */
    public Vector3 toVector3() {
        return new Vector3(x,y,z);
    }

    /**
     * Adds a vector to this vector
     * @param vector vector to add
     * @return this + vector
     */
    public Vector4 add(Vector4 vector) {
        return new Vector4(x + vector.x, y + vector.y, z + vector.z, w + vector.w);
    }

    /**
     * Subtracts a vector from this vector
     * @param vector vector to subtract
     * @return this - vector
     */
    public Vector4 subtract(Vector4 vector) {
        return new Vector4(x - vector.x, y - vector.y, z - vector.z, w - vector.w);
    }

    /**
     * Uniformly scales this vector
     * @param s scale
     * @return scaled vector
     */
    public Vector4 scale(double s) {
        return new Vector4(x * s, y * s, z * s, w * s);
    }

    /**
     * Applies a component-wise scale to this vector
     * @param vector vector comprised of component scales
     * @return scaled vector
     */
    public Vector4 scale(Vector4 vector) {
        return new Vector4(x * vector.x, y * vector.y, z * vector.z, w * vector.w);
    }

    /**
     * Normalizes this vector to a magnitude of 1
     * @return a vector of this direction with magnitude 1
     */
    public Vector4 normalize() {
        return this.scale(1 / magnitude);
    }

    /**
     * The vector dot product
     * @param vector vector to apply
     * @return this*vector
     */
    public double dot(Vector4 vector) {
        return x * vector.x + y * vector.y + z * vector.z + w * vector.w;
    }

    /**
     * Returns the angle separating this and another vector
     * @param vector other vector
     * @return angle between the vectors in radians
     */
    public double angle(Vector4 vector) {
        return Math.acos(this.dot(vector) / (this.magnitude * vector.magnitude));
    }

    /**
     * Gets the distance between two vectors
     * @param vector vector to check against
     * @return distance between this and vector
     */
    public double distance(Vector4 vector) {
        return subtract(vector).magnitude;
    }

    /**
     * @see Object#equals(Object obj)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector4 vector4 = (Vector4) o;

        if (Double.compare(vector4.x, x) != 0) return false;
        if (Double.compare(vector4.y, y) != 0) return false;
        if (Double.compare(vector4.z, z) != 0) return false;
        return Double.compare(vector4.w, w) == 0;
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
        temp = Double.doubleToLongBits(w);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "<" + x + ", " + y + ", " + z + ", " + w + ">";
    }

}