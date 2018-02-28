package source.util.math.interpolation;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Interpolation implements Curve {

    public static final Interpolation LERP = new Interpolation((t) -> t);
    public static final Interpolation NEAREST_NEIGHBOR = new Interpolation((t) -> (int)(t + 0.5));
    public static final Interpolation EXP = new Interpolation((t) -> t * t);
    public static final Interpolation CUBIC = new Interpolation((t) -> t * t * t);
    public static final Interpolation ALPHA_CURVE = new Interpolation((t) -> {
        double alpha = 2;
        return alpha * t / Math.pow(alpha, t);
    });
    public static final Interpolation BETA_CURVE = new Interpolation((t) -> {
        double beta = 2*t - 1;
        return (beta * beta * beta + 1) / 2;
    });
    public static final Interpolation GAMMA_CURVE = new Interpolation((t) -> {
        double gamma = Math.sin(Math.PI * t);
        return gamma * gamma;
    });
    public static final Interpolation DELTA_CURVE = new Interpolation((t) -> {
        double delta = (4 - 3 * t);
        return t * t * t * delta;
    });
    public static final Interpolation GAMMA_BETA_CURVE = new Interpolation((t) -> {
        double gamma = Math.sin(Math.PI * t);
        double beta = 2*t - 1;
        return 25*(beta * beta * beta * gamma * gamma) / 4 + 0.5;
    });
    public static final Interpolation PERLIN_CURVE = new Interpolation((t) -> t * t * (3 - 2 * t));

    private final Curve c;

    public Interpolation(Curve c) {
        this.c = c;
    }


    public double interpolate(double a, double b, double t) {
        return a + evaluate(t) * (a - b);
    }

    @Override
    public double evaluate(double t) {
        return c.evaluate(t);
    }
}
