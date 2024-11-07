package com.userloggingplugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class PlayerJoinListener implements Listener {
    private final UserLoggingPlugin plugin;

    public PlayerJoinListener(UserLoggingPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        String ipAddress = event.getPlayer().getAddress().getAddress().getHostAddress();
        String uuid = event.getPlayer().getUniqueId().toString();
        String minecraftVersion = event.getPlayer().getVersion(); // Adjust based on server version
        String operatingSystem = System.getProperty("os.name");

        String message = String.format("Player Info:\nUsername: %s\nIP: %s\nUUID: %s\nMinecraft Version: %s\nOperating System: %s",
                playerName, ipAddress, uuid, minecraftVersion, operatingSystem);

        CompletableFuture.runAsync(() -> { // Send data to Discord webhook
            // Check if VPN checking is enabled. THE FUNCTION IS AUTOMATICLY TURNED OFF AND CAN BE CHANGED IN THIS PLUGINS "config" FILE!
            boolean enableVPNCheck = plugin.getConfig().getBoolean("enable-vpn-check");
            if (enableVPNCheck && isUsingVPN(ipAddress)) {
                String userIdToPing = plugin.getConfig().getString("vpn-user-id");
                String vpnAlertMessage = String.format("<@%s> Player %s is using a VPN! IP: %s", userIdToPing, playerName, ipAddress);
                DiscordWebhook.sendToDiscord(plugin.getWebhookUrl(), vpnAlertMessage);
            }
            DiscordWebhook.sendToDiscord(plugin.getWebhookUrl(), message); // Send the regular log message
        });
    }

    private boolean isUsingVPN(String ipAddress) {
        try {
            // Replace with a real API endpoint that checks for VPNs
            String apiUrl = String.format("https://api.example.com/vpn-check?ip=%s", ipAddress);
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Choose a reliable API for VPN detection. Some popular options include: IPHub, IP2Proxy, VPNAPI.io
            // Make sure to read the documentation of the chosen API to understand how to format requests and interpret responses.
            // Some VPN detection services require an API key. If that's the case, you may need to include it in your request headers.
            // Be mindful of the rate limits imposed by the API. If your server has many players, you may want to implement caching or a rate-l
            // Parse the response (assuming a JSON response).
            // This is a simplified example.
            return response.toString().contains("\"vpn\": true"); // Adjust based on actual API response
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Assume not a VPN if there's an error
        }
    }
}
