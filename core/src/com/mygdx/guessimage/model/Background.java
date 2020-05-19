package com.mygdx.guessimage.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Background extends Image {

    private static final String TAG = Background.class.getSimpleName();

    public Background(Texture texture) {
        super(texture);
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        setOrigin(getWidth() / 2, getHeight() / 2);
        setX((width - getWidth()) / 2);
        setY((height - getHeight()) / 2);
        float hRatio = width / getWidth();
        float vRatio = height / getHeight();
        float ratio = Math.min(hRatio, vRatio);
        setScale(ratio);
    }

    public void notifyBounds(Rectangle bounds) {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        bounds.setSize(getScaledWidth(), getScaledHeight());
        bounds.setX((width - bounds.getWidth()) / 2);
        bounds.setY((height - bounds.getHeight()) / 2);
    }

    public float getScaledWidth() {
        return getWidth() * getScaleX();
    }

    public float getScaledHeight() {
        return getHeight() * getScaleY();
    }
}