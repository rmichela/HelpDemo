package com.ryanmichela.helpdemo;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;
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

        // Register a help topic factory for CustomExecutor
        // Note: The type of the executor is CustomExecutor instead of CommandExecutor - this is what allows the
        //       HelpTopicFactory to find this command.
        getCommand("baz").setExecutor(new CustomExecutor() {
            public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
                commandSender.sendMessage("BAZ"); return true;
            }
        });
        getServer().getHelpMap().registerHelpTopicFactory(CustomExecutor.class, new CustomExecutorHelpTopicFactory());
    }

    /**
     * This class demonstrates the simplest way to make a custom help topic for one of your commands.
     *
     * Because the name matches the name of the command, the custom topic will automatically take priority over
     * the generic topic generated by bukkit.
     */
    private class BarHelpTopic extends HelpTopic {
        private BarHelpTopic() {
            name = "/bar";
            shortText = "This is the help for /bar";
            fullText = ChatColor.RED + "Bar is the greatest command ever!\nBecause the topic name " +
                    "matches the command name, this topic will replace the default " +
                    "command help in the help index.";
        }

        @Override
        public boolean canSee(CommandSender commandSender) {
            if (amendedPermission == null) {
                return commandSender.hasPermission("help.bar") ||
                        commandSender.hasPermission("help.cheese");
            } else {
                return commandSender.hasPermission(amendedPermission);
            }
        }
    }

    /**
     * This class demonstrates how to make a help topic that is independent of any command. You can use this to add
     * extra help pages to your plugin.
     */
    private class OtherHelpTopic extends HelpTopic {
        private OtherHelpTopic() {
            name = "Other";
            shortText = "This topic has no command";
            fullText = ChatColor.BLUE + "Sometimes you need a help topic for general info on how to use " +
                    "your plugin. " + ChatColor.RED + " This could be paragraphs of text. " +
                    ChatColor.GREEN + "Newlines are supported " +
                    "and will automatically be inserted into the help output on the console " +
                    "or the client. " + ChatColor.GRAY + "If your text is too long, it will be automatically paged. " +
                    ChatColor.GOLD + "I could go on and on and on and on and on and on and on and on about how " +
                    "great paragraphs of help text are, but I won't.\n\n" +
                    ChatColor.MAGIC + "Isn't that great?";
        }

        @Override
        public boolean canSee(CommandSender commandSender) {
            if (amendedPermission == null) {
                return true;
            } else {
                return commandSender.hasPermission(amendedPermission);
            }
        }
    }

    /*
     * Below is a demonstration of how to use a custom CommandExecutor base class to simplify the registration of
     * help topics for all your commands. When you implement each command's executor, make it derive from your custom
     * base class.
     *
     * Next, create a help topic class that takes PluginCommand in the constructor and outputs the text you desire.
     *
     * Finally, create a help topic factory that accepts a PluginCommand and returns your custom help topic.
     *
     * Once all this is in place, use the registration code in your plugin's onEnable() method to tie everything together.
     */

    private abstract class CustomExecutor implements CommandExecutor {

    }

    private class CustomExecutorHelpTopic extends HelpTopic {
        private PluginCommand baseCommand;

        public CustomExecutorHelpTopic(PluginCommand baseCommand) {
            this.baseCommand = baseCommand;
            name = "/" + baseCommand.getLabel();
            shortText = baseCommand.getDescription();
            fullText = shortText + "\n" + ChatColor.LIGHT_PURPLE +
                    "This help topic is a custom topic provided by a HelpTopicFactory.";
        }

        @Override
        public boolean canSee(CommandSender commandSender) {
            if (amendedPermission == null) {
                return baseCommand.testPermissionSilent(commandSender);
            } else {
                return commandSender.hasPermission(amendedPermission);
            }
        }
    }

    private class CustomExecutorHelpTopicFactory implements HelpTopicFactory<PluginCommand> {
        public HelpTopic createTopic(PluginCommand pluginCommand) {
            return new CustomExecutorHelpTopic(pluginCommand);
        }
    }
}
