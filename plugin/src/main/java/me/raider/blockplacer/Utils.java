package me.raider.blockplacer;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Utils {


    public static boolean isGravityBlock(Material material) {
        return material == Material.SAND
                || material == Material.RED_SAND
                || material == Material.GRAVEL
                || material == Material.ANVIL
                || material == Material.CHIPPED_ANVIL
                || material == Material.DAMAGED_ANVIL
                || material == Material.DRAGON_EGG
                || material == Material.SUSPICIOUS_GRAVEL
                || material == Material.SUSPICIOUS_SAND
                || material == Material.POWDER_SNOW
                || material.name().endsWith("_CONCRETE_POWDER");
    }

    public static String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}
