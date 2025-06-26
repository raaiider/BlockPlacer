package me.raider.blockplacer.addon.def;

import me.raider.blockplacer.Utils;
import me.raider.blockplacer.addon.AbstractAddon;
import me.raider.blockplacer.addon.AddonContext;
import me.raider.blockplacer.addon.AddonPhase;
import me.raider.blockplacer.addon.NullExecution;
import org.bukkit.configuration.ConfigurationSection;

public class PlayerMessageAddon extends AbstractAddon<NullExecution> {

    private String message;

    public PlayerMessageAddon() {
        super("playerMessageAddon");
    }

    @Override
    public NullExecution execute(AddonContext context) {
        if (message != null) {
            context.getPlayer().sendMessage(message);
        }
        return null;
    }

    @Override
    public Class<NullExecution> getResultType() {
        return NullExecution.class;
    }

    @Override
    public AddonPhase getPhase() {
        return AddonPhase.START;
    }

    @Override
    public void configure(ConfigurationSection section) {
        String message = section.getString("message");
        if (message != null) {
            this.message = Utils.colorize(message);
        } else {
            this.message = "";
        }
    }


}
