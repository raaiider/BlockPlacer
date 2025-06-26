package me.raider.blockplacer.addon.def;

import me.raider.blockplacer.BlockPlacerPlugin;
import me.raider.blockplacer.Utils;
import me.raider.blockplacer.addon.AbstractAddon;
import me.raider.blockplacer.addon.AddonContext;
import me.raider.blockplacer.addon.AddonPhase;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PlayerMoneyConditionAddon extends AbstractAddon<Boolean> {

    private final Economy economy;
    private final BlockPlacerPlugin plugin;

    private double charge;
    private String successMessage;
    private String failMessage;

    public PlayerMoneyConditionAddon(BlockPlacerPlugin plugin) {
        super("playerChargeAddon");
        this.economy = plugin.getEconomy();
        this.plugin = plugin;
    }

    @Override
    public Boolean execute(AddonContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return false;
        }

        if (economy == null) {
            plugin.getLogger().warning("You don't economy enabled with vault and an user placed used a placer with an addon" +
                    " related to economy");
            return false;
        }

        if (!economy.has(player, charge)) {
            player.sendMessage(failMessage);
            return false;
        }

        EconomyResponse response = economy.withdrawPlayer(player, charge);
        if (response.transactionSuccess()) {
            player.sendMessage(successMessage);
            return true;
        }
        player.sendMessage(failMessage);
        return false;
    }

    @Override
    public Class<Boolean> getResultType() {
        return Boolean.class;
    }

    @Override
    public AddonPhase getPhase() {
        return AddonPhase.START;
    }

    @Override
    public void configure(ConfigurationSection section) {
        String successMessage = section.getString("successMessage");
        if (successMessage != null) {
            this.successMessage = Utils.colorize(successMessage);
        } else {
            this.successMessage = "";
        }
        String failMessage = section.getString("failMessage");
        if (failMessage != null) {
            this.failMessage = Utils.colorize(failMessage);
        } else {
            this.failMessage = "";
        }

        this.charge = section.getDouble("charge");
    }
}
