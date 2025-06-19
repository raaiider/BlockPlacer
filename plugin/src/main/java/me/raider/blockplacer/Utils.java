package me.raider.blockplacer;

import me.raider.blockplacer.placer.Placer;
import me.raider.blockplacer.placer.PlacerMode;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

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


    public static ProcessResult processNextLocationSync(Placer attachedPlacer, Location clonedLoc, BlockFace face) {
        if (face == BlockFace.UP) {
            int inverted = attachedPlacer.isInverted() ? 1 : -1;
            if (attachedPlacer.getMode() == PlacerMode.VERTICAL) {
                return getResultFromLocation(clonedLoc.add(0, inverted, 0));
            } else {
                return getResultFromLocation(clonedLoc);
            }
        } else if (face == BlockFace.DOWN) {
            int inverted = attachedPlacer.isInverted() ? -1 : 1;
            if (attachedPlacer.getMode() == PlacerMode.VERTICAL) {
                return getResultFromLocation(clonedLoc.add(0, inverted, 0));
            } else {
                return getResultFromLocation(clonedLoc);
            }
        } else if (face == BlockFace.WEST) {
            int inverted = attachedPlacer.isInverted() ? -1 : 1;
            if (attachedPlacer.getMode() == PlacerMode.HORIZONTAL) {
                return getResultFromLocation(clonedLoc.add(inverted, 0, 0));
            } else {
                return getResultFromLocation(clonedLoc);
            }
        } else if (face == BlockFace.EAST) {
            int inverted = attachedPlacer.isInverted() ? 1 : -1;
            if (attachedPlacer.getMode() == PlacerMode.HORIZONTAL) {
                return getResultFromLocation(clonedLoc.add(inverted, 0, 0));
            } else {
                return getResultFromLocation(clonedLoc);
            }
        } else if (face == BlockFace.NORTH) {
            int inverted = attachedPlacer.isInverted() ? -1 : 1;
            if (attachedPlacer.getMode() == PlacerMode.HORIZONTAL) {
                return getResultFromLocation(clonedLoc.add(0, 0, inverted));
            } else {
                return getResultFromLocation(clonedLoc);
            }
        } else if (face == BlockFace.SOUTH) {
            int inverted = attachedPlacer.isInverted() ? 1 : -1;
            if (attachedPlacer.getMode() == PlacerMode.HORIZONTAL) {
                return getResultFromLocation(clonedLoc.add(0, 0, inverted));
            } else {
                return getResultFromLocation(clonedLoc);
            }
        }
        return getResultFromLocation(clonedLoc);
    }

    public static Location processNextLocationAsync(Placer attachedPlacer, Location clonedLoc, BlockFace face) {
        if (face == BlockFace.UP) {
            int inverted = attachedPlacer.isInverted() ? 1 : -1;
            if (attachedPlacer.getMode() == PlacerMode.VERTICAL) {
                return clonedLoc.add(0, inverted, 0);
            } else {
                return clonedLoc;
            }
        } else if (face == BlockFace.DOWN) {
            int inverted = attachedPlacer.isInverted() ? -1 : 1;
            if (attachedPlacer.getMode() == PlacerMode.VERTICAL) {
                return clonedLoc.add(0, inverted, 0);
            } else {
                return clonedLoc;
            }
        } else if (face == BlockFace.WEST) {
            int inverted = attachedPlacer.isInverted() ? -1 : 1;
            if (attachedPlacer.getMode() == PlacerMode.HORIZONTAL) {
                return clonedLoc.add(inverted, 0, 0);
            } else {
                return clonedLoc;
            }
        } else if (face == BlockFace.EAST) {
            int inverted = attachedPlacer.isInverted() ? 1 : -1;
            if (attachedPlacer.getMode() == PlacerMode.HORIZONTAL) {
                return clonedLoc.add(inverted, 0, 0);
            } else {
                return clonedLoc;
            }
        } else if (face == BlockFace.NORTH) {
            int inverted = attachedPlacer.isInverted() ? -1 : 1;
            if (attachedPlacer.getMode() == PlacerMode.HORIZONTAL) {
                return clonedLoc.add(0, 0, inverted);
            } else {
                return clonedLoc;
            }
        } else if (face == BlockFace.SOUTH) {
            int inverted = attachedPlacer.isInverted() ? 1 : -1;
            if (attachedPlacer.getMode() == PlacerMode.HORIZONTAL) {
                return clonedLoc.add(0, 0, inverted);
            } else {
                return clonedLoc;
            }
        }
        return clonedLoc;
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
