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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowpowered.commands.Command;
import com.flowpowered.commands.CommandArguments;
import com.flowpowered.commands.CommandException;
import com.flowpowered.commands.CommandExecutor;
import com.flowpowered.commands.CommandSender;
import com.flowpowered.commands.CompletingCommandExecutor;

public class OverrideTestExecutor implements CompletingCommandExecutor {
    private final Map<String, String> overrides = new HashMap<>();

    @Override
    public boolean execute(Command command, CommandSender sender, CommandArguments args) throws CommandException {
        for (Map.Entry<String, String> entry : overrides.entrySet()) {
            args.setArgOverride(entry.getKey(), entry.getValue());
        }

        return false;
    }

    @Override
    public int complete(Command command, CommandSender sender, CommandArguments args, int cursor, List<String> candidates) {
        return -2;
    }

    public Map<String, String> getOverrides() {
        return overrides;
    }

    public CommandExecutor getSetter() {
        return new CommandExecutor() {
            @Override
            public boolean execute(Command command, CommandSender sender, CommandArguments args) throws CommandException {
                String key = args.popString("key");
                if (args.hasMore()) {
                    overrides.put(key, args.popString("value"));
                } else {
                    overrides.remove(key);
                }
                args.assertCompletelyParsed();
                return true;
            }
        };
    }

    public CommandExecutor getGetter() {
        return new CommandExecutor() {
            @Override
            public boolean execute(Command command, CommandSender sender, CommandArguments args) throws CommandException {
                if (args.hasMore()) {
                    sender.sendMessage(overrides.get(args.popString("key")));
                } else {
                    sender.sendMessage(overrides.toString());
                }
                args.assertCompletelyParsed();
                return true;
            }
        };
    }

}
