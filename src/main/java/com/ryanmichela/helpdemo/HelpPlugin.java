package com.ryanmichela.helpdemo;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.java.JavaPlugin;

/**
 */
public class HelpPlugin extends JavaPlugin
{
    @Override
    public void onEnable() {
        // Set command executors for registered commands
        getCommand("foo").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                commandSender.sendMessage("FOO");
                return true;
            }
        });
        getCommand("bar").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                commandSender.sendMessage("BAR"); return true;
            }
        });

        // Create custom help topics
        getServer().getHelpMap().addTopic(new BarHelpTopic());
        getServer().getHelpMap().addTopic(new OtherHelpTopic());
    }

    private class BarHelpTopic extends HelpTopic {
        private BarHelpTopic() {
            name = "/bar";
            shortText = "This is the help for /bar";
            fullText = ChatColor.RED + "Bar is the greatest command ever!\nBecause the topic name" +
                    "matches the command name, this topic will replace the default" +
                    "command help in the help index.";
        }

        @Override
        public boolean canSee(CommandSender commandSender) {
            return commandSender.hasPermission("help.bar") ||
                    commandSender.hasPermission("help.cheese");
        }
    }

    private class OtherHelpTopic extends HelpTopic {
        private OtherHelpTopic() {
            name = "Other";
            shortText = "This topic has no command";
            fullText = "Sometimes you need a help topic for general info on how to use" +
                    "your plugin. This could be paragraphs of text.\nNewlines are supported" +
                    "and will automatically be inserted into the help output on the console" +
                    "or the client. If your text is too long, it will be automatically paged.\n" +
                    "Isn't that great?";
        }

        @Override
        public boolean canSee(CommandSender commandSender) {
            return true;
        }
    }
}
