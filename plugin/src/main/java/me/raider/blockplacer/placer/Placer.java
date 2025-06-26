package me.raider.blockplacer.placer;

import me.raider.blockplacer.addon.Addon;
import me.raider.blockplacer.addon.AddonPhase;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public interface Placer {

    String getUniqueName();

    Material getBlockToPlace();

    ItemStack getItem();

    PlacerMode getMode();

    boolean isInverted();

    int getWaitTicks();

    int getMaxPlaced();

    PlacerTriggerType getTriggerType();

    boolean isOnlyInAir();

    boolean isBeforeBlocks();

    BlockFace getForcedFace();

    Map<AddonPhase, List<Addon<?>>> getAddons();

    // boolean hasGravity();

}
