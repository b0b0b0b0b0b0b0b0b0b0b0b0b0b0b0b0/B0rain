package com.black.minecraft.b0rain.rain;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectHelper {
    private static PotionEffectType slownessType;

    public static PotionEffectType getSlownessType() {
        if (slownessType == null) {
            slownessType = findSlownessType();
        }
        return slownessType;
    }

    private static PotionEffectType findSlownessType() {
        PotionEffectType type = PotionEffectType.getByName("SLOWNESS");
        if (type != null) {
            return type;
        }
        
        type = PotionEffectType.getByName("slowness");
        if (type != null) {
            return type;
        }

        for (PotionEffectType effectType : PotionEffectType.values()) {
            if (effectType != null && effectType.getName() != null && 
                effectType.getName().equalsIgnoreCase("SLOWNESS")) {
                return effectType;
            }
        }
        
        return null;
    }

    public static PotionEffect createSlownessEffect(int duration, int level) {
        PotionEffectType type = getSlownessType();
        if (type == null) {
            return null;
        }
        return new PotionEffect(type, duration, level, false, false);
    }

    public static boolean isPluginEffect(PotionEffect effect, int expectedLevel, int maxDuration) {
        if (effect == null) {
            return false;
        }
        return effect.getAmplifier() == expectedLevel && 
               effect.getDuration() <= maxDuration;
    }
}
