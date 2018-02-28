package source.util.dynamics;

import source.util.structures.Vector4;

/**
 * A vector parameter that stores 4 float values
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Vector4Parameter extends MaterialParameter<Vector4> {

    public Vector4Parameter(String identifier) {
        super(identifier, new Vector4(0,0,0,0));
    }
}
