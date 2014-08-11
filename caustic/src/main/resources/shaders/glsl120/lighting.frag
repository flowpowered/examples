// $shader_type: fragment

#version 120

varying vec3 positionView;
varying vec3 normalView;
varying vec3 lightDirectionView;

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
    gl_FragColor = vec4(vec3(1, 1, 1) * (ambientTerm + diffuseTerm + specularTerm), 1);
}
