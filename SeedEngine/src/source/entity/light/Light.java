package source.entity.light;

import source.entity.Entity;
import source.util.structures.Vector3;

import java.awt.*;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public abstract class Light extends Entity {
    protected Color color;
    protected float intensity;

    protected Light() {
        this.renderComponent.setLight(true);
        this.color = new Color(255,255,255);
        this.intensity = 1;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
