package com.spacenerd24.cosmic_controllers.utils;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;
import com.spacenerd24.cosmic_controllers.Constants;
import com.spacenerd24.cosmic_controllers.listeners.CControllerListener;
import com.spacenerd24.cosmic_controllers.listeners.CControllerListenerNEW;
import org.checkerframework.checker.units.qual.C;

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
        CControllerListenerNEW listener = new CControllerListenerNEW();
        controller.addListener(listener);
        Constants.listeners.add(listener);
        Constants.LOGGER.info("Added listener for controller: {}", controller.getName());
    }

//    public static void removeListener(Controller controller) {
//        for (CControllerListener listener : Constants.listeners) {
//            controller.removeListener(listener);
//            Constants.LOGGER.info("Removed listener for controller: {}", controller.getName());
//            Constants.listeners.removeValue(listener, true);
//        }
//    }
}
