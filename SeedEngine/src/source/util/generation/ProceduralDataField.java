package source.util.generation;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public abstract class ProceduralDataField {

    public static final int MIN_BOUND = Integer.MIN_VALUE;
    public static final int MAX_BOUND = Integer.MAX_VALUE;
    public static final long RANGE = (long) MAX_BOUND - MIN_BOUND;
    public static final int RESOLUTION = 256;

    public abstract double[] generate(int ptX, int ptY);

}
