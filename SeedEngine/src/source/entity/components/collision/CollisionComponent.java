package source.entity.components.collision;

import source.entity.components.PrimitiveComponent;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class CollisionComponent extends PrimitiveComponent {

    public enum CollisionType {
        RECTANGULAR,
        SPHERICAL,
        CAPSULE,
        COMPLEX
    }

}
