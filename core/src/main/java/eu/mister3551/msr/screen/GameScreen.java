package eu.mister3551.msr.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import eu.mister3551.msr.Static;
import eu.mister3551.msr.database.object.Mission;
import eu.mister3551.msr.map.TiledMapHelper;
import eu.mister3551.msr.map.character.Player;
import eu.mister3551.msr.map.character.collision.WorldCollision;
import eu.mister3551.msr.screen.camera.Camera;
import eu.mister3551.msr.screen.components.Popup;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

@Data
public class GameScreen implements Screen {

    private final Stage stage;
    private final Skin skin;
    private final World world;
    private final WorldCollision worldCollision;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private final SpriteBatch spriteBatch;
    private final Camera camera;
    private final Popup popup;

    public static Player player;
    public static ArrayList<Rectangle> ladders;
    public static ArrayList<Rectangle> waters;
    public static ArrayList<MapObject> doors;
    public static HashMap<String, ArrayList<Vector2>> ziplines;

    private Label timerLabel;
    private Label scoreLabel;
    private Label distanceLabel;
    private Label playerLifelabel;
    private Label ammoLabel;
    private Label enemiesLabel;
    private Label hostagesLabel;

    private GameStats gameStats;
    private enum GameStats {
        IN_PROCESS, PAUSE, COMPLETE, FAILED, END
    }

    public GameScreen(Mission mission) {
        this.skin = Static.skin;
        this.stage = Static.stage;
        this.world = new World(new Vector2(0, -25), true);
        this.box2DDebugRenderer = new Box2DDebugRenderer();

        ladders = new ArrayList<>();
        waters = new ArrayList<>();
        doors = new ArrayList<>();
        ziplines = new HashMap<>();

        this.orthogonalTiledMapRenderer = new TiledMapHelper(world).setupMap(mission.getMap()).renderer;
        this.worldCollision = new WorldCollision(world, player);
        this.spriteBatch = new SpriteBatch();
        this.camera = new Camera();
        this.popup = Static.popup;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.padLeft(30.0f);
        table.padTop(10.0f);
        table.align(Align.topLeft);
        table.setFillParent(true);

        Table table1 = new Table();

        playerLifelabel = new Label(null, skin);
        table1.add(playerLifelabel);

        Image image = new Image(skin, "heart");
        image.setTouchable(Touchable.disabled);
        table1.add(image).padLeft(5.0f).maxSize(30.0f);
        table.add(table1).padLeft(5.0f);

        table1 = new Table();

        enemiesLabel = new Label(null, skin);
        table1.add(enemiesLabel);

        image = new Image(skin, "enemy");
        image.setTouchable(Touchable.disabled);
        table1.add(image).padLeft(5.0f).maxSize(30.0f);
        table.add(table1).padLeft(10.0f);

        table1 = new Table();

        hostagesLabel = new Label(null, skin);
        table1.add(hostagesLabel);

        image = new Image(skin, "hostage");
        image.setTouchable(Touchable.disabled);
        table1.add(image).padLeft(5.0f).maxSize(30.0f);
        table.add(table1).padLeft(10.0f);

        stage.addActor(table);

        table = new Table();
        table.padTop(15.0f);
        table.align(Align.top);
        table.setFillParent(true);

        timerLabel = new Label(null, skin);
        table.add(timerLabel).padBottom(5.0f);

        table.row();
        scoreLabel = new Label(null, skin);
        table.add(scoreLabel);
        stage.addActor(table);

        table = new Table();
        table.padRight(20.0f);
        table.align(Align.right);
        table.setFillParent(true);

        table1 = new Table();
        table1.pad(0.0f);

        distanceLabel = new Label(null, skin);
        table1.add(distanceLabel).padRight(50.0f).align(Align.left);
        table.add(table1);

        table.row();
        table1 = new Table();
        table1.padBottom(200.0f);

        ammoLabel = new Label(null, skin);
        table1.add(ammoLabel);
        table.add(table1).align(Align.left);
        stage.addActor(table);

        table = new Table();
        table.align(Align.topRight);
        table.setFillParent(true);

        TextButton textButton = new TextButton("Options", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (popup.isOpen()) {
                    gameStats = GameStats.PAUSE;
                    stage.addActor(popup.pausePopup(skin, GameScreen.this));
                }
                return true;
            }
        });
        table.add(textButton);
        stage.addActor(table);

        player.show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        orthogonalTiledMapRenderer.setView(camera.getCamera());
        orthogonalTiledMapRenderer.render();

        if (Gdx.app.getType().equals(Application.ApplicationType.Android)) {
            camera.setZoom(0.6f);
        }

        stage.act(delta);
        stage.draw();

        spriteBatch.setProjectionMatrix(camera.getCamera().combined);
        spriteBatch.begin();

        player.render(spriteBatch, delta);

        //box2DDebugRenderer.render(world, camera.getCamera().combined.scl(Static.PPM));

        spriteBatch.end();

        update(delta);
    }

    @Override
    public void resize(int width, int height) {
        camera.getCamera().setToOrtho(false, width, height);
        camera.update(player);
        stage.getViewport().update(width, height, true);
        popup.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        popup.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
        player.dispose();
    }

    private void update(float delta) {
        world.step(delta, 6, 2);
        world.clearForces();

        player.update(delta);
        camera.update(player);
    }
}
