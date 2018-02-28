package source.util.dynamics;

/**
 * A boolean parameter that stores a single boolean value
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class BoolParameter extends MaterialParameter<Boolean> {

    public BoolParameter(String identifier) {
        super(identifier, false);
    }

}
