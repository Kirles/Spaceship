package com.spaceship.game.actors.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.spaceship.game.MyGdxGame;
import com.spaceship.game.actors.Asteroid;
import com.spaceship.game.actors.Asteroid2;
import com.spaceship.game.actors.Ship;
import com.spaceship.game.screens.PlayScreen;

public class Buff extends Effect {
    private static Sound collectSound;
    private final Ship ship;

    public Buff(Ship ship) {
        super(PlayScreen.buffTexture, (float)MathUtils.random(-20, 580));
        this.ship = ship;
        collectSound = Gdx.audio.newSound(Gdx.files.internal("collect-buff.mp3"));
    }

    public void take() {
        collectSound.play(1.0F);
        String text = "";
        int random = MathUtils.random(1, 7);
        int percent;
        if (random == 1) {
            percent = MathUtils.random(2, 4) * 10;
            ship.addHealth((float)percent / 100.0F);
            text = "+" + percent + " HP";
        } else if (random == 2) {
            percent = MathUtils.random(3, 6);
            Asteroid.makeAsteroidsSmallerBy(percent);
            Asteroid2.makeAsteroidsSmallerBy(percent);
            text = percent + "% SMALLER\nASTEROIDS";
        } else if (random == 3) {
            percent = MathUtils.random(7, 15) * 100;
            ship.addScore((float)percent);
            text = "+" + percent + " SCORE";
        } else if (random == 4) {
            percent = MathUtils.random(2, 4) * 10;
            ship.setScorePerSecond(this.ship.getScorePerSecond() + (float)percent / 100.0F * this.ship.getScorePerSecond());
            text = "+" + percent + "% SCORE\nPER SECOND";
        } else if (random == 5) {
            percent = MathUtils.random(2, 3) * 10;
            ship.increaseSpeedBy(percent);
            text = "+" + percent + "% STEERING\nSPEED";
        } else if (random == 6) {
            percent = MathUtils.random(5, 10);
            ship.increaseShootingSpeedBy(percent);
            text = "+" + percent + "% SHOOTING\nSPEED";
        } else if (random == 7) {
            percent = MathUtils.random(5, 10);
            Asteroid.setDamagePercent(Asteroid.getDamagePercent() - (float)percent / 100.0F);
            Asteroid2.setDamagePercent(Asteroid2.getDamagePercent() - (float)percent / 100.0F);
            text = "+" + percent + "% DEFENSE";
        }

        super.take(ship, text, MyGdxGame.labelStyleSmallGreen);
    }
}
