package source.util.generation;

import source.util.math.interpolation.Interpolation;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class InterpolateDataField extends ProceduralDataField {

    private final ProceduralDataField a;
    private final ProceduralDataField b;
    private final ProceduralDataField t;
    private Interpolation interpolation;


    public InterpolateDataField(ProceduralDataField a, ProceduralDataField b, ProceduralDataField t) {
        this(a, b, t, Interpolation.LERP);
    }

    public InterpolateDataField(ProceduralDataField a, ProceduralDataField b,
                                ProceduralDataField t, Interpolation interpolation) {
        super();
        this.a = a;
        this.b = b;
        this.t = t;
        this.interpolation = interpolation;
    }

    @Override
    public double[] generate(int ptX, int ptY) {
        double [] aField = a.generate(ptX, ptY);
        double [] bField = b.generate(ptX, ptY);
        double [] tField = t.generate(ptX, ptY);
        for (int i = 0; i < RESOLUTION * RESOLUTION; i++) {
            aField[i] = interpolation.interpolate(aField[i], bField[i], tField[i]);
        }
        return aField;
    }
}