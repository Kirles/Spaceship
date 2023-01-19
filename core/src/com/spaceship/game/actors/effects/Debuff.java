package com.spaceship.game.actors.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.spaceship.game.MyGdxGame;
import com.spaceship.game.actors.Asteroid;
import com.spaceship.game.actors.Ship;
import com.spaceship.game.screens.PlayScreen;

public class Debuff extends Effect {
    private static Sound collectSound;
    private Ship ship;

    public Debuff(Ship ship) {
        super(PlayScreen.debuffTexture, ship.getX());
        this.ship = ship;
        collectSound = Gdx.audio.newSound(Gdx.files.internal("collect-debuff.mp3"));
    }

    public void take() {
        collectSound.play(1.0F);
        String text = "";
        int random = MathUtils.random(1, 5);
        int percent;
        if (random == 1) {
            percent = MathUtils.random(6, 12) * 100;
            this.ship.decreaseScoreBy((float)percent);
            text = "-" + percent + " SCORE";
        } else if (random == 2) {
            percent = MathUtils.random(6, 12);
            Asteroid.makeAsteroidsSmallerBy(-percent);
            text = percent + "% BIGGER\nASTEROIDS";
        } else if (random == 3) {
            percent = MathUtils.random(2, 3) * 10;
            this.ship.increaseSpeedBy(-percent);
            text = "-" + percent + "% STEERING\nSPEED";
        } else if (random == 4) {
            percent = MathUtils.random(5, 15);
            this.ship.increaseShootingSpeedBy(-percent);
            text = "-" + percent + "% SHOOTING\nSPEED";
        } else if (random == 5) {
            percent = MathUtils.random(5, 15);
            Asteroid.setDamagePercent(Asteroid.getDamagePercent() + (float)percent / 100.0F);
            text = "-" + percent + "% DEFENSE";
        }

        super.take(this.ship, text, MyGdxGame.labelStyleSmallRed);
    }
}

