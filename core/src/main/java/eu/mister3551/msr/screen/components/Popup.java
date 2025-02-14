package eu.mister3551.msr.screen.components;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.database.object.Gear;
import eu.mister3551.msr.database.object.Mission;
import eu.mister3551.msr.database.object.Options;
import eu.mister3551.msr.screen.GameScreen;
import eu.mister3551.msr.screen.link.Callback;
import lombok.Setter;

import java.util.ArrayList;

@Setter
public class Popup {

    private Table table;
    private boolean open;

    public Popup() {
        this.open = false;
    }

    public Table loadingPopup(Skin skin) {
        setOpen(true);
        table = new Table();
        table.setSize(340.0f, 150.0f);

        float viewportWidth = Constants.stage.getViewport().getWorldWidth();
        float viewportHeight = Constants.stage.getViewport().getWorldHeight();

        table.setPosition((viewportWidth - table.getWidth()) / 2, (viewportHeight - table.getHeight()) / 2);
        table.setBackground(Background.setBackground(Background.lightGray));

        Table table1 = new Table();

        Label label = new Label("Loading", skin, "title_label");
        table1.add(label);

        table1.row();
        Image image = new Image(skin, "line-separator");
        table1.add(image).padTop(5.0f).padBottom(5.0f).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        image = new Image(skin, "loading");
        image.setSize(50, 50);
        image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
        image.addAction(Actions.forever(Actions.rotateBy(360.0f, 2.0f)));
        table1.add(image).minSize(50.0f).maxSize(50.0f);
        table.add(table1).minWidth(400.0f).maxWidth(400.0f);
        return table;
    }

