#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 TexCoords;
out vec3 ToCamera;
out vec3 FragPos;
out vec3 Normal;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

vec2 calculate_TexCoords() {
    return textureCoords;
}

vec3 calculate_ToCamera() {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    return normalize(worldPosition.xyz - (viewMatrix * vec4(0,0,0,1)).xyz);
}

vec3 calculate_FragPos() {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    return worldPosition.xyz;
}

vec3 calculate_Normal() {
    return ((transformationMatrix * vec4(normal, 0.0)).xyz + 1) / 2;
}

void main(void) {

    TexCoords = calculate_TexCoords();
    ToCamera = calculate_ToCamera();
    FragPos = calculate_FragPos();
    Normal = calculate_Normal();

}
