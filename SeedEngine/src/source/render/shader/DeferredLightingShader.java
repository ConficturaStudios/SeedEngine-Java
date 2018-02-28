package source.render.shader;

import org.lwjgl.opengl.GL20;
import source.engine.GameEngine;
import source.entity.light.DirectionalLight;
import source.entity.light.PointLight;
import source.util.structures.Vector3;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class DeferredLightingShader extends ShaderProgram {
    private static final String VERTEX_FILE = "./SeedEngine/res/shaders/lightingPassVertexShader.glsl";
    private static final String FRAGMENT_FILE = "./SeedEngine/res/shaders/lightingPassFragmentShader.glsl";

    private int location_gColor;
    private int location_gPosition;
    private int location_gNormal;
    private int location_gRSMAo;
    private int location_gCamera;


    private int pointLightCount;
    private int directionalLightCount;
    private int spotLightCount;

    private int location_pointLightCount;
    private int location_directionalLightCount;
    private int location_spotLightCount;


    private int[] locations_pointLightPosition;
    private int[] locations_pointLightColor;
    private int[] locations_pointLightIntensity;
    private int[] locations_pointLightRadius;
    private int[] locations_pointLightAttenuation;


    private int[] locations_directionalLightDirection;
    private int[] locations_directionalLightColor;
    private int[] locations_directionalLightIntensity;


    private static final int MAX_LIGHTS =
            (int) GameEngine.ENGINE_INI.getFloat("Shader.Deferred", "maxLights");

    public DeferredLightingShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        pointLightCount = 0;

        locations_pointLightPosition = new int[MAX_LIGHTS];
        locations_pointLightColor = new int[MAX_LIGHTS];
        locations_pointLightIntensity = new int[MAX_LIGHTS];
        locations_pointLightRadius = new int[MAX_LIGHTS];
        locations_pointLightAttenuation = new int[MAX_LIGHTS];

        locations_directionalLightDirection = new int[MAX_LIGHTS];
        locations_directionalLightColor = new int[MAX_LIGHTS];
        locations_directionalLightIntensity = new int[MAX_LIGHTS];

        location_gColor = super.getUniformLocation("gColor");
        location_gPosition = super.getUniformLocation("gPosition");
        location_gNormal = super.getUniformLocation("gNormal");
        location_gRSMAo = super.getUniformLocation("gRSMAo");
        location_gCamera = super.getUniformLocation("gCamera");

        location_pointLightCount = super.getUniformLocation("PointLightCount");
        location_directionalLightCount = super.getUniformLocation("DirectionalLightCount");
        location_spotLightCount = super.getUniformLocation("SpotLightCount");

        for (int i = 0; i < MAX_LIGHTS; i++) {
            locations_pointLightPosition[i] =
                    super.getUniformLocation("pointLights[" + i + "].Position");
            locations_pointLightColor[i] =
                    super.getUniformLocation("pointLights[" + i + "].Color");
            locations_pointLightIntensity[i] =
                    super.getUniformLocation("pointLights[" + i + "].Intensity");
            locations_pointLightRadius[i] =
                    super.getUniformLocation("pointLights[" + i + "].Radius");
            locations_pointLightAttenuation[i] =
                    super.getUniformLocation("pointLights[" + i + "].Attenuation");
        }

        for (int i = 0; i < MAX_LIGHTS; i++) {
            locations_directionalLightDirection[i] =
                    super.getUniformLocation("directionalLights[" + i + "].Direction");
            locations_directionalLightColor[i] =
                    super.getUniformLocation("directionalLights[" + i + "].Color");
            locations_directionalLightIntensity[i] =
                    super.getUniformLocation("directionalLights[" + i + "].Intensity");
        }

    }

    public void loadPointLight(PointLight pointLight) {
        if (pointLightCount >= MAX_LIGHTS) return;

        Vector3 lColor = new Vector3(
                pointLight.getColor().getRed() / 255.0f,
                pointLight.getColor().getGreen() / 255.0f,
                pointLight.getColor().getBlue() / 255.0f
        );

        super.loadVector3(locations_pointLightPosition[pointLightCount], pointLight.transformComponent.transform.getPosition());
        super.loadVector3(locations_pointLightColor[pointLightCount], lColor);
        super.loadFloat(locations_pointLightIntensity[pointLightCount], pointLight.getIntensity());
        super.loadFloat(locations_pointLightRadius[pointLightCount], pointLight.getRadius());
        super.loadFloat(locations_pointLightAttenuation[pointLightCount], 0);

        pointLightCount++;
        GL20.glUniform1i(location_pointLightCount, pointLightCount);
    }

    public void loadDirectionalLight(DirectionalLight directionalLight) {
        if (directionalLightCount >= MAX_LIGHTS) return;

        Vector3 lColor = new Vector3(
                directionalLight.getColor().getRed() / 255.0f,
                directionalLight.getColor().getGreen() / 255.0f,
                directionalLight.getColor().getBlue() / 255.0f
        );

        super.loadVector3(locations_directionalLightDirection[directionalLightCount],
                directionalLight.transformComponent.transform.getForwardVector());
        super.loadVector3(locations_directionalLightColor[directionalLightCount], lColor);
        super.loadFloat(locations_directionalLightIntensity[directionalLightCount], directionalLight.getIntensity());

        directionalLightCount++;
        GL20.glUniform1i(location_directionalLightCount, directionalLightCount);
    }

    public void unloadLights() {
        pointLightCount = 0;
        directionalLightCount = 0;
        spotLightCount = 0;
    }

    public void loadUnlitData() {
        super.loadInt(location_gColor, 0);
        super.loadInt(location_gPosition, 1);
        super.loadInt(location_gNormal, 2);
        super.loadInt(location_gRSMAo, 3);
        super.loadInt(location_gCamera, 4);
    }
}
