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

    public static final float MIN_SIZE = 3 * 20;
    private static final float WIDTH = 2 * 2;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Rectangle bounds;

    public Frame(Rectangle bounds) {
        this.bounds = bounds;
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        setSize(Utils.dip(MIN_SIZE), Utils.dip(MIN_SIZE));
        setX((width - getWidth()) / 2);
        setY((height - getHeight()) / 2);
    }

    public Frame(Rectangle bounds, float xC, float yC, float w, float h) {
        this.bounds = bounds;
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        float cX = width / 2f;
        float cY = height / 2f;
        setSize(w, h);
        setX(cX + xC);
        setY(cY + yC);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Gdx.gl.glLineWidth(Utils.dip(WIDTH));
        float zoom = Utils.getApp().camera.zoom;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Utils.parseColor("#ff0000"));
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();
    }

    public void setTranslation(float dX, float dY) {
        if (bounds.perimeter() <= 0) {
            return;
        }
        if (dX > 0) {
            float right = Utils.getRight(bounds);
            if (getRight() + dX > right) {
                dX = Math.max(0, right - getRight());
            }
        } else {
            if (getLeft() + dX < bounds.x) {
                dX = Math.min(0, bounds.x - getLeft());
            }
        }
        if (dY < 0) {
            if (getBottom() + dY < bounds.y) {
                dY = Math.min(0, bounds.y - getBottom());
            }
        } else {
            float top = Utils.getTop(bounds);
            if (getTop() + dY > top) {
                dY = Math.max(0, top - getTop());
            }
        }
        if (dX != 0 || dY != 0) {
            moveBy(dX, dY);
        }
    }

    public float getTop() {
        return getY() + getHeight() + Utils.dip(WIDTH) / 2;
    }

    public float getLeft() {
        return getX() - Utils.dip(WIDTH) / 2;
    }

    public float getRight() {
        return getX() + getWidth() + Utils.dip(WIDTH) / 2;
    }

    public float getBottom() {
        return getY() - Utils.dip(WIDTH) / 2;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}