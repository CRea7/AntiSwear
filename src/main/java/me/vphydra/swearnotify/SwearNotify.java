package me.vphydra.swearnotify;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.LuckPermsEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class SwearNotify extends JavaPlugin implements Listener {

    Collection<String> possiibleGroups;

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(this,this);
        if(!getConfig().contains("words")){
            List<String> words = new ArrayList<String>();
            words.add("test");
            words.add("test2");
            getConfig().set("words", words);
        }
        saveConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent)
    {
        Player player = playerJoinEvent.getPlayer();
        player.sendMessage(ChatColor.GREEN + "before check");
        boolean test = isPlayerInGroup(player, "admin");
        player.sendMessage(String.valueOf(test));
    }

    public static boolean isPlayerInGroup(Player player, String group) {
        return player.hasPermission("group." + group);
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        String msg = event.getMessage();
        List<String> words = getConfig().getStringList("words");
        Set<OfflinePlayer> Op = Bukkit.getOperators();
        for (String word :words) {
            if(msg.contains(word))
            {
                for (OfflinePlayer toon: Op) {
                    Player target =Bukkit.getPlayer(toon.getName());
                    if(target != null) {
                        target.sendMessage(ChatColor.RED + event.getPlayer().getDisplayName() + " Has Sworn ");
                    }
                }
            }
        }
    }

    
}
