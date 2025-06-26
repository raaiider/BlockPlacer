package me.raider.blockplacer.addon;

import org.bukkit.configuration.ConfigurationSection;

public interface Addon<T> {

    T execute(AddonContext context);

    Class<T> getResultType();

    AddonPhase getPhase();

    String getUniqueName();

    void configure(ConfigurationSection section);

}
