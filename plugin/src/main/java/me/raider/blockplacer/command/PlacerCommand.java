package me.raider.blockplacer.command;

import me.raider.blockplacer.BlockPlacerPlugin;
import me.raider.blockplacer.placer.ConfigurablePlacer;
import me.raider.blockplacer.placer.Placer;
import me.raider.blockplacer.placer.PlacerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.CommandPlaceholder;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.Map;

@Command("placer")
public class PlacerCommand {

    private final BlockPlacerPlugin plugin;
    private final PlacerManager placerManager;

    public PlacerCommand(BlockPlacerPlugin plugin, PlacerManager placerManager) {
        this.plugin = plugin;
        this.placerManager = placerManager;
    }

    @CommandPlaceholder
    @CommandPermission("blockplacer.admin")
    public void placerDefault(BukkitCommandActor actor) {
        Player player = actor.requirePlayer();
        help(player);
    }

    @Subcommand("help")
    @CommandPermission("blockplacer.admin")
    public void help(BukkitCommandActor actor) {
        Player player = actor.requirePlayer();
        help(player);
    }

    @Subcommand("give")
    @CommandPermission("blockplacer.admin")
    public void give(BukkitCommandActor actor, Player player, Placer placer) {
        CommandSender sender = actor.sender();

        ItemStack item = placer.getItem();
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.getPersistentDataContainer().set(
                    new NamespacedKey(plugin, "placerId"),
                    PersistentDataType.STRING,
                    placer.getUniqueName()
            );
            item.setItemMeta(meta); // ← Don't forget this!
        }

        Map<Integer, ItemStack> leftovers = player.getInventory().addItem(item);

        for (ItemStack leftover : leftovers.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), leftover);
        }
        sender.sendMessage(plugin.getConfigFile().getMessage("messages.gave-placer", "You gave a placer!"));
    }

    @Subcommand("reload")
    @CommandPermission("blockplacer.admin")
    public void reload(BukkitCommandActor actor) {
        CommandSender sender = actor.sender();
        placerManager.reloadPlacers();
        sender.sendMessage(plugin.getConfigFile().getMessage("messages.reload-plugin", "You reloaded the plugin!"));
    }

    @Subcommand("setitem")
    @CommandPermission("blockplacer.admin")
    public void setItem(BukkitCommandActor actor, Placer placer) {
        Player player = actor.requirePlayer();
        if (placer instanceof ConfigurablePlacer configurablePlacer) {
            if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                configurablePlacer.setItem(player.getInventory().getItemInMainHand());
                player.sendMessage(plugin.getConfigFile().getMessage("messages.valid-item", "New item set!"));
            } else {
                player.sendMessage(plugin.getConfigFile().getMessage("messages.invalid-item", "You need to hold a valid item!"));
            }
        }
    }

    private void help(Player sender) {
        sender.sendMessage(ChatColor.GOLD + "BlockPlacer");
    }

}
