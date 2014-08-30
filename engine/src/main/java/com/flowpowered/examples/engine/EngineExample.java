/*
 * This file is part of Flow Engine Example, licensed under the MIT License (MIT).
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
/*
 * This file is part of Flow Engine TestPlugin, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Spout LLC <https://spout.org/>
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
package com.flowpowered.examples.engine;

import com.flowpowered.api.Engine;
import com.flowpowered.api.Server;
import com.flowpowered.api.component.entity.PlayerControlledMovementComponent;
import com.flowpowered.api.entity.Entity;
import com.flowpowered.api.event.PlayerJoinedEvent;
import com.flowpowered.api.generator.FlatWorldGenerator;
import com.flowpowered.api.geo.LoadOption;
import com.flowpowered.api.geo.World;
import com.flowpowered.api.material.BlockMaterial;
import com.flowpowered.api.player.Player;
import com.flowpowered.api.plugins.FlowContext;
import com.flowpowered.events.EventHandler;
import com.flowpowered.math.vector.Vector3f;
import com.flowpowered.plugins.annotated.Disable;
import com.flowpowered.plugins.annotated.Enable;
import com.flowpowered.plugins.annotated.Plugin;
import org.spout.physics.collision.shape.BoxShape;

@Plugin(name = "EngineExamplePlugin")
public class EngineExample {
    protected Entity testEntity;

    @Enable
    public void onEnable(FlowContext c) {
        Engine e = c.getEngine();

        if (e.getPlatform().isServer()) {
            Server s = (Server) e;
            World loadedWorld = s.getWorldManager().loadWorld("fallback", new FlatWorldGenerator(BlockMaterial.SOLID_BLUE));
            BoxShape shape = new BoxShape(5, 5, 5) {
                @Override
                public int getNbSimilarCreatedShapes() {
                    return 1;
                }
            };
            @SuppressWarnings("unchecked")
            Entity entity = loadedWorld.spawnEntity(Vector3f.ZERO.add(0, 10, 0), LoadOption.LOAD_GEN);
            //entity.getPhysics().activate(50, shape);
            this.testEntity = entity;
        }
        e.getEventManager().registerEvents(this, this);
    }

    @Disable
    public void onDisable(FlowContext c) {

    }

    @EventHandler
    public void onPlayerJoined(PlayerJoinedEvent e) {
        Player player = e.getPlayer();

        player.setTransformProvider(testEntity.getPhysics());
        testEntity.add(PlayerControlledMovementComponent.class).setController(player);
    }

    public Entity getTestEntity() {
        return testEntity;
    }
}
