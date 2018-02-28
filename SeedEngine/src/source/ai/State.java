package source.ai;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class State<E> {
    private final E value;

    public State(E value) {
        this.value = value;
    }

    public E getValue() {
        return this.value;
    }

}
