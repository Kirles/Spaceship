package com.spaceship.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.spaceship.game.MyGdxGame;
import com.spaceship.game.actors.*;
import com.spaceship.game.actors.effects.Buff;
import com.spaceship.game.actors.effects.Debuff;
import com.spaceship.game.actors.effects.Effect;
import com.spaceship.game.util.LinkedList;

public class PlayScreen extends BaseScreen {
    public static Texture buffTexture;
    public static Texture debuffTexture;
    private final Texture pixelTexture;
    private float cloudSpeed;
    private float asteroidsSpawnTimer;
    private float asteroids2SpawnTimer;
    private float increaseDifficultyDelay;
    private float increaseDifficultyTimer;
    private float effectDelay;
    private float effectsSpawnTimer;
    private final Image bg;
    private final Image cloud1;
    private final Image cloud2;
    private final Image healthBar;
    private final Ship ship;
    private final LinkedList asteroids;
    private final LinkedList asteroids2;
    private final LinkedList effects;
    private final Label healthLabel;
    private final Label scoreLabel;
    private final Music bgMusic;

    public PlayScreen(MyGdxGame game) {
        super(game);
        buffTexture = new Texture("buff.png");
        debuffTexture = new Texture("debuff.png");
        Texture bgTexture = new Texture("background.png");
        bg = new Image(bgTexture);
        Texture cloudTexture = new Texture("clouds.png");
        cloud1 = new Image(cloudTexture);
        cloud2 = new Image(cloudTexture);
        ship = new Ship(40.0F);
        asteroids = new LinkedList();
        asteroids2 = new LinkedList();
        effects = new LinkedList();
        pixelTexture = new Texture("pixel.png");
        healthBar = new Image(this.pixelTexture);
        healthBar.setPosition(0.0F, 0.0F);
        healthBar.setColor(Color.GREEN);
        healthLabel = new Label("HEALTH", game.labelStyleMedium);
        healthLabel.setPosition(3.0F, 13.0F);
        scoreLabel = new Label("0", game.labelStyleBig);
        scoreLabel.setY(500.0F);
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("play.mp3"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.4F);
    }

