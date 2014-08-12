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

import java.util.Arrays;
import java.util.Scanner;

import org.spout.renderer.api.Camera;
import org.spout.renderer.api.GLImplementation;
import org.spout.renderer.api.GLVersioned.GLVersion;
import org.spout.renderer.api.Material;
import org.spout.renderer.api.Pipeline;
import org.spout.renderer.api.Pipeline.PipelineBuilder;
import org.spout.renderer.api.data.ShaderSource;
import org.spout.renderer.api.data.Uniform.Vector3Uniform;
import org.spout.renderer.api.gl.Context;
import org.spout.renderer.api.gl.Context.Capability;
import org.spout.renderer.api.gl.Program;
import org.spout.renderer.api.gl.Shader;
import org.spout.renderer.api.gl.Shader.ShaderType;
import org.spout.renderer.api.gl.VertexArray;
import org.spout.renderer.api.model.Model;
import org.spout.renderer.api.util.CausticUtil;
import org.spout.renderer.api.util.MeshGenerator;
import org.spout.renderer.lwjgl.LWJGLUtil;
import org.spout.renderer.software.SoftwareUtil;

import com.flowpowered.math.imaginary.Quaternionf;
import com.flowpowered.math.vector.Vector3f;

public class CausticExample {
    public static void main(String[] args) throws Exception {
        LWJGLUtil.deployNatives(null);
        // Settings
        final GLImplementation implementation = getImpl(args);
        CausticUtil.setDebugEnabled(true);
        final float fps = 60;
        final int windowWidth = 320, windowHeight = 240;
        final String windowTitle = "CausticExample";
        final float fieldOfView = 60, nearPlane = 0.1f, farPlane = 100;
        final Vector3f parentModelPosition = new Vector3f(0, 0, -5);
        final Vector3f childModelPosition = Vector3f.ONE;
        final Vector3f childModelScale = Vector3f.ONE.div(2);
        final Quaternionf parentRotationIncrement = Quaternionf.fromAngleDegAxis(1, Vector3f.ONE);
        // Context
        final Context context = GLImplementation.get(implementation);
        context.setWindowSize(windowWidth, windowHeight);
        context.setWindowTitle(windowTitle);
        context.create();
        context.enableCapability(Capability.CULL_FACE);
        context.enableCapability(Capability.DEPTH_TEST);
        context.enableCapability(Capability.DEPTH_CLAMP);
        context.getUniforms().add(new Vector3Uniform("lightDirection", Vector3f.UP.negate()));
        // Vertex shader
        final GLVersion glVersion = context.getGLVersion();
        final Shader vertex = context.newShader();
        vertex.create();
        vertex.setSource(getShaderSource(glVersion, ShaderType.VERTEX));
        vertex.compile();
        // Fragment shader
        final Shader fragment = context.newShader();
        fragment.create();
        fragment.setSource(getShaderSource(glVersion, ShaderType.FRAGMENT));
        fragment.compile();
        // Program
        final Program program = context.newProgram();
        program.create();
        program.attachShader(vertex);
        program.attachShader(fragment);
        program.link();
        // Vertex array
        final VertexArray vertexArray = context.newVertexArray();
        vertexArray.create();
        vertexArray.setData(MeshGenerator.generateCapsule(1, 2));
        // Material
        final Material material = new Material(program);
        // Parent model
        final Model parent = new Model(vertexArray, material);
        parent.setPosition(parentModelPosition);
        // Child model
        final Model child = parent.getInstance();
        child.setParent(parent);
        child.setPosition(childModelPosition);
        child.setScale(childModelScale);
        // Pipeline
        final Pipeline pipeline = new PipelineBuilder().clearBuffer().renderModels(Arrays.asList(parent, child)).updateDisplay().build();
        // Camera
        final Camera camera = Camera.createPerspective(fieldOfView, windowWidth, windowHeight, nearPlane, farPlane);
        context.setCamera(camera);
        // Render loop
        final long sleepTime = Math.round(1 / fps * 1000);
        try {
            while (!context.isWindowCloseRequested()) {
                final long start = System.nanoTime();
                pipeline.run(context);
                parent.setRotation(parentRotationIncrement.mul(parent.getRotation()));
                final long delta = Math.round((System.nanoTime() - start) / 1e6);
                Thread.sleep(Math.max(sleepTime - delta, 0));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Destroy the context to properly exit
        context.destroy();
    }

    private static ShaderSource getShaderSource(GLVersion glVersion, ShaderType type) {
        switch (glVersion) {
            case GL20:
            case GL21:
            case GL30:
                switch (type) {
                    case VERTEX:
                        return new ShaderSource(CausticExample.class.getResourceAsStream("/shaders/glsl120/lighting.vert"));
                    case FRAGMENT:
                        return new ShaderSource(CausticExample.class.getResourceAsStream("/shaders/glsl120/lighting.frag"));
                }
            case GL32:
                switch (type) {
                    case VERTEX:
                        return new ShaderSource(CausticExample.class.getResourceAsStream("/shaders/glsl330/lighting.vert"));
                    case FRAGMENT:
                        return new ShaderSource(CausticExample.class.getResourceAsStream("/shaders/glsl330/lighting.frag"));
                }
            case SOFTWARE:
                switch (type) {
                    case VERTEX:
                        return new ShaderSource(LightingVertexShader.class.getCanonicalName());
                    case FRAGMENT:
                        return new ShaderSource(LightingFragmentShader.class.getCanonicalName());
                }
            default:
                throw new IllegalArgumentException("Unknown GLVersion: " + glVersion);
        }
    }

    private static GLImplementation getImpl(String[] args) {
        final String name;
        if (args.length < 1) {
            System.out.print("Please enter the desired implementation's name: ");
            final Scanner scanner = new Scanner(System.in);
            name = scanner.nextLine();
            scanner.close();
        } else {
            name = args[0];
        }
        switch (name.toLowerCase()) {
            case "soft":
            case "software":
                return SoftwareUtil.SOFT_IMPL;
            case "gl20":
                return LWJGLUtil.GL20_IMPL;
            case "gl21":
                return LWJGLUtil.GL21_IMPL;
            case "gl30":
                return LWJGLUtil.GL30_IMPL;
            case "gl32":
                return LWJGLUtil.GL32_IMPL;
            default:
                throw new IllegalArgumentException("Unknown implementation: " + name);
        }
    }
}
