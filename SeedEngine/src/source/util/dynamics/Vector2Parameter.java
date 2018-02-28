package source.util.dynamics;

import source.util.structures.Vector2;

/**
 * A vector parameter that stores 2 float values
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Vector2Parameter extends MaterialParameter<Vector2> {

    public Vector2Parameter(String identifier) {
        super(identifier, new Vector2(0,0));
    }
}
