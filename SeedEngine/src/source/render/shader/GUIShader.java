package source.render.shader;

import source.util.structures.Matrix4f;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class GUIShader extends ShaderProgram {
    private static final String VERTEX_FILE = "./SeedEngine/res/shaders/guiVertexShader.txt";
    private static final String FRAGMENT_FILE = "./SeedEngine/res/shaders/guiFragmentShader.txt";

    private int location_transformationMatrix;

    public GUIShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformation(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
