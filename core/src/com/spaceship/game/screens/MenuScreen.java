package com.spaceship.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.spaceship.game.MyGdxGame;


public class MenuScreen extends BaseScreen {
    private Music bgMusic;
    private Image menubg;

    public MenuScreen(MyGdxGame game) {
        super(game);
        Texture bgTexture = new Texture("menuscreen.png");
        menubg = new Image(bgTexture);
        menubg.setY(0.0F);
        stage.addActor(menubg);
        Label keybindsLabel = new Label("PRESS SPACE TO START THE GAME\n\n\nPRESS H TO SEE THE CURRENT\n\nHIGH SCORE", game.labelStyleMedium);
        keybindsLabel.setPosition(68.0F, 300.0F - keybindsLabel.getHeight() / 2.0F);
        stage.addActor(keybindsLabel);
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sound1.mp3"));
    }

    public void show() {
        super.show();
        bgMusic.play();
    }

    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(game.bgColor.r, game.bgColor.g, game.bgColor.b, game.bgColor.a);
        Gdx.gl.glClear(16384);
        stage.draw();
    }

    private void update(float delta) {
        stage.act(delta);
        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(62)) {
            bgMusic.stop();
            game.tapSound.play();
            game.setScreen(game.playScreen);
        }

        if (Gdx.input.isKeyJustPressed(36)) {
            game.tapSound.play();
            game.setScreen(game.highScoreScreen);
        }

    }

    public void dispose() {
        super.dispose();
        bgMusic.dispose();
    }
}

