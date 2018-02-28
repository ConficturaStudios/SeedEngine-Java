package source;

import source.engine.*;
import source.entity.Camera;
import source.entity.Entity;
import source.entity.components.physics.RigidBody;
import source.entity.light.DirectionalLight;
import source.entity.light.PointLight;
import source.entity.components.StaticMeshComponent;
import source.event.Event;
import source.render.object.TextureObject;
import source.render.shader.material.Material;
import source.render.shader.material.MaterialProperties;
import source.util.command.CommandLibrary;
import source.util.generation.CompositeDataField;
import source.util.generation.FunctionalDataField;
import source.util.generation.noise.GradientNoise;
import source.util.structures.*;
import source.entity.terrain.Terrain;


import java.awt.*;

/**
 * Main execution class for debugging and engine launch control
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Main {

    public static void main(String[] args) {
        Debugger.begin();
        /*
        Rotator r = new Rotator(20,30,20);
        Quaternion q = r.toQuaternion();
        Transform t = new Transform();
        t.setRotation(r);
        Debugger.out.println("r: " + r);
        Debugger.out.println("q: " + q);
        Debugger.out.println("q.matrix: " + q.getRotationMatrix());
        Debugger.out.println("r.matrix: " + r.toLocalRotationMatrix());
        Debugger.out.println("t.matrix: " + t.getTransformationMatrix());*/

        CommandLibrary.execute("print timeScale");

        try {
            IGameLogic gameLogic = new Game("SeedEngine - [untitled]") {
                @Override
                public void init() throws Exception {
                    super.init();

                    this.scene = new Scene() {

                        @Override
                        public void buildEvents() {
                            super.buildEvents();
                        }
                    };

                    Entity entity = new Entity() {

                        @Override
                        public void buildEvents() {
                            registerEvent(Event.GAME_TICK, (event) -> {
                                transformComponent.transform.rotate(new Rotator(0,300 * Time.getDeltaTime(),0));
                            });
                        }

                    };

                    Entity sphere = new Entity();

                    sphere.registerComponent(new RigidBody());

                    PointLight pointLight = new PointLight();
                    DirectionalLight directionalLight = new DirectionalLight();

                    //GUI gui = new ScreenTexture(loader.loadTexture("DEFAULT_INVERT.png"));

                    this.camera = new Camera() {

                        private float speed = 3;

                        @Override
                        public void buildEvents() {
                            //TODO: set up directional vectors, and revise vector classes

                            registerEvent(Event.KeyDown_A, (event) -> {
                                transformComponent.transform.moveInDirection(
                                        transformComponent.transform.getRightVector().scale(
                                                new Vector3(1,0,1)).normalize(),
                                        -speed * Time.getDeltaTime()
                                );
                            });
                            registerEvent(Event.KeyDown_D, (event) -> {
                                transformComponent.transform.moveInDirection(
                                        transformComponent.transform.getRightVector().scale(
                                                new Vector3(1,0,1)).normalize(),
                                        speed * Time.getDeltaTime()
                                );
                            });
                            registerEvent(Event.KeyDown_W, (event) -> {
                                transformComponent.transform.moveInDirection(transformComponent.transform.getForwardVector().normalize(), speed * Time.getDeltaTime());
                            });
                            registerEvent(Event.KeyDown_S, (event) -> {
                                transformComponent.transform.moveInDirection(transformComponent.transform.getForwardVector().normalize(), -speed * Time.getDeltaTime());
                            });
                            registerEvent(Event.KeyDown_SPACE, (event) -> {
                                transformComponent.transform.moveInDirection(Vector3.UP, speed * Time.getDeltaTime());
                            });
                            registerEvent(Event.KeyDown_LSHIFT, (event) -> {
                                transformComponent.transform.moveInDirection(Vector3.UP, -speed * Time.getDeltaTime());
                            });
                            registerEvent(Event.KeyDown_LEFT, (event) -> {
                                transformComponent.transform.rotateAbout(
                                        -700 * Time.getDeltaTime() * speed,
                                        new Vector3(0,1,0)
                                );
                            });
                            registerEvent(Event.KeyDown_RIGHT, (event) -> {
                                transformComponent.transform.rotateAbout(
                                        700 * Time.getDeltaTime() * speed,
                                        new Vector3(0,1,0)
                                );
                            });
                            registerEvent(Event.KeyDown_DOWN, (event) -> {
                                transformComponent.transform.rotateAbout(
                                        700 * Time.getDeltaTime() * speed,
                                        new Vector3(1,0,0)
                                );
                            });
                            registerEvent(Event.KeyDown_UP, (event) -> {
                                transformComponent.transform.rotateAbout(
                                        -700 * Time.getDeltaTime() * speed,
                                        new Vector3(1,0,0)
                                );
                            });
                        }
                    };

                    this.camera.transformComponent.transform.setPosition(new Vector3(0,0,5));

                    StaticMeshComponent model =
                            (StaticMeshComponent) scene.SCENE_LOADER.loadObjModel("stall", true);
                    StaticMeshComponent sphereModel =
                            (StaticMeshComponent) scene.SCENE_LOADER.loadObjModel("sphere00", true);
                    entity.registerComponent(model);
                    sphere.registerComponent(sphereModel);
                    TextureObject textureObject = new TextureObject(
                            scene.SCENE_LOADER.loadTexture("stallTexture.png")
                    );
                    TextureObject blankTexture = new TextureObject(
                            scene.SCENE_LOADER.loadTexture("DEFAULT_BLANK.png")
                    );

                    Material mat = new Material(new MaterialProperties("M_Test"));
                    mat.addTexture(textureObject);
                    mat.compile();
                    entity.renderComponent.setMaterial(mat);

                    Material matSphere = new Material(new MaterialProperties("M_SphereTest"));
                    matSphere.addTexture(blankTexture);
                    matSphere.compile();
                    sphere.renderComponent.setMaterial(matSphere);

                    GradientNoise terrainHeightmapA = new GradientNoise(9560);
                    terrainHeightmapA.setOctaves(6);
                    //terrainHeightmapA.perOctaveCurve = (t) -> Interpolation.GAMMA_BETA_CURVE.evaluate((t + 1) / 2);

                    FunctionalDataField terrainHeightmapB = new FunctionalDataField((x, y) -> 0.0);

                    GradientNoise terrainHeightmapT = new GradientNoise(9750);
                    terrainHeightmapT.setOctaves(6);
                    terrainHeightmapT.perPointCurve = (t) -> (t + 1) / 2;

                    CompositeDataField terrainHeightmapC = new CompositeDataField(terrainHeightmapA, terrainHeightmapB,
                            Math::max);

                    Terrain terrain = new Terrain(scene.SCENE_LOADER, terrainHeightmapC);
                    Material terrainMaterial = new Material(new MaterialProperties("M_terrain"));
                    terrainMaterial.addTexture(
                            new TextureObject(scene.SCENE_LOADER.loadTexture("DEFAULT.png"))
                    );
                    terrainMaterial.compile();
                    terrain.renderComponent.setMaterial(terrainMaterial);

                    terrain.transformComponent.transform.setPosition(new Vector3(0, -40, 0));

                    terrain.renderComponent.getMaterial().setTwoSided(false);

                    pointLight.transformComponent.transform.setPosition(new Vector3(0, 5, 0));
                    pointLight.setColor(new Color(1.0f, 0.94509804f, 0.8627451f));
                    pointLight.setIntensity(10.0f);

                    directionalLight.transformComponent.transform.setPosition(new Vector3(15, 5, -6));
                    directionalLight.setColor(new Color(1.0f, 1.0f, 1.0f));
                    directionalLight.setIntensity(4.0f);

                    entity.transformComponent.transform.setPosition(new Vector3(0, -2.5, -15.5));
                    entity.transformComponent.transform.setScale(new Vector3(1, 1, 1));

                    sphere.transformComponent.transform.setPosition(new Vector3(0, 0, 0));
                    sphere.transformComponent.transform.setScale(new Vector3(1, 1, 1));

                    //entity.renderComponent.getMaterialList().get(0).setShadingModel(Material.ShadingModel.Lit);


                    scene.pushEntity(entity);
                    scene.pushEntity(sphere);
                    scene.pushEntity(pointLight);
                    scene.pushEntity(directionalLight);
                    scene.pushEntity(terrain);

                    renderer.setCurrentScene(scene);
                    this.renderer.setActiveCamera(camera);

                }
            };
            GameEngine gameEng = new GameEngine(gameLogic);
            gameEng.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }
}
