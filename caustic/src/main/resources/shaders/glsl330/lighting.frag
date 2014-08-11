// $shader_type: fragment

#version 330

in vec3 positionView;
in vec3 normalView;
in vec3 lightDirectionView;

out vec4 outputColor;

void main() {
    float ambientTerm = 0.2;
    vec3 nNormalView = normalize(normalView);
    vec3 nLightDirectionView = -normalize(lightDirectionView);
    float diffuseTerm = max(0, dot(nNormalView, nLightDirectionView));
    float specularTerm;
    if (diffuseTerm > 0) {
        specularTerm = pow(max(0, dot(reflect(nLightDirectionView, nNormalView), normalize(positionView))), 50);
    } else {
        specularTerm = 0;
    }
    outputColor = vec4(vec3(1, 1, 1) * (ambientTerm + diffuseTerm + specularTerm), 1);
}
