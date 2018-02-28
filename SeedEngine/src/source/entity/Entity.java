package source.entity;

import source.engine.GameEngine;
import source.entity.components.*;
import source.entity.components.physics.PhysicsBody;
import source.event.Event;
import source.event.IEventCallable;
import source.event.IEventCallback;

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
public class Entity implements IEventCallable {

    public final int ID;

    private static int entityCount = 0;

    private List<ActorComponent> actorComponents;
    private List<SceneComponent> sceneComponents;
    private List<PrimitiveComponent> primitiveComponents;

    private Map<Event, IEventCallback> registeredEvents;

    protected boolean actorNeverTicks = false;

    public TransformComponent transformComponent;

    public RenderComponent renderComponent;

    public PhysicsBody physicsComponent;

    public Entity() {
        this.ID = entityCount++;
        this.actorComponents = new ArrayList<>();
        this.sceneComponents = new ArrayList<>();
        this.primitiveComponents = new ArrayList<>();
        this.registeredEvents = new HashMap<>();
        onCreate();
        buildEvents();

        this.transformComponent = new TransformComponent();
        this.registerComponent(transformComponent);
        this.renderComponent = new RenderComponent();
        this.registerComponent(renderComponent);

    }

    public void registerComponent(ActorComponent actorComponent) {
        if (actorComponent == null) {

            return;

        } else if (actorComponent instanceof TransformComponent ||
                actorComponent instanceof RenderComponent) {

            return;

        } else if (actorComponent instanceof MeshComponent) {

            renderComponent.setMeshComponent( (MeshComponent) actorComponent);

        } else if (actorComponent instanceof PhysicsBody) {

            physicsComponent = (PhysicsBody) actorComponent;
            registerEvent(Event.GAME_TICK, (event) -> {
                this.physicsComponent.applyForce();
            });

        } else if (actorComponent instanceof PrimitiveComponent) {

            primitiveComponents.add((PrimitiveComponent) actorComponent);

        } else if (actorComponent instanceof SceneComponent) {

            sceneComponents.add((SceneComponent) actorComponent);

        } else {

            actorComponents.add(actorComponent);

        }

        actorComponent.registerParent(this);
    }

    public void registerEvent(Event event, IEventCallback eventCallback) {
        if (registeredEvents.containsKey(event)) {
            IEventCallback i = registeredEvents.get(event);
            IEventCallback newIC = (Event) -> {
                i.callback(Event);
                eventCallback.callback(Event);
            };
            registeredEvents.put(event, newIC);
        } else {
            registeredEvents.put(event, eventCallback);
            GameEngine.EVENT_MESSAGING_SYSTEM.register(this, event);
        }
    }

    public void receiveEvent(Event event) {
        if (registeredEvents.containsKey(event)) {
            registeredEvents.get(event).callback(event);
        }
    }

    public void buildEvents() {

    }

    protected void onCreate() {

    }

    public List<ActorComponent> getActorComponents() {
        return actorComponents;
    }

    public List<SceneComponent> getSceneComponents() {
        return sceneComponents;
    }

    public List<PrimitiveComponent> getPrimitiveComponents() {
        return primitiveComponents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (o instanceof Entity) {
            Entity entity = (Entity) o;

            if (actorNeverTicks != entity.actorNeverTicks) return false;
            if (actorComponents != null ? !actorComponents.equals(entity.actorComponents) : entity.actorComponents != null)
                return false;
            if (sceneComponents != null ? !sceneComponents.equals(entity.sceneComponents) : entity.sceneComponents != null)
                return false;
            return primitiveComponents != null ? primitiveComponents.equals(entity.primitiveComponents) :
                    entity.primitiveComponents == null;
        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        return ID;
    }

}
