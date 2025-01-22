package com.spacenerd24.cosmic_controllers.utils;

import com.spacenerd24.cosmic_controllers.Constants;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ScreenUtilis {

    public static enum MessageType {
        INFO,
        WARNING,
        ERROR
    }

    public static class Message {
        String content;
        MessageType type;

        public Message(String content, MessageType type) {
            this.content = content;
            this.type = type;
        }
    }

    public static volatile boolean running = true;

    public static volatile MessageType currentMessageType = MessageType.INFO;
    public static volatile String currentScreenMessage = "N/A";

    static {
        new Thread(() -> {
            while (running) {
                try {
                    Message message = Constants.messageQueue.take();
                    currentScreenMessage = message.content;
                    currentMessageType = message.type;

                    if (currentMessageType == MessageType.ERROR) {
                        Thread.sleep(8000);
                    } else {
                        Thread.sleep(5000);
                    }

                    if (Constants.messageQueue.isEmpty()) {
                        currentScreenMessage = "N/A";
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void postScreenMessage(String message, MessageType type) {
        Constants.LOGGER.info("Queueing Screen Message: " + message);
        Constants.messageQueue.add(new Message(message, type));
    }

    public static void shutdown() {
        running = false;
    }

    public static void start() {
        running = true;
    }
}
