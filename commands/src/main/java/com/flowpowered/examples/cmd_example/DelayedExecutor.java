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

import java.util.ArrayList;
import java.util.List;

import com.flowpowered.commands.Command;
import com.flowpowered.commands.CommandArguments;
import com.flowpowered.commands.CommandException;
import com.flowpowered.commands.CommandExecutor;
import com.flowpowered.commands.flags.CommandFlags;
import com.flowpowered.commands.flags.Flag;
import com.flowpowered.commands.CommandSender;
import com.flowpowered.commands.CompletingCommandExecutor;
import com.flowpowered.commands.InvalidCommandArgumentException;

public class DelayedExecutor implements CompletingCommandExecutor {
    private final CommandExecutor parent;
    private final int delay;

    public DelayedExecutor(CommandExecutor parent, int delay) {
        this.parent = parent;
        this.delay = delay;
    }

    @Override
    public boolean execute(final Command command, final CommandSender sender, final CommandArguments args) throws CommandException {
        CommandFlags flags = new CommandFlags();
        Flag d = new Flag(new String[]{"delay"}, new char[]{'d'}, 1, 1);
        flags.add(d);
        args.popFlags("options", flags);

        final int delay;
        if (d.isPresent()) {
            delay = d.getArgs().popInteger("delay");
        } else {
            delay = this.delay;
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                    DelayedExecutor.this.parent.execute(command, sender, args);
                } catch (InterruptedException | CommandException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return true;
    }

    @Override
    public int complete(Command command, CommandSender sender, CommandArguments args, int cursor, List<String> candidates) {
        try {
            CommandFlags flags = new CommandFlags();
            Flag d = new Flag(new String[]{"delay"}, new char[]{'d'}, 1, 1);
            flags.add(d);
            int result = args.completeFlags(command, sender, "options", flags, cursor, candidates);
            if (!(result == -2 || flags.getCanCompleteNextArgToo())) {
                return result;
            }
            if (result < -1) {
                result = -1;
            }

            if (parent instanceof CompletingCommandExecutor) {
                List<String> candidates1 = null;
                if (result != -1) {
                    candidates1 = new ArrayList<>(candidates);
                    candidates.clear();
                }
                int result2 = ((CompletingCommandExecutor) parent).complete(command, sender, args, cursor, candidates);
                if (result != -1) {
                    List<String> candidates2 = new ArrayList<>(candidates);
                    candidates.clear();
                    result2 = args.mergeCompletions(null, result, candidates1, result2, candidates2, candidates);
                }
                return result2;
            } else {
                return result;
            }
        } catch (InvalidCommandArgumentException e) {
            return -1;
        }
    }

}
