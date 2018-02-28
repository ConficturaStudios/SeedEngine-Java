package source.util.structures;

/**
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Transform {
    private Vector3 position;
    private Rotator rotation;
    private Vector3 scale;

    public Transform() {
        this(new Vector3(), new Rotator(), new Vector3(1, 1, 1));
    }

    public Transform(Vector3 position, Rotator rotation, Vector3 scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Matrix4f getTransformationMatrix() {
        Matrix4f matrix = new Matrix4f();
        Matrix4f translation = Matrix4f.translate(position.xf, position.yf, position.zf);

        Matrix4f rotationX = Matrix4f.rotate( (float) Math.toRadians(-rotation.pitch), 1, 0 , 0 );
        Matrix4f rotationY = Matrix4f.rotate( (float) Math.toRadians(-rotation.yaw), 0, 1 , 0 );
        Matrix4f rotationZ = Matrix4f.rotate( (float) Math.toRadians(-rotation.roll), 0, 0 , 1 );

        Matrix4f scale = Matrix4f.scale(this.scale.xf, this.scale.xf, this.scale.zf);

        matrix = matrix.multiply(scale);
        matrix = matrix.multiply(rotationX);
        matrix = matrix.multiply(rotationY);
        matrix = matrix.multiply(rotationZ);
        matrix = matrix.multiply(translation);

        return matrix;
    }

    public static Matrix4f get2DTransformationMatrix(Vector2 position, Vector2 scale) {
        Matrix4f matrix = new Matrix4f();
        Matrix4f translation = Matrix4f.translate(position.xf, position.yf, 0);
        Matrix4f scale1 = Matrix4f.scale(scale.xf, scale.yf, 1);
        matrix = matrix.multiply(translation);
        matrix = matrix.multiply(scale1);
        return matrix;
    }

    public Matrix4f getViewMatrix() {
        Matrix4f viewMatrix = new Matrix4f();
        Matrix4f pitch = Matrix4f.rotate((float) Math.toRadians(rotation.pitch), 1, 0, 0);
        Matrix4f yaw = Matrix4f.rotate((float) Math.toRadians(rotation.yaw), 0, 1, 0);
        Matrix4f roll = Matrix4f.rotate((float) Math.toRadians(rotation.roll), 0, 0, 1);
        Matrix4f translation = Matrix4f.translate(-position.xf, -position.yf, -position.zf);

        viewMatrix = viewMatrix.multiply(pitch);
        viewMatrix = viewMatrix.multiply(yaw);
        viewMatrix = viewMatrix.multiply(roll);
        viewMatrix = viewMatrix.multiply(translation);
        return viewMatrix;
    }

    public Matrix4f getInverseViewMatrix() {
        Matrix4f viewMatrix = new Matrix4f();
        Matrix4f pitch = Matrix4f.rotate((float) Math.toRadians(rotation.pitch), 1, 0, 0);
        Matrix4f yaw = Matrix4f.rotate((float) Math.toRadians(rotation.yaw), 0, 1, 0);
        Matrix4f roll = Matrix4f.rotate((float) Math.toRadians(rotation.roll), 0, 0, 1);
        Matrix4f translation = Matrix4f.translate(position.xf, position.yf, position.zf);

        viewMatrix = viewMatrix.multiply(pitch);
        viewMatrix = viewMatrix.multiply(yaw);
        viewMatrix = viewMatrix.multiply(roll).transpose();
        viewMatrix = viewMatrix.multiply(translation);
        return viewMatrix;
    }

    public Vector3 getForwardVector() {
        return getTransformationMatrix().getColumns()[2].toVector3().scale(-1);
    }

    public Vector3 getUpVector() {
        return getTransformationMatrix().getColumns()[1].toVector3();
    }

    public Vector3 getRightVector() {
        return getTransformationMatrix().getColumns()[0].toVector3();
        //return getForwardVector().cross(getUpVector());
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public void translate(Vector3 delta) {
        this.position = this.position.add(delta);
    }

    public void moveInDirection(Vector3 direction, double rate) {
        this.translate(direction.scale(rate));
    }

    public Rotator getRotation() {
        return rotation;
    }

    public void setRotation(Rotator rotation) {
        this.rotation = rotation;
    }

    public void rotate(Quaternion q) {
        this.rotation = this.rotation.rotate(q);
    }

    public void rotate(Rotator r) {
        this.rotation = this.rotation.rotate(r);
    }

    public void rotateAbout(double angle, Vector3 axis) {
        double s = Math.sin(angle / 2);
        rotate(new Quaternion(Math.cos(angle / 2), axis.z * s, axis.x * s, axis.y * s));
    }

    public Vector3 getScale() {
        return scale;
    }

    public void setScale(Vector3 scale) {
        this.scale = scale;
    }

    public void scale(Vector3 delta) {
        this.scale = this.scale.scale(delta);
    }
}
