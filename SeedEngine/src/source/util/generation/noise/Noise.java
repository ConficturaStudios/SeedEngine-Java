package source.util.generation.noise;

import source.util.generation.ProceduralDataField;

import java.util.Random;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public abstract class Noise extends ProceduralDataField {
    protected final long seed;
    protected final Random rnd;

    //^^^^
    /*TODO: in INI file: set up different quality levels as float lists, given a number of graphics levels, store index
        in ini file */

    protected Noise(long seed) {
        this.seed = seed;
        this.rnd = new Random(seed);
    }

}
