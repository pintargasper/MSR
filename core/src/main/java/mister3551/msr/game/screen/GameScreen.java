package mister3551.msr.game.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import mister3551.msr.game.Static;
import mister3551.msr.game.characters.Collision;
import mister3551.msr.game.characters.DetectionSystem;
import mister3551.msr.game.characters.object.Bullet;
import mister3551.msr.game.characters.object.Enemy;
import mister3551.msr.game.characters.object.Player;
import mister3551.msr.game.database.object.Statistics;
import mister3551.msr.game.map.CleanUp;
import mister3551.msr.game.map.Converter;
import mister3551.msr.game.map.TiledMapHelper;
import mister3551.msr.game.screen.camera.Camera;
import mister3551.msr.game.screen.components.Popup;
import mister3551.msr.game.screen.timer.Timer;

import java.util.ArrayList;
import java.util.HashMap;

public class GameScreen implements Screen {

    private final Stage stage;
    private final Skin skin;
    private final World world;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private final SpriteBatch spriteBatch;
    private final Player player;
    private final ArrayList<Enemy> enemies;
    private final Collision collision;
    private final DetectionSystem detectionSystem;
    private final CleanUp cleanUp;
    private final Camera camera;
    private final Timer timer;
    private final Popup popup;

    private Label timerLabel;
    private Label scoreLabel;
    private Label distanceLabel;
    private Label playerLifelabel;
    private Label ammoLabel;
    private Label enemiesLabel;

    private final String mapName;

    public GameStats gameStats;

    public enum GameStats {
        IN_PROCESS,
        PAUSE,
        COMPLETE,
        FAILED
    }

