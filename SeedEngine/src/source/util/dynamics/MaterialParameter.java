package source.util.dynamics;

/**
 * An interface designating Parameters as a Material Parameter option
 */
public abstract class MaterialParameter<E> extends Parameter<E> {

    public MaterialParameter(String identifier, E defaultValue) {
        super(identifier, defaultValue);
    }

}
