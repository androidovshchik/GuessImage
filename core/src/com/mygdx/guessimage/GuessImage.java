package com.mygdx.guessimage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.guessimage.model.Background;
import com.mygdx.guessimage.model.Frame;

public class GuessImage extends BaseAdapter {

    private static final String TAG = GuessImage.class.getSimpleName();

    private BoundedCamera camera = new BoundedCamera();
    private ScreenViewport viewport;
    private SpriteBatch spriteBatch;
    private Stage backgroundStage;
    private Stage framesStage;

    private Mode mode;
    private Listener listener;

    private Sound winSound, wrongSound;

    private int rendersCount = 0;

    public GuessImage(Mode mode, Listener listener) {
        this.mode = mode;
        this.listener = listener;
    }

    @Override
    public void create() {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ScreenViewport(camera);
        spriteBatch = new SpriteBatch();
        backgroundStage = new Stage(viewport, spriteBatch);
        Background background = new Background(new Texture("image.png"));
        camera.setImageBounds(background.getScaledWidth(), background.getScaledHeight());
        backgroundStage.addActor(background);
        framesStage = new Stage(viewport, spriteBatch);
        framesStage.addActor(new Frame());

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
        Gdx.gl.glLineWidth(Utils.dip(4));

        if (mode == Mode.PLAY) {
            camera.normalize();
        }

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
        if (mode == Mode.PLAY) {
            camera.idle = false;
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (mode == Mode.PLAY) {
            camera.setTranslation(-deltaX * camera.zoom, deltaY * camera.zoom);
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
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (mode == Mode.PLAY) {
            int activeTouch = 0;
            for (int i = 0; i < 20; i++) {
                if (Gdx.input.isTouched(i)) {
                    activeTouch++;
                }
            }
            camera.idle = activeTouch == 0;
            if (camera.idle) {
                Gdx.graphics.requestRendering();
            }
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        // todo
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
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