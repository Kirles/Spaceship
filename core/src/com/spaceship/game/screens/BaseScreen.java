package com.spaceship.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.spaceship.game.MyGdxGame;

class BaseScreen implements Screen {

    MyGdxGame game;
    Stage stage;

    BaseScreen(MyGdxGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport(), game.spriteBatch);
    }

    public void show() {
    }

    public void render(float delta) {
    }

    public void resize(int width, int height) {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void hide() {
    }

    public void dispose() {
    }
}

