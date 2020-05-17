package com.mygdx.guessimage;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class CustomCamera extends OrthographicCamera {

    private static final String TAG = CustomCamera.class.getSimpleName();

    private Rectangle imageBounds = new Rectangle();
    private float imageTop;
    private float imageRight;

    float startZoom = 1f;

    boolean zooming = false;
    boolean translating = false;

    public CustomCamera() {
    }

    public void setImageBounds(float width, float height) {
        imageBounds.set((viewportWidth - width) / 2, (viewportHeight - height) / 2, width, height);
        imageTop = imageBounds.y + imageBounds.height;
        imageRight = imageBounds.x + imageBounds.width;
    }

    public void setTranslation(float dX, float dY) {
        translate(dX, dY);
        update();
    }

    public void setZoom(float zoom) {
        zoom = MathUtils.clamp(zoom, 0.2f, 1);
        if (this.zoom != zoom) {
            this.zoom = zoom;
            update();
        }
    }

    public void normalize() {
        if (zooming || translating) {
            return;
        }
        float dx = 0;
        float dy = 0;
        if (getHeight() < imageBounds.height) {
            float topDiff = getVisualTop() - imageTop;
            float bottomDiff = getVisualBottom() - imageBounds.y;
            if (topDiff > 0) {
                dy = -topDiff;
            } else if (bottomDiff < 0) {
                dy = -bottomDiff;
            }
        } else {

        }
        if (getWidth() < imageBounds.width) {
            float leftDiff = getVisualLeft() - imageBounds.x;
            float rightDiff = getVisualRight() - imageRight;
            if (leftDiff < 0) {
                dx = -leftDiff;
            } else if (rightDiff > 0) {
                dx = -rightDiff;
            }
        } else {

        }
        if (dx > 0.1 || dx < -0.1 || dy > 0.1 || dy < -0.1) {
            dx = MathUtils.clamp(20 * Math.signum(dx), -Math.abs(dx), Math.abs(dx));
            dy = MathUtils.clamp(20 * Math.signum(dy), -Math.abs(dy), Math.abs(dy));
            setTranslation(dx, dy);
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

    public float getTop() {
        return position.y + viewportHeight / 2;
    }

    public float getLeft() {
        return position.x - viewportWidth / 2;
    }

    public float getRight() {
        return position.x + viewportWidth / 2;
    }

    public float getBottom() {
        return position.y - viewportHeight / 2;
    }

    private static float clampReverse(float value, float min, float max) {
        if (value < 0 && value > min) return min;
        if (value > 0 && value < max) return max;
        return value;
    }
}