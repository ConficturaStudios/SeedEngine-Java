package source.render.shader;

import source.entity.light.PointLight;
import source.render.shader.material.Material;
import source.util.structures.Matrix4f;
import source.util.structures.Vector3;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class StaticShader extends ShaderProgram {

    private static final String VERTEX_FILE = "./SeedEngine/res/shaders/defaultVertexShader.txt";
    private static final String FRAGMENT_FILE = "./SeedEngine/res/shaders/defaultFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_specular;

    private int[] location_toggles;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColor = super.getUniformLocation("lightColor");
        location_specular = super.getUniformLocation("specular");

        location_toggles = new int[15];
        location_toggles[0] = super.getUniformLocation("lightEnabled");
        location_toggles[1] = super.getUniformLocation("emissiveColorEnabled");
        location_toggles[2] = super.getUniformLocation("baseColorEnabled");
        location_toggles[3] = super.getUniformLocation("metallicEnabled");
        location_toggles[4] = super.getUniformLocation("specularEnabled");
        location_toggles[5] = super.getUniformLocation("roughnessEnabled");
        location_toggles[6] = super.getUniformLocation("opacityEnabled");
        location_toggles[7] = super.getUniformLocation("opacityMaskEnabled");
        location_toggles[8] = super.getUniformLocation("normalEnabled");
        location_toggles[9] = super.getUniformLocation("positionOffsetEnabled");
        location_toggles[10] = super.getUniformLocation("displacementEnabled");
        location_toggles[11] = super.getUniformLocation("tessellationEnabled");
        location_toggles[12] = super.getUniformLocation("subsurfaceEnabled");
        location_toggles[13] = super.getUniformLocation("ambientOcclusionEnabled");
        location_toggles[14] = super.getUniformLocation("refractionEnabled");
    }

    public void loadLight(PointLight pointLight) {
        super.loadVector3(location_lightPosition, pointLight.transformComponent.transform.getPosition());
        super.loadVector3(location_lightColor, new Vector3(
                pointLight.getColor().getRed() / 255.0,
                pointLight.getColor().getGreen() / 255.0,
                pointLight.getColor().getBlue() / 255.0
        ));
    }

    public void loadMaterial(Material material) {
        for (int i = 0; i < location_toggles.length; i++) {
            super.loadBoolean(location_toggles[i], true);
        }
    }

    public void loadSpecularVariables(float specular) {
        super.loadFloat(location_specular, specular);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrix(location_projectionMatrix, projection);
    }

    public void loadViewMatrix(Matrix4f view) {
        super.loadMatrix(location_viewMatrix, view);
    }

}
