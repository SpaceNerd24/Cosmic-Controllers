package com.spacenerd24.cosmic_controllers;

import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientModInitializer;
import com.spacenerd24.cosmic_controllers.utils.ControllerUtils;
import com.spacenerd24.cosmic_controllers.utils.ScreenUtilis;

import java.awt.*;

public class ClientInitializer implements ClientModInitializer {

    @Override
    public void onInit() {
        Constants.LOGGER.info("CosmicControllers Client Started");

        Constants.controllers = ControllerUtils.getControllers();

        Constants.LOGGER.info("Found {} controllers", Constants.controllers.size);
        for (int i = 0; i < Constants.controllers.size; i++) {
            Constants.LOGGER.info("Controller {}: {}", i, Constants.controllers.get(i).getName());
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

        ScreenUtilis.postScreenMessage("Cosmic Controllers Loaded", ScreenUtilis.MessageType.INFO);

        new Thread(() -> {
            try {
                Thread.sleep(6000);

                if (Constants.controllers.size == 0) {
                    ScreenUtilis.postScreenMessage("No Controllers Found", ScreenUtilis.MessageType.WARNING);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
