/*
 * This file is part of Flow Commands Example, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Spout LLC <http://www.spout.org/>
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
package com.flowpowered.examples.cmd_example;

import java.util.List;
import java.util.Random;

import com.flowpowered.math.matrix.Matrix4f;
import com.flowpowered.math.vector.Vector3f;

import com.flowpowered.commands.Command;
import com.flowpowered.commands.CommandArguments;
import com.flowpowered.commands.CommandException;
import com.flowpowered.commands.CommandSender;
import com.flowpowered.commands.CompletingCommandExecutor;
import com.flowpowered.commands.flags.CommandFlags;
import com.flowpowered.commands.util.RelativeVector3f;

public class VectorTestExecutor implements CompletingCommandExecutor {
    private final Random rnd = new Random();

    @Override
    public boolean execute(Command command, CommandSender sender, CommandArguments args) throws CommandException {
        CommandFlags flags = new CommandFlags().f(1, 3, "rel", 'r');
        args.popFlags("vecOptions", flags);

        RelativeVector3f v;
        if (flags.getFlag('r').isPresent()) {
            Vector3f b0 = flags.getFlag('r').getArgs().popVector3("refPoint");
            v = args.popRelativeVector3("vec", b0);
        } else {
            v = args.popVector3("vec");
        }

        Vector3f v0 = v.asVector();
        sender.sendMessage("The vector was " + v0);

        Vector3f b1 = randomVector();
        RelativeVector3f v1 = v.withRefPoint(b1);
        sender.sendMessage("Relatively to " + b1 + " it would be " + v1);

        Vector3f b2 = randomVector();
        Vector3f v2 = v.withRefPoint(b2).asVector();
        sender.sendMessage("And relatively to " + b2 + " it would be " + v2);

        Matrix4f m = v.asMatrix();
        sender.sendMessage("As matrix: " + m);
        sender.sendMessage(v1.toString() + " should be " + m.transform(b1.toVector4(1)).toVector3());
        sender.sendMessage(v2.toString() + " should be " + m.transform(b2.toVector4(1)).toVector3());
        return true;
    }

    @Override
    public int complete(Command arg0, CommandSender arg1, CommandArguments arg2, int arg3, List<String> arg4) {
        // TODO Auto-generated method stub
        return -1;
    }

    protected Vector3f randomVector() {
        return new Vector3f(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat());
    }
}
