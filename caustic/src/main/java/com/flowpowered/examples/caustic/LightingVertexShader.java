package com.flowpowered.examples.caustic;

import com.flowpowered.math.matrix.Matrix4f;
import com.flowpowered.math.vector.Vector3f;
import com.flowpowered.math.vector.Vector4f;

import org.spout.renderer.api.data.VertexAttribute.DataType;
import org.spout.renderer.api.gl.Shader.ShaderType;
import org.spout.renderer.software.DataFormat;
import org.spout.renderer.software.InBuffer;
import org.spout.renderer.software.OutBuffer;
import org.spout.renderer.software.ShaderImplementation;
import org.spout.renderer.software.Uniform;

/**
 *
 */
public class LightingVertexShader extends ShaderImplementation {
    private static final DataFormat[] OUTPUT = {
            new DataFormat(DataType.FLOAT, 4),
            new DataFormat(DataType.FLOAT, 3),
            new DataFormat(DataType.FLOAT, 3),
            new DataFormat(DataType.FLOAT, 3)
    };
    @Uniform
    private Vector3f lightDirection = Vector3f.UP.negate();
    @Uniform
    private Matrix4f modelMatrix = Matrix4f.IDENTITY;
    @Uniform
    private Matrix4f normalMatrix = Matrix4f.IDENTITY;
    @Uniform
    private Matrix4f viewMatrix = Matrix4f.IDENTITY;
    @Uniform
    private Matrix4f projectionMatrix = Matrix4f.IDENTITY;

    public LightingVertexShader() {
        super(OUTPUT);
    }

    @Override
    public void main(InBuffer in, OutBuffer out) {
        final Vector3f position = in.readVector3f();
        final Vector3f normal = in.readVector3f();
        final Vector4f positionView = viewMatrix.transform(modelMatrix.transform(position.toVector4(1)));
        out.writeVector4f(projectionMatrix.transform(positionView));
        out.writeVector3f(positionView.toVector3());
        out.writeVector3f(normalMatrix.transform(normal.toVector4(0)).toVector3());
        out.writeVector3f(viewMatrix.transform(lightDirection.toVector4(0)).toVector3());
    }

    @Override
    public ShaderType getType() {
        return ShaderType.VERTEX;
    }
}
