package com.spacenerd24.cosmic_controllers.mixins;

import com.spacenerd24.cosmic_controllers.Constants;
import com.spacenerd24.cosmic_controllers.gamestates.ControllerConfigGameState;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.MainMenu;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.settings.Controls;
import finalforeach.cosmicreach.ui.UIElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MainMenu.class)
public class MainMenuMixin extends GameState {

    UIElement continueButon = new UIElement(0.0F, 270.0F, 275.0F, 35.0F) {
        public void onClick() {
            super.onClick();
            GameState.switchToGameState(new ControllerConfigGameState());
        }
    };

    @Inject(method = "create", at = @At("HEAD"))
    public void create0(CallbackInfo ci) {
        continueButon.setText("Controller Setup");
        continueButon.show();
        this.uiObjects.add(continueButon);
    }


    @Inject(method = "render", at = @At("HEAD"))
    public void render0(CallbackInfo ci) {
        if (Constants.activeController != null) {
            continueButon.hide();
        } else {
            continueButon.show();
        }
    }
}
