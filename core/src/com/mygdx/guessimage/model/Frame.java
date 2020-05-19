package com.mygdx.guessimage.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.guessimage.BoundedCamera;
import com.mygdx.guessimage.Utils;

public class Frame extends Actor implements Disposable {

    private static final String TAG = Frame.class.getSimpleName();

    public static final float MIN_SIZE = 3 * 16;
    public static final float WIDTH = 2 * 2;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Rectangle bounds;

    public Action action = Action.MOVE;

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
        float w = getWidth();
        float h = getHeight();
        BoundedCamera camera = Utils.getApp().camera;
        float lW = Utils.dip(WIDTH) / 2 * camera.zoom;
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Utils.parseColor("#00ff00"));
        shapeRenderer.line(getX() - lW, getY(), getX() + w + lW, getY());
        shapeRenderer.line(getX() + w, getY(), getX() + w, getY() + h);
        shapeRenderer.line(getX() + w + lW, getY() + h, getX() - lW, getY() + h);
        shapeRenderer.line(getX(), getY() + h, getX(), getY());
        shapeRenderer.end();
    }

    public void setAction(float x, float y) {
        float size3 = Utils.dip(MIN_SIZE) / 3;
        if (x > getX() + size3 && x < getX() + size3 * 2) {
            if (y > getY() + size3 && y < getY() + size3 * 2) {
                action = Action.MOVE;
                return;
            }
        }
        float size2 = Utils.dip(MIN_SIZE) / 2;
        if (x < getX() + size2) {
            if (y < getY() + size2) {
                action = Action.SCALE_SW;
            } else {
                action = Action.SCALE_NW;
            }
        } else {
            if (y < getY() + size2) {
                action = Action.SCALE_SE;
            } else {
                action = Action.SCALE_NE;
            }
        }
    }

    public void pan(float dX, float dY) {
        if (bounds.perimeter() <= 0) {
            return;
        }
        switch (action) {
            case MOVE:
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
                break;
            case SCALE_NW:
                break;
            case SCALE_NE:
                break;
            case SCALE_SW:
                break;
            case SCALE_SE:
                break;
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

    enum Action {
        MOVE, SCALE_NW, SCALE_NE, SCALE_SW, SCALE_SE
    }
}