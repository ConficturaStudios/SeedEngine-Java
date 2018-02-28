package source.render.object;

import source.engine.GameEngine;
import source.event.Event;
import source.event.IEventCallable;
import source.event.IEventCallback;
import source.render.DisplayManager;
import source.util.structures.Vector2;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class GUI extends TextureObject implements IEventCallable {

    private Vector2 position;
    private Vector2 scale;

    private boolean visible;

    private Map<Event, IEventCallback> registeredEvents;

    public GUI(int id, Vector2 position, Vector2 scale) {
        super(id);
        this.position = position;
        this.scale = scale;
        registeredEvents = new HashMap<>();
        buildEvents();
    }
    public GUI(int id, Vector2 position, int width, int height) {
        this(id, position, new Vector2(
                ((double)width) / DisplayManager.getWidth(),
                ((double)height) / DisplayManager.getHeight()
        ));
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getScale() {
        return scale;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }

    @Override
    public void buildEvents() {

    }

    @Override
    public void registerEvent(Event event, IEventCallback eventCallback) {
        registeredEvents.put(event, eventCallback);
        GameEngine.EVENT_MESSAGING_SYSTEM.register(this, event);
    }

    @Override
    public void receiveEvent(Event event) {
        if (registeredEvents.containsKey(event)) {
            registeredEvents.get(event).callback(event);
        }
    }
}
