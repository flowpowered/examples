package com.flowpowered.examples.caustic;

import com.flowpowered.math.vector.Vector3f;

import org.spout.renderer.api.gl.Shader.ShaderType;
import org.spout.renderer.software.InBuffer;
import org.spout.renderer.software.OutBuffer;
import org.spout.renderer.software.ShaderImplementation;

/**
 *
 */
public class LightingFragmentShader extends ShaderImplementation {
    @Override
    public void main(InBuffer in, OutBuffer out) {
        in.skip();
        final Vector3f positionView = in.readVector3f();
        final Vector3f normalView = in.readVector3f().normalize();
        final Vector3f lightDirectionView = in.readVector3f().normalize().negate();
        final float ambientTerm = 0.2f;
        final float diffuseTerm = Math.max(0, normalView.dot(lightDirectionView));
        final float specularTerm;
        if (diffuseTerm > 0) {
            specularTerm = (float) Math.pow(Math.max(0, reflect(lightDirectionView, normalView).dot(positionView.normalize())), 50);
        } else {
            specularTerm = 0;
        }
        out.writeVector4f(Vector3f.ONE.mul(ambientTerm + diffuseTerm + specularTerm).toVector4(1));
    }

    private Vector3f reflect(Vector3f i, Vector3f n) {
        return i.sub(n.mul(2 * n.dot(i)));
    }

    @Override
    public ShaderType getType() {
        return ShaderType.FRAGMENT;
    }
}
