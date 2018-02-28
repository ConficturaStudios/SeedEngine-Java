package source.event;

/**
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public interface IEventCallable {
    void buildEvents();
    void registerEvent(Event event, IEventCallback eventCallback);
    void receiveEvent(Event event);
}
