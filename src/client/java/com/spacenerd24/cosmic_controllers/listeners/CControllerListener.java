package com.spacenerd24.cosmic_controllers.listeners;

import com.badlogic.gdx.controllers.*;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.spacenerd24.cosmic_controllers.Constants;
import java.awt.*;
import java.awt.event.*;

public class CControllerListener implements ControllerListener {
    public CControllerListener() {
        Constants.LOGGER.info("Controller listener initialized.");
    }

    private boolean isActiveController(Controller controller) {
        return Constants.activeController != null && controller.getUniqueId().equals(Constants.activeController);
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
        ControllerMapping mapping = controller.getMapping();

        try {
            if (buttonCode == mapping.buttonA) { // Maps to Spacebar
                robot.keyPress(KeyEvent.VK_SPACE);
            } else if (buttonCode == mapping.buttonB) { // Maps to Shift
                robot.keyPress(KeyEvent.VK_SHIFT);
            } else if (buttonCode == mapping.buttonBack) { // Maps to Control
                robot.keyPress(KeyEvent.VK_CONTROL);
            } else if (buttonCode == mapping.buttonStart) { // Maps to Right Mouse Click
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
        ControllerMapping mapping = controller.getMapping();

        try {
            if (buttonCode == mapping.buttonA) { // Maps to Spacebar
                robot.keyRelease(KeyEvent.VK_SPACE);
            } else if (buttonCode == mapping.buttonB) { // Maps to Shift
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (buttonCode == mapping.buttonStart) { // Maps to Right Mouse Click
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
        ControllerMapping mapping = controller.getMapping();

        try {
            if (axisCode == mapping.axisLeftX) { // Maps to horizontal mouse movement
                int newX = mousePosition.x + (int) (value * 25);
                robot.mouseMove(newX, mousePosition.y);
            } else if (axisCode == mapping.axisLeftY) { // Maps to vertical mouse movement
                int newY = mousePosition.y + (int) (value * -25);
                robot.mouseMove(mousePosition.x, newY);
            }
        } catch (Exception e) {
            Constants.LOGGER.error("Error handling axis move: {}", e.getMessage());
        }

        return false;
    }
}