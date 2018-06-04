package de.Mondei1.utils.backend.events;

import de.Mondei1.bungee.Observer;
import de.Mondei1.utils.LogEvents;
import de.Mondei1.utils.LogUtil;
import org.bukkit.Bukkit;

public class shutdown {

    public shutdown(Object... args) {
        try {
            Class.forName("org.bukkit.Bukkit");
            // We are under bukkit.

            Bukkit.shutdown();
        } catch (ClassNotFoundException e) {
            // We are under bungeecord.

            Observer main = Observer.getInstance();
            main.getProxy().stop("Backend told me to stop the network! Sorry :(");
        }

        new LogUtil("Have to shutdown the server! " + System.currentTimeMillis()).event_success(LogEvents.ORDER);
    }

}
