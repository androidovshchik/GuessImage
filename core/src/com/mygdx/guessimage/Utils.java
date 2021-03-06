package com.mygdx.guessimage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class Utils {

    private Utils() {
    }

    public static GuessImage getApp() {
        return (GuessImage) Gdx.app.getApplicationListener();
    }

    public static float dip(float value) {
        return value * Gdx.graphics.getDensity();
    }

    public static float getTop(Rectangle rectangle) {
        return rectangle.y + rectangle.height;
    }

    public static float getRight(Rectangle rectangle) {
        return rectangle.x + rectangle.width;
    }

    public static int countFingers() {
        int activeTouch = 0;
        for (int i = 0; i < 20; i++) {
            if (Gdx.input.isTouched(i)) {
                activeTouch++;
            }
        }
        return activeTouch;
    }

    public static Color parseColor(String hex) {
        String s1 = hex.substring(1, 3);
        int v1 = Integer.parseInt(s1, 16);
        float f1 = v1 / 255f;
        String s2 = hex.substring(3, 5);
        int v2 = Integer.parseInt(s2, 16);
        float f2 = v2 / 255f;
        String s3 = hex.substring(5, 7);
        int v3 = Integer.parseInt(s3, 16);
        float f3 = v3 / 255f;
        return new Color(f1, f2, f3, 1f);
    }
}
