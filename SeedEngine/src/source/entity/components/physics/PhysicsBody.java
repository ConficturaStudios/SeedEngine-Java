package source.entity.components.physics;

import source.engine.Debugger;
import source.engine.Time;
import source.entity.components.PrimitiveComponent;
import source.util.structures.Vector3;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public abstract class PhysicsBody extends PrimitiveComponent {

    protected double mass;
    protected boolean enabled;
    protected Vector3 velocity;

    public static final Vector3 GRAVITY = Vector3.UP.scale(-9.81);

    public PhysicsBody() {
        velocity = new Vector3();
        mass = 1;
    }

    public Vector3 getSumOfForces() {
        return GRAVITY.scale(mass);
    }

    public void applyForce() {
        Vector3 a = getSumOfForces().scale(1/mass);
        double t = Time.getDeltaTime();
        Vector3 dX = velocity.scale(t).add(a.scale(t*t).scale(0.5));
        parent.transformComponent.transform.translate(dX);
        velocity = velocity.add(a.scale(t));
    }

}
