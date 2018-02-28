package source.entity.components;

import source.util.structures.Transform;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class TransformComponent extends SceneComponent {

    public Transform transform;

    public TransformComponent() {
        transform = new Transform();
    }

}
