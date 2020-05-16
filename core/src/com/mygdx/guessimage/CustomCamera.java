package com.mygdx.guessimage;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class CustomCamera extends OrthographicCamera {

    private Vector3 lastPosition = new Vector3();

    public CustomCamera() {
    }

    public CustomCamera(float viewportWidth, float viewportHeight) {
        super(viewportWidth, viewportHeight);
    }

    public void setWorldBounds(int left, int bottom, int width, int height) {

    }

    @Override
    public void translate(float x, float y) {
        lastPosition.set(position.x, position.y, 0);
        super.translate(x, y);
    }

    public void setZoom(float zoom) {

    }
}