package source.render.shader.material;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class MaterialProperties {

    private String name;

    private boolean twoSided;

    private boolean lit;

    public MaterialProperties(String name) {
        this.name = name;
        this.twoSided = false;
        this.lit = true;
    }

    public String getName() {
        return name;
    }

    public boolean isTwoSided() {
        return twoSided;
    }

    public void setTwoSided(boolean twoSided) {
        this.twoSided = twoSided;
    }

    public boolean isLit() {
        return lit;
    }

    public void setLit(boolean lit) {
        this.lit = lit;
    }
}
