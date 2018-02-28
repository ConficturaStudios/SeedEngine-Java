package source.engine;

import source.entity.Camera;
import source.event.Event;
import source.render.DisplayManager;
import source.render.Renderer;
import source.render.object.GUI;
import source.render.object.TextureObject;
import source.util.structures.Vector2;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glViewport;

/**
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Game implements IGameLogic {

    private String title;

    protected Renderer renderer;

    protected List<Scene> scenes;

    protected Scene scene;

    protected Camera camera;

    protected int renderMode = Renderer.RENDER_LIT; //TODO: add console command to enable flags, toggle flags, and disable flags

    public Game(String title) {
        this.scenes = new ArrayList<>();
        this.title = title;
        this.scene = new Scene();
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void init() throws Exception {

        this.camera = new Camera();
        renderer = new Renderer();
        renderer.init();

    }

    @Override
    public void input(DisplayManager display) {
        for (int heldKey : display.heldKeys) {
            GameEngine.EVENT_MESSAGING_SYSTEM.distributeEvent(new Event(heldKey + 0xF000));
        }
        for (int heldMouseButton : display.heldMouseButtons) {
            GameEngine.EVENT_MESSAGING_SYSTEM.distributeEvent(new Event(heldMouseButton + 6));
        }
    }

    @Override
    public void update(float deltaTime) {
        scene.tickGame(deltaTime);
    }

    @Override
    public void render(DisplayManager display) {
        if (display.isResized()) {
            glViewport(0, 0, DisplayManager.getWidth(), DisplayManager.getHeight());
            display.setResized(false);
        }
        renderer.prepare();
        renderer.render(this.renderMode);
    }

    @Override
    public void onClose() {
        renderer.cleanUp();
        this.scene.SCENE_LOADER.cleanUp();
    }
}
