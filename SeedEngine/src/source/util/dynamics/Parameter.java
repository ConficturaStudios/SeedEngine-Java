package source.util.dynamics;

/**
 * Superclass for all dynamic parameter instances
 *
 * @param <E> supporting parameter type
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public abstract class Parameter<E> {
    /** The identifier String of this parameter */
    private String identifier;
    /** The value of this parameter */
    private E value;
    /** The default value of this parameter */
    private E defaultValue;

    public Parameter(String identifier, E defaultValue) {
        this.identifier = identifier;
        this.defaultValue = defaultValue;
        this.setAsDefault();
    }

    /**
     * Renames this parameter
     * @param identifier new name
     */
    public void rename(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Returns this parameter's identifying name
     * @return identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Sets the value of this parameter
     * @param value new value
     */
    public void setValue(E value) {
        this.value = value;
    }

    /**
     * Sets this parameter's default value
     * @param defaultValue
     */
    public void setDefaultValue(E defaultValue){
        this.defaultValue = defaultValue;
    }

    /**
     * Sets this parameter's value equal to the default value
     */
    public void setAsDefault() {
        this.value = this.defaultValue;
    }

    /**
     * Returns the value stored in this parameter
     * @return value
     */
    public E getValue() {
        return this.value;
    }

    /**
     *Returns the default value of this parameter
     * @return
     */
    public E getDefaultValue() {
        return this.defaultValue;
    }


}
