package de.Mondei1.utils;

import org.apache.commons.lang.text.StrSubstitutor;

import java.util.HashMap;
import java.util.Map;

public class LogUtil {

    private String s;
    private String prefix;
    private String event_prefix;
    private ConfigManager cfg;

    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String GREEN = "\u001B[32m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_BLACK = "\u001B[90m";
    public static final String WHITE = "\u001B[37m";

    public LogUtil(String s) {
        this.cfg = new ConfigManager();
        this.prefix = cfg.getPrefix();
        this.event_prefix = cfg.getEvent_Prefix();

        // Replace
        this.s = translate(s);
        this.prefix = translate(prefix);
        this.event_prefix = translate(event_prefix);
    }

    private String translate(String s) {
        return s.replace("§a", GREEN)
                .replace("§4", RED)
                .replace("§c", BRIGHT_RED)
                .replace("§e", YELLOW)
                .replace("§6", BRIGHT_YELLOW)
                .replace("§3", CYAN)
                .replace("§b", BRIGHT_CYAN)
                .replace("§r", RESET)
                .replace("§f", WHITE)
                .replace("§8", BRIGHT_BLACK)
                .replace("§7", WHITE)
                .replace("§l", BOLD);
    }

    public void success() {
        System.out.println(prefix + GREEN + "SUCCESS: " + BRIGHT_GREEN + s + RESET);
    }
    public void error() {
        System.out.println(prefix + BRIGHT_RED + "ERROR: " + RED + s + RESET);
    }
    public void debug() {
        System.out.println(prefix + CYAN + "DEBUG: " + BRIGHT_CYAN + s + RESET);
    }
    public void hint() {
        System.out.println(prefix + BRIGHT_YELLOW + "HINT: " + YELLOW + s + RESET);
    }
    public void event_success(LogEvents event) {
        Map<String, String> data = new HashMap<>();
        data.put("event", event.toString());

        System.out.println(StrSubstitutor.replace(event_prefix, data) + BRIGHT_GREEN + s + RESET);
    }
    public void event_error(LogEvents event) {
        Map<String, String> data = new HashMap<>();
        data.put("event", event.toString());

        System.out.println(StrSubstitutor.replace(event_prefix, data) + BRIGHT_RED + s + RESET);
    }

}
