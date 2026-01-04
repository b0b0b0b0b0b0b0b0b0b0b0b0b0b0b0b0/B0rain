package com.black.minecraft.b0rain.elytra;

import com.black.minecraft.b0rain.B0rain;
import com.black.minecraft.b0rain.config.ConfigManager;
import com.black.minecraft.b0rain.config.LanguageManager;
import com.black.minecraft.b0rain.utils.RainUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ElytraManager implements Listener {
    private final B0rain plugin;
    private final ConfigManager configManager;
    private final LanguageManager languageManager;

    public ElytraManager(B0rain plugin, ConfigManager configManager, LanguageManager languageManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.languageManager = languageManager;
    }

    public void start() {
        if (configManager.isBlockFireworks() || configManager.isBlockRiptide()) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    public void stop() {
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFireworkUse(PlayerInteractEvent event) {
        if (!configManager.isBlockFireworks()) {
            return;
        }

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.FIREWORK_ROCKET) {
            return;
        }

        if (!configManager.isWorldEnabled(player.getWorld().getName())) {
            return;
        }

        if (RainUtils.isPlayerInRain(player, configManager)) {
            event.setCancelled(true);
            RainUtils.WeatherType weatherType = RainUtils.getWeatherType(player, configManager);
            String messageKey = weatherType == RainUtils.WeatherType.SNOW 
                ? "firework-snow-blocked" 
                : "firework-rain-blocked";
            player.sendMessage(languageManager.getMessage(messageKey));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTridentUse(PlayerInteractEvent event) {
        if (!configManager.isBlockRiptide()) {
            return;
        }

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.TRIDENT) {
            return;
        }

        if (!item.getEnchantments().containsKey(Enchantment.RIPTIDE)) {
            return;
        }

        if (!configManager.isWorldEnabled(player.getWorld().getName())) {
            return;
        }

        if (RainUtils.isPlayerInRain(player, configManager)) {
            event.setCancelled(true);
            RainUtils.WeatherType weatherType = RainUtils.getWeatherType(player, configManager);
            String messageKey = weatherType == RainUtils.WeatherType.SNOW 
                ? "riptide-snow-blocked" 
                : "riptide-rain-blocked";
            player.sendMessage(languageManager.getMessage(messageKey));
        }
    }
}
