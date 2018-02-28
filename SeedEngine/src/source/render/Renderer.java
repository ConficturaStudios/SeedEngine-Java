package source.render;

import org.lwjgl.opengl.*;
import source.engine.GameEngine;
import source.engine.Loader;
import source.engine.Scene;
import source.engine.Time;
import source.entity.Camera;
import source.entity.Entity;
import source.entity.light.DirectionalLight;
import source.entity.light.Light;
import source.entity.light.PointLight;
import source.entity.components.MeshComponent;
import source.entity.components.RenderComponent;
import source.entity.components.StaticMeshComponent;
import source.event.Event;
import source.event.IEventCallable;
import source.event.IEventCallback;
import source.render.object.GUI;
import source.render.object.RenderTarget;
import source.render.object.TextureObject;
import source.render.shader.DeferredLightingShader;
import source.render.shader.GUIShader;
import source.render.shader.ShaderProgram;
import source.render.shader.material.Material;
import source.render.shader.material.MaterialCompilationException;
import source.util.structures.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Render management system
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public final class Renderer implements IEventCallable {

    private static final Loader RENDER_LOADER = new Loader();

    private Map<Event, IEventCallback> registeredEvents;

    private GUI console = new GUI(
            new TextureObject(RENDER_LOADER.loadTexture("ui/UIPanelB.png")).getTextureID(),
            new Vector2(-0.75,0.75),
            200, 200
    ) {
        private boolean open;
        @Override
        public void buildEvents() {
            open = false;
            setPosition(new Vector2(0, -0.8));
            setScale(new Vector2(1, 0.4));
            registerEvent(Event.KeyRelease_TAB, (event) -> {
                open = !open;
                this.setVisible(open);
                Time.togglePause();
            });
        }
    };

    private Scene currentScene = Scene.DEFAULT_SCENE;

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private static int FILLED_TEXTURE_SLOTS;

    private static Matrix4f projectionMatrix;
    private static Matrix4f inverseProjectionMatrix;

    private RenderTarget deferredFBO;

    private static final StaticMeshComponent QUAD;

    private static final GUIShader guiShader = new GUIShader();
    private static final DeferredLightingShader lightingShader = new DeferredLightingShader();
    private static final TextureObject blankTexture;

    private final ArrayList<ShaderProgram> postProcessShaders = new ArrayList<>();

    //region Render Flags

    /** Enables surface rendering */
    public static final int RENDER_SURFACE_FLAG = 0x0000_0001;
    /** Enables surface color rendering; requires RENDER_SURFACE_FLAG to be enabled */
    public static final int RENDER_COLOR_FLAG = 0x0000_0002;
    /** Enables shadows; requires RENDER_SURFACE_FLAG to be enabled */
    public static final int RENDER_LIGHTING_FLAG = 0x0000_0003;
    /** Enables wireframe rendering */
    public static final int RENDER_WIREFRAME_FLAG = 0x0000_0004;
    /** Enables wireframe rendering */
    public static final int RENDER_GUI_FLAG = 0x0000_0005;
    /** Enables post-process rendering */
    public static final int RENDER_POST_FLAG = 0x0000_0006;

    public static final int RENDER_UNLIT =
            RENDER_SURFACE_FLAG | RENDER_COLOR_FLAG | RENDER_GUI_FLAG;
    public static final int RENDER_LIT =
            RENDER_SURFACE_FLAG | RENDER_COLOR_FLAG | RENDER_LIGHTING_FLAG | RENDER_GUI_FLAG;
    public static final int RENDER_WIRE =
            RENDER_WIREFRAME_FLAG;

    //endregion

    static {
        float[] positions = {
                -1, 1,
                -1, -1,
                1, 1,
                1, -1
        };
        QUAD = (StaticMeshComponent) RENDER_LOADER.loadToVAO(positions);
        blankTexture = new TextureObject(RENDER_LOADER.loadTexture("DEFAULT_BLANK.png"));
    }

    public Renderer() {
        enableBackFaceCulling();
        updateProjectionMatrix();

        this.registeredEvents = new HashMap<>();
        buildEvents();
    }

    //region Initialization

    public void init() throws Exception {

        this.deferredFBO = new RenderTarget(5);

    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        clear();
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    }

    //endregion

    //region Culling

    private static void enableBackFaceCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    private static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    //endregion

    //region Scene Control

    public void setCurrentScene(Scene scene) {
        currentScene.cleanUp();
        currentScene = scene;
        Scene.activeScene = scene;
    }

    public Scene getCurrentScene() {
        return this.currentScene;
    }

    //endregion

    //region Camera Control

    public void setActiveCamera(Camera camera) {
        currentScene.camera = camera;
    }

    public Camera getActiveCamera() {
        return currentScene.camera;
    }

    //endregion

    //region Render Passes

    public void render(int mode) {

        if ((mode & RENDER_SURFACE_FLAG) == RENDER_SURFACE_FLAG) {
            if ((mode & RENDER_LIGHTING_FLAG) == RENDER_LIGHTING_FLAG) {
                deferredRender(mode);
            } else {
                unlitRender(mode);
            }
            if ((mode & RENDER_POST_FLAG) == RENDER_POST_FLAG) {
                postProcessRender(mode);
            }
        }

        //TODO: create text rendering system

        consoleRender();

        if ((mode & RENDER_GUI_FLAG) == RENDER_GUI_FLAG) guiRender();

    }

    private void unlitRender(int mode) {

        for (int i = 0; i < this.currentScene.renderQueue.size(); i++) {
            //get current render component
            RenderComponent renderComponent = this.currentScene.renderQueue.get(i).renderComponent;
            //Skip invalid render components
            if (renderComponent.getMeshComponent() == null ||
                    !renderComponent.isVisible()) {
                continue;
            }

            //Render with material shader
            Material mat = renderComponent.getMaterial();
            MeshComponent mesh = renderComponent.getMeshComponent();

            if (mat == null || mesh == null) {
                enableBackFaceCulling();
                continue;
            }

            //Begin shader program on GPU
            try {

                //Initialize shader
                mat.start();

                //Load projection and view matrices
                mat.setMatrix(Material.PROJECTION_MATRIX, projectionMatrix);
                mat.setMatrix(Material.VIEW_MATRIX, getActiveCamera().transformComponent.transform.getViewMatrix());
                //Apply Material settings
                if (mat.isTwoSided()) {
                    disableCulling();
                } else {
                    enableBackFaceCulling();
                }
                //Get textures
                List<TextureObject> textures = mat.getTextureSamples();

                //prepare model
                GL30.glBindVertexArray(mesh.getVaoID());
                GL20.glEnableVertexAttribArray(0);
                GL20.glEnableVertexAttribArray(1);
                GL20.glEnableVertexAttribArray(2);

                //Bind relevant textures
                if ((mode & RENDER_COLOR_FLAG) == RENDER_COLOR_FLAG) {
                    for (TextureObject texture : textures) {
                        bindTexture(texture.getTextureID());
                    }
                } else {
                    for (int j = 0; j < textures.size(); j++) {
                        bindTexture(blankTexture.getTextureID());
                    }
                }


                //Iterate through instances
                for (Integer index : this.currentScene.renderQueueHash.get(mat)) {
                    Entity e = this.currentScene.renderQueue.get(index);

                    //prepare the instance
                    Matrix4f transformationMatrix = e.transformComponent.transform.getTransformationMatrix();
                    mat.setMatrix(Material.TRANSFORMATION_MATRIX, transformationMatrix);

                    //load all uniform values
                    mat.loadUniforms();

                    //draw the instance
                    GL11.glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
                }

                clearTextures();

                GL20.glDisableVertexAttribArray(0);
                GL20.glDisableVertexAttribArray(1);
                GL20.glDisableVertexAttribArray(2);
                GL30.glBindVertexArray(0);

                mat.stop();

            } catch (MaterialCompilationException e) {
                //continue;
            }

        }

    }

    private void deferredRender(int mode) {

        deferredFBO.attachToRenderer();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        unlitRender(mode);

        //region Lighting pass

        GL30.glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
        GL30.glBindFramebuffer(GL_READ_FRAMEBUFFER, deferredFBO.getBuffer());

        lightingShader.start();
        GL30.glBindVertexArray(QUAD.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        FILLED_TEXTURE_SLOTS += deferredFBO.bindAllTextures();

        lightingShader.loadUnlitData();

        for (Light light : this.currentScene.lightQueue) {
            //TODO: optimize lights
            if (light instanceof PointLight) {
                lightingShader.loadPointLight( (PointLight) light);
            } else if (light instanceof DirectionalLight) {
                lightingShader.loadDirectionalLight( (DirectionalLight) light);
            }
        }

        GL11.glDrawArrays(GL_TRIANGLE_STRIP, 0, QUAD.getVertexCount());

        lightingShader.unloadLights();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        clearTextures();
        lightingShader.stop();

        //endregion

        GL30.glBindFramebuffer(GL_FRAMEBUFFER, 0);


    }

    private void guiRender() {
        guiShader.start();
        GL30.glBindVertexArray(QUAD.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        for (GUI gui : this.currentScene.guiQueue) {
            if (gui.isVisible()) {
                bindTexture(gui.getTextureID());
                Matrix4f matrix = Transform.get2DTransformationMatrix(
                        gui.getPosition(), gui.getScale()
                );
                guiShader.loadTransformation(matrix);
                GL11.glDrawArrays(GL_TRIANGLE_STRIP, 0, QUAD.getVertexCount());
                clearTextures();
            }
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        guiShader.stop();
    }

    private void consoleRender() {
        guiShader.start();
        GL30.glBindVertexArray(QUAD.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        if (console.isVisible()) {
            bindTexture(console.getTextureID());
            Matrix4f matrix = Transform.get2DTransformationMatrix(
                    console.getPosition(), console.getScale()
            );
            guiShader.loadTransformation(matrix);
            GL11.glDrawArrays(GL_TRIANGLE_STRIP, 0, QUAD.getVertexCount());
            clearTextures();
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        guiShader.stop();
    }

    private void textRender() {
        //TODO: create text shader for distance field text
    }

    private void postProcessRender(int mode) {

    }

    private void lineRender(int mode) {
        Vector3 position = getActiveCamera().transformComponent.transform.getPosition();
        Vector3 endpoint = DisplayManager.mouseRay.scale(20).add(position);
        float[] positions = new float[] {
                position.xf, position.yf, position.zf,
                endpoint.xf, endpoint.yf, endpoint.zf
        };
    }

    //endregion

    //region OpenGL helpers

    private void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Binds the passed texture to the next available texture slot
     * @param texture texture to bind
     */
    private void bindTexture(int texture) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + FILLED_TEXTURE_SLOTS);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        FILLED_TEXTURE_SLOTS++;
    }

    private void clearTextures() {
        for (int i = 0; i < FILLED_TEXTURE_SLOTS; i++) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + FILLED_TEXTURE_SLOTS);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }
        FILLED_TEXTURE_SLOTS = 0;
    }

    public void cleanUp() {
        this.currentScene.cleanUp();
        lightingShader.cleanUp();
        guiShader.cleanUp();
    }

    private void updateProjectionMatrix() {
        float aspectRatio = (float) DisplayManager.getWidth() / (float) DisplayManager.getHeight();
        float x_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) / aspectRatio);
        float y_scale = x_scale * aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f(
                new Vector4(x_scale, 0, 0, 0),
                new Vector4(0, y_scale, 0, 0),
                new Vector4(0, 0, -((FAR_PLANE + NEAR_PLANE) / frustum_length), -1),
                new Vector4(0, 0, -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length), 0)
        );
        inverseProjectionMatrix = new Matrix4f(
                new Vector4(1/x_scale, 0, 0, 0),
                new Vector4(0, 1/y_scale, 0, 0),
                new Vector4(0, 0, 0, -1/((2 * NEAR_PLANE * FAR_PLANE) / frustum_length)),
                new Vector4(0, 0, -1, ((FAR_PLANE + NEAR_PLANE) / frustum_length)/
                        ((2 * NEAR_PLANE * FAR_PLANE) / frustum_length))
        );
    }

    public static Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public static Matrix4f getInverseProjectionMatrix() {
        return inverseProjectionMatrix;
    }

    @Override
    public void buildEvents() {
        registerEvent(Event.WINDOW_RESIZED, (event) -> {
            updateProjectionMatrix();
        });
    }

    @Override
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

    @Override
    public void receiveEvent(Event event) {
        if (registeredEvents.containsKey(event)) {
            registeredEvents.get(event).callback(event);
        }
    }

    //endregion
}
