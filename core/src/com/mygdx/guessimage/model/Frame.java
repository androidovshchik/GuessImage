package com.mygdx.guessimage.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.guessimage.BoundedCamera;
import com.mygdx.guessimage.Mode;
import com.mygdx.guessimage.Utils;

public class Frame extends Actor implements Disposable {

    private static final String TAG = Frame.class.getSimpleName();

    public static final float MIN_SIZE = 3 * 16;
    public static final float WIDTH = 2 * 2;
    public static final String GREEN = "#4CAF50";
    public static final String LIGHT_GREEN = "#CDDC39";
    public static final String BLUE = "#2196F3";

    public long id;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Mode mode;

    private Rectangle bounds;

    private Action action = Action.MOVE;
    private Rectangle startBounds = new Rectangle();

    public boolean isDone = false;

    public Frame(long id, Mode mode, Rectangle bounds) {
        this.id = id;
        this.mode = mode;
        this.bounds = bounds;
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        setSize(Utils.dip(MIN_SIZE), Utils.dip(MIN_SIZE));
        setX((width - getWidth()) / 2);
        setY((height - getHeight()) / 2);
    }

    public Frame(long id, Mode mode, Rectangle bounds, float x0, float y0, float width, float height) {
        this(id, mode, bounds);
        if (bounds.perimeter() <= 0) {
            return;
        }
        setSize(width, height);
        setX(bounds.x + x0);
        setY(bounds.y + y0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (mode == Mode.EDIT || isDone) {
            BoundedCamera camera = Utils.getApp().camera;
            float lW = Utils.dip(WIDTH) / 2 * camera.zoom;
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Utils.parseColor(isDone ? LIGHT_GREEN : BLUE));
            shapeRenderer.line(getX(), getY() + lW, getRight(), getY() + lW);
            shapeRenderer.line(getRight() - lW, getY(), getRight() - lW, getTop());
            shapeRenderer.line(getRight(), getTop() - lW, getX(), getTop() - lW);
            shapeRenderer.line(getX() + lW, getTop(), getX() + lW, getY());
            shapeRenderer.end();
        }
    }

    public void setAction(float x, float y) {
        startBounds.set(getX(), getY(), getWidth(), getHeight());
        float min = Utils.dip(MIN_SIZE) / 3;
        if (x < getX() + min) {
            if (y < getY() + min) {
                action = Action.SCALE_SW;
                return;
            } else if (y > getTop() - min) {
                action = Action.SCALE_NW;
                return;
            }
        } else if (x > getRight() - min) {
            if (y < getY() + min) {
                action = Action.SCALE_SE;
                return;
            } else if (y > getTop() - min) {
                action = Action.SCALE_NE;
                return;
            }
        }
        action = Action.MOVE;
    }

    public void pan(float x, float y, float dX, float dY) {
        if (bounds.perimeter() <= 0) {
            return;
        }
        float width;
        float height;
        float minSize = Utils.dip(MIN_SIZE);
        float top = startBounds.getY() + startBounds.getHeight();
        float right = startBounds.getX() + startBounds.getWidth();
        switch (action) {
            case MOVE:
                if (dX > 0) {
                    right = Utils.getRight(bounds);
                    if (getRight() + dX > right) {
                        dX = Math.max(0, right - getRight());
                    }
                } else {
                    if (getX() + dX < bounds.x) {
                        dX = Math.min(0, bounds.x - getX());
                    }
                }
                if (dY < 0) {
                    if (getY() + dY < bounds.y) {
                        dY = Math.min(0, bounds.y - getY());
                    }
                } else {
                    top = Utils.getTop(bounds);
                    if (getTop() + dY > top) {
                        dY = Math.max(0, top - getTop());
                    }
                }
                if (dX != 0 || dY != 0) {
                    moveBy(dX, dY);
                }
                break;
            case SCALE_NW:
                x = MathUtils.clamp(x, bounds.x, right - minSize);
                y = MathUtils.clamp(y, startBounds.getY() + minSize, Utils.getTop(bounds));
                height = y - startBounds.getY();
                if (getX() != x || getHeight() != height) {
                    setSize(right - x, height);
                    setX(x);
                }
                break;
            case SCALE_NE:
                x = MathUtils.clamp(x, startBounds.getX() + minSize, Utils.getRight(bounds));
                y = MathUtils.clamp(y, startBounds.getY() + minSize, Utils.getTop(bounds));
                width = x - startBounds.getX();
                height = y - startBounds.getY();
                if (getWidth() != width || getHeight() != height) {
                    setSize(width, height);
                }
                break;
            case SCALE_SW:
                x = MathUtils.clamp(x, bounds.x, right - minSize);
                y = MathUtils.clamp(y, bounds.y, top - minSize);
                if (getX() != x || getY() != y) {
                    setSize(right - x, top - y);
                    setPosition(x, y);
                }
                break;
            case SCALE_SE:
                x = MathUtils.clamp(x, startBounds.getX() + minSize, Utils.getRight(bounds));
                y = MathUtils.clamp(y, bounds.y, top - minSize);
                width = x - startBounds.getX();
                if (getWidth() != width || getY() != y) {
                    setSize(width, top - y);
                    setY(y);
                }
                break;
        }
    }

    public boolean contains(float x, float y) {
        if (x >= getX() && x <= getRight()) {
            return y >= getY() && y <= getTop();
        }
        return false;
    }

    public float getTop() {
        return getY() + getHeight();
    }

    public float getRight() {
        return getX() + getWidth();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    enum Action {
        MOVE, SCALE_NW, SCALE_NE, SCALE_SW, SCALE_SE
    }
}