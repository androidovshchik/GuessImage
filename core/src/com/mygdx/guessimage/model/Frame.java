package com.mygdx.guessimage.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.guessimage.Utils;

public class Frame extends Actor implements Disposable {

    private static final String TAG = Frame.class.getSimpleName();

    public static final float MIN_SIZE = 40;
    private static final float WIDTH = 4;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public Frame() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        setSize(Utils.dip(MIN_SIZE), Utils.dip(MIN_SIZE));
        setOrigin(getWidth() / 2, getHeight() / 2);
        setX((width - getWidth()) / 2);
        setY((height - getHeight()) / 2);
    }

    public Frame(float x, float y, float w, float h) {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        setSize(w, h);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setX(cX);
        setY(cY);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Gdx.gl.glLineWidth(Utils.dip(WIDTH));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Utils.parseColor("#ff0000"));
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}