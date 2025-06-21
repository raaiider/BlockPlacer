package me.raider.blockplacer.placer;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public interface Placer {

    String getUniqueName();

    Material getBlockToPlace();

    ItemStack getItem();

    PlacerMode getMode();

    void setMode(PlacerMode mode);

    boolean isInverted();

    int getWaitTicks();

    int getMaxPlaced();

    PlacerTriggerType getTriggerType();

    boolean isOnlyInAir();

    boolean isBeforeBlocks();

    BlockFace getForcedFace();

    // boolean hasGravity();

}
