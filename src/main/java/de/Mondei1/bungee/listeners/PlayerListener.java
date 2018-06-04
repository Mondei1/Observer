package de.Mondei1.bungee.listeners;

import de.Mondei1.bungee.Observer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.json.simple.JSONObject;

public class PlayerListener implements Listener {

    Observer main;

    public PlayerListener() {
        this.main = Observer.getInstance();
    }

    @EventHandler
    public void onJoin(PostLoginEvent e) {
        ProxiedPlayer p = e.getPlayer();
        JSONObject obj = new JSONObject();
        obj.put("user", p.getUniqueId().toString());
        main.getBackendManager().emit("player join", obj);
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e) {
        ProxiedPlayer p = e.getPlayer();
        JSONObject obj = new JSONObject();
        obj.put("user", p.getUniqueId().toString());
        main.getBackendManager().emit("player leave", obj);
    }

}
