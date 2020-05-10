package gg.steve.elemental.ce;

import gg.steve.elemental.ce.core.EnchantManager;
import gg.steve.elemental.ce.core.PlayerEnchantManager;
import gg.steve.elemental.ce.managers.FileManager;
import gg.steve.elemental.ce.managers.SetupManager;
import gg.steve.elemental.ce.papi.ElementalEnchantsExpansion;
import gg.steve.elemental.ce.utils.LuckyBlockUtil;
import org.bukkit.Bukkit;
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
        PlayerEnchantManager.initialise();
        LuckyBlockUtil.initialise();
        // register placeholders with papi
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ElementalEnchantsExpansion(instance).register();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ElementalEnchants get() {
        return instance;
    }
}
