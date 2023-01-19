package com.spaceship.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Explosion extends Actor {
    private static final Sound explosion = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));;
    private final Animation<TextureRegion> animation;
    private float stateTime;
    private final float speed;

    public Explosion(float x, float y, float width, float height, float speed) {
        setSize(width, height);
        setPosition(x, y);
        Texture explosionSheet = new Texture("explosion.png");
        TextureRegion[][] tmp = TextureRegion.split(explosionSheet, explosionSheet.getWidth() / 5, explosionSheet.getHeight());
        animation = new Animation<>(0.075F, tmp[0]);
        addAction(Actions.sequence(Actions.delay(this.animation.getAnimationDuration()), Actions.removeActor()));
        this.speed = speed;
        explosion.play(0.4F);
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, false);
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }

    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        moveBy(0.0F, -this.speed * delta);
    }
}

