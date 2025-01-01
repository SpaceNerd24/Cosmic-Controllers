package com.spacenerd24.cosmic_controllers.listeners;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.spacenerd24.cosmic_controllers.Constants;
import finalforeach.cosmicreach.entities.PlayerController;
import finalforeach.cosmicreach.entities.player.PlayerEntity;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.ui.UI;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class CControllerListener implements com.badlogic.gdx.controllers.ControllerListener {

    private boolean isActiveController(Controller controller) {
        return Constants.activeController != null && controller.getName().equals(Constants.activeController);
    }

    public void connected(Controller controller) {
        Constants.LOGGER.info("Controller connected: {}", controller.getName());
    }

    public void disconnected(Controller controller) {
        Constants.LOGGER.info("Controller disconnected: {}", controller.getName());
    }

    public boolean buttonDown(Controller controller, int buttonCode) {
        if (!isActiveController(controller)) {
            return false;
        }

        Constants.LOGGER.info("Button down: {} on {}", buttonCode, controller.getName());
        ControllerMapping mapping = controller.getMapping();

        Robot robot = Constants.robot;

        if (buttonCode == mapping.buttonB) {
            robot.keyPress(KeyEvent.VK_SHIFT);
        } else if (buttonCode == mapping.buttonLeftStick) {
            robot.keyPress(KeyEvent.VK_CONTROL);
        } else if (buttonCode == mapping.buttonA) {
            robot.keyPress(KeyEvent.VK_SPACE);
        } else if (buttonCode == mapping.buttonR1) {
            UI.hotbar.selectSlot((short) (UI.hotbar.getSelectedSlotNum() + 1));
        } else if (buttonCode == mapping.buttonL1) {
            UI.hotbar.selectSlot((short) (UI.hotbar.getSelectedSlotNum() - 1));
        } else if (buttonCode == mapping.buttonX) {
            UI.setInventoryOpen(!UI.isInventoryOpen());
        } else if (buttonCode == mapping.buttonRightStick) {
            robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
        } else if (buttonCode == mapping.buttonStart) {
            robot.keyPress(KeyEvent.VK_ESCAPE);
        }

        return false;
    }

    public boolean buttonUp(Controller controller, int buttonCode) {
        if (!isActiveController(controller)) {
            return false;
        }

        ControllerMapping mapping = controller.getMapping();

        Robot robot = Constants.robot;

        if (buttonCode == mapping.buttonB) {
            robot.keyRelease(KeyEvent.VK_SHIFT);
        } else if (buttonCode == mapping.buttonLeftStick) {
            robot.keyRelease(KeyEvent.VK_CONTROL);
        } else if (buttonCode == mapping.buttonA) {
            robot.keyRelease(KeyEvent.VK_SPACE);
        } else if (buttonCode == mapping.buttonRightStick) {
            robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
        }

        return false;
    }

    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if (!isActiveController(controller)) {
            return false;
        }

        if (false) {
            Constants.LOGGER.info("Axis moved: {} on {}, value: {}", axisCode, controller.getName(), value);
        }

        try {
            if (GameState.currentGameState == GameState.IN_GAME && UI.isInventoryOpen() || GameState.currentGameState != GameState.IN_GAME) {
                Robot robot = new Robot();
                Point mousePosition = MouseInfo.getPointerInfo().getLocation();

                final int sensitivityX = 25;
                final int sensitivityY = 25;

                if (axisCode == 2) {
                    int newX = mousePosition.x + (int) (value * sensitivityX);
                    robot.mouseMove(newX, mousePosition.y);
                } else if (axisCode == 3) {
                    int newY = mousePosition.y + (int) (value * sensitivityY);
                    robot.mouseMove(mousePosition.x, newY);
                }
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }

        if (axisCode == 5 && value <= Constants.limit) {
            try {
                Robot robot = new Robot();
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }

        if (axisCode == 4 && value <= Constants.limit) {
            try {
                Robot robot = new Robot();
                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}