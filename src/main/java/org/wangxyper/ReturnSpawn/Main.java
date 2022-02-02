package org.wangxyper.ReturnSpawn;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.wangxyper.ReturnSpawn.commands.CommandHome;
import org.wangxyper.ReturnSpawn.listeners.Listeners;

public final class Main extends JavaPlugin {
    public static FileConfiguration config = null;
    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            this.saveDefaultConfig();
            Listeners.loadAndAutoSave();
            Thread.sleep(3000);
            config = this.getConfig();
            Bukkit.getPluginManager().registerEvents(new Listeners(),this);
            Bukkit.getPluginCommand("home").setExecutor(new CommandHome());
            Bukkit.getPluginManager().registerEvents(new Listeners(),this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Shutting down...");
        Listeners.saveData();
        Util.executor.shutdown();
        Util.scheduledExecutor.shutdown();
        Bukkit.getLogger().info("ThreadPool closed");
    }
}
