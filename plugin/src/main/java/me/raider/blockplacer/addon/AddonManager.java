package me.raider.blockplacer.addon;

import me.raider.blockplacer.BlockPlacerPlugin;
import me.raider.blockplacer.addon.def.PlayerMessageAddon;
import me.raider.blockplacer.addon.def.PlayerMoneyConditionAddon;
import me.raider.blockplacer.placer.Placer;
import me.raider.blockplacer.placer.PlacerInstance;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddonManager {

    private final Map<String, Addon<?>> addons;
    private final BlockPlacerPlugin plugin;

    public AddonManager(BlockPlacerPlugin plugin) {
        this.addons = new HashMap<>();
        this.plugin = plugin;
        createDefaultAddons();
    }

    public Map<String, Addon<?>> getAddons() {
        return addons;
    }

    public AddonExecutionResult executeAddonInPlacer(AddonPhase phase, PlacerInstance instance, Player player) {
        List<Addon<?>> placerAddons = instance.getAttachedPlacer().getAddons().get(phase);
        AddonExecutionResult addonResult = new AddonExecutionResult();
        for (Addon<?> addon : placerAddons) {
            if (addon.getResultType() == NullExecution.class) {
                addon.execute(new AddonContext(player, instance));
            } else if (addon.getResultType() == Boolean.class) {
                addonResult.getBooleanResults().add((Boolean) addon.execute(new AddonContext(player, instance)));
            }
        }
        return addonResult;
    }

    private void createDefaultAddons() {
        Addon<NullExecution> playerMsg = new PlayerMessageAddon();
        Addon<Boolean> playerCharge = new PlayerMoneyConditionAddon(plugin);

        this.addons.put(playerMsg.getUniqueName(), playerMsg);
        this.addons.put(playerCharge.getUniqueName(), playerCharge);
    }
}
