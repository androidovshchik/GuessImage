package com.mygdx.guessimage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class BoundedCamera extends OrthographicCamera {

    private static final String TAG = BoundedCamera.class.getSimpleName();

    private Rectangle imageBounds = new Rectangle();
    private float imageTop, imageRight;

    float startZoom = 1f;

    boolean idle = false;

    public void setImageBounds(float width, float height) {
        imageBounds.set((viewportWidth - width) / 2, (viewportHeight - height) / 2, width, height);
        imageTop = imageBounds.y + imageBounds.height;
        imageRight = imageBounds.x + imageBounds.width;
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
        if (imageBounds.perimeter() <= 0 || !idle) {
            return;
        }
        float dx = 0, dy = 0, dz = 0;
        if (getWidth() < imageBounds.width) {
            float leftDiff = getVisualLeft() - imageBounds.x;
            float rightDiff = getVisualRight() - imageRight;
            if (leftDiff < 0) {
                dx = -leftDiff;
            } else if (rightDiff > 0) {
                dx = -rightDiff;
            }
        } else {
            dx = viewportWidth / 2 - position.x;
        }
        if (getHeight() < imageBounds.height) {
            float topDiff = getVisualTop() - imageTop;
            float bottomDiff = getVisualBottom() - imageBounds.y;
            if (topDiff > 0) {
                dy = -topDiff;
            } else if (bottomDiff < 0) {
                dy = -bottomDiff;
            }
        } else {
            dy = viewportHeight / 2 - position.y;
        }
        if (zoom > 1) {
            dz = zoom - 1;
        }
        float delta = Gdx.graphics.getDeltaTime();
        if (dx != 0 || dy != 0) {
            float min = Utils.dip(5);
            dx = MathUtils.clamp(Math.signum(dx) * min + dx * delta * 5, -Math.abs(dx), Math.abs(dx));
            dy = MathUtils.clamp(Math.signum(dy) * min + dy * delta * 5, -Math.abs(dy), Math.abs(dy));
            setTranslation(dx, dy);
            Gdx.graphics.requestRendering();
        }
        if (dz > 0) {
            float zoom = Math.max(this.zoom - 0.05f - dz * delta * 3, 1);
            if (setZoom(zoom)) {
                startZoom = zoom;
                Gdx.graphics.requestRendering();
            }
        }
    }

    public float getWidth() {
        return viewportWidth * zoom;
    }

    public float getHeight() {
        return viewportHeight * zoom;
    }

    public float getVisualTop() {
        return position.y + getHeight() / 2;
    }

    public float getVisualLeft() {
        return position.x - getWidth() / 2;
    }

    public float getVisualRight() {
        return position.x + getWidth() / 2;
    }

    public float getVisualBottom() {
        return position.y - getHeight() / 2;
    }
}