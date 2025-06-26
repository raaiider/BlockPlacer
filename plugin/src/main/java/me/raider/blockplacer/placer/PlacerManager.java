package me.raider.blockplacer.placer;

import me.raider.blockplacer.BlockPlacerPlugin;
import me.raider.blockplacer.addon.*;
import me.raider.blockplacer.file.YmlFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PlacerManager {

    private final ConcurrentLinkedQueue<ProcessedPlace> queue = new ConcurrentLinkedQueue<>();

    private final Map<String, Placer> placers = new HashMap<>();
    private final Set<PlacerInstance> instances = new HashSet<>();

    private final AddonManager addonManager;

    private final YmlFile placersFile;
    private final BlockPlacerPlugin plugin;

    private BukkitTask asyncTask;
    private BukkitTask syncTask;

    public PlacerManager(YmlFile placersFile, BlockPlacerPlugin plugin) {
        this.addonManager = new AddonManager(plugin);
        this.placersFile = placersFile;
        this.plugin = plugin;
    }

    public void loadPlacers() {
        ConfigurationSection section = placersFile.getConfigurationSection("placers");
        if (section == null) {
            plugin.getLogger().warning("No placers section found in " + placersFile.getName() + " please check the config.");
            return;
        }

        for (String key : section.getKeys(false)) {
            placers.put(key, new ConfigurablePlacer(this.placersFile, this.addonManager, key));
        }
    }

    public void clearManager() {
        asyncTask.cancel();
        syncTask.cancel();
        queue.clear();
        placers.clear();
        instances.clear();
    }

    public void reloadPlacers() {
        clearManager();
        placersFile.reload();
        loadPlacers();
        startTasks();
    }

    public PlacerInstance addPlacerInstance(Location location, BlockFace face, String placerId, List<Boolean> airBlocks, Player player) {
        if (location == null || placerId == null) {
            plugin.getLogger().warning("Tried to add an invalid place");
            return null;
        }
        Placer placer = placers.get(placerId);
        if (placer == null) {
            plugin.getLogger().warning("The placer with name " + placerId + " was not found, please check the config.");
            return null;
        }
        PlacerInstance instance = new PlacerInstance(placer, location, face, airBlocks, player);
        this.instances.add(instance);
        return instance;
    }

    public void executeStartConditions(PlacerInstance instance, Player player) {
        AddonExecutionResult result = addonManager.executeAddonInPlacer(AddonPhase.START, instance, player);
        for (Boolean res : result.getBooleanResults()) {
            if (!res) {
                instances.remove(instance);
                return;
            }
        }
        for (Addon<NullExecution> voidRes : result.getVoidReturn()) {
            voidRes.execute(new AddonContext(player, instance));
        }
    }

    public Set<PlacerInstance> getInstances() {
        return this.instances;
    }

    public ConcurrentLinkedQueue<ProcessedPlace> getQueue() {
        return this.queue;
    }

    public Placer getPlacer(String placerId) {
        return this.placers.get(placerId);
    }

    public void startTasks() {
        this.asyncTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new PlacerLogicAsyncTask(this), 0L, 1L);
        this.syncTask = Bukkit.getScheduler().runTaskTimer(plugin, new ProcessedPlaceSyncTask(this), 0L, 1L);
    }

}
