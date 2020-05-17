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

    boolean pinchingCamera = false;

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
        this.zoom = zoom;
        update();
    }

    public void normalize() {
        float dx = 0, dy = 0;
        float delta = Gdx.graphics.getDeltaTime();
        float visualWidth = viewportWidth * zoom;
        float visualHeight = viewportHeight * zoom;
        if (visualHeight < imageBounds.height) {
            if (getTop() > imageTop) {

            } else if (getBottom() < imageBounds.y) {

            }
        } else {

        }
        if (visualWidth < imageBounds.width) {
            if (getLeft() < imageBounds.x) {

            } else if (getRight() > imageRight) {

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

    public float getTop() {
        return position.y + getHeight() / 2;
    }

    public float getLeft() {
        return position.x - getWidth() / 2;
    }

    public float getRight() {
        return position.x + getWidth() / 2;
    }

    public float getBottom() {
        return position.y - getHeight() / 2;
    }
}