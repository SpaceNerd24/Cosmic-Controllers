package com.spacenerd24.cosmic_controllers.utils;

import com.spacenerd24.cosmic_controllers.Constants;

public class ScreenUtilis {

    public static enum MessageType {
        INFO,
        WARNING,
        ERROR
    }

    public static MessageType messageType = MessageType.INFO;
    public static String screenMessage = "N/A";

    public static void postScreenMessage(String message, MessageType type) {
       Constants.LOGGER.info("Posting Screen Message: " + message);
       screenMessage = message;
       messageType = type;

       switch (type) {
           case ERROR:
               new Thread(() -> {
                   try {
                       Thread.sleep(10000);
                       screenMessage = "N/A";
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }).start();
           default:
               new Thread(() -> {
                   try {
                       Thread.sleep(5000);
                       screenMessage = "N/A";
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }).start();
       }
    }
}
