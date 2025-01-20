package eu.mister3551.msr.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import eu.mister3551.msr.Static;
import eu.mister3551.msr.map.character.Enemy;
import eu.mister3551.msr.map.character.Hostage;
import eu.mister3551.msr.database.object.Mission;
import eu.mister3551.msr.map.CleanUp;
import eu.mister3551.msr.map.TiledMapHelper;
import eu.mister3551.msr.map.Distance;
import eu.mister3551.msr.map.Timer;
import eu.mister3551.msr.map.character.Item;
import eu.mister3551.msr.map.character.Player;
import eu.mister3551.msr.map.character.bullet.Bullet;
import eu.mister3551.msr.map.character.collision.DetectionSystem;
import eu.mister3551.msr.map.character.collision.WorldCollision;
import eu.mister3551.msr.screen.camera.Camera;
import eu.mister3551.msr.screen.components.Popup;
import eu.mister3551.msr.screen.link.Callback;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class GameScreen implements Screen {

    private final Mission mission;
    private final Stage stage;
    private final Skin skin;
    private World world;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private final SpriteBatch spriteBatch;
    private final Camera camera;
    private final Popup popup;
    private final Timer timer;
    private final Distance distance;
    private final DetectionSystem detectionSystem;
    private final CleanUp cleanUp;
    private final GameState gameState;

    public static Player player;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Bullet> bulletsToRemove;
    public static ArrayList<Enemy> enemiesToRemove;
    public static ArrayList<Hostage> hostagesToRemove;
    public static ArrayList<Item> itemsToRemove;
    public static ArrayList<Rectangle> ladders;
    public static ArrayList<Rectangle> stopOnLadders;
    public static ArrayList<Rectangle> waters;
    public static ArrayList<MapObject> doors;
    public static HashMap<String, ArrayList<Vector2>> ziplines;
    public static HashMap<String, ArrayList<Vector2>> enemyMovement;

    private Label timerLabel;
    private Label scoreLabel;
    private Label distanceLabel;
    private Label playerLifelabel;
    private Label ammoLabel;
    private Label enemiesLabel;
    private Label hostagesLabel;

    public GameStats gameStats;
    public enum GameStats {
        IN_PROCESS, PAUSE, COMPLETE, FAILED, END
    }

    public GameScreen(Mission mission) {
        this.mission = mission;
        this.skin = Static.skin;
        this.stage = Static.stage;
        this.world = new World(new Vector2(0, -25), true);
        this.box2DDebugRenderer = new Box2DDebugRenderer();

        bullets = new ArrayList<>();
        bulletsToRemove = new ArrayList<>();
        enemiesToRemove = new ArrayList<>();
        hostagesToRemove = new ArrayList<>();
        itemsToRemove = new ArrayList<>();
        ladders = new ArrayList<>();
        stopOnLadders = new ArrayList<>();
        waters = new ArrayList<>();
        doors = new ArrayList<>();
        ziplines = new HashMap<>();
        enemyMovement = new HashMap<>();

        this.orthogonalTiledMapRenderer = new TiledMapHelper(world, mission).setupMap().renderer;

        this.spriteBatch = new SpriteBatch();
        this.camera = new Camera();
        this.popup = Static.popup;
        this.timer = Static.timer;
        this.distance = new Distance();
        this.detectionSystem = new DetectionSystem(world);
        this.cleanUp = new CleanUp();
        this.gameStats = GameStats.IN_PROCESS;
        this.gameState = Static.gameState.get(mission.getMap());

        player = gameState.getPlayer();
        if (world != gameState.getWorld()) {
            world = gameState.getWorld();
            Body newBody = Static.bodyHelper.setWorld(world).copyBody(player.getBody(), player);
            Static.bodyHelper.setWorld(world).destroyBody(player.getBody());
            world.destroyBody(player.getBody());
            player.setBody(newBody);
            bullets.clear();
            bulletsToRemove.clear();
        } else {
            timer.start();
        }
        new WorldCollision(world, player, getMission().getMap());
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

        spriteBatch.setProjectionMatrix(camera.getCamera().combined);
        spriteBatch.begin();

        for (Bullet bullet : bullets) {
            bullet.render(spriteBatch, delta);
        }

        for (Enemy enemy : Static.gameState.get(mission.getMap()).getEnemies()) {
            enemy.render(spriteBatch, delta);
        }

        for (Hostage hostage : Static.gameState.get(mission.getMap()).getHostages()) {
            hostage.render(spriteBatch, delta);
        }

        for (Item item : Static.gameState.get(mission.getMap()).getItems()) {
            item.render(spriteBatch, delta);
        }

        player.render(spriteBatch, delta);

        //box2DDebugRenderer.render(world, camera.getCamera().combined.scl(Static.PPM));

        spriteBatch.end();

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Static.options.getPause())) {
            if (popup.isOpen()) {
                gameStats = GameStats.PAUSE;
                stage.addActor(popup.pausePopup(skin, GameScreen.this));
            } else if (!popup.isOpen()) {
                gameStats = GameStats.IN_PROCESS;
                popup.close();
            }
        }

        switch (gameStats) {
            case IN_PROCESS:
                resumeGame(delta);
                break;
            case PAUSE:
            case END:
                pauseGame();
                break;
            case COMPLETE:
                showCompletePopup();
                break;
            case FAILED:
                showEndPopup();
                break;
        }
    }

    private void showCompletePopup() {
        gameStats = GameStats.END;
        distanceLabel.setText("Distance: 0.00 m");
        endGame(true);
        if (popup.isOpen()) {
            Static.stage.addActor(popup.loadingPopup(skin));
        }
        Static.data.insertMission(Static.statistics, new Callback() {
            @Override
            public void onSuccess(Object object) {
                Gdx.app.postRunnable(() -> {
                    popup.close();
                    if (popup.isOpen()) {
                        stage.addActor(popup.missionCompletePopup(skin, GameScreen.this));
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                Gdx.app.postRunnable(() -> {
                    if (popup.isOpen()) {
                        popup.close();
                    }

                    if (popup.isOpen()) {
                        Static.stage.addActor(popup.errorPopup(skin, errorMessage));
                    }
                });
            }
        });
    }

    private void showEndPopup() {
        gameStats = GameStats.END;
        if (popup.isOpen()) {
            endGame(false);
            if (popup.isOpen()) {
                Static.stage.addActor(popup.loadingPopup(skin));
            }
            Static.data.insertMission(Static.statistics, new Callback() {
                @Override
                public void onSuccess(Object object) {
                    Gdx.app.postRunnable(() -> {
                        popup.close();
                        if (popup.isOpen()) {
                            stage.addActor(popup.missionFailedPopup(skin, GameScreen.this));
                        }
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    Gdx.app.postRunnable(() -> {
                        if (popup.isOpen()) {
                            popup.close();
                        }

                        if (popup.isOpen()) {
                            Static.stage.addActor(popup.errorPopup(skin, errorMessage));
                        }
                    });
                }
            });
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
        timer.pause();
        player.pause();

        for (Enemy enemy : Static.gameState.get(mission.getMap()).getEnemies()) {
            enemy.dispose();
        }

        for (Hostage hostage : Static.gameState.get(mission.getMap()).getHostages()) {
            hostage.pause();
        }

        for (Item item : Static.gameState.get(mission.getMap()).getItems()) {
            item.pause();
        }
    }

    @Override
    public void resume() {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        popup.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        player.resume();

        for (Enemy enemy : Static.gameState.get(mission.getMap()).getEnemies()) {
            enemy.resume();
        }

        for (Hostage hostage : Static.gameState.get(mission.getMap()).getHostages()) {
            hostage.resume();
        }

        for (Item item : Static.gameState.get(mission.getMap()).getItems()) {
            item.resume();
        }
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
        player.dispose();

        for (Enemy enemy : Static.gameState.get(mission.getMap()).getEnemies()) {
            enemy.dispose();
        }

        for (Hostage hostage : Static.gameState.get(mission.getMap()).getHostages()) {
            hostage.dispose();
        }

        for (Item item : Static.gameState.get(mission.getMap()).getItems()) {
            item.dispose();
        }
    }

    private void update(float delta) {
        world.step(delta, 6, 2);
        world.clearForces();

        timerLabel.setText("Time: " + timer.toString());
        distanceLabel.setText("Distance: " + distance.calculate(player));
        playerLifelabel.setText(player.getLife());
        ammoLabel.setText("Ammo: " + (player.isReloading() ? "Reloading" : (player.getWeapon().getActiveMagazineCapacity()) + "/" + player.getWeapon().getBackupMagazinesCapacity()));
        enemiesLabel.setText(Static.gameState.get(mission.getMap()).getEnemies().size());
        hostagesLabel.setText(Static.gameState.get(mission.getMap()).getHostages().size());

        scoreLabel.setText("Score: " + Static.statistics.getScore());

        camera.update(player);
        timer.update();

        for (Bullet bullet : bullets) {
            bullet.update();
        }

        for (Enemy enemy : Static.gameState.get(mission.getMap()).getEnemies()) {
            enemy.update(delta);

            boolean isDetected = detectionSystem.isDetected(player, enemy);
            enemy.setPlayerDetected(isDetected);

            if (isDetected && !enemy.isPlayerPreviouslyDetected()) {
                Static.statistics.setEnemiesAlerted(Static.statistics.getEnemiesAlerted() + 1);
                enemy.setPlayerPreviouslyDetected(true);
            } else if (!isDetected && enemy.isPlayerPreviouslyDetected()) {
                enemy.setPlayerPreviouslyDetected(false);
            }
            enemy.setBulletDetected(detectionSystem.bulletDetection(enemy));
        }

        for (Hostage hostage : Static.gameState.get(mission.getMap()).getHostages()) {
            hostage.update(delta);
        }

        for (Item item : Static.gameState.get(mission.getMap()).getItems()) {
            item.update(delta);
        }

        player.update(delta);
        cleanUp.all(mission.getMap());
        checkGameState();
    }

    private void checkGameState() {
        if (distance.calculate(player).startsWith("0.")) {
            boolean allStatesValid = true;

            for (GameState state : Static.gameState.values()) {
                if (!state.getEnemies().isEmpty() || !state.getHostages().isEmpty()) {
                    allStatesValid = false;
                    break;
                }
            }

            //TODO add for controller and mobile
            if (allStatesValid && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                gameStats = GameStats.COMPLETE;
            }
        }

        if (player.getLife() == 0) {
            gameStats = GameStats.FAILED;
        }
    }

    private void endGame(boolean isWin) {
        Static.statistics.setIdMission((long) mission.getId());
        Static.statistics.setIdUser(Long.parseLong(mission.getIdUser()));
        Static.statistics.setWin(isWin);
        Static.statistics.setAccuracy();
        Static.statistics.setUsedTime(getTimer().string());
        Static.statistics.setDistanceTraveled(distance.calculateProgress(player, gameState));
        Static.statistics.getMoney().put("total", Static.statistics.getMoney().entrySet().stream().filter(entry -> !entry.getKey().equals("ammoCosts")).map(entry -> entry.getValue()).reduce(0.0f, Float::sum) - Static.statistics.getMoney().getOrDefault("ammoCosts", 0.0f));
    }

    private void resumeGame(float delta) {
        update(delta);
        timer.resume();
    }

    private void pauseGame() {
        timer.pause();
    }
}
