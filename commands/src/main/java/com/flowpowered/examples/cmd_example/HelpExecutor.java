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

import com.flowpowered.commands.Command;
import com.flowpowered.commands.CommandArguments;
import com.flowpowered.commands.CommandException;
import com.flowpowered.commands.flags.CommandFlags;
import com.flowpowered.commands.flags.Flag;
import com.flowpowered.commands.CommandSender;
import com.flowpowered.commands.CompletingCommandExecutor;

public class HelpExecutor implements CompletingCommandExecutor {
    private static final String FLAGS_ARGNAME = "helpOpts";
    private String message;

    public HelpExecutor(String message) {
        this.message = message;
    }

    @Override
    public boolean execute(Command command, CommandSender sender, CommandArguments args) throws CommandException {
        CommandFlags flags = args.getFlags(FLAGS_ARGNAME);
        if (flags == null) {
            flags = args.popFlags(FLAGS_ARGNAME, prepareFlags());
        }

        if (flags.getFlag('V').isPresent()) {
            sender.sendMessage("version 0.0.1-derp3-SNAPSHOT+homeBuilt.42");
            return true;
        }

        if (args.hasMore() && !args.currentArgument(CommandArguments.SUBCOMMAND_ARGNAME + args.getDepth(), true, true).isEmpty()) {
            return false;
        }

        if (flags.getFlag('l').isPresent()) {
            sender.sendMessage("------ HELP ------");
            sender.sendMessage("Here's the deal:");
        }

        sender.sendMessage(this.message);

        if (flags.getFlag('l').isPresent()) {
            sender.sendMessage("That's about it!");
            sender.sendMessage("------------------");
        }
        return true;
    }

    @Override
    public int complete(Command command, CommandSender sender, CommandArguments args, int cursor, List<String> candidates) {
        return -2;
    }

    private CommandFlags prepareFlags() {
        return new CommandFlags()
        .add(new Flag(new String[]{"long", "verbose"}, new char[]{'l', 'v'}, 0, 0))
        .b("version", 'V');
    }
}
