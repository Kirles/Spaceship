package com.spaceship.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.spaceship.game.screens.GameOverScreen;
import com.spaceship.game.screens.HighScoreScreen;
import com.spaceship.game.screens.MenuScreen;
import com.spaceship.game.screens.PlayScreen;

public class MyGdxGame extends Game {
    public static boolean debug = false;
    public static ShapeRenderer shapeRenderer;
    public static LabelStyle labelStyleSmallGreen;
    public static LabelStyle labelStyleSmallRed;
    public SpriteBatch spriteBatch;
    public Color bgColor;
    public Color fontColor;
    public LabelStyle labelStyleMedium;
    public LabelStyle labelStyleBig;
    public Preferences preferences;
    public Sound tapSound;
    public MenuScreen menuScreen;
    public HighScoreScreen highScoreScreen;
    public PlayScreen playScreen;
    public GameOverScreen gameOverScreen;

    public void create() {
        shapeRenderer = new ShapeRenderer();
        labelStyleSmallGreen = new LabelStyle(new BitmapFont(Gdx.files.internal("font-small.fnt"), Gdx.files.internal("font-small.png"), false), new Color(0.047058824F, 0.98039216F, 0.09019608F, 1.0F));
        labelStyleSmallRed = new LabelStyle(labelStyleSmallGreen.font, new Color(1.0F, 0.0F, 0.0F, 1.0F));
        spriteBatch = new SpriteBatch();
        bgColor = new Color(0.53333336F, 0.40784314F, 0.24705882F, 1.0F);
        fontColor = new Color(0.87058824F, 0.78039217F, 0.6666667F, 1.0F);
        labelStyleMedium = new LabelStyle(new BitmapFont(Gdx.files.internal("font-medium.fnt")), fontColor);
        labelStyleBig = new LabelStyle(new BitmapFont(Gdx.files.internal("font-big.fnt")), fontColor);
        preferences = Gdx.app.getPreferences("Shooting Stars");
        tapSound = Gdx.audio.newSound(Gdx.files.internal("tap.mp3"));
        menuScreen = new MenuScreen(this);
        highScoreScreen = new HighScoreScreen(this);
        playScreen = new PlayScreen(this);
        gameOverScreen = new GameOverScreen(this);
        setScreen(menuScreen);
    }

    public void dispose() {
        labelStyleMedium.font.dispose();
    }
}

