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
import java.util.Map;

import com.flowpowered.commands.Command;
import com.flowpowered.commands.CommandArguments;
import com.flowpowered.commands.CommandException;
import com.flowpowered.commands.flags.CommandFlags;
import com.flowpowered.commands.flags.Flag;
import com.flowpowered.commands.CommandSender;
import com.flowpowered.commands.CompletingCommandExecutor;
import com.flowpowered.commands.InvalidCommandArgumentException;

public class MultiArgFlagExecutor implements CompletingCommandExecutor {

    @Override
    public boolean execute(Command command, CommandSender sender, CommandArguments args) throws CommandException {
        CommandFlags flags = prepareFlags();
        args.popFlags("flags", flags);
        for (Map.Entry<String, Flag> entry : flags.getLongFlags().entrySet()) {
            if (!entry.getValue().isPresent()) {
                continue;
            }
            StringBuilder sb = new StringBuilder();
            CommandArguments fargs = entry.getValue().getArgs();
            sb.append(entry.getKey()).append(":");
            if (fargs != null) {
                while (fargs.hasNext("asdadfas")) {
                    sb.append(" ");
                    sb.append(fargs.popString("asdadfas"));
                }
            }
            sender.sendMessage(sb.toString());
        }
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
            .f(0, 3, "alpha", 'a')
            .f(0, 3, "bravo",'b')
            .f(0, 3, "charlie", 'c')
            .f(0, 3, "delta", 'd')
            .f(0, 3, "echo")
            .f(0, 3, "foxtrot")
            .f(0, 3, "golf")
            .f(0, 3, "hotel")
            .f(0, 3, "juliet");
    }
}
