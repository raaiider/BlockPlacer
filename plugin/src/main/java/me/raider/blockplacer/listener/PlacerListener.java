package me.raider.blockplacer.listener;

import me.raider.blockplacer.BlockPlacerPlugin;
import me.raider.blockplacer.Utils;
import me.raider.blockplacer.placer.Placer;
import me.raider.blockplacer.placer.PlacerManager;
import me.raider.blockplacer.placer.PlacerMode;
import me.raider.blockplacer.placer.PlacerTriggerType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
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

        ItemMeta meta = item.getItemMeta();

        if (meta != null && meta.getPersistentDataContainer().has(new NamespacedKey(plugin, "placerId"), PersistentDataType.STRING)) {
            String placerId = meta.getPersistentDataContainer().get(
                    new NamespacedKey(plugin, "placerId"),
                    PersistentDataType.STRING);

            Placer placer = placerManager.getPlacer(placerId);
            if (placer == null) {
                player.sendMessage(plugin.getConfigFile().getMessage("messages.no-placer-found", "No placer found!"));
                return;
            }

            if (placer.getTriggerType() != PlacerTriggerType.PLACE) {
                player.sendMessage(plugin.getConfigFile().getMessage("messages.wrong-usage", "Can't use this by placing it!"));
                event.setCancelled(true);
                return;
            }

            List<Boolean> airBlocks = new ArrayList<>();
            Location actualLoc = clickedBlock.getLocation();
            Location clonedLoc = actualLoc.clone();

            for (int i = 0 ; i < placer.getMaxPlaced() ; i++) {
                Utils.ProcessResult result;
                if (placer.getForcedFace() != BlockFace.SELF) {
                    if (i == 0) {
                        Bukkit.getConsoleSender().sendMessage(face.toString());
                        result = Utils.processNextLocationForcedSync(clonedLoc, face);
                    } else {
                        result = Utils.processNextLocationForcedSync(placer, clonedLoc);
                    }
                } else {
                    result = Utils.processNextLocationSync(placer, clonedLoc, face);
                }

                if (result.getLocation().equals(clickedBlock.getLocation()) && i == 0) {
                    Bukkit.getConsoleSender().sendMessage(clickedBlock.getLocation().toString());
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
            for (Boolean b : airBlocks) {
                Bukkit.getConsoleSender().sendMessage(b + " blocks");
            }

            placerManager.addPlacerInstance(actualLoc.clone(), face, placerId, airBlocks);
            player.sendMessage(plugin.getConfigFile().getMessage("messages.placed-placer", "You placed a placer!"));
            event.setCancelled(true);
        }

    }

}
