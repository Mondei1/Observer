package de.Mondei1.utils.backend.events;

import de.Mondei1.utils.LogUtil;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * Spigot only
 */
public class broadcast {

    public broadcast(Object... args) {
        JSONParser parser = new JSONParser();
        JSONObject content = null;
        try {
            content = (JSONObject) parser.parse(args[0].toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        Object msg = content.get("message");
        new LogUtil("Broadcast message: " + msg);

        try {
            Class.forName("org.bukkit.Bukkit");
            // We are under bukkit.
            Bukkit.broadcastMessage("§cBROADCAST§r | §7" + msg);
        } catch (ClassNotFoundException e) {
            // We are under bungeecord. Nothing to do.
        }
    }

}
