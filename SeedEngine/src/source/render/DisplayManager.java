package source.render;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import source.engine.GameEngine;
import source.engine.Scene;
import source.event.Event;
import source.util.structures.Matrix4f;
import source.util.structures.Transform;
import source.util.structures.Vector3;
import source.util.structures.Vector4;

import java.nio.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * The DisplayManager for the engine
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class DisplayManager {

    private static GLFWErrorCallback errorCallback;

    // The windowID handle
    private long windowID;

    private String title;

    private static int width;

    private static int height;

    private boolean resized;
    private boolean fullscreen;
    private boolean borderless;

    private boolean vSync;

    public List<Integer> heldKeys = new ArrayList<>();
    public List<Integer> heldMouseButtons = new ArrayList<>();

    public static Vector3 mouseRay;

    public DisplayManager() {
        this("Untitled", 640, 480, true, false, false);
    }

    public DisplayManager(String title, int screenWidth, int screenHeight, boolean vSync,
                          boolean fullscreen, boolean borderless) {
        this.title = title;
        width = screenWidth;
        height = screenHeight;
        this.vSync = vSync;
        this.resized = false;
        this.fullscreen = fullscreen;
        this.borderless = borderless;
    }

    public long getWindowID() {
        return windowID;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public boolean isResized() {
        return resized;
    }

    public void init() {

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        errorCallback = GLFWErrorCallback.createPrint(System.err);
        glfwSetErrorCallback(errorCallback);


        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current windowID hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the windowID will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the windowID will be resizable
        glfwWindowHint(GLFW_MAXIMIZED, (fullscreen && !borderless) ? GLFW_TRUE : GLFW_FALSE);
                // the windowID will be maximized if fullscreen mode enabled
        //OS X compatibility
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (fullscreen && borderless) {
            width = vidmode.width();
            height = vidmode.height();
        }

        // Create the windowID
        windowID = glfwCreateWindow(
                width,
                height,
                this.title,
                (fullscreen && borderless) ? glfwGetPrimaryMonitor() : NULL,
                NULL);
        if ( windowID == NULL )
            throw new RuntimeException("Failed to create the GLFW windowID");

        glfwSetWindowAttrib(windowID, GLFW_DECORATED, borderless ? GLFW_FALSE : GLFW_TRUE);

        glfwSetFramebufferSizeCallback(windowID, (window, width, height) -> {
            DisplayManager.width = width;
            DisplayManager.height = height;
            this.setResized(true);
            GameEngine.EVENT_MESSAGING_SYSTEM.distributeEvent(Event.WINDOW_RESIZED);
        });

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowID, (window, key, scancode, action, mods) -> {
            if ( action == GLFW_PRESS ) {
                GameEngine.EVENT_MESSAGING_SYSTEM.distributeEvent(new Event(key));
                heldKeys.add(key);
            } else if ( action == GLFW_RELEASE ) {
                GameEngine.EVENT_MESSAGING_SYSTEM.distributeEvent(new Event(key + 0x8000));
                heldKeys.remove((Integer)key);
            }
        });
        //Mouse button callback
        glfwSetMouseButtonCallback(windowID, (window, button, action, mods) -> {
            if ( action == GLFW_PRESS ) {
                GameEngine.EVENT_MESSAGING_SYSTEM.distributeEvent(new Event(13 + button));
                heldMouseButtons.add(13 + button);
            } else if ( action == GLFW_RELEASE ) {
                GameEngine.EVENT_MESSAGING_SYSTEM.distributeEvent(new Event(16 + button));
                heldMouseButtons.remove((Integer)(13 + button));
            }
        });
        //Cursor position callback
        glfwSetCursorPosCallback(windowID, (window, xPos, yPos) -> {
            Event.MousePos_dX = xPos - Event.MousePos_X;
            Event.MousePos_dY = yPos - Event.MousePos_Y;
            Event.MousePos_X = xPos;
            Event.MousePos_Y = yPos;
            if (Event.MousePos_dX != 0) {
                GameEngine.EVENT_MESSAGING_SYSTEM.distributeEvent(Event.MouseMove_X);
            }
            if (Event.MousePos_dY != 0) {
                GameEngine.EVENT_MESSAGING_SYSTEM.distributeEvent(Event.MouseMove_Y);
            }

            double x = ((2f * xPos) / getWidth()) - 1f;
            double y = ((2f * yPos) / getHeight()) - 1f;
            Vector4 clipCoords = new Vector4(x, y, -1, 1);
            Matrix4f invertedProjection = Renderer.getInverseProjectionMatrix();
            Vector4 eyeCoords = invertedProjection.multiply(clipCoords);
            eyeCoords = eyeCoords.scale(new Vector4(1,1,0,0));
            eyeCoords = eyeCoords.add(new Vector4(0,0,1,0));
            Transform transform = Scene.activeScene.camera.transformComponent.transform;
            Matrix4f inverseView = transform.getInverseViewMatrix();
            Vector4 rayWorld = inverseView.multiply(eyeCoords);
            this.mouseRay = rayWorld.toVector3().normalize().scale(new Vector3(1,-1,1));

        });
        //Mouse Scroll callback
        glfwSetScrollCallback(windowID, (window, xOffset, yOffset) -> {
            Event.MouseScroll_X = xOffset;
            Event.MouseScroll_Y = yOffset;
            GameEngine.EVENT_MESSAGING_SYSTEM.distributeEvent(Event.MouseScroll);
        });

        glfwSetCursorEnterCallback(windowID, (window, entered) -> {
            if (entered) {
                GameEngine.EVENT_MESSAGING_SYSTEM.distributeEvent(Event.MouseEnter);
            } else {
                GameEngine.EVENT_MESSAGING_SYSTEM.distributeEvent(Event.MouseExit);
            }
        });

        glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the windowID size passed to glfwCreateWindow
            glfwGetWindowSize(windowID, pWidth, pHeight);

            if (!fullscreen) {
                // Center the windowID
                glfwSetWindowPos(
                        windowID,
                        (vidmode.width() - pWidth.get(0)) / 2,
                        (vidmode.height() - pHeight.get(0)) / 2
                );
            }
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowID);
        // Enable v-sync
        if (vSync) glfwSwapInterval(1);

        // Make the windowID visible
        glfwShowWindow(windowID);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void setClearColor(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(windowID, title);
    }

    public void close() {
        // Free the windowID callbacks and destroy the windowID
        glfwFreeCallbacks(windowID);
        glfwDestroyWindow(windowID);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowID, keyCode) == GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowID);
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public boolean isvSync() {
        return this.vSync;
    }

    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }

    public void update() {
        glfwSwapBuffers(windowID);
        glfwPollEvents();
    }

}
