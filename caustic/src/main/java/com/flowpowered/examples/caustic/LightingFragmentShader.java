/*
 * This file is part of Flow Caustic Example, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Spout LLC <https://spout.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.flowpowered.examples.caustic;

import com.flowpowered.caustic.api.gl.Shader.ShaderType;
import com.flowpowered.caustic.software.InBuffer;
import com.flowpowered.caustic.software.OutBuffer;
import com.flowpowered.caustic.software.ShaderImplementation;
import com.flowpowered.math.vector.Vector3f;

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
