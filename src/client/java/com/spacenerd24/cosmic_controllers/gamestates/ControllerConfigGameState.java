package com.spacenerd24.cosmic_controllers.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.spacenerd24.cosmic_controllers.Constants;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.MainMenu;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public class ControllerConfigGameState extends GameState {
    int stage = 0;

    String controllerName;

    UIElement continueButon = new UIElement(0.0F, 265.0F, 250.0F, 50.0F) {
        public void onClick() {
            super.onClick();

            switch (stage) {
                case 0:
                    Constants.activeController = controllerName;
                    Constants.LOGGER.info("Selected controller: {}", Constants.activeController);
                    stage++;
                    break;
            }

            this.updateText();
        }

        public void updateText() {
            String string = "Continue: ";
            this.setText(string + GameSingletons.zoneRenderer.getName());
        }
    };

    UIElement returnButton = new UIElement(0.0F, 0.0F, 250.0F, 50.0F) {
        public void onClick() {
            super.onClick();
            GameState.switchToGameState(new MainMenu());
        }
    };

    @Override
    public void create() {
        super.create();
        continueButon.updateText();
        continueButon.show();
        this.uiObjects.add(continueButon);

        // Adds buttons according to the list of controllers
        for (int i = 0; i < Constants.controllers.size; i++) {
            int finalI = i;
            UIElement controllerButton = new UIElement(0.0F, -200.0F + (finalI * 55), 250.0F, 50.0F) {
                public void onClick() {
                    super.onClick();
                    if (Constants.activeController == null) {
                        controllerName = Constants.controllers.get(finalI).getName();
                        Constants.LOGGER.info("Selected controller TEMP: {}", controllerName);
                    }
                    this.updateText();
                }

                public void updateText() {
                    this.setText(Constants.controllers.get(finalI).getName());
                }
            };

            controllerButton.updateText();
            controllerButton.show();
            this.uiObjects.add(controllerButton);
        }
    }

    @Override
    public void render() {
        super.render();
        ScreenUtils.clear(0.145F, 0.078F, 0.153F, 1.0F, true);

        batch.setProjectionMatrix(this.uiCamera.combined);
        batch.begin();

        switch (stage) {
            case 0:
                FontRenderer.drawText(batch, this.uiViewport, "Select the controller you want to use.", -6.0F,  -265.0F, HorizontalAnchor.CENTERED, VerticalAnchor.CENTERED);
                break;
            case 1:
                reset();
                returnButton.show();
                returnButton.setText("Return");
                this.uiObjects.add(returnButton);
                FontRenderer.drawText(batch, this.uiViewport, "Setup is Complete", -10.0F,  -265.0F, HorizontalAnchor.CENTERED, VerticalAnchor.CENTERED);
                break;
        }

        batch.end();

        Gdx.gl.glEnable(2929);
        Gdx.gl.glDepthFunc(513);
        Gdx.gl.glEnable(2884);
        Gdx.gl.glCullFace(1029);
        Gdx.gl.glEnable(3042);
        Gdx.gl.glBlendFunc(770, 771);
        this.drawUIElements();
    }

    private void reset() {
        this.uiObjects.clear();
    }
}
