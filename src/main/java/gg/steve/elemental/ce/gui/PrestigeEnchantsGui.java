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
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        for (int i = 0; i < config.getInt("size"); i++) {
            if (i % 2 == 0) {
                setItemInSlot(i, getFillerGlass((byte) 10), (player, click) -> {
                });
            } else {
                setItemInSlot(i, getFillerGlass((byte) 0), (player, click) -> {
                });
            }
        }
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
            setItemInSlot(config.getInt(entry + ".slot"), GuiItemUtil.createItem(config, entry, config.getString(entry + ".action"), ePlayer), (player, click) -> {
                switch (config.getString(entry + ".action")) {
                    case "none":
                        break;
                    case "close":
                        player.closeInventory();
                        break;
                    case "back":
                        player.closeInventory();
                        ElementalTokens.openShopGui(player);
                        break;
                    default:
                        if (click.equals(ClickType.RIGHT)) {
                            doEnchantmentUpgrade(EnchantManager.getEnchant(config.getString(entry + ".action")), 10);
                        } else if (click.equals(ClickType.MIDDLE)) {
                            doEnchantmentUpgrade(EnchantManager.getEnchant(config.getString(entry + ".action")), 100);
                        } else {
                            doEnchantmentUpgrade(EnchantManager.getEnchant(config.getString(entry + ".action")), 1);
                        }
                        break;
                }
            });
        }
    }

    public void doEnchantmentUpgrade(Enchant enchant, int increase) {
        if ((this.ePlayer.getEnchantLevel(enchant) + increase) > enchant.getMaxLevel()) {
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
            if (tokenPlayer.getTokens(enchant.getUpgradeType()) < (enchant.getUpgradePrice() * increase)) {
                MessageType.INSUFFICIENT_TOKENS.message(this.ePlayer.getPlayer(), enchant.getUpgradeType().name());
                this.ePlayer.getPlayer().closeInventory();
                return;
            } else {
                tokenPlayer.removeTokens(enchant.getUpgradeType(), enchant.getUpgradePrice() * increase);
            }
        }
        if (enchant.getData().getVanillaEnchantment() != null) {
            EnchantManager.applyVanilla(this.ePlayer.getPlayer(), new NBTItem(this.ePlayer.getPlayer().getItemInHand()), enchant, this.ePlayer.getEnchantLevel(enchant) + increase);
        } else {
            EnchantManager.applyEnchant(this.ePlayer.getPlayer(), new NBTItem(this.ePlayer.getPlayer().getItemInHand()), enchant, this.ePlayer.getEnchantLevel(enchant) + increase);
        }
        refresh(ePlayer);
        MessageType.UPGRADE_SUCCESS.message(this.ePlayer.getPlayer(), enchant.getName(), ElementalTokens.getNumberFormat().format(this.ePlayer.getEnchantLevel(enchant)), ElementalTokens.getNumberFormat().format(enchant.getMaxLevel()));
    }

    public ItemStack getFillerGlass(byte data) {
        ItemStack item = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }
}
