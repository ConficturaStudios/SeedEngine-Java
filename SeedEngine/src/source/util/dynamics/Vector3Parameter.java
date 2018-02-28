package source.util.dynamics;

import source.util.structures.Vector3;

/**
 * A vector parameter that stores 3 float values
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Vector3Parameter extends MaterialParameter<Vector3>  {

    public Vector3Parameter(String identifier) {
        super(identifier, new Vector3(0,0,0));
    }
}
