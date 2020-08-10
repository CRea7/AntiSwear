package me.vphydra.swearnotify;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class SwearNotify extends JavaPlugin implements Listener {

    List<String> notificationSquad = new ArrayList<String>();
    public Collection<String> possibleGroups = new ArrayList<String>();

    Connection connection = null;
    String host, database, username, password;
    int port;

    @Override
    public void onEnable() {

        try{
            connect();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        getServer().getPluginManager().registerEvents(this,this);
        if(!getConfig().contains("words")){
            List<String> words = new ArrayList<String>();
            words.add("test");
            words.add("test2");
            getConfig().set("words", words);
        }
        saveConfig();
    }

    @Override
    public void onDisable() {
        try {
            if(connection != null) {
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void connect() throws  SQLException {
        connection = DriverManager.getConnection(host,username,password);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent)
    {
        Player player = playerJoinEvent.getPlayer();

        if(isPlayerInGroup(player, "admin"))
        {
            player.sendMessage(ChatColor.GREEN + "adding player admin");
            notificationSquad.add(player.getDisplayName());
        }
        else if(isPlayerInGroup(player, "Owner"))
        {
            player.sendMessage(ChatColor.GREEN + "adding player own");
            notificationSquad.add(player.getDisplayName());
        }
        else if(isPlayerInGroup(player, "moderator"))
        {
            player.sendMessage(ChatColor.GREEN + "adding player mod");
            notificationSquad.add(player.getDisplayName());
        }
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
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        String msg = event.getMessage();
        List<String> words = getConfig().getStringList("words");
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
