package gg.steve.elemental.ce.gui;

import gg.steve.elemental.ce.core.Enchant;
import gg.steve.elemental.ce.core.EnchantManager;
import gg.steve.elemental.ce.core.EnchantPlayer;
import gg.steve.elemental.ce.message.MessageType;
import gg.steve.elemental.ce.nbt.NBTItem;
import gg.steve.elemental.ce.utils.GuiItemUtil;
import gg.steve.elemental.tokens.ElementalTokens;
import gg.steve.elemental.tokens.api.TokensApi;
import gg.steve.elemental.tokens.core.TokenPlayer;
import org.bukkit.configuration.file.YamlConfiguration;

public class PrestigeEnchantsGui extends AbstractGui {
    private EnchantPlayer ePlayer;
    private YamlConfiguration config;

    /**
     * Constructor the create a new Gui
     *
     * @param config
     */
    public PrestigeEnchantsGui(YamlConfiguration config, EnchantPlayer ePlayer) {
        super(config, config.getString("type"), config.getInt("size"));
        this.config = config;
        refresh(ePlayer);
    }

    public void refresh(EnchantPlayer ePlayer) {
        this.ePlayer = ePlayer;
        for (String entry : config.getKeys(false)) {
            try {
                Integer.parseInt(entry);
            } catch (Exception e) {
                continue;
            }
            setItemInSlot(config.getInt(entry + ".slot"), GuiItemUtil.createItem(config, entry, config.getString(entry + ".action"), ePlayer), player -> {
                switch (config.getString(entry + ".action")) {
                    case "none":
                        break;
                    case "close":
                        player.closeInventory();
                        break;
                    default:
                        doEnchantmentUpgrade(EnchantManager.getEnchant(config.getString(entry + ".action")));
                        break;
                }
            });
        }
    }

    public void doEnchantmentUpgrade(Enchant enchant) {
        if (this.ePlayer.getEnchantLevel(enchant) >= enchant.getMaxLevel()) {
            this.ePlayer.getPlayer().closeInventory();
            MessageType.ENCHANT_MAX_LEVEL.message(this.ePlayer.getPlayer());
            return;
        }
        if (!EnchantManager.isEnchantable(ePlayer.getPlayer().getItemInHand())) {
            this.ePlayer.getPlayer().closeInventory();
            MessageType.UNENCHANTABLE_ITEM.message(this.ePlayer.getPlayer());
            return;
        }
        // check that player has enough money first
        if (TokensApi.getInstance() != null) {
            TokenPlayer tokenPlayer = TokensApi.getTokenPlayer(this.ePlayer.getPlayerId());
            if (tokenPlayer.getTokens(enchant.getUpgradeType()) < enchant.getUpgradePrice()) {
                MessageType.INSUFFICIENT_TOKENS.message(this.ePlayer.getPlayer(), enchant.getUpgradeType().name());
                this.ePlayer.getPlayer().closeInventory();
                return;
            } else {
                tokenPlayer.removeTokens(enchant.getUpgradeType(), enchant.getUpgradePrice());
            }
        }
        EnchantManager.applyEnchant(this.ePlayer.getPlayer(), new NBTItem(this.ePlayer.getPlayer().getItemInHand()), enchant, this.ePlayer.getEnchantLevel(enchant) + 1);
        this.ePlayer.openPrestigeEnchantsGui();
        MessageType.UPGRADE_SUCCESS.message(this.ePlayer.getPlayer(), enchant.getName(), ElementalTokens.getNumberFormat().format(this.ePlayer.getEnchantLevel(enchant)), ElementalTokens.getNumberFormat().format(enchant.getMaxLevel()));
    }
}
