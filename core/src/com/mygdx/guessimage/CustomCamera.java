package com.mygdx.guessimage;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CustomCamera extends OrthographicCamera {

    private static final String TAG = CustomCamera.class.getSimpleName();

    private Rectangle imageBounds = new Rectangle();

    private Vector3 lastPosition = new Vector3();
    private Vector2 cameraPosition = new Vector2();
    float startZoom = 1f;

    public CustomCamera() {
    }

    public CustomCamera(float viewportWidth, float viewportHeight) {
        super(viewportWidth, viewportHeight);
    }

    public void setBackgroundBounds(float width, float height) {
        imageBounds.set((viewportWidth - width) / 2, (viewportHeight - height) / 2, width, height);
    }

    public void setTranslation(float dX, float dY) {
        translate(dX, dY);
        update();
    }

    public void setZoom(float zoom) {
        GdxLog.print(TAG, " position " + position + " viewportWidth " + viewportWidth);
        zoom = MathUtils.clamp(zoom, 0.2f, 1);
        this.zoom = zoom;
        update();
    }

    public void setToBounds() {

    }
}