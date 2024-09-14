package mister3551.msr.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import mister3551.msr.game.Static;
import mister3551.msr.game.characters.Collision;
import mister3551.msr.game.characters.Player;
import mister3551.msr.game.map.TiledMapHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class GameScreen implements Screen {

    private final World world;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final OrthographicCamera orthographicCamera;
    private final OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private final SpriteBatch spriteBatch;
    private final Player player;
    private final Collision collision;

    public GameScreen() {
        this.world = new World(new Vector2(0, -25), true);
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.orthographicCamera = new OrthographicCamera();

        Static.setLadders(new ArrayList<>());
        Static.setStopOnLadders(new ArrayList<>());
        Static.setWaters(new ArrayList<>());
        Static.setZiplines(new HashMap<>());

        TiledMapHelper tiledMapHelper = new TiledMapHelper(world);
        this.orthogonalTiledMapRenderer = tiledMapHelper.setupMap();
        this.spriteBatch = new SpriteBatch();
        this.player = Static.getPlayer();
        this.collision = new Collision(world, player);
    }

    @Override
    public void show() {
        player.show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        orthogonalTiledMapRenderer.setView(orthographicCamera);
        orthogonalTiledMapRenderer.render();

        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        spriteBatch.begin();

        player.render(spriteBatch, delta);

        //box2DDebugRenderer.render(world, orthographicCamera.combined.scl(Static.PPM));

        spriteBatch.end();

        update(delta);
    }

    @Override
    public void resize(int width, int height) {
        orthographicCamera.setToOrtho(false, width, height);
        player.resize(width, height);
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
        player.dispose();
    }

    private void update(float delta) {
        world.step(delta, 6, 2);
        world.clearForces();
        cameraOnPlayer();

        collision.collide();
        player.update(delta);
    }

    private void cameraOnPlayer() {
        float x = player.getBody().getPosition().x * Static.PPM;
        float y = player.getBody().getPosition().y * Static.PPM;

        orthographicCamera.position.x = x;
        orthographicCamera.position.y = y;
        orthographicCamera.update();
    }
}
