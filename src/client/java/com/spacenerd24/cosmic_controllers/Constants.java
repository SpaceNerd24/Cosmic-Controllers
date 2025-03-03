package com.spacenerd24.cosmic_controllers;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.utils.Array;
import com.spacenerd24.cosmic_controllers.listeners.CControllerListener;
import com.spacenerd24.cosmic_controllers.utils.ScreenUtilis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Constants {
    public static final String MOD_ID = "cosmic-controllers";
    public static final String MOD_NAME = "Cosmic Controllers";

    public static Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static Array<Controller> controllers;
//    public static Array<CControllerListener> listeners = new Array<>();
    public static Array<CControllerListener> listeners = new Array<>();

    public static boolean precisionMode = true;
    public static boolean isDebug = false;
    public static float limit = 0.0F;

    public static Robot robot;

    public static String activeController = "PS5 Controller";
    public static String activeWarning;

    public static Map<String, Map<String, Integer>> controllerConfigs = new HashMap<>();

    public static final BlockingQueue<ScreenUtilis.Message> messageQueue = new LinkedBlockingQueue<>();
    public static List<String> controllerGUIDs;
}
