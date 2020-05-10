package gg.steve.elemental.ce.cmd;

import gg.steve.elemental.ce.cmd.sub.AddCmd;
import gg.steve.elemental.ce.cmd.sub.HelpCmd;
import gg.steve.elemental.ce.cmd.sub.ReloadCmd;
import gg.steve.elemental.ce.core.Enchant;
import gg.steve.elemental.ce.core.EnchantManager;
import gg.steve.elemental.ce.message.CommandDebug;
import gg.steve.elemental.ce.nbt.NBTItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EeaCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            HelpCmd.help(sender);
            return true;
        }
        switch (args[0]) {
            case "help":
            case "h":
                HelpCmd.help(sender);
                break;
            case "reload":
            case "r":
                ReloadCmd.reload(sender);
                break;
            case "add": case "a":
                AddCmd.add(sender, args);
                break;
            default:
                CommandDebug.INVALID_COMMAND.message(sender);
                break;
        }
        return true;
    }
}
