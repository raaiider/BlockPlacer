package me.raider.blockplacer.listener;

import me.raider.blockplacer.BlockPlacerPlugin;
import me.raider.blockplacer.Utils;
import me.raider.blockplacer.placer.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class PlacerListener implements Listener {

    private final BlockPlacerPlugin plugin;
    private final PlacerManager placerManager;

    public PlacerListener(BlockPlacerPlugin plugin, PlacerManager placerManager) {
        this.plugin = plugin;
        this.placerManager = placerManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();

        ItemStack item = event.getItemInHand();
        Block placedBlock = event.getBlockPlaced();
        Block clickedBlock = event.getBlockAgainst();

        BlockFace face = placedBlock.getFace(clickedBlock);

        if (face == null) return;

        ItemMeta meta = item.getItemMeta();

        this.placerListener(meta, player, event, clickedBlock, face, PlacerTriggerType.PLACE);
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent event) {
        if (event.useItemInHand() == Event.Result.DENY || event.useInteractedBlock() == Event.Result.DENY) {
            return;
        }
        if (event.getHand() == null) {
            return;
        }
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();

        if (clickedBlock == null) return;

        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        BlockFace face = event.getBlockFace();

        this.placerListener(meta, player, event, clickedBlock, face, PlacerTriggerType.INTERACT);
    }

    private void placerListener(ItemMeta meta, Player player, Cancellable event, Block clickedBlock, BlockFace face, PlacerTriggerType triggerType) {
        if (meta != null && meta.getPersistentDataContainer().has(new NamespacedKey(plugin, "placerId"), PersistentDataType.STRING)) {
            String placerId = meta.getPersistentDataContainer().get(
                    new NamespacedKey(plugin, "placerId"),
                    PersistentDataType.STRING);

            Placer placer = placerManager.getPlacer(placerId);
            if (placer == null) {
                player.sendMessage(plugin.getConfigFile().getMessage("messages.no-placer-found", "No placer found!"));
                return;
            }

            if (placer.getTriggerType() != triggerType) {
                //Bukkit.getConsoleSender().sendMessage(triggerType.toString() + " tipoo");
                //Bukkit.getConsoleSender().sendMessage(placer.getTriggerType().toString() + " del placer");
                if (triggerType == PlacerTriggerType.INTERACT) {
                    return;
                }
                event.setCancelled(true);
                return;
            }

            List<Boolean> airBlocks = new ArrayList<>();
            Location actualLoc = clickedBlock.getLocation();
            Location clonedLoc = actualLoc.clone();

            for (int i = 0; i < placer.getMaxPlaced(); i++) {
                PlacerUtils.ProcessResult result;
                if (placer.getForcedFace() != BlockFace.SELF) {
                    if (i == 0) {
                        //Bukkit.getConsoleSender().sendMessage(face.toString());
                        result = PlacerUtils.processActualNextLocationForcedSync(clonedLoc, face);
                    } else {
                        result = PlacerUtils.processNextLocationForcedSync(placer, clonedLoc);
                    }
                } else {
                    result = PlacerUtils.processNextLocationSync(placer, clonedLoc, face);
                }

                if (result.getLocation().equals(clickedBlock.getLocation()) && i == 0) {
                    //Bukkit.getConsoleSender().sendMessage(clickedBlock.getLocation().toString());
                    player.sendMessage(plugin.getConfigFile().getMessage("messages.wrong-orientation", "Placer set in wrong orientation!"));
                    event.setCancelled(true);
                    return;
                }
                if (i == 0) {
                    airBlocks.add(true);
                } else {
                    airBlocks.add(result.isAir());
                }
                clonedLoc = result.getLocation();
            }
            /*
            for (Boolean b : airBlocks) {
                Bukkit.getConsoleSender().sendMessage(b + " blocks");
            }*/
            PlacerInstance instance = placerManager.addPlacerInstance(actualLoc.clone(), face, placerId, airBlocks, player);
            if (instance != null) {
                placerManager.executeStartConditions(instance, player);
                event.setCancelled(true);
            }
        }
    }

}
