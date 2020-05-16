package com.mygdx.guessimage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.guessimage.model.Background;

public class GuessImage extends BaseAdapter {

    private static final String TAG = GuessImage.class.getSimpleName();

    private CustomCamera camera;
    private ScreenViewport viewport;

    private Stage backgroundStage;
    private Stage framesStage;

    private Mode mode;
    private Listener listener;

    private Sound winSound;
    private Sound wrongSound;

    private int rendersCount = 0;

    public GuessImage(Mode mode, Listener listener) {
        this.mode = mode;
        this.listener = listener;
    }

    @Override
    public void create() {
        camera = new CustomCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ScreenViewport(camera);

        backgroundStage = new Stage(viewport);
        Background background = new Background(new Texture("image.png"));
        camera.setBackgroundBounds(background.getScaledWidth(), background.getScaledHeight());
        backgroundStage.addActor(background);
        framesStage = new Stage(viewport);

        winSound = Gdx.audio.newSound(Gdx.files.internal("win.mp3"));
        wrongSound = Gdx.audio.newSound(Gdx.files.internal("wrong.mp3"));

        GestureDetector gestureDetector = new GestureDetector(this);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(gestureDetector);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(224f / 255, 224f / 255, 224f / 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        backgroundStage.act();
        backgroundStage.draw();
        framesStage.act();
        framesStage.draw();

        if (rendersCount >= 2) {
            rendersCount = 0;
            Gdx.graphics.setContinuousRendering(false);
        } else {
            Gdx.graphics.setContinuousRendering(true);
            rendersCount++;
        }
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (mode == Mode.PLAY) {
            float zoom = camera.zoom;
            camera.setTranslation(-deltaX * zoom, deltaY * zoom);
            /*cameraPosition.set(camera.position.x - deltaX * zoom, camera.position.y + deltaY * zoom);
            if (worldBounds.contains(cameraPosition)) {
                camera.position.set(cameraPosition, 0f);
                camera.update();
            }*/
            //spaceBounds.contains(camera.position.)
            //camera.translateSafe(-deltaX * zoom, deltaY * zoom);
            /*float minCameraX = camera.zoom * (camera.viewportWidth / 2);
            float maxCameraX = 700 - minCameraX;
            float minCameraY = camera.zoom * (camera.viewportHeight / 2);
            float maxCameraY = 700 - minCameraY;
            camera.position.set(Math.min(maxCameraX, Math.max(camera.position.x - deltaX * zoom, minCameraX)),
                    Math.min(maxCameraY, Math.max(camera.position.y + deltaY * zoom, minCameraY)),
                    0);*/
            /*Vector3 camPos = camera.position;
            float HW = camera.viewportWidth / 2, HH = camera.viewportHeight / 2;
            camPos.x = MathUtils.clamp(camPos.x, HW, Gdx.graphics.getWidth() - HW);
            camPos.y = MathUtils.clamp(camPos.y, HH, Gdx.graphics.getHeight() - HH);
            GdxLog.print(TAG, "asdasdasd " + camera.viewportWidth + " " + Gdx.graphics.getWidth());*/
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1,
                         Vector2 pointer2) {
        if (mode == Mode.PLAY) {
            Vector2 startVector = new Vector2(initialPointer1).sub(initialPointer2);
            Vector2 currentVector = new Vector2(pointer1).sub(pointer2);
            camera.setZoom(camera.startZoom * startVector.len() / currentVector.len());
        }
        return false;
    }

    @Override
    public void pinchStop() {
        if (mode == Mode.PLAY) {
            camera.startZoom = camera.zoom;
        }
    }

    @Override
    public void resize(int width, int height) {
        // todo
    }

    @Override
    public void dispose() {
        Array<Actor> actors = backgroundStage.getActors();
        for (int i = 0; i < actors.size; i++) {
            Actor actor = actors.get(i);
            if (actor instanceof Disposable) {
                ((Disposable) actor).dispose();
            }
        }
        backgroundStage.dispose();
        actors = framesStage.getActors();
        for (int i = 0; i < actors.size; i++) {
            Actor actor = actors.get(i);
            if (actor instanceof Disposable) {
                ((Disposable) actor).dispose();
            }
        }
        framesStage.dispose();
        winSound.dispose();
        wrongSound.dispose();
    }

    public interface Listener {
    }
}