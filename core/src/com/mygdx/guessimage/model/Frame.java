package com.mygdx.guessimage.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class Frame extends Actor implements Disposable {

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    @Override
    public void draw(Batch batch, float parentAlpha) {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}