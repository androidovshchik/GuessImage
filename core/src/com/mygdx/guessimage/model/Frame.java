package com.mygdx.guessimage.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.guessimage.Utils;

public class Frame extends Actor implements Disposable {

    private static final String TAG = Frame.class.getSimpleName();

    public static final float MIN_SIZE = 60;
    private static final float WIDTH = 4;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Rectangle imageBounds;

    public Frame(Rectangle bounds) {
        imageBounds = bounds;
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        setSize(Utils.dip(MIN_SIZE), Utils.dip(MIN_SIZE));
        setOrigin(getWidth() / 2, getHeight() / 2);
        setX((width - getWidth()) / 2);
        setY((height - getHeight()) / 2);
    }

    public Frame(Rectangle bounds, float xC, float yC, float w, float h) {
        imageBounds = bounds;
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        float cX = width / 2f;
        float cY = height / 2f;
        setSize(w, h);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setX(cX + xC);
        setY(cY + yC);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Gdx.gl.glLineWidth(Utils.dip(WIDTH));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Utils.parseColor("#ff0000"));
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();
    }

    public void setTranslation(float dX, float dY) {
        if (imageBounds.perimeter() <= 0) {
            return;
        }
        if (imageBounds.contains(getX() + dX, getY() + dY)) {
            moveBy(dX, dY);
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}