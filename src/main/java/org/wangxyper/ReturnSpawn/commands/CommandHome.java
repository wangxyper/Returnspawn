package org.wangxyper.ReturnSpawn.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.wangxyper.ReturnSpawn.Main;
import org.wangxyper.ReturnSpawn.Util;
import java.util.List;

public class CommandHome implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player)sender;
            List<Integer> list = (List<Integer>) Main.config.getList("CommandConfig.HomeCommandBack");
            float x =list.get(0);
            float y =list.get(1);
            float z =list.get(2);
            player.teleport(new Location(Bukkit.getWorld("world"),x,y,z));
            Util.executor.execute(()->{
                List<String> commandsAfter = Main.config.getStringList("CommandConfig.HomeCommandAfterExecuteCommand");
                for (String commandAf : commandsAfter){
                    Bukkit.dispatchCommand(sender,commandAf);
                }
            });
        }
        return true;
    }
}
