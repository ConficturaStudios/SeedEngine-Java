package source.render.shader;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public abstract class ShaderBuilder {
    public static final String[] TEXTURE_SAMPLERS = new String[] {
            "textureSampler0",
            "textureSampler1",
            "textureSampler2",
            "textureSampler3",
            "textureSampler4",
            "textureSampler5",
            "textureSampler6",
            "textureSampler7",
            "textureSampler8",
            "textureSampler9",
            "textureSampler10",
            "textureSampler11",
            "textureSampler12",
            "textureSampler13",
            "textureSampler14",
            "textureSampler15"
    };

    public void build() {

    }

    public String buildPosition() {
        return "gPosition = FragPos;";
    }


}
