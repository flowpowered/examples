/*
 * This file is part of Flow Commands Example, licensed under the MIT License (MIT).
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
package com.flowpowered.examples.commands;

import com.flowpowered.commands.Command;
import com.flowpowered.commands.CommandException;
import com.flowpowered.commands.CommandManager;
import com.flowpowered.commands.CommandProvider;

public class ExampleCommandProvider implements CommandProvider {
    public void init(CommandManager manager) throws CommandException {
        Command example = manager.getCommand(this, "example");
        manager.setPath(example, "example");

        Command echo = manager.getCommand(this, "example.echo");
        echo.setExecutor(new EchoExecutor());
        manager.setPath(echo, "echo");
        manager.setPath(echo, "example", "echo");

        Command tell = manager.getCommand(this, "example.tell");
        tell.setExecutor(new TellExecutor());
        example.insertChild("tell", tell);

        Command help = manager.getCommand(this, "example.help");
        help.setExecutor(new HelpExecutor("/example help <command> - get help for a command\npossible commands: echo"));
        example.insertChild("help", help);

        Command helpEcho = manager.getCommand(this, "example.help.echo");
        helpEcho.setExecutor(new HelpExecutor("/example echo <message> - echos a message to the command user"));
        help.insertChild("echo", helpEcho);

        Command delayEcho = manager.getCommand(this, "example.delay");
        delayEcho.setExecutor(new DelayedExecutor(new EchoExecutor(), 500));
        example.insertChild("delay", delayEcho);

        Command delayExit = manager.getCommand(this, "example.dexit");
        Command exit = manager.getCommandByPath("exit");
        delayExit.setExecutor(new DelayedExecutor(exit.getExecutor(), 500));
        example.insertChild("dexit", delayExit);

        Command delayTell = manager.getCommand(this, "example.dtell");
        delayTell.setExecutor(new DelayedExecutor(tell.getExecutor(), 500));
        example.insertChild("dtell", delayTell);

        Command multiArgFlag = manager.getCommand(this, "example.multi-arg-flag");
        multiArgFlag.setExecutor(new MultiArgFlagExecutor());
        example.insertChild("multi-argument-flags", multiArgFlag);

        OverrideTestExecutor ote = new OverrideTestExecutor();

        Command overrideTest = manager.getCommand(this, "override-test");
        overrideTest.setExecutor(ote);
        manager.setPath(overrideTest, "with-override");

        Command overrideTestControl = manager.getCommand(this, "override-test.control");
        manager.setPath(overrideTestControl, "override");

        Command overrideTestSet = manager.getCommand(this, "override-test.set");
        overrideTestSet.setExecutor(ote.getSetter());
        overrideTestControl.insertChild("set", overrideTestSet);

        Command overrideTestGet = manager.getCommand(this, "override-test.get");
        overrideTestGet.setExecutor(ote.getGetter());
        overrideTestControl.insertChild("get", overrideTestGet);

        Command multiFlag = manager.getCommand(this, "example.multi-flag");
        multiFlag.setExecutor(new MultiFlagExecutor());
        example.insertChild("repeating-flags", multiFlag);

        Command vecTest = manager.getCommand(this, "vector-test");
        vecTest.setExecutor(new VectorTestExecutor());
        example.insertChild("vector", vecTest);
    }

    @Override
    public String getName() {
        return "example";
    }
}
