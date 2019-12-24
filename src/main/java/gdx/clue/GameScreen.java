package gdx.clue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import static gdx.clue.ClueMain.TILE_DIM;
import static gdx.clue.ClueMain.VIEWPORT_DIM_HEIGHT;
import static gdx.clue.ClueMain.VIEWPORT_DIM_WIDTH;

public class GameScreen implements Screen, InputProcessor {

    private float time = 0;

    private final Stage stage, mapStage;
    private final Batch batch, mapBatch;
    private final OrthogonalTiledMapRenderer renderer;
    private final OrthographicCamera camera;
    private final Viewport mapViewport;
    private final Viewport viewport = new ScreenViewport();
    InputMultiplexer input;

    public int mapPixelHeight;
    private final Vector3 newMapPixelCoords = new Vector3();
    private final Vector2 currentMousePos = new Vector2();

    private final Clue main;

    public GameScreen(Clue main) {
        this.main = main;

        stage = new Stage(viewport);
        batch = new SpriteBatch();

        camera = new OrthographicCamera(VIEWPORT_DIM_WIDTH, VIEWPORT_DIM_HEIGHT);
        mapViewport = new ScreenViewport(camera);

        renderer = null;//new OrthogonalTiledMapRenderer(tmxMap, 1f);
        mapBatch = null;//renderer.getBatch();
        mapStage = null;//new Stage(mapViewport, mapBatch);

        mapPixelHeight = TILE_DIM * 15;
        setMapPixelCoords(14, 9);

        input = new InputMultiplexer(this, stage);
    }

    public void setMapPixelCoords(float x, float y) {
        newMapPixelCoords.x = x * TILE_DIM;
        newMapPixelCoords.y = mapPixelHeight - y * TILE_DIM;
    }

    private void setCurrentMapCoords(Vector3 v) {
        Vector3 tmp = camera.unproject(new Vector3(TILE_DIM * 7, TILE_DIM * 4, 0), TILE_DIM, TILE_DIM, VIEWPORT_DIM_WIDTH, VIEWPORT_DIM_HEIGHT);
        v.set(Math.round(tmp.x / TILE_DIM) - 1, ((mapPixelHeight - Math.round(tmp.y) - TILE_DIM) / TILE_DIM) - 0, 0);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(input);

    }

    @Override
    public void render(float delta) {
        time += delta;

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(
                newMapPixelCoords.x + 0 * TILE_DIM + 48,
                newMapPixelCoords.y - 1 * TILE_DIM + 48,
                0);

        camera.update();

        renderer.setView(camera);

        renderer.setView(camera.combined,
                camera.position.x - TILE_DIM * 7,
                camera.position.y - TILE_DIM * 4,
                VIEWPORT_DIM_WIDTH, VIEWPORT_DIM_HEIGHT);

        renderer.render();

        mapBatch.setProjectionMatrix(camera.combined);
        mapBatch.begin();
        mapBatch.end();

        mapStage.act();
        mapStage.draw();

        batch.begin();

        //grid.draw(batch);
        //TextureRegion tr = (TextureRegion) ActorType.PLAYER1.getAnimation(Actor.NORTH).getKeyFrame(time, true);
        //batch.draw(tr, 7 * TILE_DIM + 0, 4 * TILE_DIM + 0);
        //Vector3 v = new Vector3();
        //setCurrentMapCoords(v);
        //Life.font.draw(batch, String.format("%s, %s\n", v.x, v.y), 20, 20);
        //Life.hud.render(batch, game);
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        mapViewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
    private static Pixmap createGrid() {

        int imgWidth = TILE_DIM * 24;
        int imgHeight = TILE_DIM * 25;

        Pixmap pix = new Pixmap(imgWidth, imgHeight, Pixmap.Format.RGBA8888);

        pix.setColor(255f, 255f, 0f, 1f);

        for (int x = 0; x < imgWidth; x += TILE_DIM) {
            pix.drawLine(x, 0, x, imgHeight);
        }
        for (int y = 0; y < imgHeight; y += TILE_DIM) {
            pix.drawLine(0, y, imgWidth, y);
        }

        return pix;
    }

}
