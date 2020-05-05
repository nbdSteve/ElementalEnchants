package gg.steve.elemental.ce.cmd;

import gg.steve.elemental.ce.core.Enchant;
import gg.steve.elemental.ce.core.EnchantManager;
import gg.steve.elemental.ce.nbt.NBTItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EceCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ece")) {
            if (args.length == 0) {
                // help
                return true;
            }
            Enchant enchant;
            if ((enchant = EnchantManager.getEnchant(args[0])) == null) {
                sender.sendMessage("Invalid enchantment name!");
                return true;
            }
            Player player = (Player) sender;
            EnchantManager.applyEnchant(player, new NBTItem(player.getItemInHand()), enchant, Integer.parseInt(args[1]));
        }
        return true;
    }
}
