package com.userloggingplugin;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordWebhook {
    public static void sendToDiscord(String webhookUrl, String message) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.set RequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String jsonPayload = String.format("{\"content\": \"%s\"}", message);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                // Handle error
                System.err.println("Failed to send message to Discord: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
