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

import java.util.List;

import com.flowpowered.commands.Command;
import com.flowpowered.commands.CommandArguments;
import com.flowpowered.commands.CommandException;
import com.flowpowered.commands.CommandSender;
import com.flowpowered.commands.CompletingCommandExecutor;
import com.flowpowered.commands.InvalidCommandArgumentException;
import com.flowpowered.commands.flags.CommandFlags;
import com.flowpowered.commands.flags.MultiFlag;

public class MultiFlagExecutor implements CompletingCommandExecutor {
    @Override
    public boolean execute(Command command, CommandSender sender, CommandArguments args) throws CommandException {
        CommandFlags flags = prepareFlags();
        args.popFlags("flags", flags);
       
        StringBuilder builder = new StringBuilder();
        MultiFlag a = (MultiFlag) flags.getFlag('a');
        builder.append("a:").append(a.getTimesPresent()).append(':');
        for (CommandArguments fargs : a.getAllArgs()) {
            builder.append(fargs.popString("aarg", "")).append(';');
        }
        sender.sendMessage(builder.toString());
       
        builder = new StringBuilder();
        MultiFlag b = (MultiFlag) flags.getFlag('b');
        builder.append("b:").append(b.getTimesPresent()).append(':');
        for (CommandArguments fargs : b.getAllArgs()) {
            builder.append(fargs.popString("barg", "")).append(';');
        }
        sender.sendMessage(builder.toString());
               
        return true;
    }

    @Override
    public int complete(Command command, CommandSender sender, CommandArguments args, int cursor, List<String> candidates) {
        CommandFlags flags = prepareFlags();
        try {
            return args.completeFlags(command, sender, "flags", flags, cursor, candidates);
        } catch (InvalidCommandArgumentException e) {
            return -1;
        }
    }

    private CommandFlags prepareFlags() {
        return new CommandFlags()
                .add(new MultiFlag(new String[0], new char[]{'a'}, 0, 1))
                .add(new MultiFlag(new String[0], new char[]{'b'}, 0, 1));
    }
}
