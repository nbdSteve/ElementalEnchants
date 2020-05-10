package gg.steve.elemental.ce.cmd.sub;

import gg.steve.elemental.ce.core.Enchant;
import gg.steve.elemental.ce.core.EnchantManager;
import gg.steve.elemental.ce.message.CommandDebug;
import gg.steve.elemental.ce.message.MessageType;
import gg.steve.elemental.ce.nbt.NBTItem;
import gg.steve.elemental.ce.permission.PermissionNode;
import gg.steve.elemental.tokens.ElementalTokens;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddCmd {

    public static void add(CommandSender sender, String[] args) {
        // /eea add nbdsteve tokenator 100
        if (args.length < 4) {
            CommandDebug.INVALID_NUMBER_OF_ARGUMENTS.message(sender);
            return;
        }
        if (!PermissionNode.ADD_ENCHANT.hasPermission(sender)) {
            CommandDebug.INSUFFICIENT_PERMISSION.message(sender, PermissionNode.ADD_ENCHANT.get());
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            CommandDebug.TARGET_NOT_ONLINE.message(sender);
            return;
        }
        Enchant enchant;
        if ((enchant = EnchantManager.getEnchant(args[2].toLowerCase())) == null) {
            CommandDebug.INVALID_ENCHANT.message(sender);
            return;
        }
        int level;
        try {
            level = Integer.parseInt(args[3]);
            if (level < 1) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            CommandDebug.INVALID_LEVEL.message(sender);
            return;
        }
        if (level > enchant.getMaxLevel()) {
            CommandDebug.EXCEED_MAX_LEVEL.message(sender);
            return;
        }
        if (!EnchantManager.isEnchantable(target.getItemInHand())) {
            CommandDebug.TARGET_UNENCHANTABLE_ITEM.message(sender, target.getName());
            return;
        }
        Player player = null;
        if (sender instanceof Player) {
            if (!target.getUniqueId().equals(((Player) sender).getUniqueId())) {
                player = (Player) sender;
            }
        }
        if (enchant.getData().getVanillaEnchantment() == null) {
            EnchantManager.applyEnchant(target, new NBTItem(target.getItemInHand()), enchant, level);
        } else {
            EnchantManager.applyVanilla(target, new NBTItem(target.getItemInHand()), enchant, level);
        }
        MessageType.ADD_ENCHANT_RECEIVER.message(target, enchant.getName(), ElementalTokens.getNumberFormat().format(level));
        if (!(sender instanceof Player) || player != null) {
            MessageType.ADD_ENCHANT_ADDER.message(sender, target.getName(), enchant.getName(), ElementalTokens.getNumberFormat().format(level));
        }
    }
}
