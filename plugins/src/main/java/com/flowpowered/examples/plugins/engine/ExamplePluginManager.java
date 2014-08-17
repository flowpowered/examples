/*
 * This file is part of Flow Plugins Example, licensed under the MIT License (MIT).
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
package com.flowpowered.examples.plugins.engine;


import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;

import com.flowpowered.plugins.ContextCreator;
import com.flowpowered.plugins.Plugin;
import com.flowpowered.plugins.PluginManager;
import com.flowpowered.plugins.annotated.AnnotatedPluginLoader;
import com.flowpowered.plugins.simple.SimplePluginLoader;
import org.slf4j.LoggerFactory;

public class ExamplePluginManager extends PluginManager<ExampleContext> {

    public ExamplePluginManager(final PluginsExample example) {
        super(LoggerFactory.getLogger(PluginsExample.class));
        try {
            ContextCreator<ExampleContext> cc = new ContextCreator<ExampleContext>() {
                @Override
                public ExampleContext createContext(Plugin<ExampleContext> plugin) {
                    return new ExampleContext(plugin, example);
                }
            };
            addLoader(new SimplePluginLoader<>(cc, new URLClassLoader(new URL[] { Paths.get("target/plugins-0.1.0-SNAPSHOT-test.jar").toUri().toURL() })));
            addLoader(new AnnotatedPluginLoader<>(cc, Paths.get("target/classes/com/flowpowered/examples/plugins/annotated/ExampleAnnotatedPlugin.class"), getClass().getClassLoader()));
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

}
