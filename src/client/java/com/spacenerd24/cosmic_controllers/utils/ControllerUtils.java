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
            ArrayList<String> resourceFiles = new ArrayList<>();

            // Fetch the list of controllers from index.json
            URL indexUrl = new URL("https://raw.githubusercontent.com/SpaceNerd24/Cosmic-Controllers/main/json/index.json");
            InputStream indexInputStream = indexUrl.openStream();
            BufferedReader indexReader = new BufferedReader(new InputStreamReader(indexInputStream, StandardCharsets.UTF_8));
            JsonArray indexJson = JsonParser.parseReader(indexReader).getAsJsonArray();

            for (JsonElement element : indexJson) {
                resourceFiles.add(element.getAsString());
            }
            indexReader.close();

            // Process each JSON configuration file
            for (String resource : resourceFiles) {
                if (resource.endsWith(".json")) {
                    URL url = new URL("https://raw.githubusercontent.com/SpaceNerd24/Cosmic-Controllers/main/json/" + resource);
                    InputStream inputStream = url.openStream();

                    if (inputStream == null) {
                        throw new RuntimeException("Config file not found: " + resource);
                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

                    for (String controllerName : json.keySet()) {
                        JsonObject buttons = json.getAsJsonObject(controllerName);
                        Map<String, Integer> buttonMappings = new HashMap<>();

                        for (String buttonName : buttons.keySet()) {
                            buttonMappings.put(buttonName, buttons.get(buttonName).getAsInt());
                        }

                        Constants.controllerConfigs.put(controllerName, buttonMappings);
                    }

                    reader.close();
                }
            }

            Constants.LOGGER.info("Loaded controller configs");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load controller configs", e);
        }
    }
}
