package com.spacenerd24.cosmic_controllers.mixins;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.utils.Array;
import com.spacenerd24.cosmic_controllers.Constants;
import finalforeach.cosmicreach.settings.Controls;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Controls.class)
public class ControlMixin {

    @Inject(method = "buttonPressed", at = @At("HEAD"), cancellable = true)
    private static void onButtonPressed(Controls.ButtonMappingOperator op, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "buttonJustPressed", at = @At("HEAD"), cancellable = true)
    private static void onButtonJustPressed(Controls.ButtonMappingOperator op, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private static void onUpdate(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "axisPositive", at = @At("HEAD"), cancellable = true)
    private static void onAxisPositive(Controls.ButtonMappingOperator op, CallbackInfoReturnable<Float> cir) {
        Array.ArrayIterator<Controller> controllersIterator = Controls.controllers.iterator();

        float a;
        while (controllersIterator.hasNext()) {
            Controller c = controllersIterator.next();
            if (!Objects.equals(c.getName(), Constants.activeController)) {
                continue;
            } else {
                ControllerMapping mapping = c.getMapping();
                a = c.getAxis(op.getInt(mapping));
                if (a > 0.05F) {
                    cir.setReturnValue(a);
                    return;
                }
            }
        }

        cir.setReturnValue(0.0F);
    }

    @Inject(method = "axisNegative", at = @At("HEAD"), cancellable = true)
    private static void onAxisNegative(Controls.ButtonMappingOperator op, CallbackInfoReturnable<Float> cir) {
        Array.ArrayIterator<Controller> controllersIterator = Controls.controllers.iterator();

        float a;
        while (controllersIterator.hasNext()) {
            Controller c = controllersIterator.next();
            ControllerMapping mapping = c.getMapping();
            if (!Objects.equals(c.getName(), Constants.activeController)) {
                continue;
            }
            a = c.getAxis(op.getInt(mapping));
            if (a < -0.05F) {
                cir.setReturnValue(-a);
                return;
            }
        }

        cir.setReturnValue(0.0F);
    }

    @Inject(method = "getRightXAxis", at = @At("HEAD"), cancellable = true)
    private static void onGetRightXAxis(CallbackInfoReturnable<Float> cir) {
        float x = 0.0F;
        Array.ArrayIterator<Controller> controllersIterator = Controls.controllers.iterator();

        while (controllersIterator.hasNext()) {
            Controller c = controllersIterator.next();
            if (!Objects.equals(c.getName(), Constants.activeController)) {
                continue;
            }
            float curX = c.getAxis(c.getMapping().axisRightX);
            if (Math.abs(curX) > 0.05F) {
                x += curX;
            }
        }

        cir.setReturnValue(x);
    }

    @Inject(method = "getRightYAxis", at = @At("HEAD"), cancellable = true)
    private static void onGetRightYAxis(CallbackInfoReturnable<Float> cir) {
        float y = 0.0F;
        Array.ArrayIterator<Controller> controllersIterator = Controls.controllers.iterator();

        while (controllersIterator.hasNext()) {
            Controller c = controllersIterator.next();
            if (!Objects.equals(c.getName(), Constants.activeController)) {
                continue;
            }
            float curY = c.getAxis(c.getMapping().axisRightY);
            if (Math.abs(curY) > 0.05F) {
                y += curY;
            }
        }

        cir.setReturnValue(y);
    }
}