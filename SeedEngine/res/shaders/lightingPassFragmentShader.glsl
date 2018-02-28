#version 430 core

#define M_PI 3.1415926535897932384626433832795

out vec4 FragColor;

in vec2 TexCoords;

uniform sampler2D gColor;
uniform sampler2D gPosition;
uniform sampler2D gNormal;
uniform sampler2D gRSMAo;
uniform sampler2D gCamera;

const int MAX_LIGHTS = 64;

uniform int PointLightCount = 0;
uniform int DirectionalLightCount = 0;
uniform int SpotLightCount = 0;

struct PointLight {
    vec3 Position;
    vec3 Color;
    float Intensity;
    float Radius;
    float Attenuation;
};

struct DirectionalLight {
    vec3 Direction;
    vec3 Color;
    float Intensity;
};

struct SpotLight {
    vec3 Position;
    vec3 Direction;
    vec3 Color;
    float Intensity;
    float InnerAngle;
    float OuterAngle;
    float Length;
};

uniform PointLight pointLights[MAX_LIGHTS];
uniform DirectionalLight directionalLights[MAX_LIGHTS];
uniform SpotLight spotLights[MAX_LIGHTS];

const float ambientConst = 0.1;

float D_GGX(float Roughness, float NoH);
vec3 F_Schlick(vec3 F0, float VoH);
float Frensel(float IoR, float k, float VoH);
float G1_Schlick(float NoX, float Roughness);
float G_Schlick(float Roughness, float NoV, float NoL);

void main(void) {

    //----------PBR-------------

    vec3 FragPos = texture(gPosition, TexCoords).rgb;
    //vec3 Diffuse = texture(gColor, TexCoords).rgb;
    vec3 Diffuse = pow(texture(gColor, TexCoords).rgb, vec3(2.2));
    vec3 N = normalize(texture(gNormal, TexCoords).rgb);
    N = (N - 0.5) * 2;
    vec3 V = normalize(texture(gCamera, TexCoords).rgb);
    V = (V - 0.5) * 2;
    float NoV = dot(N, V);
    float NoV2 = clamp(NoV, 0, 1);

    vec4 RSMAo = texture(gRSMAo, TexCoords);
    float Roughness = 0.8;//RSMAo.r;
    float Specular = 1;//RSMAo.g;
    float Metallic = 0; //RSMAo.b;
    float AmbientOcclusion = RSMAo.a;

    vec3 realAlbedo = Diffuse - Diffuse * Metallic;
    vec3 IoR = Specular + vec3(1,1,1);
    vec3 F0 = abs((1 - IoR) / (1 + IoR));
    F0 = F0 * F0;
    F0 = mix(F0, Diffuse, Metallic);

    vec3 albedoDiffuse = realAlbedo / M_PI;

    vec3 realSpecular = vec3(0,0,0);
    vec3 totalLightColor = vec3(0);

    vec3 specularResult = vec3(0,0,0);

    vec3 f_r = vec3(0,0,0);

    for (int i = 0; i < PointLightCount; i++) {
        vec3 L = normalize(pointLights[i].Position - FragPos);
        vec3 H = normalize(V + L);
        float NoL = dot(N, L);
        float NoH = dot(N, H);
        float NoH2 = clamp(NoH, 0, 1);
        float VoH = dot(V, H);
        float VoH2 = clamp(VoH, 0, 1);

        f_r += (1 - Metallic) * albedoDiffuse +
                (Metallic) * pointLights[i].Color * D_GGX(Roughness, NoH) * F_Schlick(F0, VoH) *
                G_Schlick(Roughness, NoH2, NoL) / (4 * NoL * NoV);
                //G_Schlick(Roughness, NoV, NoL) / (4 * NoL * NoV);
        float k = (Roughness + 1) * (Roughness + 1) / 8;
        //f_r = vec3(G1_Schlick(NoH2, k));
        //f_r = vec3(G1_Schlick(NoV2, k));
        //f_r *= vec3(G1_Schlick(NoV2, k));
        //f_r = vec3(G_Schlick(Roughness, NoV2, NoH2));
        //f_r = vec3(D_GGX(Roughness, NoH));
        //f_r = -1 * vec3(pointLights[i].Position - FragPos);
        //f_r = vec3(F_Schlick(F0, VoH));
        //f_r = vec3(NoH);
        //f_r = vec3(NoV);
        //f_r = vec3(NoL);
        //f_r = vec3(VoH);
        //f_r = V;
        //f_r = N;
        //f_r = H;
        //f_r = L;
        totalLightColor += pointLights[i].Color * max(ambientConst, NoL) * pointLights[i].Intensity;
    }
    for (int i = 0; i < DirectionalLightCount; i++) {
        vec3 L = directionalLights[i].Direction;
        vec3 H = normalize(V + L);
        float NoL = dot(N, L);
        float NoH = dot(N, H);
        float VoH = dot(V, H);
        //realSpecular += Specular * D_GGX(Roughness, NoH) * F_Schlick(F0, VoH) *
        //        G_Schlick(Roughness, NoV, NoL) / (4 * NoL * NoV);
        //totalLightColor += directionalLights[i].Color * NoL * directionalLights[i].Intensity;
    }

    realSpecular /= PointLightCount + DirectionalLightCount + SpotLightCount; //fix to act with proper contribution
    totalLightColor /= PointLightCount + DirectionalLightCount + SpotLightCount;
    FragColor = vec4(totalLightColor * (albedoDiffuse * (1 - realSpecular) + realSpecular), 1);
    //FragColor = vec4(f_r / PointLightCount, 1);
    //FragColor = vec4(N, 1);
    //FragColor = vec4(smoothstep(vec3(-1), vec3(1), N.xxx), 1);
    //FragColor = vec4(TexCoords.x, TexCoords.y, 0, 1);
    //FragColor = vec4(smoothstep(vec3(-1), vec3(0), f_r), 1);
    //FragColor = vec4(Diffuse / M_PI + SpecTerm, 1);

}

//Distribution Function: calculates specular
float D_GGX(float Roughness, float NoH) {
   float a = max(0.001, Roughness * Roughness);
   float a2 = a * a;
   //float d = ( NoH * a2 - NoH ) * NoH + 1;
   float d = (NoH) * (NoH) * (a2 - 1) + 1;
   return a2 / (M_PI*d*d );
}
//Frensel Function: light interaction
vec3 F_Schlick(vec3 F0, float VoH) {
    float F1 = (clamp(1 - VoH, 0.0, 1.0));
    return F0 + (1 - F0) * (F1 * F1 * F1 * F1 * F1);
    //return F0 + (1 - F0) * pow(2, VoH*(-5.55473*VoH - 6.98316));
}

//Geometry Function: helper
float G1_Schlick(float NoX, float k) {
    return NoX / (NoX * (1 - k) + k);
}
//Geometry Function: calculates shadows
float G_Schlick(float Roughness, float NoV, float NoL) {
    float k = (Roughness + 1) * (Roughness + 1) / 8;
    return G1_Schlick(NoL, k) * G1_Schlick(NoV, k);
}