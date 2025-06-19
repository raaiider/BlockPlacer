package me.raider.blockplacer.file;

import me.raider.blockplacer.BlockPlacerPlugin;
import me.raider.blockplacer.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ConfigFile extends YmlFile{

    private final BlockPlacerPlugin plugin;

    public ConfigFile(BlockPlacerPlugin plugin) {
        super(plugin, "config.yml");
        this.plugin = plugin;
    }


    public String getMessage(String path, String defaultMessage) {
        String message = this.getString(path);
        if (message == null) {
            this.plugin.getLogger().log(Level.WARNING, path + " not found, using default message.");
            return defaultMessage;
        }
        return getColorizedString(path);
    }

    public String getColorizedString(String path) {
        return Utils.colorize(super.getString(path));
    }

    public List<String> getColorizedStringList(String path) {

        List<String> newList = new ArrayList<>();

        for (String s : super.getStringList(path)) {
            newList.add(Utils.colorize(s));
        }
        return newList;
    }
}
