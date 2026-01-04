package com.black.minecraft.b0rain.rain;

import com.black.minecraft.b0rain.config.ConfigManager;
import com.black.minecraft.b0rain.utils.RainUtils;
import org.bukkit.entity.Player;

public class RainChecker {
    public static boolean isInRain(Player player, ConfigManager configManager) {
        return RainUtils.isPlayerInRain(player, configManager);
    }
}
