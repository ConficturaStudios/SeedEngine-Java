package source.util.structures;

/**
 * A Rotator class representing roll, pitch, and yaw
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Rotator {

    public final double pitch, yaw, roll;

    public Rotator() {
        this(0,0,0);
    }

    public Rotator(double pitch, double yaw, double roll) {
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Rotator rotate(Quaternion q) {
        double roll = Math.toDegrees(Math.atan2(2*q.w*q.x + 2*q.y*q.z,
                1 - 2*q.x*q.x - 2*q.z*q.z));

        double pitch = Math.toDegrees(Math.asin(2*q.w*q.y - 2*q.z*q.x));

        double yaw = Math.toDegrees(Math.atan2(2*q.w*q.z + 2*q.x*q.y,
                1 - 2*q.y*q.y - 2*q.z*q.z));

        return new Rotator(this.pitch + pitch, this.yaw + yaw, this.roll + roll);
    }

    public Rotator rotate(Rotator r) {
        return rotate(r.toQuaternion());
    }

    public Quaternion toQuaternion() {
        double c1 = Math.cos(Math.toRadians(roll * 0.5));
        double s1 = Math.sin(Math.toRadians(roll * 0.5));

        double c2 = Math.cos(Math.toRadians(pitch * 0.5));
        double s2 = Math.sin(Math.toRadians(pitch * 0.5));

        double c3 = Math.cos(Math.toRadians(yaw * 0.5));
        double s3 = Math.sin(Math.toRadians(yaw * 0.5));

        double qw, qx, qy, qz;

        qw = c1*c2*c3 + s1*s2*s3;
        qx = s1*c2*c3 - c1*s2*s3;
        qy = c1*s2*c3 + s1*c2*s3;
        qz = c1*c2*s3 - s1*s2*c3;

        return new Quaternion(qw, qx, qy, qz);
        //return new Quaternion(toLocalRotationMatrix());

    }

    public Matrix4f toLocalRotationMatrix() {

        double c1 = Math.cos(Math.toRadians(roll * 0.5));
        double s1 = Math.sin(Math.toRadians(roll * 0.5));

        double c2 = Math.cos(Math.toRadians(pitch * 0.5));
        double s2 = Math.sin(Math.toRadians(pitch * 0.5));

        double c3 = Math.cos(Math.toRadians(yaw * 0.5));
        double s3 = Math.sin(Math.toRadians(yaw * 0.5));

        return new Matrix4f(
                new Vector4(c2*c3, -c1*s3 + s1*s2*c3, s1*s3 + c1*s2*c3,  0),
                new Vector4(c2*s3, c1*c3 + s1*s2*s3,  -s1*c3 + c1*s2*s3, 0),
                new Vector4(-1*s2, s1*c2,             c1*c2,             0),
                new Vector4(0,     0,                 0,                 1)
        ).transpose();
        /*
        Matrix4f matrix = new Matrix4f();
        Matrix4f rotationX = Matrix4f.rotate( (float) Math.toRadians(pitch), 1, 0 , 0 );
        Matrix4f rotationY = Matrix4f.rotate( (float) Math.toRadians(yaw), 0, 1 , 0 );
        Matrix4f rotationZ = Matrix4f.rotate( (float) Math.toRadians(roll), 0, 0 , 1 );
        matrix = matrix.multiply(rotationX);
        matrix = matrix.multiply(rotationY);
        matrix = matrix.multiply(rotationZ);
        return matrix;*/
    }

    public Matrix4f toGlobalRotationMatrix() {
        Matrix4f matrix = new Matrix4f();
        Matrix4f rotationX = Matrix4f.rotate( (float) Math.toRadians(pitch), 0, 0 , 1 );
        Matrix4f rotationY = Matrix4f.rotate( (float) Math.toRadians(yaw), 0, 1 , 0 );
        Matrix4f rotationZ = Matrix4f.rotate( (float) Math.toRadians(roll), 1, 0 , 0 );
        matrix = matrix.multiply(rotationX);
        matrix = matrix.multiply(rotationY);
        matrix = matrix.multiply(rotationZ);
        return matrix;
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
        return pitch + "p + " + yaw + "y + " + roll + "r";
    }
}
