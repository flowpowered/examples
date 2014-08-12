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

import java.io.File;
import java.util.List;

import jline.console.completer.Completer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.flowpowered.cerealization.config.Configuration;
import com.flowpowered.cerealization.config.ConfigurationException;
import com.flowpowered.cerealization.config.yaml.YamlConfiguration;

import com.flowpowered.commons.console.CommandCallback;
import com.flowpowered.commons.console.JLineConsole;
import com.flowpowered.commons.console.Log4j2JLineConsole;

import com.flowpowered.commands.Command;
import com.flowpowered.commands.CommandArguments;
import com.flowpowered.commands.CommandException;
import com.flowpowered.commands.CommandExecutor;
import com.flowpowered.commands.CommandProvider;
import com.flowpowered.commands.CommandSender;
import com.flowpowered.commands.ConfigurableCommandManager;
import com.flowpowered.commands.exception.UserFriendlyCommandException;
import com.flowpowered.commands.syntax.flags.DefaultFlagSyntax;
import com.flowpowered.commands.syntax.flags.SpoutFlagSyntax;
import com.flowpowered.commands.syntax.flags.DefaultFlagSyntax.StrictnessMode;
import com.flowpowered.commands.syntax.DefaultSyntax;
import com.flowpowered.commands.syntax.RegexSyntax;
import com.flowpowered.commands.syntax.Syntax;

public class CommandExample {
    private Logger logger = LogManager.getLogger("System");
    private ConfigurableCommandManager manager;
    private Configuration config;
    private ConsoleCommandSender sender;
    private JLineConsole console;
    private CommandProvider coreProvider;
    private ExampleCommandProvider exampleProvider;
    private Syntax cmdSyntax;

    public CommandExample() throws ConfigurationException {
        this(DefaultSyntax.INSTANCE);
    }

    public CommandExample(Syntax cmdSyntax) throws ConfigurationException {
        this.config = new YamlConfiguration(new File("commands.yml"));
        this.config.load();
        this.manager = new ConfigurableCommandManager(this.config);
        this.manager.readConfig();
        this.sender = new ConsoleCommandSender(this.manager);
        this.cmdSyntax = cmdSyntax;
        this.coreProvider = new CommandProvider() {
            @Override
            public String getName() {
                return "core";
            }
        };
        Command exit = this.manager.getCommand(this.coreProvider, "exit");
        exit.setExecutor(new CommandExecutor(){
            @Override
            public boolean execute(Command command, CommandSender sender, CommandArguments args) throws CommandException {
                if (CommandExample.this.console == null) {
                    CommandExample.this.logger.warn("Issued exit command when console is null");
                    return true;
                }
                sender.sendMessage("Closing console");
                CommandExample.this.console.close();
                sender.sendMessage("Console close request sent");
                return true;
            }
        });
        this.manager.getRootCommand().insertChild("exit", exit);
        this.exampleProvider = new ExampleCommandProvider();
        try {
            this.exampleProvider.init(this.manager);
        } catch (CommandException e) {
            this.logger.error("Could not initialize example command provider", e);
        }
    }

    public void start() {
        if (!this.manager.getRootCommand().hasChild("exit")) {
            this.logger.error("Could not start console - there's no core command provider, how would you exit?");
        }
        CommandCallback callback = new CommandCallback() {
            @Override
            public void handleCommand(String command) {
                try {
                    CommandExample.this.manager.executeCommand(CommandExample.this.sender, new CommandArguments(command, cmdSyntax));
                } catch (CommandException e) {
                    if (e instanceof UserFriendlyCommandException) {
                        CommandExample.this.sender.sendErrMessage(e.getMessage());
                    } else {
                        CommandExample.this.logger.warn("Exception ocurred when executing a command:", e);
                    }
                }
            }
        };
        Completer completer = new Completer() {
            @Override
            public int complete(String buffer, int cursor, List<CharSequence> candidates) {
                try {
                    CommandExample.this.logger.debug("Complete");
                    return CommandExample.this.manager.getRootCommand().complete(CommandExample.this.sender, new CommandArguments(buffer.toString(), cmdSyntax), cursor, candidates);
                } catch (CommandException e) {
                    CommandExample.this.logger.warn("Exception ocurred when completing a command:", e);
                }
                return -1;
            }
        };

        this.console = new Log4j2JLineConsole(callback, completer);
    }

    public void joinConsole() throws InterruptedException {
        this.console.getCommandThread().join();
    }

    public static void main(String[] args) throws ConfigurationException {
        Syntax s = DefaultSyntax.INSTANCE;
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("f")) {
                s = new DefaultSyntax(SpoutFlagSyntax.INSTANCE);
            } else if (args[0].equalsIgnoreCase("s")) {
                s = RegexSyntax.SPOUT_SYNTAX;
            } else if (args[0].equalsIgnoreCase("skip")) {
                s = new DefaultSyntax(new DefaultFlagSyntax(true, false, StrictnessMode.SKIP, StrictnessMode.SKIP));
            } else if (args[0].equalsIgnoreCase("throw")) {
                s = new DefaultSyntax(new DefaultFlagSyntax(true, false, StrictnessMode.THROW, StrictnessMode.THROW));
            }
        }
        try {
            CommandExample example = new CommandExample(s);
            example.start();
            try {
                example.joinConsole();
                example.config.save();
            } catch (InterruptedException e) {
                example.logger.fatal("Main thread interrupted. Exitting.", e);
            }
        } catch (Throwable t) {
            LogManager.getLogger("main").catching(t);
        }
    }
}
