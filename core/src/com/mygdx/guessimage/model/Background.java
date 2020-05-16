package com.mygdx.guessimage.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Background extends Image {

    private static final String TAG = Background.class.getSimpleName();

    public Background(Texture texture) {
        super(texture);
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        setOrigin(getWidth() / 2, getHeight() / 2);
        setX(width / 2f - getWidth() / 2);
        setY(height / 2f - getHeight() / 2);
        float hRatio = width / getWidth();
        float vRatio = height / getHeight();
        float ratio = Math.min(hRatio, vRatio);
        setScale(ratio);
    }

    public float getScaledWidth() {
        return getWidth() * getScaleX();
    }

    public float getScaledHeight() {
        return getHeight() * getScaleY();
    }
}