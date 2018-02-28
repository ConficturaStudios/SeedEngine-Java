package source.entity.components;

import source.entity.Entity;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class ActorComponent {

    protected String componentName;

    protected Entity parent;

    //region Events

    public void registerParent(Entity parent) {
        this.parent = parent;
        onRegister();
    }

    protected void onRegister() {

    }

    //endregion

}
