package com.spacenerd24.cosmic_controllers.listeners;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.google.gson.JsonObject;
import com.spacenerd24.cosmic_controllers.Constants;
import org.jline.utils.InputStreamReader;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.github.puzzle.util.ZipPack.GSON;

public class CControllerListenerNEW implements ControllerListener {
    private final Map<String, Map<String, Integer>> controllerConfigs = new HashMap<>();

    public CControllerListenerNEW() {
        loadControllerConfigs("assets/cosmic-controllers/json");
    }

    private boolean isActiveController(Controller controller) {
        return Constants.activeController != null && controller.getName().equals(Constants.activeController);
    }

    private void loadControllerConfigs(String configsDir) {
        try {
            ArrayList<String> resourceFiles = getResourceFiles(configsDir);

            for (String resource : resourceFiles) {
                if (resource.endsWith(".json")) {
                    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource);

                    if (inputStream == null) {
                        throw new RuntimeException("Config file not found: " + resource);
                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    JsonObject json = GSON.fromJson(reader, JsonObject.class);

                    for (String controllerName : json.keySet()) {
                        JsonObject buttons = json.getAsJsonObject(controllerName);
                        Map<String, Integer> buttonMappings = new HashMap<>();

                        for (String buttonName : buttons.keySet()) {
                            buttonMappings.put(buttonName, buttons.get(buttonName).getAsInt());
                        }

                        controllerConfigs.put(controllerName, buttonMappings);
                    }

                    reader.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load controller configs from directory: " + configsDir, e);
        }
    }

    private ArrayList<String> getResourceFiles(String path) throws IOException {
        ArrayList<String> fileList = new ArrayList<String>();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);

        if (inputStream == null) {
            throw new RuntimeException("Directory not found: " + path);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ((line = reader.readLine()) != null) {
            fileList.add(path + "/" + line);
        }

        reader.close();
        return fileList;
    }

    private Map<String, Integer> getControllerConfig(String controllerName) {
        return controllerConfigs.getOrDefault(controllerName, Map.of());
    }

    @Override
    public void connected(Controller controller) {
        Constants.LOGGER.info("Controller connected: {}", controller.getName());
    }

    @Override
    public void disconnected(Controller controller) {
        Constants.LOGGER.info("Controller disconnected: {}", controller.getName());
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (!isActiveController(controller)) {
            return false;
        }

        Constants.LOGGER.info("Button down: {} on {}", buttonCode, controller.getName());
        Robot robot = Constants.robot;

        Map<String, Integer> config = getControllerConfig(controller.getName());
        if (config.isEmpty()) {
            Constants.LOGGER.warn("No configuration found for controller: {}", controller.getName());
            return false;
        }

        try {
            if (buttonCode == config.getOrDefault("BUTTON_A", -1)) {
                robot.keyPress(KeyEvent.VK_SPACE);
            } else if (buttonCode == config.getOrDefault("BUTTON_B", -1)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
            } else if (buttonCode == config.getOrDefault("BUTTON_LEFTSTICK", -1)) {
                robot.keyPress(KeyEvent.VK_CONTROL);
            } else if (buttonCode == config.getOrDefault("BUTTON_RIGHTSTICK", -1)) {
                robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
            }
        } catch (Exception e) {
            Constants.LOGGER.error("Error handling button down: {}", e.getMessage());
        }

        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        if (!isActiveController(controller)) {
            return false;
        }

        Robot robot = Constants.robot;
        Map<String, Integer> config = getControllerConfig(controller.getName());
        if (config.isEmpty()) {
            return false;
        }

        try {
            if (buttonCode == config.getOrDefault("BUTTON_A", -1)) {
                robot.keyRelease(KeyEvent.VK_SPACE);
            } else if (buttonCode == config.getOrDefault("BUTTON_B", -1)) {
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (buttonCode == config.getOrDefault("BUTTON_RIGHTSTICK", -1)) {
                robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
            }
        } catch (Exception e) {
            Constants.LOGGER.error("Error handling button up: {}", e.getMessage());
        }

        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if (!isActiveController(controller)) {
            return false;
        }

        Robot robot = Constants.robot;
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();

        Map<String, Integer> config = getControllerConfig(controller.getName());
        if (config.isEmpty()) {
            return false;
        }

        try {
            if (axisCode == config.getOrDefault("AXIS_LEFTX", -1)) {
                int newX = mousePosition.x + (int) (value * 25);
                robot.mouseMove(newX, mousePosition.y);
            } else if (axisCode == config.getOrDefault("AXIS_LEFTY", -1)) {
                int newY = mousePosition.y + (int) (value * 25);
                robot.mouseMove(mousePosition.x, newY);
            }
        } catch (Exception e) {
            Constants.LOGGER.error("Error handling axis move: {}", e.getMessage());
        }

        return false;
    }
}