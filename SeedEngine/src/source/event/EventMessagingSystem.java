package source.event;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


/**
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class EventMessagingSystem {

    private Map<Event, List<IEventCallable>> registeredActors = new HashMap<>();

    public void register(IEventCallable eventCallable, Event event) {
        if (registeredActors.containsKey(event)) {
            registeredActors.get(event).add(eventCallable);
        } else {
            registeredActors.put(event, new ArrayList<>());
            registeredActors.get(event).add(eventCallable);
        }
    }

    public void unregister(IEventCallable eventCallable, Event event) throws IllegalArgumentException {
        if (registeredActors.containsKey(event)) {
            if (registeredActors.get(event).contains(eventCallable)) {
                registeredActors.get(event).remove(eventCallable);
            } else {
                throw new IllegalArgumentException("Invalid eventCallable");
            }
        } else {
            throw new IllegalArgumentException("Invalid event id");
        }
    }

    public void distributeEvent(Event event) {
        if (registeredActors.containsKey(event)) {
            for (IEventCallable eventCallable : registeredActors.get(event)) {
                eventCallable.receiveEvent(event);
            }
        }
    }

}
