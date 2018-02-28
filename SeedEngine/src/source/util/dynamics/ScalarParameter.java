package source.util.dynamics;

/**
 * A scalar parameter object that stores a single float value
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class ScalarParameter extends MaterialParameter<Float> {
    /** Minimum valid value */
    private Float valueMin = 0.0f;
    /** Maximum valid value */
    private Float valueMax = 1.0f;

    public ScalarParameter(String identifier) {
        super(identifier, 0.0f);
    }

    /**
     * Sets the value of this parameter
     *
     * @param value new value
     */
    @Override
    public void setValue(Float value) {
        super.setValue((value > valueMin) ? ((value < valueMax) ? value : valueMax) : valueMin);
    }

    /**
     * Sets the minimum possible value for this parameter
     * @param valueMin new minimum
     */
    public void setValueMin(Float valueMin) {
        this.valueMin = valueMin;
    }

    /**
     * Sets the maximum possible value for this parameter
     * @param valueMax new maximum
     */
    public void setValueMax(Float valueMax) {
        this.valueMax = valueMax;
    }

}
