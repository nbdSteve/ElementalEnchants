package gg.steve.elemental.ce.managers;

import gg.steve.elemental.ce.ElementalEnchants;
import gg.steve.elemental.ce.cmd.EeaCmd;
import gg.steve.elemental.ce.cmd.PeCmd;
import gg.steve.elemental.ce.cmd.TeCmd;
import gg.steve.elemental.ce.core.PlayerEnchantManager;
import gg.steve.elemental.ce.data.types.ExplosiveEnchantData;
import gg.steve.elemental.ce.gui.GuiClickListener;
import gg.steve.elemental.ce.listener.EnchantProcListener;
import gg.steve.elemental.ce.listener.HoldPickaxeListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * Class that handles setting up the plugin on start
 */
public class SetupManager {

    private SetupManager() throws IllegalAccessException {
        throw new IllegalAccessException("Manager class cannot be instantiated.");
    }

    /**
     * Loads the files into the file manager
     *
     * @param fileManager FileManager, the plugins file manager
     */
    public static void setupFiles(FileManager fileManager) {
        // general files
        for (Files file : Files.values()) {
            file.load(fileManager);
        }
    }

    public static void registerCommands(ElementalEnchants instance) {
        instance.getCommand("eea").setExecutor(new EeaCmd());
        instance.getCommand("te").setExecutor(new TeCmd());
        instance.getCommand("pe").setExecutor(new PeCmd());
    }

    /**
     * Register all of the events for the plugin
     *
     * @param instance Plugin, the main plugin instance
     */
    public static void registerEvents(Plugin instance) {
        PluginManager pm = instance.getServer().getPluginManager();
        pm.registerEvents(new PlayerEnchantManager(), instance);
        pm.registerEvents(new HoldPickaxeListener(), instance);
        pm.registerEvents(new GuiClickListener(), instance);
        pm.registerEvents(new EnchantProcListener(), instance);
        pm.registerEvents(new ExplosiveEnchantData(), instance);
    }
}
