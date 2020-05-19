package com.mygdx.guessimage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class BoundedCamera extends OrthographicCamera {

    private static final String TAG = BoundedCamera.class.getSimpleName();

    private Rectangle bounds;

    public float startZoom = 1f;

    public boolean idle = false;

    public BoundedCamera(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void setTranslation(float dX, float dY) {
        translate(dX, dY);
        update();
    }

    public boolean setZoom(float zoom) {
        zoom = MathUtils.clamp(zoom, 0.2f, 2);
        if (this.zoom != zoom) {
            this.zoom = zoom;
            update();
            return true;
        }
        return false;
    }

    public void normalize() {
        if (bounds.perimeter() <= 0 || !idle) {
            return;
        }
        float dX = 0, dY = 0, dZ = 0;
        if (getVisualWidth() < bounds.width) {
            float leftDiff = getVisualLeft() - bounds.x;
            float rightDiff = getVisualRight() - Utils.getRight(bounds);
            if (leftDiff < 0) {
                dX = -leftDiff;
            } else if (rightDiff > 0) {
                dX = -rightDiff;
            }
        } else {
            dX = viewportWidth / 2 - position.x;
        }
        if (getVisualHeight() < bounds.height) {
            float topDiff = getVisualTop() - Utils.getTop(bounds);
            float bottomDiff = getVisualBottom() - bounds.y;
            if (topDiff > 0) {
                dY = -topDiff;
            } else if (bottomDiff < 0) {
                dY = -bottomDiff;
            }
        } else {
            dY = viewportHeight / 2 - position.y;
        }
        if (zoom > 1) {
            dZ = zoom - 1;
        }
        float delta = Gdx.graphics.getDeltaTime();
        if (dX != 0 || dY != 0) {
            float min = Utils.dip(5);
            dX = MathUtils.clamp(Math.signum(dX) * min + dX * delta * 5, -Math.abs(dX), Math.abs(dX));
            dY = MathUtils.clamp(Math.signum(dY) * min + dY * delta * 5, -Math.abs(dY), Math.abs(dY));
            setTranslation(dX, dY);
            Gdx.graphics.requestRendering();
        }
        if (dZ > 0) {
            float zoom = Math.max(this.zoom - 0.05f - dZ * delta * 3, 1);
            if (setZoom(zoom)) {
                startZoom = zoom;
                Gdx.graphics.requestRendering();
            }
        }
    }

    public float getVisualWidth() {
        return viewportWidth * zoom;
    }

    public float getVisualHeight() {
        return viewportHeight * zoom;
    }

    public float getVisualTop() {
        return position.y + getVisualHeight() / 2;
    }

    public float getVisualLeft() {
        return position.x - getVisualWidth() / 2;
    }

    public float getVisualRight() {
        return position.x + getVisualWidth() / 2;
    }

    public float getVisualBottom() {
        return position.y - getVisualHeight() / 2;
    }
}