    public GameScreen(String mapName) {
        this.skin = Static.getSkin();
        this.stage = Static.getStage();

        this.world = new World(new Vector2(0, -25), true);
        this.box2DDebugRenderer = new Box2DDebugRenderer();

        Static.setStatistics(new Statistics());
        Static.setEnemies(new ArrayList<>());
        Static.setEnemiesToRemove(new ArrayList<>());
        Static.setBullets(new ArrayList<>());
        Static.setBulletsToRemove(new ArrayList<>());
        Static.setLadders(new ArrayList<>());
        Static.setStopOnLadders(new ArrayList<>());
        Static.setWaters(new ArrayList<>());
        Static.setZiplines(new HashMap<>());
        Static.setEnemyMovement(new HashMap<>());

        TiledMapHelper tiledMapHelper = new TiledMapHelper(world);
        this.orthogonalTiledMapRenderer = tiledMapHelper.setupMap(mapName);
        this.spriteBatch = new SpriteBatch();
        this.player = Static.getPlayer();
        this.enemies = Static.getEnemies();
        this.collision = new Collision(world, player);
        this.detectionSystem = new DetectionSystem(world);
        this.cleanUp = new CleanUp();
        this.camera = new Camera();
        this.timer = new Timer();
        this.popup = Static.getPopup();
        this.gameStats = GameStats.IN_PROCESS;
        this.mapName = mapName;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setName("left");
        table.padLeft(30.0f);
        table.padTop(10.0f);
        table.align(Align.topLeft);
        table.setFillParent(true);

        Table table1 = new Table();
        table1.setName("life");

        playerLifelabel = new Label(null, skin);
        table1.add(playerLifelabel);

        Image image = new Image(skin, "heart");
        image.setTouchable(Touchable.disabled);
        table1.add(image).padLeft(5.0f).maxSize(30.0f);
        table.add(table1).padLeft(5.0f);

        table1 = new Table();
        table1.setName("enemies");

        enemiesLabel = new Label(null, skin);
        table1.add(enemiesLabel);

        image = new Image(skin, "enemy");
        image.setTouchable(Touchable.disabled);
        table1.add(image).padLeft(5.0f).maxSize(30.0f);
        table.add(table1).padLeft(10.0f);

        table1 = new Table();
        table1.setName("hostages");

        Label label = new Label(null, skin);
        table1.add(label);

        image = new Image(skin, "hostage");
        image.setTouchable(Touchable.disabled);
        table1.add(image).padLeft(5.0f).maxSize(30.0f);
        table.add(table1).padLeft(10.0f);

        stage.addActor(table);

        table = new Table();
        table.setName("center");
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

        if (Gdx.app.getType().equals(Application.ApplicationType.Android)) {
            camera.setZoom(0.6f);
        }

        orthogonalTiledMapRenderer.setView(camera.getCamera());
        orthogonalTiledMapRenderer.render();

        stage.act(delta);
        stage.draw();

        spriteBatch.setProjectionMatrix(camera.getCamera().combined);
        spriteBatch.begin();

        for (Enemy enemy : enemies) {
            if (camera.isVisible(enemy.getBody().getPosition())) {
                enemy.render(spriteBatch, delta);
            }
        }

        for (Bullet bullet : Static.getBullets()) {
            bullet.render(spriteBatch, delta);
        }
        player.render(spriteBatch, delta);

        //box2DDebugRenderer.render(world, camera.getCamera().combined.scl(Static.PPM));

        spriteBatch.end();

        if (gameStats == GameStats.IN_PROCESS) {
            update(delta);
            timer.resume();
        }

        if (gameStats == GameStats.PAUSE) {
            timer.pause();
        }

        if (gameStats == GameStats.COMPLETE) {
            if (popup.isOpen()) {
                stage.addActor(popup.missionCompletePopup(skin, this));
            }
        }

        if (gameStats == GameStats.FAILED) {
            if (popup.isOpen()) {
                stage.addActor(popup.missionFailedPopup(skin, this));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.getCamera().setToOrtho(false, width, height);
        camera.update(player);
        stage.getViewport().update(width, height, true);
        player.resize(width, height);
        popup.resize(width, height);
    }

    @Override
    public void pause() {
        if (gameStats == GameStats.IN_PROCESS) {
            gameStats = GameStats.PAUSE;
            timer.pause();
            if (popup.isOpen()) {
                stage.addActor(popup.pausePopup(skin, this));
            }
        }
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

        for (Enemy enemy : enemies) {
            enemy.dispose();
        }

        for (Bullet bullet : Static.getBullets()) {
            bullet.dispose();
        }
    }

    private void update(float delta) {
        world.step(delta, 6, 2);
        world.clearForces();
        collision.collide();

        timerLabel.setText("Time: " + timer.toString());
        distanceLabel.setText("Distance: " + calculateDistance(player) + " m");
        playerLifelabel.setText(player.getLive());
        ammoLabel.setText("Ammo: " + (player.isReloading() ? "Reloading" : (player.getWeapon().getActiveMagazineCapacity()) + "/" + player.getWeapon().getBackupMagazinesCapacity()));
        enemiesLabel.setText(enemies.size());

        scoreLabel.setText("Score: " + Static.getStatistics().getScore());

        camera.update(player);
        timer.update();
        player.update(delta);

        if (Gdx.input.isKeyJustPressed(Static.getOptions().getPause())) {
            if (popup.isOpen()) {
                gameStats = GameStats.PAUSE;
                stage.addActor(popup.pausePopup(skin, GameScreen.this));
            }
        }

        for (Enemy enemy : enemies) {
            enemy.update(delta);
            enemy.setPlayerDetected(detectionSystem.isDetected(player, enemy));
            enemy.setBulletComing(detectionSystem.bulletDetection(enemy));
        }

        for (Bullet bullet : Static.getBullets()) {
            bullet.update();
        }

        checkGameState();
        cleanUp.cleanUpBullets();
        cleanUp.cleanUpCharacters();
    }

    private int calculateDistance(Player player) {
        Vector2 playerPosition = player.getBody().getPosition();
        Rectangle endRectangle = Static.getEnd();

        float distanceInPixels = new Vector2(Converter.coordinates(endRectangle)).dst(playerPosition);
        return (int) distanceInPixels;
    }

    private void checkGameState() {
        if (calculateDistance(player) == 0 && enemies.isEmpty()) {
            gameStats = GameStats.COMPLETE;
        }

        if (player.getLive() == 0) {
            gameStats = GameStats.FAILED;
        }
    }

    public Timer getTimer() {
        return timer;
    }

    public String getMapName() {
        return mapName;
    }
}
