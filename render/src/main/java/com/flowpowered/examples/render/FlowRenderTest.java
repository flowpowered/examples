package com.flowpowered.examples.render;

import java.util.ArrayList;
import java.util.List;

import com.flowpowered.math.imaginary.Quaternionf;
import com.flowpowered.math.vector.Vector2i;
import com.flowpowered.math.vector.Vector3f;
import com.flowpowered.math.vector.Vector4f;
import com.flowpowered.render.RenderGraph;
import com.flowpowered.render.impl.BlurNode;
import com.flowpowered.render.impl.LightingNode;
import com.flowpowered.render.impl.RenderGUINode;
import com.flowpowered.render.impl.RenderModelsNode;
import com.flowpowered.render.impl.RenderTransparentModelsNode;
import com.flowpowered.render.impl.SSAONode;
import com.flowpowered.render.impl.ShadowMappingNode;

import org.spout.renderer.api.Camera;
import org.spout.renderer.api.GLImplementation;
import org.spout.renderer.api.Material;
import org.spout.renderer.api.data.Uniform.FloatUniform;
import org.spout.renderer.api.data.Uniform.Vector4Uniform;
import org.spout.renderer.api.data.UniformHolder;
import org.spout.renderer.api.gl.Context;
import org.spout.renderer.api.gl.Context.Capability;
import org.spout.renderer.api.gl.Texture.InternalFormat;
import org.spout.renderer.api.gl.VertexArray;
import org.spout.renderer.api.model.Model;
import org.spout.renderer.api.util.CausticUtil;
import org.spout.renderer.api.util.MeshGenerator;
import org.spout.renderer.lwjgl.LWJGLUtil;

