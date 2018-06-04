package de.Mondei1.utils.backend.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.Mondei1.bungee.Observer;
import de.Mondei1.utils.ConfigManager;
import de.Mondei1.utils.LogEvents;
import de.Mondei1.utils.LogUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

public class get_config {

    public get_config(Object... args) {
        ConfigManager cfg = new ConfigManager();
        String response = Arrays.toString(args).replace("[", "").replace("]", "");  // Poor, I know ^^
        try {
            JSONParser parser = new JSONParser();

            // Jackson JSON
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            StringWriter sw = new StringWriter();

            JSONObject got = (JSONObject) parser.parse(response);
            JSONObject new_cfg = cfg.getConfig();
            new_cfg.put("client", got.get("client"));

            mapper.writeValue(sw, new_cfg);
            System.out.println("This will be the new config: " + sw.toString());

            new File(Observer.dataPath + "config.json").delete();
            try (PrintWriter out = new PrintWriter(Observer.dataPath + "/config.json")) {
                out.println(sw.toString());
                out.flush();
            }
            new LogUtil("New config written!").event_success(LogEvents.SYNC);
        } catch (org.json.simple.parser.ParseException | IOException e) {
            e.printStackTrace();
        }
    }

}
