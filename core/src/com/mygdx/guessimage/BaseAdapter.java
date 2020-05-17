package com.mygdx.guessimage;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.Method;

public abstract class BaseAdapter extends InputAdapter
        implements ApplicationListener, GestureDetector.GestureListener {

    private static final String TAG = BaseAdapter.class.getSimpleName();

    @Override
    public void resume() {
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public void pause() {
    }

    // null may be only String params
    public void postRunnable(final String name, final Object... params) {
        Gdx.app.postRunnable(() -> {
            Method method = null;
            Class[] classes = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                classes[i] = params[i] == null ? String.class : params[i].getClass();
            }
            try {
                method = getClass().getMethod(name, classes);
            } catch (Throwable e) {
                GdxLog.print(TAG, e.toString());
            }
            if (method == null) {
                return;
            }
            try {
                method.invoke(this, params);
            } catch (Throwable e) {
                GdxLog.print(TAG, e.toString());
            }
        });
    }
}
