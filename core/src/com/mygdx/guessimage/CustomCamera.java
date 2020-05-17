package com.mygdx.guessimage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class CustomCamera extends OrthographicCamera {

    private static final String TAG = CustomCamera.class.getSimpleName();

    private Rectangle imageBounds = new Rectangle();
    private float imageTop;
    private float imageRight;

    float startZoom = 1f;

    boolean scaling = false;
    boolean translating = false;

    public CustomCamera() {
    }

    public void setImageBounds(float width, float height) {
        imageBounds.set((viewportWidth - width) / 2, (viewportHeight - height) / 2, width, height);
        imageTop = imageBounds.y + imageBounds.height;
        imageRight = imageBounds.x + imageBounds.width;
    }

    public void setTranslation(float dX, float dY) {
        translating = true;
        translate(dX, dY);
        update();
    }

    public void setZoom(float zoom) {
        scaling = true;
        zoom = MathUtils.clamp(zoom, 0.2f, 1);
        this.zoom = zoom;
        update();
    }

    public void normalize() {
        if (scaling || translating) {
            return;
        }
        float dx = 0;
        float dy = 0;
        float delta = Gdx.graphics.getDeltaTime();
        if (getHeight() < imageBounds.height) {
            float topDiff = getVisualTop() - imageTop;
            float bottomDiff = getVisualBottom() - imageBounds.y;
            if (topDiff > 0) {
                dy = getTop();
            } else if (bottomDiff < 0) {
                dy = getBottom();
            }
        } else {

        }
        if (getWidth() < imageBounds.width) {
            float leftDiff = getVisualLeft() - imageBounds.x;
            float rightDiff = getVisualRight() - imageRight;
            if (leftDiff < 0) {
                dx = getVisualLeft();
            } else if (rightDiff > 0) {
                dx = getVisualRight();
            }
        } else {

        }
        if (dx > 0 || dy > 0) {
            setTranslation(dx, dy);
        }
        //GdxLog.print(TAG, " effectiveViewportWidth " + effectiveViewportWidth + " effectiveViewportHeight " + effectiveViewportHeight);
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
}