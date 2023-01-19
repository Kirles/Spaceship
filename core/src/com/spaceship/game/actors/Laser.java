package com.spaceship.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.spaceship.game.MyGdxGame;

public class Laser extends Actor {
    public static float speed = 500.0F;
    private static final Texture texture = new Texture("laser.png");
    private static final Sound shootSound = Gdx.audio.newSound(Gdx.files.internal("shoot.mp3"));
    private final Polygon collisionBox = new Polygon();

    Laser(float x, float y) {
        setPosition(x, y);
        setSize((float)texture.getWidth(), (float)texture.getHeight());
        float[] vertices = new float[]{0.0F, 0.0F, getWidth(), 0.0F, getWidth(), getHeight(), 0.0F, getHeight()};
        collisionBox.setVertices(vertices);
        shootSound.play(0.3F);
    }

    public static void dispose() {
        texture.dispose();
    }

    boolean isOffScreen() {
        return getY() + getHeight() > (float)Gdx.graphics.getHeight();
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        batch.draw(texture, getX(), getY());
        batch.end();
        if (MyGdxGame.debug) {
            getCollisionBox();
            MyGdxGame.shapeRenderer.begin(ShapeType.Line);
            MyGdxGame.shapeRenderer.setColor(Color.CYAN);
            MyGdxGame.shapeRenderer.polygon(collisionBox.getTransformedVertices());
            MyGdxGame.shapeRenderer.setColor(Color.WHITE);
            MyGdxGame.shapeRenderer.end();
        }

        batch.begin();
    }

    public void act(float delta) {
        super.act(delta);
        moveBy(0F, speed * delta);
    }

    public Polygon getCollisionBox() {
        collisionBox.setPosition(getX(), getY());
        return collisionBox;
    }
}

