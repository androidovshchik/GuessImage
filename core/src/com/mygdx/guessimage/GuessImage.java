package com.mygdx.guessimage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.guessimage.model.Background;
import com.mygdx.guessimage.model.Frame;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class GuessImage extends BaseAdapter {

    private static final String TAG = GuessImage.class.getSimpleName();

    private Rectangle bounds = new Rectangle();

    public BoundedCamera camera = new BoundedCamera(bounds);
    private ScreenViewport viewport;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Stage backgroundStage;
    private Stage framesStage;

    private Mode mode;
    private Listener listener;

    private Sound winSound, wrongSound;
    private long winId, wrongId;

    private Frame currentFrame;
    private Vector3 coordinates = new Vector3();

    private String initialPath;
    private int rendersCount = 0;
    private int guessedCount = 0;

    public GuessImage(Mode mode, String background, Listener listener) {
        this.mode = mode;
        this.initialPath = background;
        this.listener = listener;
    }

    @Override
    public void create() {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ScreenViewport(camera);
        spriteBatch = new SpriteBatch();
        backgroundStage = new Stage(viewport, spriteBatch);
        framesStage = new Stage(viewport, spriteBatch);
        if (initialPath != null) {
            setBackground(initialPath);
        }

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
        Gdx.gl.glLineWidth(Utils.dip(Frame.WIDTH));

        if (mode == Mode.PLAY) {
            camera.normalize();
        }

        shapeRenderer.setProjectionMatrix(camera.combined);

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
        } else if (mode == Mode.EDIT) {
            if (Utils.countFingers() == 1) {
                camera.unproject(coordinates.set(x, y, 0));
                Frame frame = (Frame) framesStage.hit(coordinates.x, coordinates.y, false);
                if (frame != null) {
                    frame.setAction(coordinates.x, coordinates.y);
                }
                currentFrame = frame;
            }
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (mode == Mode.PLAY) {
            camera.setTranslation(-deltaX * camera.zoom, deltaY * camera.zoom);
        } else if (mode == Mode.EDIT) {
            Frame frame = currentFrame;
            if (frame != null) {
                camera.unproject(coordinates.set(x, y, 0));
                frame.pan(coordinates.x, coordinates.y, deltaX * camera.zoom, -deltaY * camera.zoom);
            }
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
        int fingers = Utils.countFingers();
        if (mode == Mode.PLAY) {
            camera.idle = fingers == 0;
        } else if (mode == Mode.EDIT) {
            if (bounds.perimeter() > 0 && fingers == 0) {
                Frame frame = currentFrame;
                if (frame != null) {
                    float x0 = Math.max(0, frame.getX() - bounds.x);
                    float y0 = Math.max(0, frame.getY() - bounds.y);
                    listener.onFrameChanged(frame.id, x0, y0, frame.getWidth(), frame.getHeight());
                }
            }
        }
        if (fingers == 0) {
            Gdx.graphics.requestRendering();
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if (mode == Mode.PLAY) {
            if (bounds.perimeter() <= 0 || guessedCount < 0) {
                return false;
            }
            camera.unproject(coordinates.set(x, y, 0));
            if (bounds.contains(coordinates.x, coordinates.y)) {
                guessedCount = 0;
                List<Long> ids = new ArrayList<>();
                Array<Actor> actors = framesStage.getActors();
                for (int i = 0; i < actors.size; i++) {
                    if (actors.get(i) instanceof Frame) {
                        Frame frame = (Frame) actors.get(i);
                        if (frame.contains(coordinates.x, coordinates.y)) {
                            frame.isDone = true;
                            ids.add(frame.id);
                        }
                        if (frame.isDone) {
                            guessedCount++;
                        }
                    }
                }
                if (ids.size() > 0) {
                    listener.onFramesGuessed(ids);
                } else {
                    wrongSound.stop(wrongId);
                    wrongId = wrongSound.play(1f);
                    return false;
                }
                if (guessedCount >= actors.size) {
                    guessedCount = -1;
                    winSound.stop(winId);
                    winSound.play(1f);
                }
            }
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        // todo
    }

    public void setBackground(String path) {
        backgroundStage.clear();
        Background background = new Background(new Texture(Gdx.files.absolute(path)));
        background.notifyBounds(bounds);
        backgroundStage.addActor(background);
    }

    public void addFrame(Long id) {
        framesStage.addActor(new Frame(shapeRenderer, id, mode, bounds));
    }

    public void addFrame(long id, float x0, float y0, float width, float height) {
        framesStage.addActor(new Frame(shapeRenderer, id, mode, bounds, x0, y0, width, height));
    }

    public void markFrame(Long id) {
        Array<Actor> actors = framesStage.getActors();
        for (int i = 0; i < actors.size; i++) {
            if (actors.get(i) instanceof Frame) {
                Frame frame = (Frame) actors.get(i);
                if (frame.id == id) {
                    frame.isDone = true;
                }
            }
        }
    }

    @Override
    public void dispose() {
        listener = null;
        spriteBatch.dispose();
        shapeRenderer.dispose();
        backgroundStage.dispose();
        framesStage.dispose();
        winSound.dispose();
        wrongSound.dispose();
    }

    public interface Listener {

        void onFrameChanged(long id, float x0, float y0, float width, float height);

        void onFramesGuessed(List<Long> ids);
    }
}