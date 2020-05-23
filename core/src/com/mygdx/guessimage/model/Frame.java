package com.mygdx.guessimage.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.guessimage.Mode;
import com.mygdx.guessimage.Utils;

public class Frame extends Actor {

    private static final String TAG = Frame.class.getSimpleName();

    private static final float UNIT = 20;
    public static final float MIN_RECT_SIZE = 2 * UNIT;
    public static final float MIN_VISUAL_SIZE = UNIT;
    public static final float WIDTH = 2 * 2;

    public static final String GREEN = "#4CAF50";
    public static final String LIGHT_GREEN = "#CDDC39";
    public static final String BLUE = "#2196F3";

    private ShapeRenderer shapeRenderer;

    public long id;
    private Mode mode;
    private Rectangle bounds;

    private Action action = Action.NONE;
    private Rectangle startBounds = new Rectangle();
    private float startX, startY;

    public boolean isDone = false;

    public Frame(ShapeRenderer renderer, long id, Mode mode, Rectangle bounds) {
        shapeRenderer = renderer;
        this.id = id;
        this.mode = mode;
        this.bounds = bounds;
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        float size = Utils.dip(MIN_RECT_SIZE) * 3;
        setSize(size, size);
        setX((width - getWidth()) / 2);
        setY((height - getHeight()) / 2);
    }

    public Frame(ShapeRenderer renderer, long id, Mode mode, Rectangle bounds, float x0, float y0,
                 float width, float height) {
        this(renderer, id, mode, bounds);
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
            float lW = Utils.dip(WIDTH) / 2 * Utils.getApp().camera.zoom;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Utils.parseColor(isDone ? LIGHT_GREEN : BLUE));
            shapeRenderer.line(getVisualLeft(), getVisualBottom() + lW, getVisualRight(), getVisualBottom() + lW);
            shapeRenderer.line(getVisualRight() - lW, getVisualBottom(), getVisualRight() - lW, getVisualTop());
            shapeRenderer.line(getVisualRight(), getVisualTop() - lW, getVisualLeft(), getVisualTop() - lW);
            shapeRenderer.line(getVisualLeft() + lW, getVisualTop(), getVisualLeft() + lW, getVisualBottom());
            shapeRenderer.end();
        }
    }

    public void setAction(float x, float y) {
        if (isDone) {
            action = Action.NONE;
            return;
        }
        startBounds.set(getX(), getY(), getWidth(), getHeight());
        float unit = Utils.dip(UNIT);
        if (x < getX() + unit) {
            if (y < getY() + unit) {
                action = Action.SCALE_SW;
                startX = x - getX();
                startY = y - getY();
                return;
            } else if (y > getTop() - unit) {
                action = Action.SCALE_NW;
                startX = x - getX();
                startY = y - getTop();
                return;
            }
        } else if (x > getRight() - unit) {
            if (y < getY() + unit) {
                action = Action.SCALE_SE;
                startX = x - getRight();
                startY = y - getY();
                return;
            } else if (y > getTop() - unit) {
                action = Action.SCALE_NE;
                startX = x - getRight();
                startY = y - getTop();
                return;
            }
        }
        action = Action.MOVE;
    }

    public void pan(float x, float y, float dX, float dY) {
        if (isDone) {
            return;
        }
        if (action == Action.MOVE) {
            if (dX > 0) {
                float maxRight = Utils.getRight(bounds);
                if (getVisualRight() + dX > maxRight) {
                    dX = Math.max(0, maxRight - getVisualRight());
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
                float maxTop = Utils.getTop(bounds);
                if (getVisualTop() + dY > maxTop) {
                    dY = Math.max(0, maxTop - getVisualTop());
                }
            }
            if (dX != 0 || dY != 0) {
                moveBy(dX, dY);
            }
            return;
        }
        float width;
        float height;
        float unit = Utils.dip(UNIT);
        float minSize = Utils.dip(MIN_RECT_SIZE);
        float top = Utils.getTop(startBounds);
        float right = Utils.getRight(startBounds);
        switch (action) {
            case SCALE_NW:
                x = MathUtils.clamp(x - startX, bounds.x - unit / 2, right - minSize);
                y = MathUtils.clamp(y - startY, startBounds.getY() + minSize, Utils.getTop(bounds) + unit / 2);
                height = y - startBounds.getY();
                if (getX() != x || getHeight() != height) {
                    setSize(right - x, height);
                    setX(x);
                }
                break;
            case SCALE_NE:
                x = MathUtils.clamp(x - startX, startBounds.getX() + minSize, Utils.getRight(bounds) + unit / 2);
                y = MathUtils.clamp(y - startY, startBounds.getY() + minSize, Utils.getTop(bounds) + unit / 2);
                width = x - startBounds.getX();
                height = y - startBounds.getY();
                if (getWidth() != width || getHeight() != height) {
                    setSize(width, height);
                }
                break;
            case SCALE_SW:
                x = MathUtils.clamp(x - startX, bounds.x - unit / 2, right - minSize);
                y = MathUtils.clamp(y - startY, bounds.y - unit / 2, top - minSize);
                if (getX() != x || getY() != y) {
                    setSize(right - x, top - y);
                    setPosition(x, y);
                }
                break;
            case SCALE_SE:
                x = MathUtils.clamp(x - startX, startBounds.getX() + minSize, Utils.getRight(bounds) + unit / 2);
                y = MathUtils.clamp(y - startY, bounds.y - unit / 2, top - minSize);
                width = x - startBounds.getX();
                if (getWidth() != width || getY() != y) {
                    setSize(width, top - y);
                    setY(y);
                }
                break;
        }
    }

    public boolean contains(float x, float y) {
        if (x >= getVisualLeft() && x <= getVisualRight()) {
            return y >= getVisualBottom() && y <= getVisualTop();
        }
        return false;
    }

    public float getX0() {
        return getX() - bounds.x;
    }

    public float getY0() {
        return getY() - bounds.y;
    }

    public float getTop() {
        return getY() + getHeight();
    }

    public float getRight() {
        return getX() + getWidth();
    }

    public float getVisualWidth() {
        return getWidth() - Utils.dip(UNIT);
    }

    public float getVisualHeight() {
        return getHeight() - Utils.dip(UNIT);
    }

    public float getVisualTop() {
        return getTop() - Utils.dip(UNIT) / 2;
    }

    public float getVisualLeft() {
        return getX() + Utils.dip(UNIT) / 2;
    }

    public float getVisualRight() {
        return getRight() - Utils.dip(UNIT) / 2;
    }

    public float getVisualBottom() {
        return getY() + Utils.dip(UNIT) / 2;
    }

    enum Action {
        NONE, MOVE, SCALE_NW, SCALE_NE, SCALE_SW, SCALE_SE
    }
}