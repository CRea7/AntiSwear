package me.vphydra.swearnotify;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


/*
 Author: Conor
 Date: 10/08/2020
 github: https://github.com/CRea7
 Quite a simple plugin should be easy enough to understand any questions shoot me a message.
 */
public final class SwearNotify extends JavaPlugin implements Listener {

    List<String> notificationSquad = new ArrayList<String>();
    public Collection<String> possibleGroups = new ArrayList<String>();

    Connection connection = null;
    String host = "";
    String username = "";
    String password = "";

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

    public void addSwear(String PlayerName, String Swear, String Message) throws SQLException {
        PreparedStatement swear = connection.prepareStatement("INSERT INTO `swears`(`id`, `PlayerName`, `Swear`, `Message`, `Date`) VALUES (?,?,?,?,?)");

        String uuid = UUID.randomUUID().toString();
        LocalDate date = LocalDate.now();
        swear.setString(1,uuid);
        swear.setString(2,PlayerName);
        swear.setString(3,Swear);
        swear.setString(4,Message);
        swear.setString(5, date.toString());

        swear.execute();
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
    public void onPlayerChat(AsyncPlayerChatEvent event) throws SQLException {
        String msg = event.getMessage();
        List<String> words = getConfig().getStringList("words");
        for (String word :words) {
            if(msg.toLowerCase().contains(word))
            {
                //adds swaer to database
                addSwear(event.getPlayer().getDisplayName(), word, msg);

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
