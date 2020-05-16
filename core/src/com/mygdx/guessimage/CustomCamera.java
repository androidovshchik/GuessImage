package com.mygdx.guessimage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CustomCamera extends OrthographicCamera {

    private Vector3 lastPosition = new Vector3();
    private Vector2 cameraPosition = new Vector2();
    private float startZoom = 1f;

    public CustomCamera() {
    }

    public CustomCamera(float viewportWidth, float viewportHeight) {
        super(viewportWidth, viewportHeight);
    }

    public void setImageBounds(int width, int height) {
        worldBounds = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        worldBounds = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void translate(float x, float y) {
        lastPosition.set(position.x, position.y, 0);
        super.translate(x, y);
    }

    public void setZoom(float zoom) {

    }
}