package com.spaceship.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.spaceship.game.MyGdxGame;
import com.spaceship.game.util.LinkedList;

public class Ship extends Actor {
    private float speed;
    private float shootingDelay;
    private float score;
    private float scorePerSecond;
    private float scoreDelay;
    private float scoreTimer;
    private final Animation[] animations;
    private float stateTime;
    private float tiltTimer;
    private int tilt = 2;
    private final LinkedList lasers = new LinkedList();
    private float shootingTimer = 0.0F;
    private final Polygon collisionBox = new Polygon();
    private float actualHealth;
    private float targetHealth;
    private final int offset = 4;
    float[] vertices = new float[]{(float)offset, 37.0F, (float)(64 - offset), 37.0F, (float)(64 - offset), 72.0F, 39.0F, 96.0F, 25.0F, 96.0F, (float)offset, 72.0F};

    public Ship(float y) {
        setY(y);
        setSize(64.0F, 96.0F);
        reset();
        animations = new Animation[5];
        Texture shipSheet = new Texture("ship.png");
        TextureRegion[][] tmp = TextureRegion.split(shipSheet, shipSheet.getWidth() / 2, shipSheet.getHeight() / 5);
        animations[0] = new Animation<>(0.1F, tmp[0]);
        animations[1] = new Animation<>(0.1F, tmp[1]);
        animations[3] = new Animation<>(0.1F, tmp[3]);
        animations[2] = new Animation<>(0.1F, tmp[2]);
        animations[4] = new Animation<>(0.1F, tmp[4]);
    }

    public void reset() {
        setX((float)(Gdx.graphics.getWidth() / 2 - 32));
        collisionBox.setVertices(vertices);
        actualHealth = targetHealth = 1.0F;
        lasers.removeAll();
        Laser.speed = 500.0F;
        speed = 275.0F;
        shootingDelay = 0.75F;
        score = 0.0F;
        scorePerSecond = 5.0F;
        scoreDelay = 0.2F;
        scoreTimer = 0.0F;
    }

    public LinkedList getLasers() {
        return lasers;
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        TextureRegion currentFrame = (TextureRegion)animations[tilt].getKeyFrame(stateTime, true);
        batch.draw(currentFrame, getX(), getY());
        batch.end();
        if (MyGdxGame.debug) {
            getCollisionBox();
            MyGdxGame.shapeRenderer.begin(ShapeType.Line);
            MyGdxGame.shapeRenderer.setColor(Color.GREEN);
            MyGdxGame.shapeRenderer.polygon(collisionBox.getTransformedVertices());
            MyGdxGame.shapeRenderer.setColor(Color.WHITE);
            MyGdxGame.shapeRenderer.end();
        }

        batch.begin();
    }

    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        shootingTimer -= delta;
        handleInput(delta);
        removeOffScreenLasers();
        updateScore(delta);
        actualHealth = MathUtils.lerp(actualHealth, targetHealth, 0.15F);
    }

    public Polygon getCollisionBox() {
        collisionBox.setPosition(getX(), getY());
        return collisionBox;
    }

    private void handleInput(float delta) {

        if ((Gdx.input.isKeyPressed(21) || Gdx.input.isKeyPressed(29)) && !Gdx.input.isKeyPressed(22) && !Gdx.input.isKeyPressed(32)) {
            moveBy(-speed * delta, 0.0F);
            setX(MathUtils.clamp(getX(), (float)(-offset), (float)Gdx.graphics.getWidth() - getWidth() + (float)offset));

            if ((Gdx.input.isKeyJustPressed(21) || Gdx.input.isKeyJustPressed(29)) && tilt > 0) {
                tiltTimer = 0.0F;
                tilt--;
            }
            else {
                tiltLeft(delta);
            }
        }
        else if (tilt < 2) {
           tiltRight(delta);
        }

        if ((Gdx.input.isKeyPressed(22) || Gdx.input.isKeyPressed(32)) && !Gdx.input.isKeyPressed(21) && !Gdx.input.isKeyPressed(29)) {
            moveBy(this.speed * delta, 0.0F);
            setX(MathUtils.clamp(getX(), (float)(-offset), (float)Gdx.graphics.getWidth() - getWidth() + (float)offset));

            if ((Gdx.input.isKeyJustPressed(22) || Gdx.input.isKeyJustPressed(32)) && tilt < 4) {
                tiltTimer = 0.0F;
                tilt++;
            }
            else {
                tiltRight(delta);
            }
        }
        else if (tilt > 2) {
            tiltLeft(delta);
        }
        if(shootingTimer < 0.0F){
            shoot();
            shootingTimer = shootingDelay;
        }
    }

    private void removeOffScreenLasers() {
        for(int j = 0; j < getLasers().getSize(); ++j) {
            Laser laser = (Laser)getLasers().get(j);
            if (laser.isOffScreen()) {
                laser.remove();
                getLasers().remove(laser);
            }
        }

    }

    private void updateScore(float delta) {
        scoreTimer -= delta;
        if (scoreTimer < 0.0F) {
            scoreTimer = scoreDelay;
            score += scorePerSecond * scoreDelay;
        }

    }

    private void shoot() {
        float[] pos = getLasersSpawnPos();
        Laser laser1 = new Laser(pos[0], pos[1]);
        getStage().addActor(laser1);
        lasers.add(laser1);
        Laser laser2 = new Laser(pos[2], pos[3]);
        getStage().addActor(laser2);
        lasers.add(laser2);
    }

    private void tiltLeft(float delta) {
        tiltTimer -= delta;
        if (Math.abs(tiltTimer) > 0.2F && tilt > 0) {
            tiltTimer = 0.0F;
            tilt--;
        }
    }

    private void tiltRight(float delta) {
        tiltTimer += delta;
        if (Math.abs(tiltTimer) > 0.2F && tilt < 4) {
            tiltTimer = 0.0F;
            tilt++;
        }
    }

    public float getHealth() {
        return actualHealth;
    }

    public void decreaseHealthBy(float amount) {
        targetHealth -= amount;
    }

    public void addHealth(float amount) {
        targetHealth += amount;
        targetHealth = MathUtils.clamp(targetHealth, 0.0F, 1.0F);
    }

    public boolean isDead() {
        return actualHealth <= 0.01F;
    }

    public int getScore() {
        return (int)score;
    }

    public void addScore(float amount) {
        score += amount;
    }

    public void decreaseScoreBy(float amount) {
        score -= amount;
        if (score < 0.0F) {
            score = 0.0F;
        }
    }

    public float getScorePerSecond() {
        return scorePerSecond;
    }

    public void setScorePerSecond(float scorePerSecond) {
        this.scorePerSecond = scorePerSecond;
    }

    private float[] getLasersSpawnPos() {
        return new float[]{
                getX() + 11.0F,
                getY() + 55.0F,
                getX() + getWidth() - 10.0F - 14.0F,
                getY() + 55.0F
        };
    }

    public void increaseSpeedBy(int percent) {
        speed += (float)percent / 100.0F * speed;
    }

    public void increaseShootingSpeedBy(int percent) {
        shootingDelay -= (float)percent / 100.0F * shootingDelay;
        Laser.speed += (float)percent / 100.0F * Laser.speed;
    }
}

