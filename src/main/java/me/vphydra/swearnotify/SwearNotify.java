package me.vphydra.swearnotify;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class SwearNotify extends JavaPlugin implements Listener {

    List<String> notificationSquad = new ArrayList<String>();
    public Collection<String> possibleGroups = new ArrayList<String>();

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
        boolean admin = isPlayerInGroup(player, "admin");
        player.sendMessage(String.valueOf(admin));

        possibleGroups.add("admin");
        String group = getPlayerGroup(player, possibleGroups);
        player.sendMessage(ChatColor.GREEN + group);

        if(admin == true)
        {
            player.sendMessage(ChatColor.GREEN + "adding player");
            notificationSquad.add(player.getDisplayName());
        }
//        else if(isPlayerInGroup(player, "Owner"))
//        {
//            getLogger().info("adding player to list");
//            notificationSquad.add(player.getDisplayName());
//        }
//        else if(isPlayerInGroup(player, "moderator"))
//        {
//            getLogger().info("adding player to list");
//            notificationSquad.add(player.getDisplayName());
//        }

//        if(notificationSquad.get(0) != null) {
//            getLogger().info(notificationSquad.get(0));
//        } else
//        {
//            getLogger().info("no admin online");
//        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        notificationSquad.remove(player.getDisplayName());
    }

    //checks if a player is in a group
    public static boolean isPlayerInGroup(Player player, String group) {
        return player.hasPermission("group." + group);
    }

    public static String getPlayerGroup(Player player, Collection<String> possibleGroups) {
        for (String group : possibleGroups) {
            if (player.hasPermission("group." + group)) {
                return group;
            }
        }
        return null;
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        String msg = event.getMessage();
        List<String> words = getConfig().getStringList("words");
        Set<OfflinePlayer> Op = Bukkit.getOperators();
        for (String word :words) {
            if(msg.contains(word))
            {
                for (String toon: notificationSquad) {
                    Player target =Bukkit.getPlayer(toon);
                    if(target != null) {
                        target.sendMessage(ChatColor.RED + event.getPlayer().getDisplayName() + " Has Sworn ");
                    }
                }
            }
        }
    }

    
}
