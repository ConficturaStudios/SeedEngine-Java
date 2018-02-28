package source.render.shader;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import source.engine.Debugger;
import source.util.structures.Matrix4f;
import source.util.structures.Vector2;
import source.util.structures.Vector3;
import source.util.structures.Vector4;

import java.io.*;
import java.nio.FloatBuffer;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public abstract class ShaderProgram {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private final static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertexShaderPath, String fragmentShaderPath) {
        this.vertexShaderID = loadShader(vertexShaderPath, GL20.GL_VERTEX_SHADER);
        this.fragmentShaderID = loadShader(fragmentShaderPath, GL20.GL_FRAGMENT_SHADER);
        this.programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    protected void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    protected void loadInt(int location, int value) {
        GL20.glUniform1i(location, value);
    }

    protected void loadVector2(int location, Vector2 vector) {
        GL20.glUniform2f(location, vector.xf, vector.yf);
    }

    protected void loadVector3(int location, Vector3 vector) {
        GL20.glUniform3f(location, vector.xf, vector.yf, vector.zf);
    }

    protected void loadVector4(int location, Vector4 vector) {
        GL20.glUniform4f(location, vector.xf, vector.yf, vector.zf, vector.wf);
    }

    protected void loadBoolean(int location, boolean value) {
        GL20.glUniform1f(location, value ? 1 : 0);
    }

    protected void loadMatrix(int location, Matrix4f matrix) {
        matrixBuffer.clear();
        matrix.toBuffer(matrixBuffer);
        GL20.glUniformMatrix4fv(location, false, matrixBuffer);
    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            Debugger.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.exit(-1);
        }
        return shaderID;
    }

}
