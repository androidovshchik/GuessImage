package com.mygdx.guessimage.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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

    private Action action = Action.MOVE;
    private Rectangle startBounds = new Rectangle();

    public Frame(Rectangle bounds) {
        this.bounds = bounds;
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        setSize(Utils.dip(MIN_SIZE), Utils.dip(MIN_SIZE));
        setX((width - getWidth()) / 2);
        setY((height - getHeight()) / 2);
    }

    public Frame(Rectangle bounds, float x0, float y0, float width, float height) {
        this(bounds);
        if (bounds.perimeter() <= 0) {
            return;
        }
        setSize(width, height);
        setX(bounds.x + x0);
        setY(bounds.y + y0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        BoundedCamera camera = Utils.getApp().camera;
        float lW = Utils.dip(WIDTH) / 2 * camera.zoom;
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Utils.parseColor("#0000ff"));
        shapeRenderer.line(getX() - lW, getY(), getRight() + lW, getY());
        shapeRenderer.line(getRight(), getY(), getRight(), getTop());
        shapeRenderer.line(getRight() + lW, getTop(), getX() - lW, getTop());
        shapeRenderer.line(getX(), getTop(), getX(), getY());
        shapeRenderer.end();
    }

    public void setAction(float x, float y) {
        startBounds.set(getX(), getY(), getWidth(), getHeight());
        float min = Utils.dip(MIN_SIZE) / 3;
        if (x > getX() + min && x < getRight() - min) {
            if (y > getY() + min && y < getTop() - min) {
                action = Action.MOVE;
                return;
            }
        }
        if (x < getX() + min) {
            if (y < getY() + min) {
                action = Action.SCALE_SW;
            } else if (y > getTop() - min) {
                action = Action.SCALE_NW;
            }
        } else if (x > getRight() - min) {
            if (y < getY() + min) {
                action = Action.SCALE_SE;
            } else if (y > getTop() - min) {
                action = Action.SCALE_NE;
            }
        }
    }

    public void pan(float x, float y, float dX, float dY) {
        if (bounds.perimeter() <= 0) {
            return;
        }
        switch (action) {
            case MOVE:
                if (dX > 0) {
                    float right = Utils.getRight(bounds);
                    if (getVisualRight() + dX > right) {
                        dX = Math.max(0, right - getVisualRight());
                    }
                } else {
                    if (getVisualLeft() + dX < bounds.x) {
                        dX = Math.min(0, bounds.x - getVisualLeft());
                    }
                }
                if (dY < 0) {
                    if (getVisualBottom() + dY < bounds.y) {
                        dY = Math.min(0, bounds.y - getVisualBottom());
                    }
                } else {
                    float top = Utils.getTop(bounds);
                    if (getVisualTop() + dY > top) {
                        dY = Math.max(0, top - getVisualTop());
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
                float top = startBounds.getY() + startBounds.getHeight();
                float right = startBounds.getX() + startBounds.getWidth();
                x = MathUtils.clamp(x, bounds.x, right - Utils.dip(MIN_SIZE));
                y = MathUtils.clamp(y, bounds.y, top - Utils.dip(MIN_SIZE));
                if (getX() != x || getY() != y) {
                    setSize(right - x, top - y);
                    setPosition(x, y);
                }
                break;
            case SCALE_SE:
                break;
        }
    }

    public float getTop() {
        return getY() + getHeight();
    }

    public float getRight() {
        return getX() + getWidth();
    }

    public float getVisualTop() {
        return getY() + getHeight() + Utils.dip(WIDTH) / 2;
    }

    public float getVisualLeft() {
        return getX() - Utils.dip(WIDTH) / 2;
    }

    public float getVisualRight() {
        return getX() + getWidth() + Utils.dip(WIDTH) / 2;
    }

    public float getVisualBottom() {
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