    public void show() {
        super.show();
        stage.clear();
        bg.setY(0.0F);
        stage.addActor(bg);
        stage.addActor(cloud1);
        cloud2.setY(cloud1.getY() + cloud1.getHeight() + 300.0F);
        stage.addActor(cloud2);
        effects.removeAll();
        effectsSpawnTimer = 7.0F;
        effectDelay = 4.0F;
        asteroids.removeAll();
        asteroidsSpawnTimer = 2.5F;
        asteroids2.removeAll();
        asteroids2SpawnTimer = 2.5F;
        ship.reset();
        stage.addActor(ship);
        increaseDifficultyTimer = 0.0F;
        increaseDifficultyDelay = 5.0F;
        healthBar.setSize((float)Gdx.graphics.getWidth(), 10.0F);
        stage.addActor(healthBar);
        stage.addActor(healthLabel);
        stage.addActor(scoreLabel);
        bgMusic.setVolume(2.0F);
        bgMusic.play();
        stage.addAction(Actions.fadeOut(0.0F));
        stage.addAction(Actions.fadeIn(1.0F));
        healthLabel.addAction(Actions.after(Actions.sequence(Actions.delay(5.0F), Actions.fadeOut(1.0F))));
    }

    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(game.bgColor.r, game.bgColor.g, game.bgColor.b, game.bgColor.a);
        Gdx.gl.glClear(16384);
        stage.draw();
    }

    private void update(float delta) {
        stage.act(delta);
        updateClouds(delta);
        spawnEffects(delta);
        spawnAsteroids(delta);
        removeOffScreenAsteroids();
        removeOffScreenEffects();
        handleCollisions();
        increaseDifficulty(delta);
        updateHealth();
        updateScoreLabel();
    }

    private void updateClouds(float delta) {
        cloud1.toFront();
        cloud2.toFront();
        cloud1.addAction(Actions.moveBy(0.0F, -cloudSpeed * delta));
        cloud2.addAction(Actions.moveBy(0.0F, -cloudSpeed * delta));
        if (cloud1.getY() + cloud1.getHeight() < 0.0F) {
            cloud1.setY(cloud2.getY() + cloud2.getHeight() + 300.0F);
        }

        if (cloud2.getY() + cloud2.getHeight() < 0.0F) {
            cloud2.setY(cloud1.getY() + cloud1.getHeight() + 300.0F);
        }

    }

    private void spawnEffects(float delta) {
        effectsSpawnTimer -= delta;
        if (effectsSpawnTimer < 0.0F) {
            effectsSpawnTimer = effectDelay;
            int random = MathUtils.random(1, 3);
            if (random == 1) {
                Debuff debuff = new Debuff(ship);
                effects.add(debuff);
                stage.addActor(debuff);
            } else {
                Buff buff = new Buff(ship);
                effects.add(buff);
                stage.addActor(buff);
            }

            healthBar.toFront();
            scoreLabel.toFront();
        }

    }

    private void spawnAsteroids(float delta) {
        asteroidsSpawnTimer -= delta;
        asteroids2SpawnTimer -= delta;
        if (asteroidsSpawnTimer < 0.0F) {
            Asteroid asteroid = new Asteroid();
            asteroids.add(asteroid);
            stage.addActor(asteroid);
            asteroid.toBack();
            asteroidsSpawnTimer = MathUtils.random(Asteroid.minSpawnDelay, Asteroid.maxSpawnDelay);
        }
        if (asteroids2SpawnTimer < 0.0F) {
            Asteroid2 asteroid2 = new Asteroid2();
            asteroids2.add(asteroid2);
            stage.addActor(asteroid2);
            asteroid2.toBack();
            asteroids2SpawnTimer = MathUtils.random(Asteroid2.minSpawnDelay, Asteroid2.maxSpawnDelay);
        }

        ship.toBack();
        bg.toBack();

    }

    private void removeOffScreenAsteroids() {
        for(int i = 0; i < asteroids.getSize(); ++i) {
            Asteroid asteroid = (Asteroid)asteroids.get(i);
            if (asteroid.isOffScreen()) {
                asteroid.remove();
                asteroids.remove(asteroid);
            }
        }
        for(int i = 0; i < asteroids2.getSize(); ++i) {
            Asteroid2 asteroid2 = (Asteroid2)asteroids2.get(i);
            if (asteroid2.isOffScreen()) {
                asteroid2.remove();
                asteroids2.remove(asteroid2);
            }
        }
    }

    private void removeOffScreenEffects() {
        for(int i = 0; i < effects.getSize(); ++i) {
            Effect effect = (Effect)effects.get(i);
            if (effect.isOffScreen()) {
                effect.remove();
                effects.remove(effect);
            }
        }

    }

    private void handleCollisions() {
        int i;
        for(i = 0; i < asteroids.getSize(); ++i) {
            Asteroid asteroid = (Asteroid)asteroids.get(i);
            if (Intersector.overlapConvexPolygons(ship.getCollisionBox(), asteroid.getCollisionBox())) {
                Explosion explosion = new Explosion(asteroid.getX(), asteroid.getY(), asteroid.getWidth(), asteroid.getHeight(), Asteroid.getSpeed());
                stage.addActor(explosion);
                ship.decreaseHealthBy(asteroid.getDamage());
                asteroid.remove();
                asteroids.remove(asteroid);
            }
            else {
                for(int j = 0; j < ship.getLasers().getSize(); ++j) {
                    Laser laser = (Laser)ship.getLasers().get(j);
                    if (Intersector.overlapConvexPolygons(laser.getCollisionBox(), asteroid.getCollisionBox())) {
                        Explosion explosion = new Explosion(asteroid.getX(), asteroid.getY(), asteroid.getWidth(), asteroid.getHeight(), Asteroid.getSpeed());
                        stage.addActor(explosion);
                        ship.addScore(asteroid.getScore());
                        Label scoreObtained = new Label("+" + (int)asteroid.getScore() + "", game.labelStyleMedium);
                        scoreObtained.setPosition(asteroid.getX() + asteroid.getWidth() / 2.0F - scoreObtained.getPrefWidth() / 2.0F, asteroid.getY() + asteroid.getHeight() / 2.0F);
                        stage.addActor(scoreObtained);
                        scoreObtained.addAction(Actions.parallel(Actions.moveBy(0.0F, -Asteroid.getSpeed() + 20.0F, 1.5F), Actions.fadeOut(1.5F)));
                        asteroid.remove();
                        asteroids.remove(asteroid);
                        laser.remove();
                        ship.getLasers().remove(laser);
                        break;
                    }
                }
            }
        }

        for(i = 0; i < asteroids2.getSize(); ++i) {
            Asteroid2 asteroid2 = (Asteroid2)asteroids2.get(i);
            if (Intersector.overlapConvexPolygons(ship.getCollisionBox(), asteroid2.getCollisionBox())) {
                Explosion explosion = new Explosion(asteroid2.getX(), asteroid2.getY(), asteroid2.getWidth(), asteroid2.getHeight(), Asteroid2.getSpeed());
                stage.addActor(explosion);
                ship.decreaseHealthBy(asteroid2.getDamage());
                asteroid2.remove();
                asteroids2.remove(asteroid2);
            }
            else {
                for(int j = 0; j < ship.getLasers().getSize(); ++j) {
                    Laser laser = (Laser)ship.getLasers().get(j);
                    if (Intersector.overlapConvexPolygons(laser.getCollisionBox(), asteroid2.getCollisionBox())) {
                        if(asteroid2.hp == 1) {
                            Explosion explosion = new Explosion(asteroid2.getX(), asteroid2.getY(), asteroid2.getWidth(), asteroid2.getHeight(), Asteroid2.getSpeed());
                            stage.addActor(explosion);
                            ship.addScore(asteroid2.getScore());
                            Label scoreObtained = new Label("+" + (int) asteroid2.getScore() + "", game.labelStyleMedium);
                            scoreObtained.setPosition(asteroid2.getX() + asteroid2.getWidth() / 2.0F - scoreObtained.getPrefWidth() / 2.0F, asteroid2.getY() + asteroid2.getHeight() / 2.0F);
                            stage.addActor(scoreObtained);
                            scoreObtained.addAction(Actions.parallel(Actions.moveBy(0.0F, -Asteroid.getSpeed() + 20.0F, 1.5F), Actions.fadeOut(1.5F)));
                            asteroid2.remove();
                            asteroids2.remove(asteroid2);
                            laser.remove();
                            ship.getLasers().remove(laser);
                            break;
                        }
                        if(asteroid2.hp == 2){
                            asteroid2.hp--;
                            laser.remove();
                            ship.getLasers().remove(laser);
                            break;
                        }
                    }
                }
            }
        }

        for(i = 0; i < effects.getSize(); ++i) {
            Effect effect = (Effect)effects.get(i);
            if (Intersector.overlapConvexPolygons(ship.getCollisionBox(), effect.getCollisionBox())) {
                if (effect.getClass() == Buff.class) {
                    ((Buff)effect).take();
                } else if (effect.getClass() == Debuff.class) {
                    ((Debuff)effect).take();
                }

                effects.remove(effect);
                effect.remove();
            }
        }

    }

    private void increaseDifficulty(float delta) {
        this.increaseDifficultyTimer -= delta;
        if (increaseDifficultyTimer < 0.0F) {
            cloudSpeed += 10.0F;
            Asteroid.setSpeed(Asteroid.getSpeed() + 10.0F);
            Asteroid2.setSpeed(Asteroid2.getSpeed() + 10.0F);
            if (Asteroid.minSpawnDelay > 0.003F) {
                Asteroid.minSpawnDelay -= 0.003F;
                Asteroid.maxSpawnDelay -= 0.003F;
                Asteroid2.minSpawnDelay -= 0.003F;
                Asteroid2.maxSpawnDelay -= 0.003F;
            }

            increaseDifficultyTimer = increaseDifficultyDelay;
        }

    }

    private void updateHealth() {
        healthBar.setWidth((float)Gdx.graphics.getWidth() * ship.getHealth());
        if (ship.isDead()) {
            bgMusic.stop();
            game.gameOverScreen.setScore(ship.getScore());
            game.setScreen(game.gameOverScreen);
        }

    }

    private void updateScoreLabel() {
        scoreLabel.toFront();
        scoreLabel.setText(ship.getScore() + "");
        scoreLabel.setX(300.0F - scoreLabel.getPrefWidth() / 2.0F);
    }

    public void dispose() {
        super.dispose();
        Asteroid.dispose();
        Asteroid2.dispose();
        Laser.dispose();
        pixelTexture.dispose();
    }
}

