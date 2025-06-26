package me.raider.blockplacer.addon;

import me.raider.blockplacer.placer.PlacerInstance;
import org.bukkit.entity.Player;

public class AddonContext {

    private final Player player;
    private final PlacerInstance instance;

    public AddonContext(Player player, PlacerInstance instance) {
        this.player = player;
        this.instance = instance;
    }

    public Player getPlayer() {
        return player;
    }

    public PlacerInstance getInstance() {
        return instance;
    }

}
