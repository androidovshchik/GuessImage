package com.mygdx.guessimage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class Player extends Image {

    public static final int INDEX_NONE = -1;

    public static final int TYPE_STICKER = 1;

    public static final int TYPE_TOP_BACKGROUND = 20;
    public static final int TYPE_CENTER_BACKGROUND = 21;
    public static final int TYPE_BOTTOM_BACKGROUND = 22;

    public static final int TYPE_PHOTO = 30;

    protected static final float ANIMATION_TIME_MODE_TRANSLATION = 0.3f;

    public int type;

    public String path;

    public Player(Texture texture, int type, String path) {
        super(texture);
        this.type = type;
        this.path = path;
    }
}
