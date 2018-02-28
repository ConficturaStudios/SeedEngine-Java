package source.util.structures;

/**
 * A quaternion of the form w + xi + yj + zk
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Quaternion {
    public final double w, x, y, z;

    private final Matrix4f rotationMatrix;

    public Quaternion() {
        this(1,0,0,0);
    }

    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
        rotationMatrix = toRotationMatrix();
    }

    public Quaternion(Matrix4f rotationMatrix) {
        this.rotationMatrix = rotationMatrix;
        Vector4[] m = rotationMatrix.getColumns();
        Vector4 m0 = m[0];
        Vector4 m1 = m[1];
        Vector4 m2 = m[2];
        this.w = Math.sqrt(1 + m0.x + m1.y + m2.z) / 2;
        this.x = (m2.y - m1.z) / (4 * w);
        this.y = (m0.z - m2.x) / (4 * w);
        this.z = (m1.x - m0.y) / (4 * w);
    }

    public double norm() {
        return Math.sqrt(w * w + x * x + y * y + z * z);
    }

    public Quaternion conjugate() {
        return new Quaternion(w, -x, -y, -z);
    }

    public Quaternion add(Quaternion b) {
        return new Quaternion(this.w +b.w, this.x +b.x,this.y +b.y,this.z +b.z);
    }

    public Quaternion multiply(Quaternion b) {
        double xn0 = this.w *b.w - this.x *b.x - this.y *b.y - this.z *b.z;
        double xn1 = this.w *b.x + this.x *b.w + this.y *b.z - this.z *b.y;
        double xn2 = this.w *b.y - this.x *b.z + this.y *b.w + this.z *b.x;
        double xn3 = this.w *b.z + this.x *b.y - this.y *b.x + this.z *b.w;
        return new Quaternion(xn0, xn1, xn2, xn3);
    }

    public Quaternion divide(Quaternion b) {
        return this.multiply(b.inverse());
    }

    public Quaternion inverse() {
        double d = w * w + x * x + y * y + z * z;
        return new Quaternion(w /d, -x /d, -y /d, -z /d);
    }

    private Matrix4f toRotationMatrix() {
        double n = norm();
        double x = this.x / n;
        double y = this.y / n;
        double z = this.z / n;
        double w = this.w / n;
        return new Matrix4f(
                new Vector4(1 - 2*y*y - 2*z*z, 2*x*y - 2*w*z,     2*w*y + 2*x*z,     0),
                new Vector4(2*x*y + 2*w*z,     1 - 2*x*x - 2*z*z, 2*y*z - 2*w*x,     0),
                new Vector4(2*x*z - 2*w*y,     2*w*x + 2*y*z,     1 - 2*x*x - 2*y*y, 0),
                new Vector4(0,                 0,                 0,                 1)
        ).transpose();
    }

    public Rotator toRotator() {
        return new Rotator().rotate(this);
    }

    public Matrix4f getRotationMatrix() {
        return this.rotationMatrix;
    }

    
    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return w + " + " + x + "i + " + y + "j + " + z + "k";
    }
}
