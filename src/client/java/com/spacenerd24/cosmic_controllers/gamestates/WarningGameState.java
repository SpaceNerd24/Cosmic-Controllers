package com.spacenerd24.cosmic_controllers.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.spacenerd24.cosmic_controllers.Constants;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public class WarningGameState extends GameState {
    String warningMessage;

    @Override
    public void create() {
        super.create();
        warningMessage = Constants.activeWarning;

        if (warningMessage == null) {
            warningMessage = "No warning message provided";
        }
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.145F, 0.078F, 0.153F, 1.0F, true);

        batch.setProjectionMatrix(this.uiCamera.combined);
        batch.begin();

        FontRenderer.drawText(batch, this.uiViewport, "Teds", 190.0F,  100.0F, HorizontalAnchor.CENTERED, VerticalAnchor.CENTERED);

        batch.end();

        Gdx.gl.glEnable(2929);
        Gdx.gl.glDepthFunc(513);
        Gdx.gl.glEnable(2884);
        Gdx.gl.glCullFace(1029);
        Gdx.gl.glEnable(3042);
        Gdx.gl.glBlendFunc(770, 771);
        this.drawUIElements();
    }
}
