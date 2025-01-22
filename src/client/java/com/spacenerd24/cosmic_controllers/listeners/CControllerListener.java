package com.spacenerd24.cosmic_controllers.listeners;

import com.badlogic.gdx.controllers.*;
import com.spacenerd24.cosmic_controllers.Constants;

import com.spacenerd24.cosmic_controllers.utils.ControllerUtils;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CControllerListener implements ControllerListener {
    public CControllerListener() {
        ControllerUtils.loadControllerConfigs(Constants.controllerGUIDs);
    }

    private boolean isActiveController(Controller controller) {
        return Constants.activeController != null && controller.getUniqueId().equals(Constants.activeController);
    }

    private Map<String, Integer> getControllerConfig(String guid) {
        return Constants.controllerConfigs.getOrDefault(guid, Map.of());
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

        Map<String, Integer> config = getControllerConfig(controller.getUniqueId());
        if (config.isEmpty()) {
            Constants.LOGGER.warn("No configuration found for controller: {}, GUID: {}", controller.getName(), controller.getUniqueId());
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
        Map<String, Integer> config = getControllerConfig(controller.getUniqueId());
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

        Map<String, Integer> config = getControllerConfig(controller.getUniqueId());
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