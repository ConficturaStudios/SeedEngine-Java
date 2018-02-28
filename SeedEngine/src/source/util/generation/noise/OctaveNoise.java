package source.util.generation.noise;

import source.util.math.interpolation.Curve;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public abstract class OctaveNoise extends Noise {
    protected int octaves;
    protected int currentOctave;

    public Curve perOctaveCurve;
    public Curve perPointCurve;

    protected OctaveNoise(long seed) {
        this(seed, 1);
    }

    protected OctaveNoise(long seed, int octaves) {
        super(seed);
        this.octaves = octaves;
        this.currentOctave = 0;
    }


    public int getOctaves() {
        return octaves;
    }

    public void setOctaves(int octaves) {
        this.octaves = octaves >= 1 ? octaves : this.octaves;
    }

}