public class FlowRenderTest {
    public static void main(String[] args) {
        LWJGLUtil.deployNatives(null);

        final Vector2i size = new Vector2i(640, 480);

        final Context context = GLImplementation.get(LWJGLUtil.GL32_IMPL);
        context.setWindowSize(size);
        context.create();
        context.enableCapability(Capability.CULL_FACE);
        context.enableCapability(Capability.DEPTH_TEST);
        context.enableCapability(Capability.DEPTH_CLAMP);

        final RenderGraph graph = new RenderGraph(context, "/shaders/glsl330");
        graph.create();

        final RenderModelsNode renderModels = new RenderModelsNode(graph, "renderModels");

        final SSAONode ssao = new SSAONode(graph, "ssao");

        final BlurNode ssaoBlur = new BlurNode(graph, "ssaoBlur");
        ssaoBlur.setAttribute("kernelGenerator", BlurNode.GAUSSIAN_KERNEL);
        ssaoBlur.setAttribute("kernelSize", 3);
        ssaoBlur.setAttribute("outputFormat", InternalFormat.R8);

        final ShadowMappingNode shadowMapping = new ShadowMappingNode(graph, "shadow");

        final BlurNode shadowMappingBlur = new BlurNode(graph, "shadowMappingBlur");
        shadowMappingBlur.setAttribute("kernelGenerator", BlurNode.BOX_KERNEL);
        shadowMappingBlur.setAttribute("kernelSize", 3);
        shadowMappingBlur.setAttribute("outputFormat", InternalFormat.R8);

        final LightingNode lighting = new LightingNode(graph, "lighting");
        lighting.setShadowsInput(graph.getWhiteDummy());

        final RenderTransparentModelsNode renderTransparentModels = new RenderTransparentModelsNode(graph, "renderTransparentModels");
        final List<Model> transparentModels = new ArrayList<>();
        renderTransparentModels.setAttribute("transparentModels", transparentModels);

        final RenderGUINode renderGUI = new RenderGUINode(graph, "renderGUI");

        graph.setAttribute("outputSize", size);
        graph.setAttribute("camera", Camera.createPerspective(60, size.getX(), size.getY(), 0.1f, 15));
        final List<Model> solidModels = new ArrayList<>();
        graph.setAttribute("models", solidModels);
        graph.setAttribute("lightDirection", Vector3f.UP.negate());

        renderModels.update();
        ssao.update();
        ssaoBlur.update();
        shadowMapping.update();
        shadowMappingBlur.update();
        lighting.update();
        renderTransparentModels.update();
        renderGUI.update();

        ssao.connect("normals", "normals", renderModels);
        ssao.connect("depths", "depths", renderModels);

        ssaoBlur.connect("colors", "occlusions", ssao);

        shadowMapping.connect("normals", "normals", renderModels);
        shadowMapping.connect("depths", "depths", renderModels);

        shadowMappingBlur.connect("colors", "shadows", shadowMapping);

        lighting.connect("colors", "colors", renderModels);
        lighting.connect("normals", "normals", renderModels);
        lighting.connect("depths", "depths", renderModels);
        lighting.connect("materials", "materials", renderModels);
        lighting.connect("occlusions", "colors", ssaoBlur);
        lighting.connect("shadows", "colors", shadowMappingBlur);

        renderTransparentModels.connect("colors", "colors", lighting);
        renderTransparentModels.connect("depths", "depths", renderModels);

        renderGUI.connect("colors", "colors", renderTransparentModels);

        graph.addNode(renderModels);
        graph.addNode(ssao);
        graph.addNode(ssaoBlur);
        graph.addNode(shadowMapping);
        graph.addNode(shadowMappingBlur);
        graph.addNode(lighting);
        graph.addNode(renderTransparentModels);
        graph.addNode(renderGUI);

        graph.build();

        final VertexArray vertexArray = context.newVertexArray();
        vertexArray.create();
        vertexArray.setData(MeshGenerator.generateCapsule(1, 2));

        UniformHolder uniforms;

        final Material solidMaterial = new Material(graph.getProgram("solid"));
        uniforms = solidMaterial.getUniforms();
        uniforms.add(new FloatUniform("diffuseIntensity", 0.7f));
        uniforms.add(new FloatUniform("specularIntensity", 1));
        uniforms.add(new FloatUniform("ambientIntensity", 0.2f));
        uniforms.add(new FloatUniform("shininess", 0.5f));
        final Model solid = new Model(vertexArray, solidMaterial);
        uniforms = solid.getUniforms();
        uniforms.add(new Vector4Uniform("modelColor", CausticUtil.CYAN));
        solid.setPosition(new Vector3f(-1, 0, 0));
        solid.setRotation(Quaternionf.fromAngleDegAxis(90, 1, 0, 0));
        solidModels.add(solid);

        final Model instance = solid.getInstance();
        uniforms = instance.getUniforms();
        uniforms.add(new Vector4Uniform("modelColor", CausticUtil.GREEN));
        instance.setPosition(new Vector3f(0, 2.5f, 0));
        instance.setRotation(Quaternionf.fromAngleDegAxis(90, 0, 0, 1));
        instance.setScale(new Vector3f(0.5f, 2, 1.5f));
        solidModels.add(instance);

        for (int i = 0; i < 12; i++) {
            final Model random = solid.getInstance();
            uniforms = random.getUniforms();
            uniforms.add(new Vector4Uniform("modelColor", new Vector4f(Math.random(), Math.random(), Math.random(), 1)));
            random.setPosition(new Vector3f(Math.random() * 8 - 4, Math.random() * 8 - 4, Math.random() * 8 - 4));
            solidModels.add(random);
        }

        final Material transparentMaterial = new Material(graph.getProgram("weightedSum"));
        uniforms = transparentMaterial.getUniforms();
        uniforms.add(new FloatUniform("diffuseIntensity", 0.7f));
        uniforms.add(new FloatUniform("specularIntensity", 1));
        uniforms.add(new FloatUniform("ambientIntensity", 0.2f));
        uniforms.add(new FloatUniform("shininess", 0.5f));
        final Model transparent = new Model(vertexArray, transparentMaterial);
        uniforms = transparent.getUniforms();
        uniforms.add(new Vector4Uniform("modelColor", CausticUtil.ORANGE.toVector3().toVector4(0.5f)));
        transparent.setPosition(new Vector3f(1, 0, 0));
        transparentModels.add(transparent);

        final float fps = 60;
        final long sleepTime = Math.round(1 / fps * 1000);
        final Quaternionf modelRotationIncrement = Quaternionf.fromAngleDegAxis(1, Vector3f.ONE);
        int frame = 0;
        float frameTime = 0;
        float sleptTime = 0;
        try {
            while (!context.isWindowCloseRequested()) {
                final long start = System.nanoTime();

                final Vector3f lightDirection = Vector3f.createDirection(frame, 90).negate();
                final Vector3f cameraDirection = Vector3f.createDirection(0, frame / 10f);

                final Camera camera = graph.getAttribute("camera");
                camera.setPosition(cameraDirection.mul(10));
                camera.setRotation(Quaternionf.fromRotationTo(Vector3f.UNIT_Z, cameraDirection));

                solid.setRotation(modelRotationIncrement.mul(solid.getRotation()));
                transparent.setRotation(modelRotationIncrement.mul(transparent.getRotation()));

                graph.setAttribute("lightDirection", lightDirection);

                graph.render();

                final long delta = Math.round((System.nanoTime() - start) / 1e6);
                frameTime += delta;

                final long sleep = Math.max(sleepTime - delta, 0);
                sleptTime += sleep;
                Thread.sleep(sleep);

                frame++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        graph.destroy();

        System.out.println("Spent on average " + (frameTime / 1000 / frame) + "s computing frames and " + (sleptTime / 1000 / frame) + "s sleeping");
        System.out.println("About " + (frameTime / sleptTime * 100) + "% of runtime was used to compute the frames");
    }
}