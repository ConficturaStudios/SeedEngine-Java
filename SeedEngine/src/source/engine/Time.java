package source.engine;


import source.event.Event;

/**
 * The global time system
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Time {

    private static double lastLoopTime = getTime();
    private static float deltaTime;
    private static float timeScale = 1.0f;
    private static boolean paused = false;
    private static float lastTimeScale = 1.0f;
    private static double startTime = getTime();


    public static double getTime() {
        return (System.nanoTime() / 1000000000.0);
    }

    public static double getElapsedTime() {
        return (System.nanoTime() / 1000000000.0) - startTime;
    }

    public static float getUpTime() {
        double time = getTime();
        float deltaTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return deltaTime * getTimeScale();
    }

    public static float getDeltaTime() {
        return deltaTime * getTimeScale();
    }

    public static void setDeltaTime(float delta) {
        deltaTime = delta;
    }

    public static boolean isPaused() {
        return paused;
    }

    public static void togglePause() {
        if (paused) {
            setTimeScale(lastTimeScale);
            GameEngine.EVENT_MESSAGING_SYSTEM.distributeEvent(Event.GAME_UNPAUSE);
            paused = false;
        } else {
            setTimeScale(0.0f);
            GameEngine.EVENT_MESSAGING_SYSTEM.distributeEvent(Event.GAME_PAUSE);
            paused = true;
        }
    }

    public static double getLastLoopTime() {
        return lastLoopTime;
    }

    public static float getTimeScale() {
        return timeScale;
    }

    public static void setTimeScale(float scale) {
        lastTimeScale = timeScale;
        timeScale = scale;
    }

}
