package gg.steve.elemental.ce.api;

import gg.steve.elemental.ce.ElementalEnchants;
import gg.steve.elemental.ce.core.EnchantPlayer;
import gg.steve.elemental.ce.core.PlayerEnchantManager;
import org.bukkit.entity.Player;

public class EnchantsApi {

    public static ElementalEnchants getInstance() {
        return ElementalEnchants.get();
    }

    public static void openTokenEnchantsGui(Player player) {
        PlayerEnchantManager.getEnchantPlayer(player.getUniqueId()).openTokenEnchantsGui();
    }

    public static void openPrestigeEnchantsGui(Player player) {
        PlayerEnchantManager.getEnchantPlayer(player.getUniqueId()).openPrestigeEnchantsGui();
    }

    public static EnchantPlayer getEnchantPlayer(Player player) {
        return PlayerEnchantManager.getEnchantPlayer(player.getUniqueId());
    }
}
