package com.black.minecraft.b0rain.rain;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class RainChecker {
    public static boolean isInRain(Player player, boolean checkThunder, boolean checkSnow, List<String> ignoredBiomes) {
        if (player.isInsideVehicle() || player.isDead()) {
            return false;
        }

        World world = player.getWorld();
        boolean hasWeather = world.hasStorm() || (checkThunder && world.isThundering());

        if (!hasWeather) {
            return false;
        }

        Location location = player.getLocation();
        String biomeName = location.getBlock().getBiome().getKey().toString();
        
        if (ignoredBiomes != null && ignoredBiomes.contains(biomeName)) {
            return false;
        }

        int blockX = location.getBlockX();
        int blockZ = location.getBlockZ();
        int highestY = world.getHighestBlockYAt(blockX, blockZ);
        
        return location.getY() >= highestY;
    }
}
