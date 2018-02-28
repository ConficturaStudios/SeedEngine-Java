package source.entity.light;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class PointLight extends Light {

    private float radius;

    public PointLight() {
        super();
        setRadius();
    }

    public float getRadius() {
        return radius;
    }

    private void setRadius() {
        float constant = 1.0f;
        float linear = 0.7f;
        float quadratic = 1.8f;
        float lightMax = Math.max(
                Math.max(color.getRed(), color.getGreen()), color.getBlue()) * intensity;
        this.radius =
                (float)(-linear +  Math.sqrt(linear * linear - 4 * quadratic * (constant - (256.0 / 5.0) * lightMax)))
                        / (2 * quadratic);
    }


}
