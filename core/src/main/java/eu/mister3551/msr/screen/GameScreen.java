package eu.mister3551.msr.screen;

import box2dLight.RayHandler;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.database.object.Mission;
import eu.mister3551.msr.database.object.Statistics;
import eu.mister3551.msr.map.*;
import eu.mister3551.msr.map.character.Enemy;
import eu.mister3551.msr.map.character.Hostage;
import eu.mister3551.msr.map.character.Item;
import eu.mister3551.msr.map.character.Player;
import eu.mister3551.msr.map.character.control.Mobile;
import eu.mister3551.msr.map.character.movement.Distance;
import eu.mister3551.msr.map.character.movement.enemy.DetectionSystem;
import eu.mister3551.msr.map.character.weapon.bullet.Bullet;
import eu.mister3551.msr.map.light.Generator;
import eu.mister3551.msr.screen.camera.Camera;
import eu.mister3551.msr.screen.components.Popup;
import eu.mister3551.msr.screen.gamescreen.Timer;
import eu.mister3551.msr.screen.gamescreen.Visualisation;
import eu.mister3551.msr.screen.link.Callback;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
public class GameScreen implements Screen {

    private final World world;
    private final RayHandler rayHandler;
    private final Mission mission;
    private final Skin skin;
    private final Stage stage;
    private final Popup popup;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private final SpriteBatch spriteBatch;
    private final Camera camera;
    private final Player player;
    private final GameState gameState;
    private final BodyHelper bodyHelper;
    private final DetectionSystem detectionSystem;
    private final ArrayList<Bullet> bullets;
    private final ArrayList<Bullet> bulletsToRemove;
    private final ArrayList<Enemy> enemiesToRemove;
    private final ArrayList<Hostage> hostageToRemove;
    private final ArrayList<Item> itemsToRemove;
    private final Map<String, Integer> characterCounts;
    private final CleanUp cleanUp;
    private final Visualisation visualisation;
    private final Timer timer;
    private final Distance distance;
    private final float light;
    private Label ammoLabel;
    private Label distanceLabel;

    public GameStats gameStats;
    public enum GameStats {
        IN_PROCESS, PAUSE, COMPLETE, FAILED, END
    }

