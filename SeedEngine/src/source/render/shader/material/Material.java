package source.render.shader.material;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import source.engine.Debugger;
import source.engine.GameEngine;
import source.render.object.TextureObject;
import source.util.collections.Pair;
import source.util.dynamics.*;
import source.util.structures.Matrix4f;
import source.util.structures.Vector2;
import source.util.structures.Vector3;
import source.util.structures.Vector4;

import java.io.*;
import java.nio.FloatBuffer;
import java.util.*;

/**
 * A Material object to represent a custom object shader
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Material {

    //region Shader build collections

    private List<MaterialParameter> vertexShaderUniforms; //Material parameters needed for this material
    private List<MaterialParameter> vertexShaderInputs; //Material inputs needed for this material
    private Map<MaterialParameter, MaterialFunction> vertexShaderOutputs; //Parameters passed between shaders

    private List<MaterialParameter> fragmentShaderUniforms; //Material parameters needed for this material
    private Map<MaterialParameter, MaterialFunction> fragmentShaderOutputs; //Parameters out of shaders
    private List<Pair<MaterialParameter, MaterialFunction>> fragmentShaderLayoutOutputs; //Parameters out of shaders at layout points
    private Map<MaterialParameter, MaterialFunction> fragmentShaderAllOutputs; //Parameters out of shaders at layout points

    private Map<Integer, String> shaders;

    //endregion

    private final int programID;

    private final static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    private Map<Integer, MaterialParameter> uniforms;

    private Stack<Integer> attachedShaders;

    //region Texture Fields
    
    private static final int MAX_TEXTURES = (int) GameEngine.ENGINE_INI.getFloat("Material", "maxTextures");

    private TextureObject[] textureSamples = new TextureObject[MAX_TEXTURES];

    private int FILLED_TEXTURE_SLOTS;

    //endregion
    
    //region shader path data

    private String path;
    private String vertexShaderPath;
    private String geometryShaderPath;
    private String fragmentShaderPath;

    //endregion

    private MaterialProperties properties; //implement

    //region Uniform constants



    public static final String TRANSFORMATION_MATRIX = "transformationMatrix";
    public static final String PROJECTION_MATRIX = "projectionMatrix";
    public static final String VIEW_MATRIX = "viewMatrix";

    //endregion

    //region Shader Templates

    protected static String textureSampleTemplate = "textureSampler";

    private static String vertexShaderTemplate =
            "#version 400 core\n" +
                    "\n" +
                    "@V@in\n" +
                    "\n" +
                    "@V@out\n" +
                    "\n" +
                    "@V@uniform\n" +
                    "\n" +
                    "@V@function" +
                    "\n" +
                    "void main(void) {\n" +
                    "\n" +
                    "@V@outFunction\n" +
                    "\n" +
                    "}";

    private static String geometryShaderTemplate =
            "#version 400 core\n";

    private static String fragmentShaderTemplate =
            "#version 400 core\n" +
                    "@F@layout\n" +
                    "\n" +
                    "@F@out\n" +
                    "\n"+
                    "@F@in\n" +
                    "\n" +
                    "@F@uniform\n" +
                    "\n" +
                    "@F@function" +
                    "\n" +
                    "void main(void) {\n" +
                    "\n" +
                    "@F@outFunction\n" +
                    "\n" +
                    "}";
    //endregion

    private boolean compiled;


    public Material(MaterialProperties properties) {

        //TODO: check to see if name is already used, if so append/increment number

        this.properties = properties;

        this.programID = GL20.glCreateProgram();

        this.uniforms = new HashMap<>();


        this.shaders = new HashMap<>();

        this.path = "./SeedEngine/res/shaders/materials/" + properties.getName();

        this.vertexShaderPath = this.path + "/vertexShader.glsl";
        this.geometryShaderPath = this.path + "/geometryShader.glsl";
        this.fragmentShaderPath = this.path + "/fragmentShader.glsl";

        this.vertexShaderUniforms = new ArrayList<>();
        this.vertexShaderInputs = new ArrayList<>();
        this.vertexShaderOutputs = new HashMap<>();

        this.fragmentShaderUniforms = new ArrayList<>();
        this.fragmentShaderOutputs = new HashMap<>();
        this.fragmentShaderLayoutOutputs = new ArrayList<>();
        this.fragmentShaderAllOutputs = new HashMap<>();

        this.attachedShaders = new Stack<>();

        this.compiled = false;

        initFields();

    }

    protected void initFields() {

        vertexShaderInputs.add(new Vector3Parameter("position"));
        vertexShaderInputs.add(new Vector2Parameter("textureCoords"));
        vertexShaderInputs.add(new Vector3Parameter("normal"));

        
        MaterialParameter TexCoords = new Vector2Parameter("TexCoords");
        MaterialFunction TexCoordsFunction = new MaterialFunction("calculate_TexCoords");
        List<String> TexCoordsExpressions = new ArrayList<>();
        TexCoordsExpressions.add("return textureCoords;");
        TexCoordsFunction.buildExpression(null, TexCoords, TexCoordsExpressions);

        MaterialParameter FragPos = new Vector3Parameter("FragPos");
        MaterialFunction FragPosFunction = new MaterialFunction("calculate_FragPos");
        List<String> FragPosExpressions = new ArrayList<>();
        FragPosExpressions.add("vec4 worldPosition = transformationMatrix * vec4(position, 1.0);");
        FragPosExpressions.add("gl_Position = projectionMatrix * viewMatrix * worldPosition;");
        FragPosExpressions.add("return worldPosition.xyz;");
        FragPosFunction.buildExpression(null, FragPos, FragPosExpressions);

        MaterialParameter Normal = new Vector3Parameter("Normal");
        MaterialFunction NormalFunction = new MaterialFunction("calculate_Normal");
        List<String> NormalExpressions = new ArrayList<>();
        NormalExpressions.add("return ((transformationMatrix * vec4(normal, 0.0)).xyz + 1) / 2;");
        NormalFunction.buildExpression(null, Normal, NormalExpressions);

        MaterialParameter ToCamera = new Vector3Parameter("ToCamera");
        MaterialFunction ToCameraFunction = new MaterialFunction("calculate_ToCamera");
        List<String> ToCameraExpressions = new ArrayList<>();
        ToCameraExpressions.add("vec4 worldPosition = transformationMatrix * vec4(position, 1.0);");
        //ToCameraExpressions.add("return (inverse(viewMatrix) * vec4(0,0,0,1)).xyz - worldPosition.xyz;");
        //ToCameraExpressions.add("return (viewMatrix * vec4(0,0,0,1)).xyz - worldPosition.xyz;");
        ToCameraExpressions.add("return normalize(worldPosition.xyz - (viewMatrix * vec4(0,0,0,1)).xyz);");
        ToCameraFunction.buildExpression(null, ToCamera, ToCameraExpressions);
        
        addVertexOutput(new Vector2Parameter("TexCoords"), TexCoordsFunction);
        addVertexOutput(new Vector3Parameter("FragPos"), FragPosFunction);
        addVertexOutput(new Vector3Parameter("Normal"), NormalFunction);
        addVertexOutput(new Vector3Parameter("ToCamera"), ToCameraFunction);

        addVertexParameter(new Matrix4Parameter("transformationMatrix"));
        addVertexParameter(new Matrix4Parameter("projectionMatrix"));
        addVertexParameter(new Matrix4Parameter("viewMatrix"));

        
        MaterialParameter gPosition = new Vector4Parameter("gPosition");
        MaterialFunction gPositionFunction = new MaterialFunction("calculate_gPosition");
        List<String> gPositionExpressions = new ArrayList<>();
        gPositionExpressions.add("return vec4(FragPos, gl_FragDepth);");
        gPositionFunction.buildExpression(null, gPosition, gPositionExpressions);

        MaterialParameter gNormal = new Vector3Parameter("gNormal");
        MaterialFunction gNormalFunction = new MaterialFunction("calculate_gNormal");
        List<String> gNormalExpressions = new ArrayList<>();
        gNormalExpressions.add("return Normal;");
        gNormalFunction.buildExpression(null, gNormal, gNormalExpressions);

        MaterialParameter gColor = new Vector4Parameter("gColor");
        MaterialFunction gColorFunction = new MaterialFunction("calculate_gColor");
        List<String> gColorExpressions = new ArrayList<>();
        gColorExpressions.add("return texture(textureSampler0, TexCoords);");
        gColorFunction.buildExpression(null, gColor, gColorExpressions);

        MaterialParameter gRSMAo = new Vector4Parameter("gRSMAo");
        MaterialFunction gRSMAoFunction = new MaterialFunction("calculate_gRSMAo");
        List<String> gRSMAoExpressions = new ArrayList<>();
        gRSMAoExpressions.add("return vec4(0,0,0,0);");
        gRSMAoFunction.buildExpression(null, gRSMAo, gRSMAoExpressions);

        MaterialParameter gCamera = new Vector3Parameter("gCamera");
        MaterialFunction gCameraFunction = new MaterialFunction("calculate_gCamera");
        List<String> gCameraExpressions = new ArrayList<>();
        gCameraExpressions.add("return (ToCamera + 1) / 2;");
        gCameraFunction.buildExpression(null, gCamera, gCameraExpressions);

        addFragmentOutput(gColor, true, gColorFunction);
        addFragmentOutput(gPosition, true, gPositionFunction);
        addFragmentOutput(gNormal, true, gNormalFunction);
        addFragmentOutput(gRSMAo, true, gRSMAoFunction);
        addFragmentOutput(gCamera, true, gCameraFunction);


        MaterialParameter color = new Vector4Parameter("color");
        MaterialFunction colorFunction = new MaterialFunction("calculate_color");
        List<String> colorExpressions = new ArrayList<>();
        colorExpressions.add("return texture(textureSampler0, TexCoords);");
        colorFunction.buildExpression(null, color, colorExpressions);

        addFragmentOutput(color, false, colorFunction);

        //TODO: simplify field initialization

    }

    //region Manage material components

    public void addTexture(TextureObject textureObject) {
        //TODO: add texture wrapping
        this.compiled = false;
        if (FILLED_TEXTURE_SLOTS < MAX_TEXTURES) textureSamples[FILLED_TEXTURE_SLOTS++] = textureObject;
    }

    public void clearTextures() {
        this.compiled = false;
        this.FILLED_TEXTURE_SLOTS = 0;
        this.textureSamples = new TextureObject[MAX_TEXTURES];
    }

    protected void addVertexOutput(MaterialParameter materialParam, MaterialFunction materialFunction) {
        for (MaterialParameter materialParam1 : vertexShaderOutputs.keySet()) {
            if (materialParam1.getIdentifier().equals(materialParam.getIdentifier())) {
                throw new IllegalArgumentException("Material vertex output already exists");
            }
        }
        vertexShaderOutputs.put(materialParam, materialFunction);
        this.compiled = false;
    }

    protected void addFragmentOutput(MaterialParameter materialParam, boolean isLayout, MaterialFunction materialFunction) {
        for (MaterialParameter materialParam1 : fragmentShaderAllOutputs.keySet()) {
            if (materialParam1.getIdentifier().equals(materialParam.getIdentifier())) {
                throw new IllegalArgumentException("Material fragment output already exists");
            }
        }
        if (isLayout) {
            fragmentShaderLayoutOutputs.add(new Pair<>(materialParam, materialFunction));
        } else {
            fragmentShaderOutputs.put(materialParam, materialFunction);
        }
        fragmentShaderAllOutputs.put(materialParam, materialFunction);
        this.compiled = false;
    }

    protected void addVertexParameter(MaterialParameter materialParam) {
        for (MaterialParameter materialParam1 : vertexShaderUniforms) {
            if (materialParam1.getIdentifier().equals(materialParam.getIdentifier())) {
                throw new IllegalArgumentException("Material uniform already exists");
            }
        }
        for (MaterialParameter materialParam1 : fragmentShaderUniforms) {
            if (materialParam1.getIdentifier().equals(materialParam.getIdentifier())) {
                throw new IllegalArgumentException("Material uniform already exists");
            }
        }
        vertexShaderUniforms.add(materialParam);
        this.compiled = false;
    }

    protected void addFragmentParameter(MaterialParameter materialParam) {
        for (MaterialParameter materialParam1 : vertexShaderUniforms) {
            if (materialParam1.getIdentifier().equals(materialParam.getIdentifier())) {
                throw new IllegalArgumentException("Material uniform already exists");
            }
        }
        for (MaterialParameter materialParam1 : fragmentShaderUniforms) {
            if (materialParam1.getIdentifier().equals(materialParam.getIdentifier())) {
                throw new IllegalArgumentException("Material uniform already exists");
            }
        }
        this.fragmentShaderUniforms.add(materialParam);
        this.compiled = false;
    }

    protected MaterialParameter getParameter(String identifier) {
        for (MaterialParameter materialParam : vertexShaderUniforms) {
            if (materialParam.getIdentifier().equals(identifier)) {
                return materialParam;
            }
        }
        for (MaterialParameter materialParam : fragmentShaderUniforms) {
            if (materialParam.getIdentifier().equals(identifier)) {
                return materialParam;
            }
        }
        return null;
    }

    //endregion


    public void compile() throws MaterialCompilationException {
        
        //create sub folders in path based off of shading model
        //write all necessary shaders into appropriate sub folders with generated names
        this.compiled = false;
        detachAllShaders();
        deleteAllShaders();

        StringBuilder vertexShader = new StringBuilder();
        StringBuilder geometryShader = new StringBuilder();
        StringBuilder fragmentShader = new StringBuilder();

        Scanner vertexScan = new Scanner(vertexShaderTemplate);
        Scanner geometryScan = new Scanner(geometryShaderTemplate);
        Scanner fragScan = new Scanner(fragmentShaderTemplate);

        List<String> passedValues = new ArrayList<>();

        //region vertex compile
        while (vertexScan.hasNextLine()) {
            String line = vertexScan.nextLine();
            if (line.equals("@V@in")) {
                for (MaterialParameter materialParam : vertexShaderInputs) {
                    if (materialParam == null) continue;

                    vertexShader.append("in ");
                    vertexShader.append(getType(materialParam));
                    vertexShader.append(" ");
                    vertexShader.append(materialParam.getIdentifier());
                    vertexShader.append(";\n");
                }
            } else if (line.equals("@V@out")) {
                for (MaterialParameter materialParam : vertexShaderOutputs.keySet()) {
                    if (materialParam == null) continue;
                    String type = getType(materialParam);
                    vertexShader.append("out ");
                    vertexShader.append(type);
                    vertexShader.append(" ");
                    vertexShader.append(materialParam.getIdentifier());
                    vertexShader.append(";\n");
                    passedValues.add("out " + type + " " + materialParam.getIdentifier() + ";\n");
                }
            } else if (line.equals("@V@uniform")) {
                for (MaterialParameter materialParam : vertexShaderUniforms) {
                    if (materialParam == null) continue;

                    vertexShader.append("uniform ");
                    vertexShader.append(getType(materialParam));
                    vertexShader.append(" ");
                    vertexShader.append(materialParam.getIdentifier());
                    vertexShader.append(";\n");
                }
            } else if (line.equals("@V@outFunction")) {
                for (MaterialParameter materialParam : vertexShaderOutputs.keySet()) {
                    if (materialParam == null) continue;

                    vertexShader.append("    ");
                    vertexShader.append(materialParam.getIdentifier());
                    vertexShader.append(" = calculate_");
                    vertexShader.append(materialParam.getIdentifier());
                    vertexShader.append("(");
                    if (vertexShaderOutputs.get(materialParam) != null) {
                        List<MaterialParameter> params = vertexShaderOutputs.get(materialParam).getParameters();
                        int i = 0;
                        if (params != null) {
                            for (MaterialParameter materialParam1 : params) {
                                if (materialParam1 == null) continue;

                                vertexShader.append(materialParam1.getIdentifier());
                                if (i++ != params.size() - 1) vertexShader.append(", ");
                            }
                        }
                    }
                    vertexShader.append(");\n");
                }

            } else if (line.equals("@V@function")) {
                for (MaterialParameter materialParam : vertexShaderOutputs.keySet()) {
                    if (materialParam == null) continue;

                    MaterialFunction materialFunction = vertexShaderOutputs.get(materialParam);
                    vertexShader.append(materialFunction.getExpression());
                    vertexShader.append("\n");
                }
                //TODO: add material functions

            } else {
                vertexShader.append(line);
                vertexShader.append("\n");
            }

        }
        //endregion

        //region fragment compile
        while (fragScan.hasNextLine()) {
            String line = fragScan.nextLine();
            if (line.equals("@F@layout")) {
                int i = 0;
                for (Pair<MaterialParameter, MaterialFunction> pair : fragmentShaderLayoutOutputs) {
                    MaterialParameter materialParam = pair.getKey();
                    if (materialParam == null) continue;

                    fragmentShader.append("layout (location = ");
                    fragmentShader.append(i);
                    fragmentShader.append(") out ");
                    fragmentShader.append(getType(materialParam));
                    fragmentShader.append(" ");
                    fragmentShader.append(materialParam.getIdentifier());
                    fragmentShader.append(";\n");
                    i++;
                }
            } else if (line.equals("@F@out")) {
                for (MaterialParameter materialParam : fragmentShaderOutputs.keySet()) {
                    if (materialParam == null) continue;

                    fragmentShader.append("out ");
                    fragmentShader.append(getType(materialParam));
                    fragmentShader.append(" ");
                    fragmentShader.append(materialParam.getIdentifier());
                    fragmentShader.append(";\n");
                }
                //add other uniforms (i.e. parameters)
            } else if (line.equals("@F@in")) {
                for (String in : passedValues) {
                    fragmentShader.append(in.replaceFirst("out", "in"));
                }
            } else if (line.equals("@F@uniform")) {
                for (int i = 0; i < FILLED_TEXTURE_SLOTS; i++) {
                    fragmentShader.append("uniform sampler2D ");
                    fragmentShader.append(textureSampleTemplate);
                    fragmentShader.append(i);
                    fragmentShader.append(";\n");
                }
                for (MaterialParameter materialParam : fragmentShaderUniforms) {
                    if (materialParam == null) continue;

                    fragmentShader.append("uniform ");
                    fragmentShader.append(getType(materialParam));
                    fragmentShader.append(" ");
                    fragmentShader.append(materialParam.getIdentifier());
                    fragmentShader.append(";\n");
                }
                //add other uniforms (i.e. parameters)
            } else if (line.equals("@F@outFunction")) {
                for (MaterialParameter materialParam : fragmentShaderAllOutputs.keySet()) {
                    if (materialParam == null) continue;

                    fragmentShader.append("    ");
                    fragmentShader.append(materialParam.getIdentifier());
                    fragmentShader.append(" = calculate_");
                    fragmentShader.append(materialParam.getIdentifier());
                    fragmentShader.append("(");
                    if (fragmentShaderAllOutputs.get(materialParam) != null) {
                        List<MaterialParameter> params = fragmentShaderAllOutputs.get(materialParam).getParameters();
                        int i = 0;
                        if (params != null) {
                            for (MaterialParameter materialParam1 : params) {
                                if (materialParam1 == null) continue;
                                fragmentShader.append(materialParam1.getIdentifier());
                                if (i++ != params.size() - 1) fragmentShader.append(", ");
                            }
                        }
                    }
                    fragmentShader.append(");\n");
                }

            } else if (line.equals("@F@function")) {
                for (MaterialParameter materialParam : fragmentShaderAllOutputs.keySet()) {
                    MaterialFunction materialFunction = fragmentShaderAllOutputs.get(materialParam);
                    fragmentShader.append(materialFunction.getExpression());
                    fragmentShader.append("\n");
                }
            } else {
                fragmentShader.append(line);
                fragmentShader.append("\n");
            }

        }
        //endregion



        writeShadersToMemory(vertexShader.toString(), geometryShader.toString(), fragmentShader.toString());

        attachShader(loadShader(vertexShader.toString(), GL20.GL_VERTEX_SHADER));
        //attachShader(loadShader(geometryShader.toString(), GL20.GL_VERTEX_SHADER));
        attachShader(loadShader(fragmentShader.toString(), GL20.GL_FRAGMENT_SHADER));

        bindAttributes();

        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);

        getAllUniforms();

        this.compiled = true;

    }

    public void writeShadersToMemory(String vertex, String geometry, String fragment) {
        File matDir = new File(path);
        if (!matDir.mkdir() && !matDir.exists()) {
            throw new RuntimeException("Error while compiling material to path " + path);
        }

        File vertexFile = new File(this.vertexShaderPath);
        File geometryFile = new File(this.geometryShaderPath);
        File fragmentFile = new File(this.fragmentShaderPath);


        //write shaders into folders
        try {
            PrintStream vertexStream = new PrintStream(vertexFile);
            PrintStream geometryStream = new PrintStream(geometryFile);
            PrintStream fragmentStream = new PrintStream(fragmentFile);

            vertexStream.print(vertex);
            geometryStream.print(geometry);
            fragmentStream.print(fragment);

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error while compiling material to path " + path);
        }

    }


    public void setFloat(String name, float value) {
        int id = GL20.glGetUniformLocation(programID, name);
        MaterialParameter parameter = uniforms.get(id);
        if (parameter == null || !(parameter instanceof ScalarParameter)) {
            throw new IllegalArgumentException();
        } else {
            ((ScalarParameter) uniforms.get(id)).setValue(value);
        }
    }

    public void setInt(String name, int value) {
        int id = GL20.glGetUniformLocation(programID, name);
        MaterialParameter parameter = uniforms.get(id);
        if (parameter == null || !(parameter instanceof ScalarParameter)) {
            throw new IllegalArgumentException();
        } else {
            ((ScalarParameter) uniforms.get(id)).setValue((float)value);
        }
    }

    public void setVector2(String name, Vector2 value) {
        int id = GL20.glGetUniformLocation(programID, name);
        MaterialParameter parameter = uniforms.get(id);
        if (parameter == null || !(parameter instanceof Vector2Parameter)) {
            throw new IllegalArgumentException();
        } else {
            ((Vector2Parameter) uniforms.get(id)).setValue(value);
        }
    }

    public void setVector3(String name, Vector3 value) {
        int id = GL20.glGetUniformLocation(programID, name);
        MaterialParameter parameter = uniforms.get(id);
        if (parameter == null || !(parameter instanceof Vector3Parameter)) {
            throw new IllegalArgumentException();
        } else {
            ((Vector3Parameter) uniforms.get(id)).setValue(value);
        }
    }

    public void setVector4(String name, Vector4 value) {
        int id = GL20.glGetUniformLocation(programID, name);
        MaterialParameter parameter = uniforms.get(id);
        if (parameter == null || !(parameter instanceof Vector4Parameter)) {
            throw new IllegalArgumentException();
        } else {
            ((Vector4Parameter) uniforms.get(id)).setValue(value);
        }
    }

    public void setBoolean(String name, boolean value) {
        int id = GL20.glGetUniformLocation(programID, name);
        MaterialParameter parameter = uniforms.get(id);
        if (parameter == null || !(parameter instanceof BoolParameter)) {
            throw new IllegalArgumentException();
        } else {
            ((BoolParameter) uniforms.get(id)).setValue(value);
        }
    }

    public void setMatrix(String name, Matrix4f value) {
        int id = GL20.glGetUniformLocation(programID, name);
        MaterialParameter parameter = uniforms.get(id);
        if (parameter == null || !(parameter instanceof Matrix4Parameter)) {
            throw new IllegalArgumentException();
        } else {
            ((Matrix4Parameter) uniforms.get(id)).setValue(value);
        }
    }

    public void loadUniforms() {
        for (Integer i : uniforms.keySet()) {
            MaterialParameter parameter = uniforms.get(i);
            attachParameter(i, parameter);
        }
    }

    private void attachParameter(int id, MaterialParameter parameter) {
        if (parameter instanceof ScalarParameter) {
            GL20.glUniform1f(id, ((ScalarParameter)parameter).getValue());
        } else if (parameter instanceof Vector2Parameter) {
            Vector2 value = ((Vector2Parameter)parameter).getValue();
            GL20.glUniform2f(id, value.xf, value.yf);
        } else if (parameter instanceof Vector3Parameter) {
            Vector3 value = ((Vector3Parameter)parameter).getValue();
            GL20.glUniform3f(id, value.xf, value.yf, value.zf);
        } else if (parameter instanceof Vector4Parameter) {
            Vector4 value = ((Vector4Parameter)parameter).getValue();
            GL20.glUniform4f(id, value.xf, value.yf, value.zf, value.wf);
        } else if (parameter instanceof BoolParameter) {
            Boolean value = ((BoolParameter)parameter).getValue();
            GL20.glUniform1f(id, value ? 1 : 0);
        } else if (parameter instanceof Matrix4Parameter) {
            Matrix4f value = ((Matrix4Parameter)parameter).getValue();
            matrixBuffer.clear();
            value.toBuffer(matrixBuffer);
            GL20.glUniformMatrix4fv(id, false, matrixBuffer);
        }
    }

    private void bindAttributes() {
        int size = vertexShaderInputs.size();
        for (int i = 0; i < size; i++) {
            GL20.glBindAttribLocation(programID, i, vertexShaderInputs.get(i).getIdentifier());
        }

    }

    private void getAllUniforms() {
        for (MaterialParameter parameter : vertexShaderUniforms) {
            uniforms.put(GL20.glGetUniformLocation(programID, parameter.getIdentifier()), parameter);
        }
        for (MaterialParameter parameter : fragmentShaderUniforms) {
            uniforms.put(GL20.glGetUniformLocation(programID, parameter.getIdentifier()), parameter);
        }
    }


    private int loadShader(String source, int type) throws MaterialCompilationException {
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, source);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            Debugger.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            throw new MaterialCompilationException();
        }
        this.shaders.put(shaderID, source);
        return shaderID;
    }

    private void attachShader(int id) {
        if (GL20.glIsShader(id)) {
            GL20.glAttachShader(this.programID, id);
            this.attachedShaders.push(id);
        }
    }

    private void detachAllShaders() {

        while (this.attachedShaders.size() > 0) {
            GL20.glDetachShader(this.programID, this.attachedShaders.pop());
        }

    }

    private void deleteAllShaders() {
        for (int id : this.shaders.keySet()) {
            if (GL20.glIsShader(id)) GL20.glDeleteShader(id);
            else {}
        }
        this.shaders = new HashMap<>();
    }


    public void start() throws MaterialCompilationException {
        if (compiled) GL20.glUseProgram(programID);
        else throw new MaterialCompilationException("Material not compiled before shader start");
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        detachAllShaders();
        deleteAllShaders();
        GL20.glDeleteProgram(programID);
    }

    /**
     * Returns the glsl type keyword for the parameter
     * @param materialParam material parameter
     * @return type keyword
     */
    public static String getType(MaterialParameter materialParam) {
        String type = "";
        if (materialParam instanceof ScalarParameter) {
            type = "float";
        } else if (materialParam instanceof Vector2Parameter) {
            type = "vec2";
        } else if (materialParam instanceof Vector3Parameter) {
            type = "vec3";
        } else if (materialParam instanceof Vector4Parameter) {
            type = "vec4";
        } else if (materialParam instanceof Matrix4Parameter) {
            type = "mat4";
        } else if (materialParam == null) {
            type = "void";
        }
        return type;
    }


    public boolean isTwoSided() {
        return properties.isTwoSided();
    }

    public void setTwoSided(boolean twoSided) {
        this.properties.setTwoSided(twoSided);
    }


    public List<TextureObject> getTextureSamples() {
        List<TextureObject> ret = new ArrayList<>();
        for (int i = 0; i < FILLED_TEXTURE_SLOTS; i++) {
            ret.add(textureSamples[i]);
        }
        return ret;
    }

    public TextureObject getTextureSample(int index) {
        if (index >= FILLED_TEXTURE_SLOTS || index >= MAX_TEXTURES || index < 0) {
            throw new IllegalArgumentException("Texture sample does not exist");
        }
        return textureSamples[index];
    }


}