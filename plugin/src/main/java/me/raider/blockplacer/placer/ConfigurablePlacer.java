package me.raider.blockplacer.placer;

import me.raider.blockplacer.addon.Addon;
import me.raider.blockplacer.addon.AddonManager;
import me.raider.blockplacer.addon.AddonPhase;
import me.raider.blockplacer.file.YmlFile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurablePlacer implements Placer {

    private final String placerName;
    private final Material material;
    private ItemStack item;
    private PlacerMode mode;
    private final boolean inverted;
    private final int waitTicks;
    private final int maxPlaced;
    private final PlacerTriggerType triggerType;
    private final boolean onlyInAir;
    private final boolean beforeBlocks;
    private final YmlFile ymlFile;
    private final BlockFace forcedFace;
    private final Map<AddonPhase, List<Addon<?>>> addons;

    public ConfigurablePlacer(YmlFile placerFile, AddonManager addonManager, String placerUniqueName) {
        this.ymlFile = placerFile;
        this.placerName = placerUniqueName;
        String materialStr = placerFile.getString("placers." + placerUniqueName + ".material");
        if (materialStr == null) {
            this.material = Material.STONE;
        } else {
            this.material = Material.getMaterial(materialStr);
        }

        ItemStack defaultItem = new ItemStack(Material.STONE);
        ItemMeta meta = defaultItem.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("Default Item", NamedTextColor.GRAY));
            defaultItem.setItemMeta(meta);
        }

        this.item = placerFile.getItemStack("placers." + placerUniqueName + ".item", defaultItem);

        String modeStr = placerFile.getString("placers." + placerUniqueName + ".mode");
        if (modeStr != null) {
            this.mode = PlacerMode.valueOf(modeStr);
        } else {
            this.mode = PlacerMode.VERTICAL;
        }

        String triggerStr = placerFile.getString("placers." + placerUniqueName + ".trigger");
        if (triggerStr != null) {
            this.triggerType = PlacerTriggerType.valueOf(triggerStr);
        } else {
            this.triggerType = PlacerTriggerType.PLACE;
        }

        String faceStr = placerFile.getString("placers." + placerUniqueName + ".forcedFace");
        if (faceStr != null) {
            this.forcedFace = BlockFace.valueOf(faceStr);
        } else {
            this.forcedFace = BlockFace.SELF;
        }
        this.inverted = placerFile.getBoolean("placers." + placerUniqueName + ".inverted");
        this.onlyInAir = placerFile.getBoolean("placers." + placerUniqueName + ".onlyInAir");
        this.beforeBlocks = placerFile.getBoolean("placers." + placerUniqueName + ".beforeBlocks");
        this.maxPlaced = placerFile.getInt("placers." + placerUniqueName + ".maxPlaced");
        this.waitTicks = placerFile.getInt("placers." + placerUniqueName + ".waitTicks");

        this.addons = new HashMap<>();

        this.addons.put(AddonPhase.START, new ArrayList<>());
        this.addons.put(AddonPhase.ON_PLACE, new ArrayList<>());
        this.addons.put(AddonPhase.STOP, new ArrayList<>());

        ConfigurationSection addonSection = placerFile.getConfigurationSection("placers." + placerUniqueName + ".addons");
        if (addonSection != null) {
            for (String str : addonSection.getKeys(false)) {
                String addonName = placerFile.getString("placers." + placerUniqueName + ".addons." + str + ".name");
                if (addonName == null) {
                    continue;
                }
                Addon<?> addon = addonManager.getAddons().get(addonName);
                if (addon == null) {
                    continue;
                }
                addon.configure(placerFile.getConfigurationSection("placers." + placerUniqueName + ".addons." + str));
                this.addons.get(addon.getPhase()).add(addon);
            }
        }
    }


    @Override
    public String getUniqueName() {
        return placerName;
    }

    @Override
    public Material getBlockToPlace() {
        return material;
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public PlacerMode getMode() {
        return mode;
    }

    @Override
    public boolean isInverted() {
        return inverted;
    }

    @Override
    public int getWaitTicks() {
        return this.waitTicks;
    }

    @Override
    public int getMaxPlaced() {
        return this.maxPlaced;
    }

    @Override
    public PlacerTriggerType getTriggerType() {
        return this.triggerType;
    }

    @Override
    public boolean isOnlyInAir() {
        return this.onlyInAir;
    }

    @Override
    public boolean isBeforeBlocks() {
        return this.beforeBlocks;
    }

    @Override
    public BlockFace getForcedFace() {
        return this.forcedFace;
    }

    @Override
    public Map<AddonPhase, List<Addon<?>>> getAddons() {
        return this.addons;
    }

    public void setItem(ItemStack item) {
        this.item = item;
        ymlFile.set("placers." + placerName + ".item", item);
        ymlFile.save();
    }

    /*
    @Override
    public boolean hasGravity() {
        return false;
    }
     */
}
