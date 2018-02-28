package source.engine;

import org.lwjgl.glfw.GLFW;
import source.event.EventMessagingSystem;
import source.render.DisplayManager;
import source.render.Renderer;
import source.util.iniReader.IniFile;


/**
 * Game engine thread manager
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class GameEngine implements Runnable {

    public static final IniFile ENGINE_INI = new IniFile("./SeedEngine/res/ini/Engine.ini");

    public static final EventMessagingSystem EVENT_MESSAGING_SYSTEM = new EventMessagingSystem();

    public static long WINDOW_ID;

    private final Thread gameLoopThread;

    private final IGameLogic gameLogic;

    private static DisplayManager display;

    private static final float TARGET_FPS = ENGINE_INI.getFloat("Engine", "targetFPS");
    private static final float TARGET_UPS = ENGINE_INI.getFloat("Engine", "targetUPS");

    public static float CURRENT_FPS = TARGET_FPS;
    public static float CURRENT_UPS = TARGET_UPS;

    private boolean running = false;

    public GameEngine(IGameLogic gameLogic) {

        this.gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");

        display = new DisplayManager(
                gameLogic.getTitle(),
                (int)ENGINE_INI.getFloat("Display", "defaultWidth"),
                (int)ENGINE_INI.getFloat("Display", "defaultHeight"),
                ENGINE_INI.getBoolean("Display", "vSync"),
                ENGINE_INI.getBoolean("Display", "fullscreen"),
                ENGINE_INI.getBoolean("Display", "borderless"));
        this.gameLogic = gameLogic;

    }

    public void start() {
        String osName = System.getProperty("os.name");
        if (osName.contains("Mac")) {
            gameLoopThread.run();
        } else {
            gameLoopThread.start();
        }
    }

    @Override
    public void run() {
        this.running = true;
        try {
            init();
            mainLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        display.init();
        WINDOW_ID = display.getWindowID();
        try {
            this.gameLogic.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mainLoop() {
        float deltaTime;
        float accumulator = 0.0f;
        float interval = 1.0f / TARGET_UPS;

        while (this.running && !display.windowShouldClose()) {
            deltaTime = Time.getUpTime();
            accumulator += deltaTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                Time.setDeltaTime(interval);
                CURRENT_UPS = 1 / interval;
                accumulator -= interval;
            }

            render();

            CURRENT_FPS = 1 / deltaTime;

            if (display.isvSync()) sync();
        }

        gameLogic.onClose();
        display.close();

    }

    private void input() {
        this.gameLogic.input(display);
    }

    private void update(float interval) {
        this.gameLogic.update(interval);
    }

    protected void render() {
        this.gameLogic.render(display);
        display.update();
    }

    private void sync() {
        float loopSlot = 1.0f / TARGET_FPS;
        double endTime = Time.getLastLoopTime() + loopSlot;
        while (Time.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static DisplayManager getDisplay() {
        return display;
    }

    public static void close() {
        GLFW.glfwSetWindowShouldClose(GameEngine.WINDOW_ID, true);
    }

}
