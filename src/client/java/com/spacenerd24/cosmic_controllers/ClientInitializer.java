package com.spacenerd24.cosmic_controllers;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.utils.Array;
import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientModInitializer;
import com.spacenerd24.cosmic_controllers.utils.ControllerUtils;
import com.spacenerd24.cosmic_controllers.utils.ScreenUtilis;
import finalforeach.cosmicreach.gamestates.GameState;

import java.awt.*;

import static com.spacenerd24.cosmic_controllers.utils.ControllerUtils.getControllers;

public class ClientInitializer implements ClientModInitializer {

    @Override
    public void onInit() {
        Constants.LOGGER.info("CosmicControllers Client Started");

        Constants.controllers = getControllers();

        Constants.LOGGER.info("Found {} controllers", Constants.controllers.size);
        for (int i = 0; i < Constants.controllers.size; i++) {
            Constants.LOGGER.info("Controller {}: {}", Constants.controllers.get(i).get, Constants.controllers.get(i).getName());
            ControllerUtils.addListener(Constants.controllers.get(i));
        }

        if (!Constants.precisionMode) {
            Constants.limit = 0.05F;
        }

        try {
            Constants.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                ScreenUtilis.postScreenMessage("Cosmic Controllers Loaded", ScreenUtilis.MessageType.INFO);

                if (Constants.controllers.size == 0) {
                    ScreenUtilis.postScreenMessage("No Controllers Found", ScreenUtilis.MessageType.WARNING);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
