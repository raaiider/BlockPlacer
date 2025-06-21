package me.raider.blockplacer;

import me.raider.blockplacer.placer.Placer;
import me.raider.blockplacer.placer.PlacerMode;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.Map;


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

    private static final Map<BlockFace, Vector> FACE_TO_VECTOR = Map.of(
            BlockFace.UP, new Vector(0, -1, 0),
            BlockFace.DOWN, new Vector(0, 1, 0),
            BlockFace.NORTH, new Vector(0, 0, 1),
            BlockFace.SOUTH, new Vector(0, 0, -1),
            BlockFace.EAST, new Vector(-1, 0, 0),
            BlockFace.WEST, new Vector(1, 0, 0)
    );

    public static ProcessResult processNextLocationForcedSync(Placer placer, Location location) {
        return processNextLocationForcedSync(location, placer.getForcedFace());
    }

    public static ProcessResult processNextLocationForcedSync(Location location, BlockFace face) {
        Vector offset = FACE_TO_VECTOR.getOrDefault(face, new Vector(0, 0, 0));
        return getResultFromLocation(location.add(offset));
    }

    public static Location processNextLocationForcedAsync(Placer placer, Location location) {
        return processNextLocationForcedAsync(location, placer.getForcedFace());
    }

    public static Location processNextLocationForcedAsync(Location location, BlockFace face) {
        Vector offset = FACE_TO_VECTOR.getOrDefault(face, new Vector(0, 0, 0));
        return location.add(offset);
    }

    public static ProcessResult processNextLocationSync(Placer placer, Location location, BlockFace face) {
        return getResultFromLocation(offsetByPlacer(placer, location, face));
    }

    public static Location processNextLocationAsync(Placer placer, Location location, BlockFace face) {
        return offsetByPlacer(placer, location, face);
    }

    private static Location offsetByPlacer(Placer placer, Location location, BlockFace face) {
        int dx = 0, dy = 0, dz = 0;

        switch (face) {
            case UP:
            case DOWN:
                if (placer.getMode() == PlacerMode.VERTICAL) {
                    dy = placer.isInverted() ? (face == BlockFace.UP ? 1 : -1) : (face == BlockFace.UP ? -1 : 1);
                }
                break;
            case EAST:
            case WEST:
                if (placer.getMode() == PlacerMode.HORIZONTAL) {
                    dx = placer.isInverted() ? (face == BlockFace.EAST ? 1 : -1) : (face == BlockFace.EAST ? -1 : 1);
                }
                break;
            case NORTH:
            case SOUTH:
                if (placer.getMode() == PlacerMode.HORIZONTAL) {
                    dz = placer.isInverted() ? (face == BlockFace.SOUTH ? 1 : -1) : (face == BlockFace.SOUTH ? -1 : 1);
                }
                break;
            default:
                break;
        }

        return location.add(dx, dy, dz);
    }

    private static ProcessResult getResultFromLocation(Location location) {
        boolean isAir = location.getBlock().getType() == Material.AIR;
        return new ProcessResult(isAir, location);
    }

    public static class ProcessResult {

        private final boolean isAir;
        private final Location location;

        public ProcessResult(boolean isAir, Location location) {
            this.isAir = isAir;
            this.location = location;
        }

        public boolean isAir() {
            return isAir;
        }

        public Location getLocation() {
            return location;
        }

    }


}
