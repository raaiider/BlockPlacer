package me.raider.blockplacer.placer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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

    // boolean hasGravity();

}
