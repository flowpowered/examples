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
import com.flowpowered.commands.CommandArguments;
import com.flowpowered.commands.CommandException;
import com.flowpowered.commands.CommandManager;
import com.flowpowered.commands.CommandProvider;
import com.flowpowered.commands.CommandSender;
import com.flowpowered.commands.annotated.AnnotatedCommandExecutorFactory;
import com.flowpowered.commands.annotated.CommandDescription;
import com.flowpowered.commands.annotated.Parent;

public class AnnotatedCommandProvider implements CommandProvider {

    public void init(CommandManager manager) {
        Command example = manager.getCommand(this, "example");
        AnnotatedCommandExecutorFactory factory = new AnnotatedCommandExecutorFactory(manager, this);
        factory.create(this, example);
    }

    @Override
    public String getName() {
        return "annotated";
    }

    @Parent("flow:root")
    @CommandDescription(name = "example.echo")
    public void echo(CommandSender sender, CommandArguments args) throws CommandException {
        sender.sendMessage(args.popRemainingStrings("message"));
    }

    @CommandDescription(name = "example.tell")
    public void tell(CommandSender sender, CommandArguments args) throws CommandException {
        sender.sendMessage("[You -> " + args.popString("to") + "] " + args.popRemainingStrings("msg"));
    }

    @Parent(value = "example:example.help", name = "tell")
    @CommandDescription(name = "example.tell-help", autoParent = false)
    public void tellHelp(CommandSender sender, CommandArguments args) throws CommandException {
        sender.sendMessage("example tell <target> <message> - \"sends\" the message to the target user");
    }

}
