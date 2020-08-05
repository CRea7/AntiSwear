package me.vphydra.swearnotify;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SwearNotify extends JavaPlugin implements Listener {

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
    public void onPlayerChat(AsyncPlayerChatEvent event){
        String msg = event.getMessage();
        List<String> words = getConfig().getStringList("words");
        for (String word :words) {
            if(msg.contains(word))
            {
                event.getPlayer().sendMessage("you swore");
            }
        }
    }

    
}
