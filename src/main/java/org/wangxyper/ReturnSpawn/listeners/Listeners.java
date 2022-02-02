package org.wangxyper.ReturnSpawn.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.wangxyper.ReturnSpawn.Main;
import org.wangxyper.ReturnSpawn.Util;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Listeners implements Listener {
    private static FileConfiguration configuration = new YamlConfiguration();
    private static File dataFile = new File("dataRPG.yml");
    public static ConcurrentHashMap<Player,Boolean> playerMap = new ConcurrentHashMap<>();
    public static void saveData(){
        try {
            long time = System.currentTimeMillis();
            configuration.save(dataFile);
            Bukkit.getLogger().log(Level.INFO,"玩家数据已存储,用时："+(System.currentTimeMillis()-time));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadAndAutoSave(){
        try {
            if (!dataFile.exists()){
                Bukkit.getLogger().info("Saving file...");
                Util.saveResource("dataRPG.yml",true);
                Thread.sleep(3000);
                Bukkit.getLogger().info("Saved!");
            }
            configuration.load(dataFile);
            Util.scheduledExecutor.scheduleAtFixedRate(()->{
                try {
                    Thread.currentThread().setName("RPGScheduledExecutor");
                    long time = System.currentTimeMillis();
                    configuration.save(dataFile);
                    Bukkit.getLogger().log(Level.INFO,"玩家数据已存储,用时："+(System.currentTimeMillis()-time));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            },0,2, TimeUnit.MINUTES);
        }catch (Exception e){e.printStackTrace();}
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Util.executor.execute(()->{
            sendMessage(event,configuration.contains(event.getPlayer().getName()));
        });
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Util.executor.execute(() -> {
            if (configuration.contains(event.getPlayer().getName())) {
                if (!Main.config.getString("JoinedPlayer.Quit.BroadcastMessage").equals("DEFAULT")) {
                    //{0}:Quit Message,{1}:Player name
                    ChatColor color = ChatColor.valueOf(Main.config.getString("JoinedPlayer.Quit.MessageColor"));
                    String s = MessageFormat.format(color + Main.config.getString("JoinedPlayer.Quit.BroadcastMessage"), event.getQuitMessage(), event.getPlayer().getName());
                    event.setQuitMessage(s);
                }
            }
        });

    }
    public static void sendMessage(PlayerJoinEvent event,boolean isJoined){
        if(!isJoined){
            configuration.set(event.getPlayer().getName(),0);
            if (!Main.config.getString("NewPlayer.Join.BroadcastMessage").equals("DEFAULT")) {
                //{0}:Join Message,{1}:Player name
                ChatColor color = ChatColor.valueOf(Main.config.getString("NewPlayer.Join.MessageColor"));
                String s = MessageFormat.format(color+Main.config.getString("NewPlayer.Join.BroadcastMessage"), event.getJoinMessage(), event.getPlayer().getName());
                event.setJoinMessage(s);
            }
            playerMap.put(event.getPlayer(), true);
            ChatColor colorMainTitle = ChatColor.valueOf(Main.config.getString("NewPlayer.Join.ColorMainTitle"));
            ChatColor colorSubTitle = ChatColor.valueOf(Main.config.getString("NewPlayer.Join.ColorSubTitle"));
            event.getPlayer().sendTitle(
                    colorMainTitle + Main.config.getString("NewPlayer.Join.TitleNewPlayer"),
                    colorSubTitle + Main.config.getString("NewPlayer.Join.SubTitleNewPlayer"),
                    10,
                    70,
                    20
            );
        }else{
            configuration.set(event.getPlayer().getName(),true);
            if (!Main.config.getString("JoinedPlayer.Join.BroadcastMessage").equals("DEFAULT")) {
                //{0}:Join Message,{1}:Player name
                ChatColor color = ChatColor.valueOf(Main.config.getString("JoinedPlayer.Join.MessageColor"));
                String s = MessageFormat.format(color+Main.config.getString("JoinedPlayer.Join.BroadcastMessage"), event.getJoinMessage(), event.getPlayer().getName());
                event.setJoinMessage(s);
            }
            playerMap.put(event.getPlayer(), true);
            ChatColor colorMainTitle = ChatColor.valueOf(Main.config.getString("JoinedPlayer.Join.ColorMainTitle"));
            ChatColor colorSubTitle = ChatColor.valueOf(Main.config.getString("JoinedPlayer.Join.ColorSubTitle"));
            event.getPlayer().sendTitle(
                    colorMainTitle + Main.config.getString("JoinedPlayer.Join.TitleNewPlayer"),
                    colorSubTitle + Main.config.getString("JoinedPlayer.Join.SubTitleNewPlayer"),
                    10,
                    70,
                    20
            );
        }
    }
}
