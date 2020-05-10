package com.mygdx.guessimage.model;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.guessimage.Player;

public class Background extends Player {

    private static final String TAG = Background.class.getSimpleName();

    public Background(Texture texture, int type, String path) {
        super(texture, type, path);
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    public void center(int worldWidth, int worldHeight, int rotation) {
        setRotation(-rotation);
        setX(worldWidth / 2 - getWidth() / 2);
        setY(worldHeight / 2 - getHeight() / 2);
        float minSize, scale;
        minSize = getHeight() > getWidth() ? getWidth() : getHeight();
        scale = 1f * worldHeight / minSize;
        setScale(scale);
    }
}