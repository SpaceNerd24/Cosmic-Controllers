package com.spacenerd24.cosmic_controllers.utils;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spacenerd24.cosmic_controllers.Constants;
import com.spacenerd24.cosmic_controllers.listeners.CControllerListener;
import org.jline.utils.InputStreamReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;

import static com.github.puzzle.util.ZipPack.GSON;

public class ControllerUtils {

    public static Array<Controller> getControllers() {
        return Controllers.getControllers();
    }

    public static void addAllListeners() {
        for (Controller controller : Constants.controllers) {
            addListener(controller);
        }
    }

    public static void removeAllListeners() {
        for (Controller controller : Constants.controllers) {
            //removeListener(controller);
        }
    }

    public static void addListener(Controller controller) {
        //CControllerListener listener = new CControllerListener();
        CControllerListener listener = new CControllerListener();
        controller.addListener(listener);
        Constants.listeners.add(listener);
        Constants.LOGGER.info("Added listener for controller: {}", controller.getName());
    }

    public static void loadControllerConfigs() {
        try {
            // Load index.json
            URL indexUrl = new URL("https://raw.githubusercontent.com/SpaceNerd24/Cosmic-Controllers/main/json/index.json");
            InputStream indexInputStream = indexUrl.openStream();
            BufferedReader indexReader = new BufferedReader(new InputStreamReader(indexInputStream, StandardCharsets.UTF_8));
            JsonObject indexJson = JsonParser.parseReader(indexReader).getAsJsonObject();
            indexReader.close();

            // Iterate through GUIDs in the index
            for (Map.Entry<String, JsonElement> entry : indexJson.entrySet()) {
                String controllerName = entry.getKey();
                String guid = entry.getValue().getAsString();

                // Construct the URL for the configuration file
                URL configUrl = new URL("https://raw.githubusercontent.com/SpaceNerd24/Cosmic-Controllers/main/json/" + guid + ".json");
                InputStream configInputStream = configUrl.openStream();

                BufferedReader configReader = new BufferedReader(new InputStreamReader(configInputStream, StandardCharsets.UTF_8));
                JsonObject configJson = JsonParser.parseReader(configReader).getAsJsonObject();
                configReader.close();

                // Parse the configuration
                String configGUID = configJson.get("GUID").getAsString();
                if (!guid.equals(configGUID)) {
                    throw new RuntimeException("GUID mismatch in config file for: " + controllerName);
                }

                Map<String, Integer> buttonMappings = new HashMap<>();
                for (Map.Entry<String, JsonElement> buttonEntry : configJson.entrySet()) {
                    if (!buttonEntry.getKey().equals("GUID")) {
                        buttonMappings.put(buttonEntry.getKey(), buttonEntry.getValue().getAsInt());
                    }
                }

                Constants.controllerConfigs.put(guid, buttonMappings);
            }

            Constants.LOGGER.info("Loaded controller configs");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load controller configs", e);
        }
    }

}
