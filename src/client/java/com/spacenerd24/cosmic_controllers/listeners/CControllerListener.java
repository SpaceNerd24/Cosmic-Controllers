package com.spacenerd24.cosmic_controllers.listeners;

import com.badlogic.gdx.controllers.*;
import com.google.gson.JsonObject;
import com.spacenerd24.cosmic_controllers.Constants;

import com.spacenerd24.cosmic_controllers.utils.ControllerUtils;
import org.jline.utils.InputStreamReader;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import static com.github.puzzle.util.ZipPack.GSON;

public class CControllerListener implements ControllerListener {
    private final Map<String, Map<String, Integer>> controllerConfigs = new HashMap<>();

    public CControllerListener() {
        ControllerUtils.loadControllerConfigs();
    }

    private boolean isActiveController(Controller controller) {
        return Constants.activeController != null && controller.getName().equals(Constants.activeController);
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