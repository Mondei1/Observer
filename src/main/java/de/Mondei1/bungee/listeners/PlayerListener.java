package de.Mondei1.bungee.listeners;

import de.Mondei1.bungee.Observer;
import de.Mondei1.utils.Network;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.json.simple.JSONObject;

import java.net.SocketException;
import java.net.UnknownHostException;

public class PlayerListener implements Listener {

    Observer main;

    public PlayerListener() {
        this.main = Observer.getInstance();
    }

    @EventHandler
    public void onJoin(PostLoginEvent e) throws SocketException, UnknownHostException {
        ProxiedPlayer p = e.getPlayer();
        JSONObject obj = new JSONObject();
        obj.put("user", p.getUniqueId().toString());
        obj.put("name", p.getName());
        obj.put("network", Network.getMacAddress());
        main.getBackendManager().emit("player join", obj);
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e) {
        ProxiedPlayer p = e.getPlayer();
        JSONObject obj = new JSONObject();
        obj.put("user", p.getUniqueId().toString());
        main.getBackendManager().emit("player leave", obj);
    }

    @EventHandler
    public void onSwitch(ServerSwitchEvent e) {
        ProxiedPlayer p = e.getPlayer();
        JSONObject obj = new JSONObject();
        obj.put("user", p.getUniqueId().toString());
        obj.put("switchedTo", p.getServer().getInfo().getAddress().getPort());  // Using port to identify the actual server.
        main.getBackendManager().emit("player switch", obj);
    }

}
