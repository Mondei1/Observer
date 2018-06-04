package de.Mondei1.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URISyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ConfigManager {

    JSONParser parser;
    private String jarPath;
    private String dataFolder;

    public ConfigManager() {
        this.parser = new JSONParser();
        try {
            this.jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replace("\\", "/");
            this.dataFolder = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent().replace("\\", "/") + "/Observer";
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getConfig() {
        try {
            return (JSONObject) parser.parse(new FileReader(this.dataFolder + "/config.json"));
        } catch (IOException | ParseException e) {
            System.out.println("Observer §f-> " + LogUtil.RESET + "Can't read config file." + LogUtil.RESET);
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject client() {
        return (JSONObject) getConfig().get("client");
    }

    public JSONObject backend() {
        return (JSONObject) getConfig().get("backend");
    }

    public String getPrefix() {
        return client().get("prefix").toString();
    }

    public String getEvent_Prefix() { return client().get("event_prefix").toString(); }

    public boolean exists() {
        return new File(this.dataFolder + "/config.json").exists();
    }

    public void createConfig() throws IOException {
        if(exists()) return;
        new File(this.dataFolder).mkdir();

        // Unpacking config.json from local package and copy it to plugins/Observer/config.json.
        try {
            // Unpack config.json from jar file
            OutputStream out = new FileOutputStream(this.dataFolder + "/config.json");
            FileInputStream fin = new FileInputStream(this.jarPath);
            BufferedInputStream bin = new BufferedInputStream(fin);
            ZipInputStream zin = new ZipInputStream(bin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.getName().equals("config.json")) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zin.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    out.close();
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Observer -> " + LogUtil.RED + "Error while unpacking me. That's an error!" + LogUtil.RESET);
            e.printStackTrace();
        }
    }

}
