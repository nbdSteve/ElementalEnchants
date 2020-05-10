package gg.steve.elemental.ce.papi;

import gg.steve.elemental.ce.ElementalEnchants;
import gg.steve.elemental.ce.core.Enchant;
import gg.steve.elemental.ce.core.EnchantPlayer;
import gg.steve.elemental.ce.core.PlayerEnchantManager;
import gg.steve.elemental.tokens.ElementalTokens;
import gg.steve.elemental.tokens.core.PlayerTokenManager;
import gg.steve.elemental.tokens.core.TokenPlayer;
import gg.steve.elemental.tokens.core.TokenType;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class ElementalEnchantsExpansion extends PlaceholderExpansion {
    private ElementalEnchants instance;

    public ElementalEnchantsExpansion(ElementalEnchants instance) {
        this.instance = instance;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return instance.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "elemental-enchants";
    }

    @Override
    public String getVersion() {
        return instance.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) return "";
        EnchantPlayer ePlayer = PlayerEnchantManager.getEnchantPlayer(player.getUniqueId());
        if (identifier.equalsIgnoreCase("token")) {
            int count = 0;
            for(Enchant enchant : ePlayer.getActiveEnchants().keySet()) {
                if (enchant.getUpgradeType().equals(TokenType.TOKEN)) count++;
            }
            return ElementalTokens.getNumberFormat().format(count);
        }
        if (identifier.equalsIgnoreCase("prestige")) {
            int count = 0;
            for(Enchant enchant : ePlayer.getActiveEnchants().keySet()) {
                if (enchant.getUpgradeType().equals(TokenType.PRESTIGE)) count++;
            }
            return ElementalTokens.getNumberFormat().format(count);
        }
        return "0";
    }
}
