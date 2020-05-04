package gg.steve.elemental.ce;

import gg.steve.elemental.ce.core.EnchantManager;
import gg.steve.elemental.ce.managers.FileManager;
import gg.steve.elemental.ce.managers.SetupManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ElementalEnchants extends JavaPlugin {
    private static ElementalEnchants instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        SetupManager.setupFiles(new FileManager(instance));
        SetupManager.registerCommands(instance);
        SetupManager.registerEvents(instance);
        EnchantManager.loadEnchants();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ElementalEnchants get() {
        return instance;
    }
}
