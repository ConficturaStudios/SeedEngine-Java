package source.util.generation;

import source.engine.Debugger;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class CompositeDataField extends ProceduralDataField {

    private final ProceduralDataField a;
    private final ProceduralDataField b;
    private Composition composition;

    public interface Composition {
        double composite(double a, double b);
    }

    public CompositeDataField(ProceduralDataField a, ProceduralDataField b) {
        this(a, b, (A, B) -> A * B);
    }

    public CompositeDataField(ProceduralDataField a, ProceduralDataField b, Composition composition) {
        super();
        this.a = a;
        this.b = b;
        this.composition = composition;
    }

    @Override
    public double[] generate(int ptX, int ptY) {
        double [] aField = a.generate(ptX, ptY);
        double [] bField = b.generate(ptX, ptY);
        for (int i = 0; i < RESOLUTION * RESOLUTION; i++) {
            aField[i] = composition.composite(aField[i], bField[i]);
        }
        return aField;
    }
}
