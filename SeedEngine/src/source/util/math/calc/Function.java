package source.util.math.calc;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Function {

    private static final double H = 0.00000001;

    public double f(double t) {
        return 4 * t * t * t;
    }

    public double fPrime(double t) {
        return ((f(t + H) - f(t)) / H);
    }
}
