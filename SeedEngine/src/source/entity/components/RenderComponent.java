package source.entity.components;

import source.render.shader.ShaderProgram;
import source.render.shader.material.Material;
import source.render.shader.StaticShader;
import source.render.object.TextureObject;

import java.util.*;

/**
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class RenderComponent extends SceneComponent {

    private boolean visible = true;

    private boolean isLight = false;

    private MeshComponent meshComponent;
    private Material material;

    public RenderComponent() {
        this.meshComponent = null;
        this.material = null;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isLight() {
        return isLight;
    }

    /**
     * Assigns the light variable. ONLY USE WITH LIGHT CLASS AND SUBCLASSES
     * @param light true if light
     */
    public void setLight(boolean light) {
        isLight = light;
    }

    public MeshComponent getMeshComponent() {
        return meshComponent;
    }

    public void setMeshComponent(MeshComponent mesh) {
        this.meshComponent = mesh;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }


}
