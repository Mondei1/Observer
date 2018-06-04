package de.Mondei1.bungee.commands;

import de.Mondei1.bungee.Observer;
import de.Mondei1.utils.ConfigManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.json.simple.JSONObject;

public class Sync extends Command {

    ConfigManager cfg;
    private Observer observer;

    public Sync() {
        super("sync");
        this.observer = Observer.getInstance();
        this.cfg = new ConfigManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(!p.hasPermission("observer.sync")) {
                p.sendMessage(cfg.getPrefix() + "§4You don't have permission to execute this command!");
                return;
            }
        }
        observer.getBackendManager().emit("get config", new JSONObject());
        sender.sendMessage(cfg.getPrefix() + "§aSent request.§7 Config will be updated soon!");
    }
}
