package com.mygdx.guessimage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.guessimage.model.Background;

public class GuessImage extends BaseAdapter {

    private static final String TAG = GuessImage.class.getSimpleName();

    private OrthographicCamera camera;
    private FitViewport viewport;
    private ShapeRenderer shapeRenderer;
    private Stage stage;

    private Mode mode;
    private Listener listener;

    private Sound winSound;
    private Sound wrongSound;

    private float startZoom = 1f;
    private int rendersCount = 0;

    public GuessImage(Mode mode, Listener listener) {
        this.mode = mode;
        this.listener = listener;
    }

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        shapeRenderer = new ShapeRenderer();

        stage = new Stage(viewport);
        Background background = new Background(new Texture("image.png"));
        stage.addActor(background);

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

        shapeRenderer.setProjectionMatrix(camera.combined);

        stage.act();
        stage.draw();

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
            camera.translate(-deltaX * zoom, deltaY * zoom);
            camera.update();
        }
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1,
                         Vector2 pointer2) {
        if (mode == Mode.PLAY) {
            Vector2 startVector = new Vector2(initialPointer1).sub(initialPointer2);
            Vector2 currentVector = new Vector2(pointer1).sub(pointer2);
            float zoom = startZoom * startVector.len() / currentVector.len();
            camera.zoom = Math.max(0.2f, Math.min(1, zoom));
            camera.update();
        }
        return false;
    }

    @Override
    public void pinchStop() {
        if (mode == Mode.PLAY) {
            startZoom = camera.zoom;
        }
    }

    @Override
    public void resize(int width, int height) {
        // todo
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        stage.dispose();
        winSound.dispose();
        wrongSound.dispose();
    }

    public interface Listener {
    }
}