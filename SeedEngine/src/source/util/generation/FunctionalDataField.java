package source.util.generation;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class FunctionalDataField extends ProceduralDataField {

    public interface Function {
        double evaluate(double x, double y);
    }

    private Function function;

    public FunctionalDataField(Function function) {
        this.function = function;
    }

    @Override
    public double[] generate(int ptX, int ptY) {
        double[] data = new double[RESOLUTION * RESOLUTION];

        long xPt = ptX - MIN_BOUND;
        long yPt = ptY - MIN_BOUND;

        double X, Y;
        double range = RANGE;

        for (int x = RESOLUTION - 1; x >= 0; x--) {
            for (int y = RESOLUTION - 1; y >= 0; y--) {
                X = (xPt + x) / range;
                Y = (yPt + y) / range;
                data[(y * RESOLUTION) + x] = function.evaluate(X, Y);
            }
        }
        return data;
    }
}
