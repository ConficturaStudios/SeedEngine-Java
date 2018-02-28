package source.util.math.interpolation;

import source.util.structures.DistanceComparable;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class SplinePoint implements DistanceComparable<SplinePoint> {

    private double m;
    private double value;
    private double t;

    public SplinePoint(double value, double t, double m) {
        this.value = value;
        this.t = t;
        this.m = m;
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    @Override
    public double distance(SplinePoint o) {
        return Math.sqrt((o.t - t) * (o.t - t) + (o.value - value) * (o.value - value));
    }
}
