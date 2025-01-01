package com.spacenerd24.cosmic_controllers.listeners;

import com.badlogic.gdx.controllers.Controller;
import com.spacenerd24.cosmic_controllers.Constants;
import finalforeach.cosmicreach.entities.PlayerController;
import finalforeach.cosmicreach.entities.player.PlayerEntity;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.ui.UI;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class CControllerListener implements com.badlogic.gdx.controllers.ControllerListener {
    
    public void connected(Controller controller) {
        Constants.LOGGER.info("Controller connected: {}", controller.getName());
    }

    public void disconnected(Controller controller) {
        Constants.LOGGER.info("Controller disconnected: {}", controller.getName());
    }

    public boolean buttonDown(Controller controller, int buttonCode) {
        Constants.LOGGER.info("Button down: {} on {}", buttonCode, controller.getName());


        switch (buttonCode) {
            case 0:
                // Jump
                try {
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_SPACE);
                } catch (AWTException e) {
                    e.printStackTrace();
                }                break;
            case 1:
                // Crouch
                try {
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_SHIFT);
                } catch (AWTException e) {
                    e.printStackTrace();
                }

                break;
            case 7:
                try {
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_CONTROL);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                break;
            case 10:
                // Right Trigger
                UI.hotbar.selectSlot((short) (UI.hotbar.getSelectedSlotNum() + 1));
                break;
            case 9:
                // Left Trigger
                UI.hotbar.selectSlot((short) (UI.hotbar.getSelectedSlotNum() - 1));
                break;
            case 3:
                // Triangle

                break;
            case 2:
                // Square
                if (UI.isInventoryOpen()) {
                    UI.setInventoryOpen(false);
                } else {
                    UI.setInventoryOpen(true);
                }
                break;
            case 8:
                try {
                    Robot robot = new Robot();
                    robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            case 6:
                // Pause
        }

        return false;
    }
    
    public boolean buttonUp(Controller controller, int buttonCode) {
//        Constants.LOGGER.info("Button up: {} on {}", buttonCode, controller.getName());

        switch (buttonCode) {
            case 1:
                try {
                    Robot robot = new Robot();
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } catch (AWTException e) {
                    e.printStackTrace();
                }

                break;
            case 7:
                try {
                    Robot robot = new Robot();
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                break;
            case 0:
                try {
                    Robot robot = new Robot();
                    robot.keyRelease(KeyEvent.VK_SPACE);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                try {
                    Robot robot = new Robot();
                    robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
        }

        return false;
    }
    
    public boolean axisMoved(Controller controller, int axisCode, float value) {
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
