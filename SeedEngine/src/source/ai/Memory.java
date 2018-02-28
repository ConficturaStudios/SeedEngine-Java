package source.ai;

import java.util.ArrayList;
import java.util.List;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Memory {

    private final List<Association> memories;

    public Memory() {
        this.memories = new ArrayList<>();
    }

    public float addAssociation(Association association) {
        if (memories.contains(association)) {
            Association a = memories.get(memories.indexOf(association));
            a.setStrength(association.getStrength() + a.getStrength());
            return a.getStrength();
        } else {
            memories.add(association);
            return association.getStrength();
        }
    }

}
