package me.raider.blockplacer;

import me.raider.blockplacer.command.PlacerCommand;
import me.raider.blockplacer.command.PlacerParameterType;
import me.raider.blockplacer.file.ConfigFile;
import me.raider.blockplacer.file.YmlFile;
import me.raider.blockplacer.listener.PlacerListener;
import me.raider.blockplacer.placer.Placer;
import me.raider.blockplacer.placer.PlacerManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;


public class BlockPlacerPlugin extends JavaPlugin {

    private Economy economy = null;
    private ConfigFile configFile;
    private PlacerManager placerManager;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().warning("You don't have vault installed!");
        } else {
            getLogger().info("Vault hooked successfully!");
        }

        this.configFile = new ConfigFile(this);
        YmlFile placersFile = new YmlFile(this, "placers");
        this.placerManager = new PlacerManager(placersFile, this);

        // command registration
        Lamp<BukkitCommandActor> lamp = BukkitLamp.builder(this).parameterTypes(builder -> {
            builder.addParameterType(Placer.class, new PlacerParameterType(placerManager));
        }).build();
        lamp.register(new PlacerCommand(this, placerManager));

        placerManager.loadPlacers();
        placerManager.startTasks();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlacerListener(this, placerManager), this);
    }

    @Override
    public void onDisable() {
        placerManager.clearManager();
    }


    public ConfigFile getConfigFile() {
        return configFile;
    }

    public Economy getEconomy() {
        return economy;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer()
                .getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return true;
    }

}
