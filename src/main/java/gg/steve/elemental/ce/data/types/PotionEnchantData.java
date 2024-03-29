package gg.steve.elemental.ce.data.types;

import gg.steve.elemental.bps.event.PreBackpackSaleEvent;
import gg.steve.elemental.ce.core.Enchant;
import gg.steve.elemental.ce.data.EnchantData;
import gg.steve.elemental.ce.data.EnchantDataType;
import gg.steve.elemental.ce.utils.EnchantProcUtil;
import gg.steve.elemental.ce.utils.LogUtil;
import gg.steve.elemental.tokens.event.PreTokenAddEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionEnchantData implements EnchantData {
    private ConfigurationSection section;
    private PotionEffectType type;
    private int duration;

    public PotionEnchantData(ConfigurationSection section, Enchant enchant) {
        this.section = section;
        try {
            this.type = PotionEffectType.getByName(section.getString("effect").toUpperCase());
        } catch (Exception e) {
            LogUtil.info("Error when enabling enchant: " + enchant.getName() + ", the potion effect: " + section.getString("effect").toUpperCase() + " does not exist in Bukkit.");
        }
        this.duration = section.getInt("duration");
    }

    @Override
    public EnchantDataType getType() {
        return EnchantDataType.POTION;
    }

    @Override
    public void remove(Player player, int enchantLevel) {
        potionCheck(player, this.type, enchantLevel - 1);
    }

    @Override
    public void onHold(Player player, int enchantLevel) {
        PotionEffect effect = new PotionEffect(this.type, this.duration, enchantLevel - 1);
        potionCheck(player, this.type, enchantLevel - 1);
        player.addPotionEffect(effect);
        EnchantProcUtil.doProc(this.section, player, enchantLevel);
    }

    @Override
    public void onMine(BlockBreakEvent event, int enchantLevel) {

    }

    @Override
    public void onBackpackSell(PreBackpackSaleEvent event, int enchantLevel) {

    }

    @Override
    public void onTokenAdd(PreTokenAddEvent event, int enchantLevel) {

    }

    @Override
    public Enchantment getVanillaEnchantment() {
        return null;
    }

    /**
     * If the player has that potion effect but the amplifier is less that the level, remove it
     *
     * @param player    the player being checked
     * @param type      the effect to check
     * @param amplifier the amplifier of the new effect
     */
    public void potionCheck(Player player, PotionEffectType type, int amplifier) {
        for (PotionEffect active : player.getActivePotionEffects()) {
            if (!active.getType().equals(type)) continue;
            if (active.getAmplifier() <= amplifier) {
//                EnchantProcUtil.doRemove(this.section, player);
                player.removePotionEffect(type);
            }
        }
    }
}
