package source.entity.terrain;

import source.entity.Entity;
import source.engine.Loader;
import source.entity.components.MeshComponent;
import source.util.generation.FunctionalDataField;
import source.util.generation.ProceduralDataField;
import source.util.generation.noise.GradientNoise;
import source.util.generation.noise.Noise;
import source.util.structures.Vector3;

/**
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Terrain extends Entity {

    private MeshComponent meshComponent;

    private static final FunctionalDataField FLAT_HEIGHT = new FunctionalDataField((x, y) -> 0);
    private final float[] dataField = new float[R * R];

    private static final float MAX_HEIGHT = 40;

    private static final int R = ProceduralDataField.RESOLUTION;

    private static final float TERRAIN_SIZE = 200;
    private static final int VERTEX_DENSITY = R;

    public Terrain(Loader loader) {
        this(loader, FLAT_HEIGHT);
    }

    public Terrain(Loader loader, ProceduralDataField heightmap) {
        double[] noiseFieldD = heightmap.generate(0,0);

        for (int i = 0; i < dataField.length; i++)
        {
            dataField[i] = (float) noiseFieldD[i];
        }
        this.meshComponent = generateTerrain(loader);
        super.registerComponent(this.meshComponent);
    }

    private MeshComponent generateTerrain(Loader loader) {
        int vertexCount = VERTEX_DENSITY * VERTEX_DENSITY;
        float[] vertices = new float[vertexCount * 3];
        float[] normals = new float[vertexCount * 3];
        float[] uvs = new float[vertexCount * 2];
        int[] indices = new int[6*(VERTEX_DENSITY-1)*(VERTEX_DENSITY-1)];
        int vertexPointer = 0;
        for (int i = 0; i < VERTEX_DENSITY; i++) {
            for (int j = 0; j < VERTEX_DENSITY; j++) {
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_DENSITY - 1) * TERRAIN_SIZE - TERRAIN_SIZE / 2;

                vertices[vertexPointer*3+1] = MAX_HEIGHT * dataField[j + i * R]; //height----------
                //vertices[vertexPointer*3+1] = 0; //height-----------

                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_DENSITY - 1) * TERRAIN_SIZE - TERRAIN_SIZE / 2;


                try {
                    float hL = vertices[((i) * VERTEX_DENSITY + j - 1)*3+1];
                    float hR = vertices[((i) * VERTEX_DENSITY + j + 1)*3+1];
                    float hD = vertices[((i - 1) * VERTEX_DENSITY + j)*3+1];
                    float hU = vertices[((i + 1) * VERTEX_DENSITY + j)*3+1];

                    Vector3 normal = new Vector3(hL - hR, hD - hU, 2.0).normalize();

                    normals[vertexPointer*3] = normal.xf; //0 for flat
                    normals[vertexPointer*3+1] = normal.yf; //1 for flat
                    normals[vertexPointer*3+2] = normal.zf; //0 for flat
                } catch (ArrayIndexOutOfBoundsException e) {
                    normals[vertexPointer*3] = 0; //0 for flat
                    normals[vertexPointer*3+1] = 1; //1 for flat
                    normals[vertexPointer*3+2] = 0; //0 for flat
                }

                //normals[vertexPointer*3] = 0; //0 for flat
                //normals[vertexPointer*3+1] = 1; //1 for flat
                //normals[vertexPointer*3+2] = 0; //0 for flat

                uvs[vertexPointer*2] = (float)j/((float)VERTEX_DENSITY - 1);
                uvs[vertexPointer*2+1] = (float)i/((float)VERTEX_DENSITY - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz = 0; gz < VERTEX_DENSITY - 1; gz++){
            for(int gx = 0 ; gx < VERTEX_DENSITY - 1; gx++){
                int topLeft = (gz * VERTEX_DENSITY) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * VERTEX_DENSITY) + gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, uvs, normals, indices, false);
    }

    public MeshComponent getMeshComponent() {
        return meshComponent;
    }
}