    public GameScreen(Mission mission, GameState gameState) {
        this.mission = mission;
        this.skin = Constants.skin;
        this.stage = Constants.stage;
        this.popup = Constants.popup;
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.spriteBatch = new SpriteBatch();
        this.camera = new Camera();
        this.gameState = gameState;
        this.world = gameState.getGameStates().get(mission.getMap()).getWorld();
        this.orthogonalTiledMapRenderer = gameState.getGameStates().get(mission.getMap()).getOrthogonalTiledMapRenderer();
        this.player = gameState.getGameStates().get(mission.getMap()).getPlayer();
        this.rayHandler = new RayHandler(world);
        this.bodyHelper = new BodyHelper().setWorld(world);
        this.detectionSystem = new DetectionSystem(world);
        this.bullets = new ArrayList<>();
        this.bulletsToRemove = new ArrayList<>();
        this.enemiesToRemove = new ArrayList<>();
        this.hostageToRemove = new ArrayList<>();
        this.itemsToRemove = new ArrayList<>();
        this.characterCounts = new HashMap<>();
        this.cleanUp = new CleanUp();
        this.visualisation = new Visualisation();
        this.distance = new Distance();
        this.timer = Constants.screenChanger.getTimer();
        this.light = gameState.getGameStates().get(mission.getMap()).getLight();
        this.player.setMobile(new Mobile(player, Constants.options, light < 0.5f));
        this.world.setContactListener(new WorldCollision(player));
        new Generator(rayHandler).generate(mission.getMap());
        this.gameStats = GameStats.IN_PROCESS;
        Constants.gameScreen = this;
        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(gameState.getGameStates().get(mission.getMap()).isDiffuseLight());
        this.rayHandler.setAmbientLight(light);
        this.rayHandler.setShadows(true);
        this.rayHandler.setCulling(true);
        this.rayHandler.setBlur(true);
        this.rayHandler.setBlurNum(1);
        this.rayHandler.setLightMapRendering(true);
        this.timer.start();
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

        Label playerLifelabel = new Label(null, skin.get(light > 0.5f ? "default": "light", Label.LabelStyle.class));
        table1.add(playerLifelabel);

        Image image = new Image(skin, light > 0.5f ? "heart-dark": "heart-light");
        image.setTouchable(Touchable.disabled);
        table1.add(image).padLeft(5.0f).maxSize(30.0f);
        table.add(table1).padLeft(5.0f);

        table1 = new Table();

        Label enemiesLabel = new Label(null, skin.get(light > 0.5f ? "default": "light", Label.LabelStyle.class));
        table1.add(enemiesLabel);

        image = new Image(skin, "enemy");
        image.setTouchable(Touchable.disabled);
        table1.add(image).padLeft(5.0f).maxSize(30.0f);
        table.add(table1).padLeft(10.0f);

        table1 = new Table();

        Label hostagesLabel = new Label(null, skin.get(light > 0.5f ? "default": "light", Label.LabelStyle.class));
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

        Label timerLabel = new Label(null, skin.get(light > 0.5f ? "default": "light", Label.LabelStyle.class));
        table.add(timerLabel).padBottom(5.0f);

        table.row();
        Label scoreLabel = new Label(null, skin.get(light > 0.5f ? "default": "light", Label.LabelStyle.class));
        table.add(scoreLabel);
        stage.addActor(table);

        table = new Table();
        table.padRight(20.0f);
        table.align(Align.right);
        table.setFillParent(true);

        table1 = new Table();
        table1.pad(0.0f);

        distanceLabel = new Label(null, skin.get(light > 0.5f ? "default": "light", Label.LabelStyle.class));
        table1.add(distanceLabel).padRight(50.0f).align(Align.left);
        table.add(table1);

        table.row();
        table1 = new Table();
        table1.padBottom(200.0f);

        ammoLabel = new Label(null, skin.get(light > 0.5f ? "default": "light", Label.LabelStyle.class));
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
                pause();
                return true;
            }
        });
        table.add(textButton);
        stage.addActor(table);

        visualisation.setElements(enemiesLabel, hostagesLabel, playerLifelabel, timerLabel, scoreLabel);

        player.show();
        player.setLight(rayHandler, light);

        for (Enemy enemy : Constants.screenChanger.getGameState().getGameStates().get(mission.getMap()).getEnemies()) {
            enemy.setLight(rayHandler, light);
        }

        for (Hostage hostage : Constants.screenChanger.getGameState().getGameStates().get(mission.getMap()).getHostages()) {
            hostage.setLight(rayHandler, light);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        orthogonalTiledMapRenderer.setView(camera.getOrthographicCamera());
        orthogonalTiledMapRenderer.render();

        spriteBatch.setProjectionMatrix(camera.getOrthographicCamera().combined);
        spriteBatch.begin();

        if (Gdx.app.getType().equals(Application.ApplicationType.Android)) {
            camera.setZoom(0.6f);
        }

        player.render(spriteBatch, delta);

        for (Bullet bullet : bullets) {
            bullet.render(spriteBatch, delta);
        }

        for (Enemy enemy : Constants.screenChanger.getGameState().getGameStates().get(mission.getMap()).getEnemies()) {
            if (camera.isVisible(enemy.getBody().getPosition())) {
                enemy.render(spriteBatch, delta);
            }
        }

        for (Hostage hostage : Constants.screenChanger.getGameState().getGameStates().get(mission.getMap()).getHostages()) {
            if (camera.isVisible(hostage.getBody().getPosition())) {
                hostage.render(spriteBatch, delta);
            }
        }

        for (Item item : Constants.screenChanger.getGameState().getGameStates().get(mission.getMap()).getItems()) {
            if (camera.isVisible(item.getBody().getPosition())) {
                item.render(spriteBatch, delta);
            }
        }

        //box2DDebugRenderer.render(world, camera.getOrthographicCamera().combined.scl(Constants.PPM));

        spriteBatch.end();
        rayHandler.render();
        spriteBatch.begin();
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            player.getMobile().render(spriteBatch, delta);
        }
        spriteBatch.end();

        switch (gameStats) {
            case IN_PROCESS:
                resumeGame(delta);
                break;
            case PAUSE:
                pauseGame();
                break;
            case COMPLETE:
                completeMission();
                break;
            case FAILED:
                failedMission();
                break;
        }
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.getOrthographicCamera().setToOrtho(false, width, height);
        camera.update(player);
        stage.getViewport().update(width, height, true);
        popup.resize(width, height);
    }

    @Override
    public void pause() {
        gameStats = GameStats.PAUSE;
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

        for (Enemy enemy : Constants.screenChanger.getGameState().getGameStates().get(mission.getMap()).getEnemies()) {
            enemy.dispose();
        }

        for (Hostage hostage : Constants.screenChanger.getGameState().getGameStates().get(mission.getMap()).getHostages()) {
            hostage.dispose();
        }

        for (Item item : Constants.screenChanger.getGameState().getGameStates().get(mission.getMap()).getItems()) {
            item.dispose();
        }
        skin.dispose();
        stage.dispose();
        rayHandler.dispose();
    }

    public void update(float delta) {
        world.step(delta, 6, 2);
        world.clearForces();

        rayHandler.update();

        visualisation.visualize();
        timer.update();

        camera.update(player);
        player.update(delta);

        for (Enemy enemy : Constants.screenChanger.getGameState().getGameStates().get(mission.getMap()).getEnemies()) {
            enemy.setPlayerDetected(detectionSystem.isDetected(player, enemy));
            enemy.setBulletDetected(detectionSystem.bulletDetection(enemy));
            enemy.update(delta);
        }

        for (Bullet bullet : bullets) {
            bullet.update();
        }

        for (Hostage hostage : Constants.screenChanger.getGameState().getGameStates().get(mission.getMap()).getHostages()) {
            hostage.update(delta);
        }

        for (Item item : Constants.screenChanger.getGameState().getGameStates().get(mission.getMap()).getItems()) {
            item.update(delta);
        }

        cleanUp.all(mission.getMap());
        setRayHandlerMatrix(new Matrix4(camera.getOrthographicCamera().combined));

        boolean reloading = player.getComputer().isReloading()
            || player.getController().isReloading()
            || player.getMobile().isReloading();
        ammoLabel.setText("Ammo: " + (reloading ? "Reloading" : (player.getWeapon().getActiveMagazineCapacity()) + "/" + player.getWeapon().getBackupMagazinesCapacity()));
        distanceLabel.setText("Distance: " + distance.calculate(player));

        synchronization();
    }

    private void setRayHandlerMatrix(Matrix4 combinedMatrix) {
        combinedMatrix.scl(Constants.PPM);

        float invWidth = combinedMatrix.val[Matrix4.M00];
        float invHeight = combinedMatrix.val[Matrix4.M11];

        float halfWidth = 1f / invWidth;
        float halfHeight = 1f / invHeight;

        rayHandler.setCombinedMatrix(
            combinedMatrix,
            -halfWidth * combinedMatrix.val[Matrix4.M03],
            -halfHeight * combinedMatrix.val[Matrix4.M13],
            halfWidth * 2f,
            halfHeight * 2f
        );
    }

    private void pauseGame() {
        timer.pause();
        player.pause();

        for (Enemy enemy : Constants.screenChanger.getGameState().getGameStates().get(mission.getMap()).getEnemies()) {
            enemy.pause();
        }

        for (Hostage hostage : Constants.screenChanger.getGameState().getGameStates().get(mission.getMap()).getHostages()) {
            hostage.pause();
        }

        if (popup.isOpen()) {
            stage.addActor(popup.pausePopup(skin, GameScreen.this));
        }
    }

    private void resumeGame(float delta) {
        update(delta);
        timer.resume();

        for (Enemy enemy : Constants.screenChanger.getGameState().getGameStates().get(mission.getMap()).getEnemies()) {
            enemy.resume();
        }

        for (Hostage hostage : Constants.screenChanger.getGameState().getGameStates().get(mission.getMap()).getHostages()) {
            hostage.resume();
        }
    }

    private void failedMission() {
        gameStats = GameStats.END;
        checkRemains();

        Constants.data.insertMission(statistics(false), new Callback() {
            @Override
            public void onSuccess(Object object) {
                Gdx.app.postRunnable(() -> {
                    if (popup.isOpen()) {
                        stage.addActor(popup.missionFailedPopup(skin, GameScreen.this));
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                Gdx.app.postRunnable(() -> {
                    if (popup.isOpen()) {
                        stage.addActor(popup.errorPopup(skin, errorMessage));
                    }
                });
            }
        });
    }

    private void completeMission() {
        gameStats = GameStats.END;
        checkRemains();

        Constants.data.insertMission(statistics(true), new Callback() {
            @Override
            public void onSuccess(Object object) {
                Gdx.app.postRunnable(() -> {
                    if (popup.isOpen()) {
                        stage.addActor(popup.missionCompletePopup(skin, GameScreen.this));
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                Gdx.app.postRunnable(() -> {
                    if (popup.isOpen()) {
                        stage.addActor(popup.errorPopup(skin, errorMessage));
                    }
                });
            }
        });
    }

    private void synchronization() {
        for (Helper helper : Constants.screenChanger.getGameState().getGameStates().values()) {
            helper.getPlayer().setWeapon(player.getWeapon());
            helper.getPlayer().setLife(player.getLife());
        }
    }

    private Statistics statistics(boolean win) {
        Statistics statistics = Constants.statistics;
        statistics.setIdUser(Long.valueOf(mission.getIdUser()));
        statistics.setIdMission((long) mission.getId());
        statistics.setUsedTime(timer.string());
        statistics.setAccuracy();
        statistics.setWin(win);
        statistics.setTotalMoney();
        statistics.setDistanceTraveled(distance.calculateProgress());
        return statistics;
    }

    private void checkRemains() {
        characterCounts.put("remainHostages", Constants.screenChanger.getGameState().getGameStates().values().stream()
            .mapToInt(helper -> helper.getHostages().size())
            .sum());

        characterCounts.put("remainEnemies", Constants.screenChanger.getGameState().getGameStates().values().stream()
            .mapToInt(helper -> helper.getEnemies().size())
            .sum());

        characterCounts.put("totalHostages", Constants.screenChanger.getGameState().getGameStatesBackup().values().stream()
            .mapToInt(helper -> helper.getHostages().size())
            .sum());

        characterCounts.put("totalEnemies", Constants.screenChanger.getGameState().getGameStatesBackup().values().stream()
            .mapToInt(helper -> helper.getEnemies().size())
            .sum());
    }
}
