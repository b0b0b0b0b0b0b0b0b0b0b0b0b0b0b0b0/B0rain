package com.black.minecraft.b0rain.utils;

import com.black.minecraft.b0rain.config.ConfigManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class RainUtils {
    public static boolean isPlayerInRain(Player player, ConfigManager configManager) {
        if (player.isInsideVehicle() || player.isDead()) {
            return false;
        }

        World world = player.getWorld();
        if (!configManager.isWorldEnabled(world.getName())) {
            return false;
        }

        boolean hasWeather = world.hasStorm() || (configManager.isCheckThunder() && world.isThundering());

        if (!hasWeather) {
            return false;
        }

        Location location = player.getLocation();
        String biomeName = location.getBlock().getBiome().getKey().toString();
        List<String> ignoredBiomes = configManager.getIgnoredBiomes();
        
        if (ignoredBiomes != null && ignoredBiomes.contains(biomeName)) {
            return false;
        }

        if (!configManager.isCheckSnow()) {
            if (biomeName.contains("snow") || biomeName.contains("ice") || biomeName.contains("frozen")) {
                return false;
            }
        }

        int blockX = location.getBlockX();
        int blockZ = location.getBlockZ();
        int highestY = world.getHighestBlockYAt(blockX, blockZ);
        
        return location.getY() >= highestY;
    }

    public static WeatherType getWeatherType(Player player, ConfigManager configManager) {
        if (!isPlayerInRain(player, configManager)) {
            return null;
        }

        Location location = player.getLocation();
        String biomeName = location.getBlock().getBiome().getKey().toString();
        
        if (biomeName.contains("snow") || biomeName.contains("ice") || biomeName.contains("frozen")) {
            return WeatherType.SNOW;
        }
        
        return WeatherType.RAIN;
    }

    public enum WeatherType {
        RAIN,
        SNOW
    }
}
