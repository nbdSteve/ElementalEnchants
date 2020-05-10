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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class RegionalEnchantData implements EnchantData {
    private ConfigurationSection section;
    private ClearanceType type;
    private double baseRate, multiplier;

    private enum ClearanceType {
        LAYER,
        WHOLE;
    }

    public RegionalEnchantData(ConfigurationSection section) {
        this.section = section;
        this.type = ClearanceType.valueOf(section.getString("clearance-type").toUpperCase());
        this.baseRate = section.getDouble("base-rate");
        this.multiplier = section.getDouble("multiplier");
    }

    @Override
    public EnchantDataType getType() {
        return EnchantDataType.REGIONAL;
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
        int amount = 0;
        switch (type) {
            case LAYER:
                amount = blockCalulation(mine, event.getBlock().getWorld(), event.getPlayer(), event.getBlock().getY(), true);
                break;
            case WHOLE:
                amount = blockCalulation(mine, event.getBlock().getWorld(), event.getPlayer(), event.getBlock().getY(), false);
                break;
        }
        EnchantProcUtil.doProc(section, event.getPlayer(), amount);
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

    private int blockCalulation(ProtectedRegion mine, World world, Player player, int startingY, boolean layer) {
        int blocksRemoved = 0;
        int fortune = 1;
        if (player.getItemInHand().getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
            fortune = player.getItemInHand().getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS) + 1;
        }
        for (int x = mine.getMinimumPoint().getBlockX(); x <= mine.getMaximumPoint().getBlockX(); x++) {
            for (int z = mine.getMinimumPoint().getBlockZ(); z <= mine.getMaximumPoint().getBlockZ(); z++) {
                if (layer) {
                    Block block = new Location(world, x, startingY, z).getBlock();
                    if (block.getType().equals(Material.AIR)) continue;
                    if (BackpackManager.isBackpackBlock(block)) {
                        Backpack backpack = PlayerBackpackManager.getBackpackPlayer(player.getUniqueId()).getBackpack();
                        for (ItemStack drop : block.getDrops(player.getItemInHand())) {
                            for (int i = 0; i < fortune; i++) {
                                if (!BackpackManager.isBackpackItem(drop)) continue;
                                backpack.add(BackpackManager.getItemId(drop), drop.getAmount());
                                blocksRemoved++;
                                if (PetApi.isPetActive(player, PetType.FORTUNE) &&
                                        PetApi.isProcing(PetApi.getActivePet(player, PetType.FORTUNE), PetApi.getPetRarity(player, PetType.FORTUNE))) {
                                    for (int y = 0; y < PetApi.getBoostAmount(PetType.FORTUNE); y++) {
                                        backpack.add(BackpackManager.getItemId(drop), drop.getAmount());
                                        blocksRemoved++;
                                    }
                                }
                            }
                        }
                    }
                    block.setType(Material.AIR);
                } else {
                    for (int y = mine.getMinimumPoint().getBlockY(); y <= mine.getMaximumPoint().getBlockY(); y++) {
                        Block block = new Location(world, x, y, z).getBlock();
                        if (block.getType().equals(Material.AIR)) continue;
                        if (BackpackManager.isBackpackBlock(block)) {
                            Backpack backpack = PlayerBackpackManager.getBackpackPlayer(player.getUniqueId()).getBackpack();
                            for (ItemStack drop : block.getDrops(player.getItemInHand())) {
                                for (int i = 0; i < fortune; i++) {
                                    if (!BackpackManager.isBackpackItem(drop)) continue;
                                    backpack.add(BackpackManager.getItemId(drop), drop.getAmount());
                                    blocksRemoved++;
                                    if (PetApi.isPetActive(player, PetType.FORTUNE) &&
                                            PetApi.isProcing(PetApi.getActivePet(player, PetType.FORTUNE), PetApi.getPetRarity(player, PetType.FORTUNE))) {
                                        for (int j = 0; j < PetApi.getBoostAmount(PetType.FORTUNE); j++) {
                                            backpack.add(BackpackManager.getItemId(drop), drop.getAmount());
                                            blocksRemoved++;
                                        }
                                    }
                                }
                            }
                        }
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        return blocksRemoved;
    }
}
