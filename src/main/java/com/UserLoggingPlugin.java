package com.userloggingplugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class MyLoggingPlugin extends JavaPlugin {
    private FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig(); // Create default config if not exists
        config = getConfig();
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    public String getWebhookUrl() {
        return config.getString("webhook-url");
    }

    public String getLogFileName() {
        return config.getString("log-file");
    }

    @Override
    public void onDisable() {
        // Any cleanup logic if necessary
    }
}
