package com.spaceship.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.spaceship.game.MyGdxGame;

public class HighScoreScreen extends BaseScreen {
    private Image menubg;

    public HighScoreScreen(MyGdxGame game) {
        super(game);
        Texture bgTexture = new Texture("menuscreen.png");
        this.menubg = new Image(bgTexture);
        this.menubg.setY(0.0F);
        this.stage.addActor(this.menubg);
        int highscore = game.preferences.getInteger("highscore");
        Label label = new Label("HIGHSCORE: " + highscore + "\n\n\nPRESS SPACE TO GO BACK TO THE\n\nMAIN MENU", game.labelStyleMedium);
        label.setPosition(68.0F, 300.0F - label.getHeight() / 2.0F);
        this.stage.addActor(label);
    }

    public void show() {
        super.show();
    }

    public void render(float delta) {
        this.update(delta);
        Gdx.gl.glClearColor(this.game.bgColor.r, this.game.bgColor.g, this.game.bgColor.b, this.game.bgColor.a);
        Gdx.gl.glClear(16384);
        super.render(delta);
        this.stage.draw();
    }

    private void update(float delta) {
        this.stage.act(delta);
        this.handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(62)) {
            this.game.tapSound.play();
            this.game.setScreen(this.game.menuScreen);
        }

    }
}

