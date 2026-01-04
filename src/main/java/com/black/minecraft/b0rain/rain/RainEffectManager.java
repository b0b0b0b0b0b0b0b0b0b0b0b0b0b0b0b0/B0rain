package com.black.minecraft.b0rain.rain;

import com.black.minecraft.b0rain.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import com.black.minecraft.b0rain.B0rain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RainEffectManager {
    private final B0rain plugin;
    private final ConfigManager configManager;
    private BukkitRunnable task;
    private final Set<UUID> playersWithPluginEffect;
    private final int slownessLevel;
    private final int slownessDuration;

    public RainEffectManager(B0rain plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.playersWithPluginEffect = new HashSet<>();
        this.slownessLevel = configManager.getSlownessLevel();
        this.slownessDuration = configManager.getSlownessDurationTicks();
        
        if (PotionEffectHelper.getSlownessType() == null) {
            plugin.getLogger().severe("Failed to load SLOWNESS potion effect type! Plugin may not work correctly.");
        }
    }

    public void start() {
        if (task != null) {
            task.cancel();
        }

        task = new BukkitRunnable() {
            @Override
            public void run() {
                PotionEffectType slownessType = PotionEffectHelper.getSlownessType();
                if (slownessType == null) {
                    return;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!configManager.isWorldEnabled(player.getWorld().getName())) {
                        continue;
                    }

                    UUID playerId = player.getUniqueId();
                    boolean inRain = RainChecker.isInRain(
                            player,
                            configManager.isCheckThunder(),
                            configManager.isCheckSnow(),
                            configManager.getIgnoredBiomes()
                    );

                    if (inRain) {
                        PotionEffect slowness = PotionEffectHelper.createSlownessEffect(
                                slownessDuration,
                                slownessLevel
                        );
                        if (slowness != null) {
                            player.addPotionEffect(slowness);
                            playersWithPluginEffect.add(playerId);
                        }
                    } else {
                        if (playersWithPluginEffect.contains(playerId)) {
                            PotionEffect currentEffect = player.getPotionEffect(slownessType);
                            if (PotionEffectHelper.isPluginEffect(currentEffect, slownessLevel, slownessDuration + 20)) {
                                player.removePotionEffect(slownessType);
                            }
                            playersWithPluginEffect.remove(playerId);
                        }
                    }
                }
            }
        };

        task.runTaskTimer(plugin, 0L, configManager.getCheckIntervalTicks());
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }

        PotionEffectType slownessType = PotionEffectHelper.getSlownessType();
        if (slownessType != null) {
            for (UUID playerId : new HashSet<>(playersWithPluginEffect)) {
                Player player = Bukkit.getPlayer(playerId);
                if (player != null) {
                    PotionEffect currentEffect = player.getPotionEffect(slownessType);
                    if (PotionEffectHelper.isPluginEffect(currentEffect, slownessLevel, slownessDuration + 20)) {
                        player.removePotionEffect(slownessType);
                    }
                }
            }
            playersWithPluginEffect.clear();
        }
    }
}
