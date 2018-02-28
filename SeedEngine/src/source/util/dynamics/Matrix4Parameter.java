package source.util.dynamics;

import source.util.structures.Matrix4f;

/**
 * A matrix parameter that stores a 4x4 float array
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Matrix4Parameter extends MaterialParameter<Matrix4f> {

    public Matrix4Parameter(String identifier) {
        super(identifier, new Matrix4f());
    }
}
