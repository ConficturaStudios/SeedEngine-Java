package source.entity.components.collision;

import source.util.structures.Transform;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class BoundingSphere {
    public Transform transform;

    public boolean intersects(BoundingSphere sphere) {
        return false;
    }

}
