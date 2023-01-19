package com.spaceship.game.actors.effects;

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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.spaceship.game.MyGdxGame;
import com.spaceship.game.actors.Asteroid;
import com.spaceship.game.actors.Ship;

public abstract class Effect extends Actor {
    private Animation animation;
    private float stateTime;
    private Polygon collisionBox = new Polygon();

    Effect(Texture sheet, float x) {
        setSize(40.0F, 40.0F);
        setPosition(x, (float)Gdx.graphics.getHeight());
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / 2, sheet.getHeight());
        animation = new Animation(0.5F, tmp[0]);
        float[] vertices = new float[]{12.0F, 0.0F, 27.0F, 0.0F, 40.0F, 14.0F, 40.0F, 28.0F, 27.0F, 40.0F, 12.0F, 40.0F, 0.0F, 28.0F, 0.0F, 14.0F};
        collisionBox.setVertices(vertices);
    }

    void take(Ship ship, String text, LabelStyle labelStyle) {
        Label label = new Label(text, labelStyle);
        label.setAlignment(1, 1);
        label.setPosition(ship.getX() + ship.getWidth() / 2.0F - label.getPrefWidth() / 2.0F, ship.getY() + ship.getHeight() / 2.0F + 40.0F);
        label.setX(MathUtils.clamp(label.getX(), 0.0F, 600.0F - label.getPrefWidth()));
        ship.getStage().addActor(label);
        label.addAction(Actions.parallel(Actions.moveBy(0.0F, 80.0F, 2.0F), Actions.fadeOut(2.0F)));
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        TextureRegion currentFrame = (TextureRegion)animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
        batch.end();
        if (MyGdxGame.debug) {
            getCollisionBox();
            MyGdxGame.shapeRenderer.begin(ShapeType.Line);
            MyGdxGame.shapeRenderer.setColor(Color.YELLOW);
            MyGdxGame.shapeRenderer.polygon(collisionBox.getTransformedVertices());
            MyGdxGame.shapeRenderer.setColor(Color.WHITE);
            MyGdxGame.shapeRenderer.end();
        }

        batch.begin();
    }

    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        moveBy(0.0F, -Asteroid.getSpeed() * delta);
    }

    public Polygon getCollisionBox() {
        collisionBox.setPosition(getX(), getY());
        return collisionBox;
    }

    public boolean isOffScreen() {
        return getY() + getHeight() < 0.0F;
    }
}

