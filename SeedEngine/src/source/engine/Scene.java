package source.engine;

import org.lwjgl.glfw.GLFW;
import source.entity.Camera;
import source.entity.Entity;
import source.entity.components.RenderComponent;
import source.entity.light.Light;
import source.event.Event;
import source.event.IEventCallable;
import source.event.IEventCallback;
import source.render.object.GUI;
import source.render.shader.material.Material;

import java.util.*;

/**
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Scene implements IEventCallable {

    public static final Scene DEFAULT_SCENE = new Scene();
    public static Scene activeScene = DEFAULT_SCENE;

    public final ArrayList<Entity> renderQueue;
    public final ArrayList<Light> lightQueue;
    public final ArrayList<GUI> guiQueue;


    public final Map<Material, List<Integer>> renderQueueHash;

    private float deltaTime;

    private Map<Event, IEventCallback> registeredEvents;

    public final Loader SCENE_LOADER = new Loader();

    public Camera camera = new Camera();

    public Scene() {
        this.renderQueue = new ArrayList<>();
        this.lightQueue = new ArrayList<>();
        this.guiQueue = new ArrayList<>();
        this.renderQueueHash = new HashMap<>();
        registeredEvents = new HashMap<>();
        buildEvents();
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public void buildEvents() {
        registerEvent(Event.KeyRelease_ESC, (event) -> {
            GLFW.glfwSetWindowShouldClose(GameEngine.WINDOW_ID, true);
        });
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

    public void tickGame(float deltaTime) {
        this.deltaTime = deltaTime;
        GameEngine.EVENT_MESSAGING_SYSTEM.distributeEvent(Event.GAME_TICK);
    }

    public void pushEntity(Entity entity) {
        if (entity == null) return;

        if (entity instanceof Light) {
            lightQueue.add( (Light) entity );
            return;
        }

        RenderComponent rc = entity.renderComponent;
        renderQueue.add(entity);
        int index = renderQueue.size() - 1;

        if (renderQueueHash.containsKey(rc.getMaterial())) {
            List<Integer> old = renderQueueHash.get(rc.getMaterial());
            old.add(index);
        } else {
            List<Integer> newValue = new ArrayList<>();
            newValue.add(index);
            renderQueueHash.put(rc.getMaterial(), newValue);
        }


    }

    public void pushGUI(GUI gui) {
        if (gui == null) return;
        guiQueue.add(gui);
    }

    public void cleanUp() {
        for (Map.Entry<Material, List<Integer>> entry :
                this.renderQueueHash.entrySet()) {
            entry.getKey().cleanUp();
        }
    }

}
