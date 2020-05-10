package gg.steve.elemental.ce.data.types;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import gg.steve.elemental.bps.core.Backpack;
import gg.steve.elemental.bps.core.BackpackManager;
import gg.steve.elemental.bps.event.PreBackpackSaleEvent;
import gg.steve.elemental.bps.player.PlayerBackpackManager;
import gg.steve.elemental.ce.data.EnchantData;
import gg.steve.elemental.ce.data.EnchantDataType;
import gg.steve.elemental.ce.utils.EnchantProcUtil;
import gg.steve.elemental.ce.utils.LogUtil;
import gg.steve.elemental.pets.api.PetApi;
import gg.steve.elemental.pets.core.PetType;
import gg.steve.elemental.tokens.event.PreTokenAddEvent;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ExplosiveEnchantData implements EnchantData, Listener {
    private ConfigurationSection section;
    private double baseRate, multiplier, basePower, powerMultiplier;
    private static Map<Block, UUID> blocks;

    public ExplosiveEnchantData() {

    }

    public ExplosiveEnchantData(ConfigurationSection section) {
        this.section = section;
        this.baseRate = section.getDouble("base-rate");
        this.multiplier = section.getDouble("multiplier");
        this.basePower = section.getDouble("base-power");
        this.powerMultiplier = section.getDouble("power-multiplier");
        blocks = new HashMap<>();
    }

    @Override
    public EnchantDataType getType() {
        return EnchantDataType.EXPLOSIVE;
    }

    @Override
    public void remove(Player player, int enchantLevel) {

    }

    @Override
    public void onHold(Player player, int enchantLevel) {

    }

    @Override
    public void onMine(BlockBreakEvent event, int enchantLevel) {
        if (Math.random() * 100 > (this.baseRate + (this.multiplier * enchantLevel))) return;
        ProtectedRegion mine = null;
        for (ProtectedRegion region : WGBukkit.getRegionManager(event.getBlock().getWorld()).getApplicableRegions(event.getBlock().getLocation())) {
            if (region.getId().endsWith("-mine")) {
                mine = region;
                break;
            }
        }
        if (mine == null) return;
        blocks.put(event.getBlock(), event.getPlayer().getUniqueId());
        event.getBlock().getWorld().createExplosion(event.getBlock().getLocation(), (float) (this.basePower + (this.powerMultiplier * enchantLevel)));
        blocks.clear();
        EnchantProcUtil.doProc(section, event.getPlayer());
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

    @EventHandler
    public void damage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        ProtectedRegion mine = null;
        for (ProtectedRegion region : WGBukkit.getRegionManager(event.getEntity().getWorld()).getApplicableRegions(event.getEntity().getLocation())) {
            if (region.getId().endsWith("-mine")) {
                mine = region;
                break;
            }
        }
        if (mine == null) return;
        if (event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) event.setCancelled(true);
    }

    @EventHandler
    public void explosion(BlockExplodeEvent event) {
        if (event.isCancelled()) return;
        if (!blocks.containsKey(event.getBlock())) return;
        event.setCancelled(true);
        Player player = Bukkit.getPlayer(blocks.get(event.getBlock()));
        blocks.remove(event.getBlock());
        int fortune = 1;
        if (player.getItemInHand().getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
            fortune = player.getItemInHand().getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS) + 1;
        }
        for (Block block : event.blockList()) {
            if (block.getType().equals(Material.AIR)) continue;
            ProtectedRegion mine = null;
            for (ProtectedRegion region : WGBukkit.getRegionManager(block.getWorld()).getApplicableRegions(block.getLocation())) {
                if (region.getId().endsWith("-mine")) {
                    mine = region;
                    break;
                }
            }
            if (mine == null) continue;
            block.getWorld().playEffect(block.getLocation(), Effect.SMOKE, 0);
            if (block.getType().equals(Material.AIR)) continue;
            if (BackpackManager.isBackpackBlock(block)) {
                Backpack backpack = PlayerBackpackManager.getBackpackPlayer(player.getUniqueId()).getBackpack();
                for (ItemStack drop : block.getDrops()) {
                    for (int i = 0; i < fortune; i++) {
                        if (!BackpackManager.isBackpackItem(drop)) continue;
                        backpack.add(BackpackManager.getItemId(drop), drop.getAmount());
                        if (PetApi.isPetActive(player, PetType.FORTUNE) &&
                                PetApi.isProcing(PetApi.getActivePet(player, PetType.FORTUNE), PetApi.getPetRarity(player, PetType.FORTUNE))) {
                            for (int y = 0; y < PetApi.getBoostAmount(PetType.FORTUNE); y++) {
                                backpack.add(BackpackManager.getItemId(drop), drop.getAmount());
                            }
                        }
                    }
                }
            }
            block.setType(Material.AIR);
        }
    }
}
