package com.mygdx.guessimage;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CustomCamera extends OrthographicCamera {

    private static final String TAG = CustomCamera.class.getSimpleName();

    private Vector3 lastPosition = new Vector3();
    private Vector2 cameraPosition = new Vector2();
    float startZoom = 1f;

    public CustomCamera() {
    }

    public CustomCamera(float viewportWidth, float viewportHeight) {
        super(viewportWidth, viewportHeight);
    }

    public void setImageBounds(int width, int height) {
        //worldBounds = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //worldBounds = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
}