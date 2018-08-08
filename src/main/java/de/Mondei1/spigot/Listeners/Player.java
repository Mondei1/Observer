package de.Mondei1.spigot.Listeners;

import de.Mondei1.spigot.Observer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.json.simple.JSONObject;

public class Player implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        JSONObject obj = new JSONObject();
        obj.put("user", e.getPlayer().getUniqueId().toString());
        obj.put("message", e.getMessage());
        Observer.getInstance().getBackendManager().emit("player chat", obj);
    }

}
