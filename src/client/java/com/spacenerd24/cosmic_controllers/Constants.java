package com.spacenerd24.cosmic_controllers;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.spacenerd24.cosmic_controllers.listeners.CControllerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class Constants {
    public static final String MOD_ID = "cosmic-controllers";
    public static final String MOD_NAME = "Cosmic Controllers";

    public static Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static Array<Controller> controllers;
    public static Array<CControllerListener> listeners = new Array<>();

    public static boolean precisionMode = true;
    public static float limit = 0.0F;

    public static String activeController = "PS5 Controller";
}
