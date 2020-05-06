package gg.steve.elemental.ce.core;

import gg.steve.elemental.ce.gui.PrestigeEnchantsGui;
import gg.steve.elemental.ce.gui.TokenEnchantsGui;
import gg.steve.elemental.ce.managers.Files;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnchantPlayer {
    private UUID playerId;
    private Map<Enchant, Integer> activeEnchants;
    private TokenEnchantsGui tokenEnchantsGui;
    private PrestigeEnchantsGui prestigeEnchantsGui;

    public EnchantPlayer(UUID playerId) {
        this.playerId = playerId;
        this.activeEnchants = new HashMap<>();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.playerId);
    }

    public void addEnchant(Enchant enchant, int enchantLevel) {
        this.activeEnchants.put(enchant, enchantLevel);
        enchant.onHold(getPlayer(), enchantLevel);
    }

    public void removeEnchant(Enchant enchant) {
        if (!this.activeEnchants.containsKey(enchant)) return;
        enchant.remove(this.getPlayer(), this.getEnchantLevel(enchant));
        this.activeEnchants.remove(enchant);
    }

    public boolean isEnchantActive(Enchant enchant) {
        return this.activeEnchants.containsKey(enchant);
    }

    public int getEnchantLevel(Enchant enchant) {
        if (!this.activeEnchants.containsKey(enchant)) return 0;
        return this.activeEnchants.get(enchant);
    }

    public void removeAllEnchants() {
        for (Enchant enchant : this.activeEnchants.keySet()) {
            enchant.remove(getPlayer(), this.activeEnchants.get(enchant));
        }
    }

    public Map<Enchant, Integer> getActiveEnchants() {
        return activeEnchants;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void openTokenEnchantsGui() {
        if (this.tokenEnchantsGui == null) {
            this.tokenEnchantsGui = new TokenEnchantsGui(Files.TOKEN_GUI.get(), this);
        } else {
            this.tokenEnchantsGui.refresh(this);
        }
        this.tokenEnchantsGui.open(getPlayer());
    }

    public void openPrestigeEnchantsGui() {
        if (this.prestigeEnchantsGui == null) {
            this.prestigeEnchantsGui = new PrestigeEnchantsGui(Files.PRESTIGE_GUI.get(), this);
        } else {
            this.prestigeEnchantsGui.refresh(this);
        }
        this.prestigeEnchantsGui.open(getPlayer());
    }
}
