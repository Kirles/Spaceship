package com.spaceship.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.spaceship.game.MyGdxGame;

public class GameOverScreen extends BaseScreen {
    private final Label scoreLabel;
    private final Music bgMusic;
    private int score;

    public GameOverScreen(MyGdxGame game) {
        super(game);
        Texture bgTexture = new Texture("menuscreen.png");
        Image menubg = new Image(bgTexture);
        menubg.setY(0.0F);
        stage.addActor(menubg);
        Label gameOverLabel = new Label("GAME OVER", game.labelStyleBig);
        gameOverLabel.setPosition(142.0F, 450.0F);
        stage.addActor(gameOverLabel);
        scoreLabel = new Label("", game.labelStyleMedium);
        scoreLabel.setPosition(68.0F, 325.0F);
        stage.addActor(scoreLabel);
        Label infoLabel = new Label("PRESS SPACE TO GO BACK TO THE\n\nMAIN MENU", game.labelStyleMedium);
        infoLabel.setPosition(68.0F, scoreLabel.getY() - infoLabel.getHeight() - scoreLabel.getPrefHeight() * 2.0F);
        stage.addActor(infoLabel);
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("menu-sound.mp3"));
    }

    public void show() {
        super.show();
        scoreLabel.setText("SCORE: " + score);
        int highscore = game.preferences.getInteger("highscore");
        if (score > highscore) {
            scoreLabel.setText(scoreLabel.getText() + " (NEW BEST)");
            game.preferences.putInteger("highscore", this.score);
            game.preferences.flush();
        }

        stage.addAction(Actions.fadeOut(0.0F));
        stage.addAction(Actions.fadeIn(1.0F));
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
        if (Gdx.input.isKeyJustPressed(62)) {
            bgMusic.stop();
            game.tapSound.play();
            game.setScreen(game.menuScreen);
        }

    }

    void setScore(int score) {
        this.score = score;
    }
}

