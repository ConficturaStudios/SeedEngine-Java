package source.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Association<A, B> {
    private final State<A> state;
    private final Action<A, B> action;
    private float strength;

    public Association(State<A> state, Action<A, B> action, float strength) {
        this.state = state;
        this.action = action;
        this.strength = (strength < 1) ? ((strength > 0) ? strength : 0) : 1;
    }

    public State<A> getState() {
        return state;
    }

    public Action<A, B> getAction() {
        return action;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = (strength < 1) ? ((strength > 0) ? strength : 0) : 1;
    }
}
