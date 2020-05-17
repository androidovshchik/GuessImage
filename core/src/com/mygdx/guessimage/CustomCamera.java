package com.mygdx.guessimage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class CustomCamera extends OrthographicCamera {

    private static final String TAG = CustomCamera.class.getSimpleName();

    private Rectangle imageBounds = new Rectangle();

    float startZoom = 1f;

    public CustomCamera() {
    }

    public void setBackgroundBounds(float width, float height) {
        imageBounds.set((viewportWidth - width) / 2, (viewportHeight - height) / 2, width, height);
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
        //imageBounds.overlaps()
        float dx = 0, dy = 0;
        float delta = Gdx.graphics.getDeltaTime();
        float visualWidth = viewportWidth * zoom;
        float visualHeight = viewportHeight * zoom;
        if (visualHeight < imageBounds.height) {

        } else {

        }
        if (visualWidth < imageBounds.width) {
            imageBounds.fitOutside()
        } else {

        }
        if (dx > 0 || dy > 0) {
            setTranslation(dx, dy);
        }
        //GdxLog.print(TAG, " effectiveViewportWidth " + effectiveViewportWidth + " effectiveViewportHeight " + effectiveViewportHeight);
    }
}