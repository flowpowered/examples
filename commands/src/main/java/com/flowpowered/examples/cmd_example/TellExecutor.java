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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.flowpowered.math.vector.Vector2i;

import com.flowpowered.commands.Command;
import com.flowpowered.commands.CommandArguments;
import com.flowpowered.commands.CommandException;
import com.flowpowered.commands.CommandSender;
import com.flowpowered.commands.CompletingCommandExecutor;
import com.flowpowered.commands.InvalidCommandArgumentException;

public class TellExecutor implements CompletingCommandExecutor {

    @Override
    public boolean execute(Command command, CommandSender sender, CommandArguments args) throws CommandException {
        sender.sendMessage("[You -> " + args.popString("to") + "] " + args.popRemainingStrings("msg"));
        return true;
    }

    @Override
    public int complete(Command command, CommandSender sender, CommandArguments args, int cursor, List<String> candidates) {
        Vector2i pos = args.offsetToArgument(cursor);
        if (pos.getX() == 0 && !args.hasOverride("to")) {
            return args.complete("to", pos, new TreeSet<>(getPeopleAround()), candidates);
        }
        try {
            args.popString("to");
        } catch (InvalidCommandArgumentException e) {
            return -1;
        }
        return args.completeRemainingStrings("msg", cursor, new TreeSet<>(Arrays.asList("", "I love you\\!")), candidates);
    }

    private Set<String> getPeopleAround() {
        Set<String> set = new HashSet<>();
        set.add("Wolf480pl");
        set.add("kitskub");
        set.add("Wulfspider");
        set.add("DDoS");
        set.add("Zidane");
        set.add("Grinch");
        set.add("DonRedhorse");
        set.add("narrowtux");
        set.add("!ops");
        return set;
    }

}
