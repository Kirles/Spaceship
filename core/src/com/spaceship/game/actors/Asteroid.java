package com.spaceship.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.spaceship.game.MyGdxGame;

public class Asteroid extends Actor {
    public static float minSpawnDelay = 0.1F;
    public static float maxSpawnDelay = 0.25F;
    public static float damagePercent = 0.7F;
    private static float minSize = 50.0F;
    private static float maxSize = 110.0F;
    private static float speed = 200.0F;
    private static final Texture texture = new Texture("asteroid.png");;
    private final Polygon collisionBox = new Polygon();

    public Asteroid() {
        float size = MathUtils.random(minSize, maxSize);
        setSize(size, (float)texture.getHeight() / (float)texture.getWidth() * size);
        setPosition(MathUtils.random(-getWidth() / 2.0F, (float)Gdx.graphics.getWidth() - getWidth() / 2.0F), (float)Gdx.graphics.getHeight());
        float[] vertices = new float[]{35.0F, 0.0F, 52.0F, 0.0F, 80.0F, 25.0F, 80.0F, 32.0F, 63.0F, 56.0F, 42.0F, 67.0F, 29.0F, 67.0F, 0.0F, 43.0F, 0.0F, 24.0F};
        collisionBox.setVertices(vertices);
        collisionBox.setScale(this.getWidth() / 80.0F, this.getHeight() / 67.0F);
    }

    public static void dispose() {
        texture.dispose();
    }

    public static float getSpeed() {
        return speed;
    }

    public static void setSpeed(float speed) {
        Asteroid.speed = speed;
    }

    public static void makeAsteroidsSmallerBy(int percent) {
        if (minSize > 15.0F) {
            minSize -= (float)percent / 100.0F * minSize;
        }

        if (maxSize > 30.0F) {
            maxSize -= (float)percent / 100.0F * maxSize;
        }

    }

    public static float getDamagePercent() {
        return damagePercent;
    }

    public static void setDamagePercent(float damagePercent) {
        if (damagePercent > 0.3F) {
            Asteroid.damagePercent = damagePercent;
        }

    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        batch.end();
        if (MyGdxGame.debug) {
            getCollisionBox();
            MyGdxGame.shapeRenderer.begin(ShapeType.Line);
            MyGdxGame.shapeRenderer.setColor(Color.RED);
            MyGdxGame.shapeRenderer.polygon(collisionBox.getTransformedVertices());
            MyGdxGame.shapeRenderer.setColor(Color.WHITE);
            MyGdxGame.shapeRenderer.end();
        }

        batch.begin();
    }

    public Polygon getCollisionBox() {
        collisionBox.setPosition(getX(), getY());
        return collisionBox;
    }

    public void act(float delta) {
        super.act(delta);
        if (getActions().size == 0) {
            moveBy(0.0F, -speed * delta);
        }

    }

    public boolean isOffScreen() {
        return getY() + getHeight() < 0.0F;
    }

    public float getDamage() {
        return (getWidth() / 300.0F - 20.0F / getWidth() + 0.4F) * damagePercent;
    }

    public float getScore() {
        float score = 8000.0F / getWidth() - 7.0F * getWidth() / 11.0F;
        if (score < 0.0F) {
            score = 0.0F;
        }
        return score;
    }
}

