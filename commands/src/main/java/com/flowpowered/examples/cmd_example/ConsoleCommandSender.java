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

import java.util.Collections;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.flowpowered.chat.ChatReceiver;
import com.flowpowered.commands.CommandArguments;
import com.flowpowered.commands.CommandException;
import com.flowpowered.commands.CommandManager;
import com.flowpowered.commands.CommandSender;
import com.flowpowered.permissions.PermissionDomain;

public class ConsoleCommandSender implements CommandSender {
    private final Logger logger;
    private final CommandManager manager;
    
    public static final String MSG_TYPE_ERR = "error" ;

    public ConsoleCommandSender(CommandManager manager) {
        this(manager, LogManager.getLogger("Msg"));
    }

    public ConsoleCommandSender(CommandManager manager, Logger logger) {
        this.logger = logger;
        this.manager = manager;
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public boolean hasPermission(String permission, PermissionDomain domain) {
        return true;
    }

    @Override
    public boolean isInGroup(String group) {
        return false;
    }

    @Override
    public boolean isInGroup(String group, PermissionDomain domain) {
        return false;
    }

    @Override
    public Set<String> getGroups() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getGroups(PermissionDomain domain) {
        return Collections.emptySet();
    }

    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public void sendMessage(String message) {
        sendMessageRaw(message, "");
    }

    @Override
    public void sendMessage(ChatReceiver from, String message) {
        sendMessage(message);
    }

    @Override
    public void sendMessageRaw(String message, String type) {
    	// This part can be on the other end of any kind of wire (i.e. on the client side).
    	if (type == MSG_TYPE_ERR) {
    		this.logger.error(message);
    	} else {
    		this.logger.info(message);
    	}
    }

    @Override
    public void processCommand(CommandArguments commandLine) throws CommandException {
        this.manager.executeCommand(this, commandLine);
    }
    
    public void sendErrMessage(String message) {
    	sendMessageRaw(message, MSG_TYPE_ERR);
    }

}
