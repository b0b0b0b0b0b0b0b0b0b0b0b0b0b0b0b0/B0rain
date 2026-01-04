package com.black.minecraft.b0rain.commands;

import com.black.minecraft.b0rain.B0rain;
import com.black.minecraft.b0rain.config.ConfigManager;
import com.black.minecraft.b0rain.config.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class B0rainCommand implements CommandExecutor {
    private final B0rain plugin;
    private final ConfigManager configManager;
    private final LanguageManager languageManager;

    public B0rainCommand(B0rain plugin, ConfigManager configManager, LanguageManager languageManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.languageManager = languageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("rainb0.reload")) {
                sender.sendMessage(languageManager.getMessage("no-permission"));
                return true;
            }

            try {
                configManager.loadConfig();
                languageManager.loadLanguage();
                plugin.getRainEffectManager().stop();
                plugin.getRainEffectManager().start();
                sender.sendMessage(languageManager.getMessage("reload-success"));
            } catch (Exception e) {
                sender.sendMessage(languageManager.getMessage("reload-error"));
                plugin.getLogger().severe("Error reloading configuration: " + e.getMessage());
            }
            return true;
        }

        return false;
    }
}
