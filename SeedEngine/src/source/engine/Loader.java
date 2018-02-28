package source.engine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import source.entity.components.DynamicMeshComponent;
import source.entity.components.MeshComponent;
import source.entity.components.StaticMeshComponent;
import source.util.structures.Vector2;
import source.util.structures.Vector3;

import java.io.*;
import java.nio.*;
import java.util.*;

/**
 * A class for loading different forms of render data
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Loader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public MeshComponent loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices, boolean isStatic) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices, isStatic);
        storeDataInAttributeList(0, 3, positions, isStatic); //Vertex positions
        storeDataInAttributeList(1, 2, textureCoords, isStatic); //UV Texture coordinates
        storeDataInAttributeList(2, 3, normals, isStatic); //Normal vectors
        unbindVAO();
        if (isStatic) return new StaticMeshComponent(vaoID, indices.length);
        else return new DynamicMeshComponent(vaoID, indices.length);
    }

    /**
     * An overload of loadToVAO purposed for generating simple static meshes such as quads
     * @param positions
     * @return
     */
    public MeshComponent loadToVAO(float[] positions) {
        int vaoID = createVAO();
        this.storeDataInAttributeList(0, 2, positions, true);
        unbindVAO();
        return new StaticMeshComponent(vaoID, positions.length / 2);
    }

    public MeshComponent loadObjModel(String fileName, boolean isStatic) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(new File("./SeedEngine/res/obj/" + fileName + ".obj"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        List<Vector3> vertices = new ArrayList<>();
        List<Vector2> uvs = new ArrayList<>();
        List<Vector3> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] uvsArray = null;
        int[] indicesArray = null;
        try {

            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3 vertex = new Vector3(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")) {
                    Vector2 uv = new Vector2(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]));
                    uvs.add(uv);
                } else if (line.startsWith("vn ")) {
                    Vector3 normal = new Vector3(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    uvsArray = new float[vertices.size()*2];
                    normalsArray = new float[vertices.size()*3];
                    break;
                } else {

                }
            }

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                String[][] vertexList = new String[][] {
                        vertex1, vertex2, vertex3
                };

                for (int i = 0; i < 3; i++) {
                    int vertexPointer = Integer.parseInt(vertexList[i][0]) - 1;
                    indices.add(vertexPointer);

                    Vector2 currentUV = uvs.get(Integer.parseInt(vertexList[i][1]) - 1);
                    uvsArray[vertexPointer*2] = currentUV.xf;
                    uvsArray[vertexPointer*2 + 1] = 1 - currentUV.yf;

                    Vector3 currentNorm = normals.get(Integer.parseInt(vertexList[i][2]) - 1);
                    normalsArray[vertexPointer*3] = currentNorm.xf;
                    normalsArray[vertexPointer*3 + 1] = currentNorm.yf;
                    normalsArray[vertexPointer*3 + 2] = currentNorm.zf;
                }

                line = reader.readLine();

            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        for (int i = 0; i < vertices.size(); i++) {
            verticesArray[i * 3] = vertices.get(i).xf;
            verticesArray[i * 3 + 1] = vertices.get(i).yf;
            verticesArray[i * 3 + 2] = vertices.get(i).zf;
        }

        for (int i = 0; i < indicesArray.length; i++) {
            indicesArray[i] = indices.get(i);
        }

        return this.loadToVAO(verticesArray, uvsArray, normalsArray, indicesArray, isStatic);

    }

    public int loadTexture(String fileName) {

        MemoryStack stack = MemoryStack.stackPush();

        IntBuffer w = stack.mallocInt(1);
        IntBuffer h = stack.mallocInt(1);
        IntBuffer comp = stack.mallocInt(1);

        STBImage.stbi_set_flip_vertically_on_load(false);

        ByteBuffer image = STBImage.stbi_load("./SeedEngine/res/textures/" + fileName,
                w, h, comp, 4);
        if (image == null) {
            throw new RuntimeException("Failed to load a texture file");
        }

        int width = w.get();
        int height = h.get();

        int textureID = GL11.glGenTextures();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);

        textures.add(textureID);
        return textureID;
    }

    public void cleanUp() {
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        for (int texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data, boolean isStatic) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer,
                isStatic ? GL15.GL_STATIC_DRAW : GL15.GL_DYNAMIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT,false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices, boolean isStatic) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer,
                isStatic ? GL15.GL_STATIC_DRAW : GL15.GL_DYNAMIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

}
