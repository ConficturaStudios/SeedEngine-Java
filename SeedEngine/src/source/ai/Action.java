package source.ai;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public interface Action<I, F> {

    State<F> perform(State<I> initial);

}
