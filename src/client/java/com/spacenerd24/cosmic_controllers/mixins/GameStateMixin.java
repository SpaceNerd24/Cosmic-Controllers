package com.spacenerd24.cosmic_controllers.mixins;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.spacenerd24.cosmic_controllers.Constants;
import com.spacenerd24.cosmic_controllers.utils.ScreenUtilis;
import com.sun.tools.javac.Main;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.MainMenu;
import finalforeach.cosmicreach.settings.Controls;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(MainMenu.class)
public class GameStateMixin extends GameState {

    @Inject(method = "render", at = @At("TAIL"))
    private void render0(CallbackInfo ci) {
        batch.begin();

        String message = ScreenUtilis.screenMessage;


        switch (ScreenUtilis.messageType) {
            case INFO:
                batch.setColor(Color.WHITE);
                message = "[INFO] " + message;
                break;
            case WARNING:
                batch.setColor(Color.YELLOW);
                message = "[WARNING] " + message;
                break;
            case ERROR:
                batch.setColor(Color.RED);
                message = "[ERROR] " + message;
                break;
        }

        if (!Objects.equals(ScreenUtilis.screenMessage, "N/A")) {
            FontRenderer.drawText(batch, this.uiViewport, message, 0.0F, -260.0F, HorizontalAnchor.CENTERED, VerticalAnchor.CENTERED);
        }

        batch.setColor(Color.WHITE);

        batch.end();
    }

    @Inject(method = "create", at = @At("TAIL"))
    private void create0(CallbackInfo ci) {
        //ScreenUtilis.postScreenMessage("Cosmic Controllers Loaded", ScreenUtilis.MessageType.INFO);
    }
}