    public Table errorPopup(Skin skin, String message) {
        setOpen(true);
        table = new Table();
        table.setSize(340.0f, 142.0f);

        float viewportWidth = Constants.stage.getViewport().getWorldWidth();
        float viewportHeight = Constants.stage.getViewport().getWorldHeight();

        table.setPosition((viewportWidth - table.getWidth()) / 2, (viewportHeight - table.getHeight()) / 2);
        table.setBackground(Background.setBackground(Background.lightGray));

        Table table1 = new Table();

        Label label = new Label("Notification", skin, "title_label");
        table1.add(label);

        table1.row();
        Image image = new Image(skin, "line-separator");
        table1.add(image).padTop(5.0f).padBottom(5.0f).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        label = new Label(message, skin);
        label.setAlignment(Align.top);
        label.setWrap(true);
        table1.add(label).pad(5.0f).align(Align.top).minWidth(200.0f).minHeight(50.0f).maxWidth(200.0f).maxHeight(50.0f);

        table1.row();
        TextButton textButton = new TextButton("Close", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                close();
                return true;
            }
        });
        table1.add(textButton);
        table.add(table1).minWidth(400.0f).maxWidth(400.0f);
        return table;
    }

    public Table optionsSelectPopup(Skin skin, TextButton textButtonKey) {
        setOpen(true);
        table = new Table();
        table.setSize(340.0f, 148.0f);

        float viewportWidth = Constants.stage.getViewport().getWorldWidth();
        float viewportHeight = Constants.stage.getViewport().getWorldHeight();

        table.setPosition((viewportWidth - table.getWidth()) / 2, (viewportHeight - table.getHeight()) / 2);
        table.setBackground(Background.setBackground(Background.lightGray));

        Table table1 = new Table();

        Label label = new Label("Change key", skin, "title_label");
        table1.add(label);

        table1.row();
        Image image = new Image(skin, "line-separator");
        table1.add(image).padTop(5.0f).padBottom(5.0f).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        label = new Label("Select new key", skin);
        label.setAlignment(Align.center);
        label.setWrap(true);
        table1.add(label).padBottom(5.0f).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        Table table2 = new Table();

        label = new Label("Current:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(textButtonKey.getText().toString(), skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("New:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        Label newKeyLabel = new Label(textButtonKey.getText().toString(), skin);
        newKeyLabel.setAlignment(Align.center);
        table2.add(newKeyLabel).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        TextButton textButton = new TextButton("Close", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                textButtonKey.setText(newKeyLabel.getText().toString());
                Gdx.input.setOnscreenKeyboardVisible(false);
                close();
                return true;
            }
        });
        table2.add(textButton);
        table1.add(table2).padTop(5.0f);
        table.add(table1).minWidth(400.0f).maxWidth(400.0f);

        Constants.stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                String key = Input.Keys.toString(keycode);
                if (!key.matches("Unknown")) {
                    newKeyLabel.setText(Input.Keys.toString(keycode));
                }
                return true;
            }
        });
        Gdx.input.setOnscreenKeyboardVisible(true);
        return table;
    }

    public Table missionDetailsPopup(Skin skin, Mission mission) {
        setOpen(true);
        table = new Table();
        table.setSize(340.0f, 304.0f);

        float viewportWidth = Constants.stage.getViewport().getWorldWidth();
        float viewportHeight = Constants.stage.getViewport().getWorldHeight();

        table.setPosition((viewportWidth - table.getWidth()) / 2, (viewportHeight - table.getHeight()) / 2);
        table.setBackground(Background.setBackground(Background.lightGray));

        Table table1 = new Table();

        Label label = new Label(mission.getName(), skin, "title_label");
        table1.add(label);

        table1.row();
        label = new Label("Description", skin);
        table1.add(label).padTop(5.0f).padBottom(5.0f);

        table1.row();
        label = new Label(mission.getDescription(), skin);
        label.setAlignment(Align.center);
        label.setWrap(true);
        table1.add(label).minWidth(200.0f).minHeight(110.0f).maxWidth(200.0f).maxHeight(110.0f);

        table1.row();
        Image image = new Image(skin, "line-separator");
        table1.add(image).padTop(5.0f).padBottom(5.0f).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        label = new Label("Statistics", skin);
        label.setAlignment(Align.center);
        label.setWrap(true);
        table1.add(label).padBottom(5.0f).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        Table table2 = new Table();

        label = new Label("Best score:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(String.valueOf(mission.getScore()), skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Best time:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(mission.getUsedTime(), skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Played count:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(String.valueOf(mission.getPlayedCount()), skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        TextButton textButton = new TextButton("Play", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                close();
                if (isOpen()) {
                    Constants.stage.addActor(loadingPopup(skin));
                }
                Constants.data.options(new Callback() {
                    @Override
                    public void onSuccess(Object object) {
                        Constants.options = (Options) object;
                        Gdx.app.postRunnable(() -> {
                            setOpen(false);
                            Constants.screenChanger.reset().changeScreen("GameScreen", mission);
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Gdx.app.postRunnable(() -> {
                            if (isOpen()) {
                                close();
                                setOpen(true);
                            }

                            if (isOpen()) {
                                Constants.stage.addActor(errorPopup(skin, errorMessage));
                            }
                            System.out.println(errorMessage);
                        });
                    }
                });
                return true;
            }
        });
        table2.add(textButton).padRight(5.0f);

        textButton = new TextButton("Close", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                close();
                return true;
            }
        });
        table2.add(textButton);

        table1.add(table2).padTop(5.0f);
        table.add(table1).minWidth(400.0f).maxWidth(400.0f);
        return table;
    }

    public Table pausePopup(Skin skin, GameScreen gameScreen) {
        setOpen(true);
        table = new Table();
        table.setSize(340.0f, 148.0f);

        float viewportWidth = Constants.stage.getViewport().getWorldWidth();
        float viewportHeight = Constants.stage.getViewport().getWorldHeight();

        table.setPosition((viewportWidth - table.getWidth()) / 2, (viewportHeight - table.getHeight()) / 2);
        table.setBackground(Background.setBackground(Background.lightGray));

        Table table1 = new Table();

        Label label = new Label("Pause", skin, "title_label");
        table1.add(label);

        table1.row();
        Image image = new Image(skin, "line-separator");
        table1.add(image).padTop(5.0f).padBottom(5.0f).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        label = new Label("Statistics", skin);
        label.setAlignment(Align.center);
        label.setWrap(true);
        table1.add(label).padBottom(5.0f).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        Table table2 = new Table();

        label = new Label("Score:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(String.valueOf(Constants.statistics.getScore()), skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Time:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(gameScreen.getTimer().toString(), skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        TextButton textButton = new TextButton("Continue", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.gameStats = GameScreen.GameStats.IN_PROCESS;
                close();
                return true;
            }
        });
        table2.add(textButton).padRight(5.0f);

        textButton = new TextButton("Exit", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                close();
                Constants.screenChanger.changeScreen("MissionScreen");
                return true;
            }
        });
        table2.add(textButton);
        table1.add(table2).padTop(5.0f);
        table.add(table1).minWidth(400.0f).maxWidth(400.0f);
        return table;
    }

    public Table missionCompletePopup(Skin skin, GameScreen gameScreen) {
        setOpen(true);
        table = new Table();
        table.setSize(340.0f, 242.0f);

        float viewportWidth = Constants.stage.getViewport().getWorldWidth();
        float viewportHeight = Constants.stage.getViewport().getWorldHeight();

        table.setPosition((viewportWidth - table.getWidth()) / 2, (viewportHeight - table.getHeight()) / 2);
        table.setBackground(Background.setBackground(Background.lightGray));

        Table table1 = new Table();

        Label label = new Label("Mission complete", skin, "title_label");
        table1.add(label);

        table1.row();
        Image image = new Image(skin, "line-separator");
        table1.add(image).padTop(5.0f).padBottom(5.0f).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        label = new Label("Statistics", skin);
        label.setAlignment(Align.center);
        label.setWrap(true);
        table1.add(label).padBottom(5.0f).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        Table table2 = new Table();

        label = new Label("Score:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(String.valueOf(Constants.statistics.getScore()), skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Earned money:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(Constants.statistics.getMoney().getOrDefault("total", 0.0f) + "€", skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Hostage collected:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(Constants.statistics.getMoney().getOrDefault("hostages", 0.0f) + "€", skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Enemy killed:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(Constants.statistics.getMoney().getOrDefault("enemies", 0.0f) + "€", skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Ammo costs:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(Constants.statistics.getMoney().getOrDefault("ammoCosts", 0.0f) + "€", skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Used time:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(gameScreen.getTimer().toString(), skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Total Earned money:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(Constants.statistics.getMoney().getOrDefault("total", 0.0f) + "€", skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).padTop(5.0f).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        TextButton textButton = new TextButton("Repeat", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                close();
                Mission mission = gameScreen.getMission();
                mission.setMap(Constants.screenChanger.getMapName());
                Constants.screenChanger.reset().changeScreen("GameScreen", mission);
                return true;
            }
        });
        table2.add(textButton).padRight(5.0f);

        textButton = new TextButton("Missions", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (Constants.popup.isOpen()) {
                    Constants.stage.addActor(Constants.popup.loadingPopup(skin));
                }
                Constants.data.missions(new Callback() {
                    @Override
                    public void onSuccess(Object object) {
                        Constants.missions = (ArrayList<Mission>) object;
                        Gdx.app.postRunnable(() -> {
                            Constants.popup.close();
                            Constants.screenChanger.changeScreen("MissionScreen");
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Gdx.app.postRunnable(() -> {
                            if (!Constants.popup.isOpen()) {
                                Constants.popup.close();
                            }

                            if (Constants.popup.isOpen()) {
                                Constants.stage.addActor(Constants.popup.errorPopup(skin, errorMessage));
                            }
                        });
                    }
                });
                return true;
            }
        });
        table2.add(textButton);
        table1.add(table2).padTop(5.0f);
        table.add(table1).minWidth(400.0f).maxWidth(400.0f);
        return table;
    }

    public Table missionFailedPopup(Skin skin, GameScreen gameScreen) {
        setOpen(true);
        table = new Table();
        table.setSize(340.0f, 242.0f);

        float viewportWidth = Constants.stage.getViewport().getWorldWidth();
        float viewportHeight = Constants.stage.getViewport().getWorldHeight();

        table.setPosition((viewportWidth - table.getWidth()) / 2, (viewportHeight - table.getHeight()) / 2);
        table.setBackground(Background.setBackground(Background.lightGray));

        Table table1 = new Table();

        Label label = new Label("Mission failed", skin, "title_label");
        table1.add(label);

        table1.row();
        Image image = new Image(skin, "line-separator");
        table1.add(image).padTop(5.0f).padBottom(5.0f).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        label = new Label("Statistics", skin);
        label.setAlignment(Align.center);
        label.setWrap(true);
        table1.add(label).padBottom(5.0f).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        Table table2 = new Table();

        label = new Label("Score:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(String.valueOf(Constants.statistics.getScore()), skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Earned money:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(Constants.statistics.getMoney().getOrDefault("total", 0.0f) + "€", skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Hostage collected:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label((
            Constants.gameScreen.getGameState().getGameStates().get(Constants.gameScreen.getMission().getMap()).getTotalHostages()
                - Constants.gameScreen.getGameState().getGameStates().get(Constants.gameScreen.getMission().getMap()).getHostages().size()
                + "/" + Constants.gameScreen.getGameState().getGameStates().get(Constants.gameScreen.getMission().getMap()).getTotalHostages()), skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Enemy killed:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        //TODO total enemies / total hostages
        label = new Label((
            Constants.gameScreen.getGameState().getGameStates().get(Constants.gameScreen.getMission().getMap()).getTotalEnemies()
                - Constants.gameScreen.getGameState().getGameStates().get(Constants.gameScreen.getMission().getMap()).getEnemies().size()
                + "/" + Constants.gameScreen.getGameState().getGameStates().get(Constants.gameScreen.getMission().getMap()).getTotalEnemies()), skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Ammo costs:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(Constants.statistics.getMoney().getOrDefault("ammoCosts", 0.0f) + "€", skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Used time:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(gameScreen.getTimer().toString(), skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Total Earned money:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(Constants.statistics.getMoney().getOrDefault("total", 0.0f) + "€", skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).padTop(5.0f).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        TextButton textButton = new TextButton("Repeat", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                close();
                Mission mission = gameScreen.getMission();
                mission.setMap(Constants.screenChanger.getMapName());
                Constants.screenChanger.reset().changeScreen("GameScreen", mission);
                return true;
            }
        });
        table2.add(textButton).padRight(5.0f);

        textButton = new TextButton("Missions", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (Constants.popup.isOpen()) {
                    Constants.stage.addActor(Constants.popup.loadingPopup(skin));
                }
                Constants.data.missions(new Callback() {
                    @Override
                    public void onSuccess(Object object) {
                        Constants.missions = (ArrayList<Mission>) object;
                        Gdx.app.postRunnable(() -> {
                            Constants.popup.close();
                            Constants.screenChanger.changeScreen("MissionScreen");
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Gdx.app.postRunnable(() -> {
                            if (!Constants.popup.isOpen()) {
                                Constants.popup.close();
                            }

                            if (Constants.popup.isOpen()) {
                                Constants.stage.addActor(Constants.popup.errorPopup(skin, errorMessage));
                            }
                        });
                    }
                });
                return true;
            }
        });
        table2.add(textButton);
        table1.add(table2).padTop(5.0f);
        table.add(table1).minWidth(400.0f).maxWidth(400.0f);
        return table;
    }

    public Table gearPopup(Skin skin, Gear gear) {
        setOpen(true);
        table = new Table();
        table.setSize(340.0f, 230.0f);

        float viewportWidth = Constants.stage.getViewport().getWorldWidth();
        float viewportHeight = Constants.stage.getViewport().getWorldHeight();

        table.setPosition((viewportWidth - table.getWidth()) / 2, (viewportHeight - table.getHeight()) / 2);
        table.setBackground(Background.setBackground(Background.lightGray));

        Table table1 = new Table();

        Label label = new Label("Buy", skin, "title_label");
        table1.add(label);

        table1.row();
        Image image = new Image(skin, "line-separator");
        table1.add(image).padTop(5.0f).padBottom(5.0f).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        image = new Image(skin, gear.getImage());
        image.setScaling(Scaling.fillX);
        table1.add(image).padBottom(5.0f).minWidth(150.0f).minHeight(100.0f).maxWidth(150.0f).maxHeight(100.0f);

        table1.row();
        Table table2 = new Table();

        label = new Label("Name:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(gear.getName(), skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Price:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(gear.getPrice() + "€", skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        TextButton textButton = new TextButton("Buy", skin, "navigation");
        table2.add(textButton).padRight(5.0f);

        textButton = new TextButton("Cancel", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                close();
                return true;
            }
        });
        table2.add(textButton);
        table1.add(table2).padTop(5.0f);
        table.add(table1).minWidth(400.0f).maxWidth(400.0f);

        return table;
    }

    public void resize(int width, int height) {
        if (table != null) {
            if (Gdx.app.getType() == Application.ApplicationType.Android) {
                width = (int) Constants.stage.getViewport().getWorldWidth();
                height = (int) Constants.stage.getViewport().getWorldHeight();
            }
            table.setPosition((width - table.getWidth()) / 2, (height - table.getHeight()) / 2);
        }
    }

    public void close() {
        Array<Actor> actors = Constants.stage.getActors();
        actors.get(actors.size - 1).remove();
        setOpen(false);
    }

    public boolean isOpen() {
        return !open;
    }
}
