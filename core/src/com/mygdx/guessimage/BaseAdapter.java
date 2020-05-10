package com.mygdx.guessimage;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.lang.reflect.Method;

public abstract class BaseAdapter extends InputAdapter
        implements ApplicationListener, GestureDetector.GestureListener {

    private static final String TAG = BaseAdapter.class.getSimpleName();

    protected OrthographicCamera camera;

    protected FitViewport viewport;

    @Override
    public void resume() {
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
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
                method = GuessImage.class.getMethod(name, classes);
            } catch (Throwable e) {
                GdxLog.print(TAG, e.toString());
            }
            if (method == null) {
                return;
            }
            try {
                method.invoke(BaseAdapter.this, params);
            } catch (Throwable e) {
                GdxLog.print(TAG, e.toString());
            }
        });
    }

    protected Color parseColor(String hex) {
        String s1 = hex.substring(0, 2);
        int v1 = Integer.parseInt(s1, 16);
        float f1 = v1 / 255f;
        String s2 = hex.substring(2, 4);
        int v2 = Integer.parseInt(s2, 16);
        float f2 = v2 / 255f;
        String s3 = hex.substring(4, 6);
        int v3 = Integer.parseInt(s3, 16);
        float f3 = v3 / 255f;
        return new Color(f1, f2, f3, 1f);
    }
}
