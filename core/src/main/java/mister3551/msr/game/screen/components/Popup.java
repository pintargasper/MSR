package mister3551.msr.game.screen.components;

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
import mister3551.msr.game.Static;
import mister3551.msr.game.database.object.Gear;
import mister3551.msr.game.database.object.Mission;
import mister3551.msr.game.database.object.Options;
import mister3551.msr.game.screen.GameScreen;
import mister3551.msr.game.screen.link.Callback;

import java.util.ArrayList;

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

        float viewportWidth = Static.getStage().getViewport().getWorldWidth();
        float viewportHeight = Static.getStage().getViewport().getWorldHeight();

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

        float viewportWidth = Static.getStage().getViewport().getWorldWidth();
        float viewportHeight = Static.getStage().getViewport().getWorldHeight();

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

        float viewportWidth = Static.getStage().getViewport().getWorldWidth();
        float viewportHeight = Static.getStage().getViewport().getWorldHeight();

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

        Static.getStage().addListener(new InputListener() {
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

        float viewportWidth = Static.getStage().getViewport().getWorldWidth();
        float viewportHeight = Static.getStage().getViewport().getWorldHeight();

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
                    Static.getStage().addActor(loadingPopup(skin));
                }
                Static.getData().options(new Callback() {
                    @Override
                    public void onSuccess(Object object) {
                        Static.setOptions((Options) object);
                        Gdx.app.postRunnable(() -> {
                            setOpen(false);
                            Static.getScreenChanger().changeScreen("GameScreen", mission);
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
                                Static.getStage().addActor(errorPopup(skin, errorMessage));
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

        float viewportWidth = Static.getStage().getViewport().getWorldWidth();
        float viewportHeight = Static.getStage().getViewport().getWorldHeight();

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

        label = new Label(String.valueOf(Static.getStatistics().getScore()), skin);
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
                close();
                gameScreen.gameStats = GameScreen.GameStats.IN_PROCESS;
                return true;
            }
        });
        table2.add(textButton).padRight(5.0f);

        textButton = new TextButton("Exit", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                close();
                Static.getScreenChanger().changeScreen("MissionScreen");
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

        float viewportWidth = Static.getStage().getViewport().getWorldWidth();
        float viewportHeight = Static.getStage().getViewport().getWorldHeight();

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

        label = new Label(String.valueOf(Static.getStatistics().getScore()), skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Earned money:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(Static.getStatistics().getMoney().getOrDefault("earned", 0.0f) + "€", skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Hostage killed:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(Static.getStatistics().getMoney().getOrDefault("hostageKilled", 0.0f) + "€", skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Enemy killed:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(Static.getStatistics().getMoney().getOrDefault("enemyKilled", 0.0f) + "€", skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Ammo costs:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(Static.getStatistics().getMoney().getOrDefault("ammoCosts", 0.0f) + "€", skin);
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

        label = new Label(Static.getStatistics().getMoney().getOrDefault("total", 0.0f) + "€", skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).padTop(5.0f).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        TextButton textButton = new TextButton("Continue", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                close();
                return true;
            }
        });
        table2.add(textButton).padRight(5.0f);

        textButton = new TextButton("Missions", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (Static.getPopup().isOpen()) {
                    Static.getStage().addActor(Static.getPopup().loadingPopup(skin));
                }
                Static.getData().missions(new Callback() {
                    @Override
                    public void onSuccess(Object object) {
                        Static.setMissions((ArrayList<Mission>) object);
                        Gdx.app.postRunnable(() -> {
                            Static.getPopup().close();
                            Static.getScreenChanger().changeScreen("MissionScreen");
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Gdx.app.postRunnable(() -> {
                            if (!Static.getPopup().isOpen()) {
                                Static.getPopup().close();
                            }

                            if (Static.getPopup().isOpen()) {
                                Static.getStage().addActor(Static.getPopup().errorPopup(skin, errorMessage));
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

        float viewportWidth = Static.getStage().getViewport().getWorldWidth();
        float viewportHeight = Static.getStage().getViewport().getWorldHeight();

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

        label = new Label(String.valueOf(Static.getStatistics().getScore()), skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Earned money:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(Static.getStatistics().getMoney().getOrDefault("earned", 0.0f) + "€", skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Hostage killed:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label("0/10", skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Enemy killed:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label((Static.getTotalEnemies() - Static.getEnemies().size()) + "/" + Static.getTotalEnemies(), skin);
        label.setAlignment(Align.center);
        table2.add(label).padLeft(5.0f).minWidth(70.0f).maxWidth(70.0f);
        table1.add(table2).minWidth(200.0f).maxWidth(200.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Ammo costs:", skin);
        table2.add(label).padLeft(5.0f).minWidth(130.0f).maxWidth(130.0f);

        label = new Label(Static.getStatistics().getMoney().getOrDefault("ammoCosts", 0.0f) + "€", skin);
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

        label = new Label(Static.getStatistics().getMoney().getOrDefault("total", 0.0f) + "€", skin);
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
                Static.getScreenChanger().changeScreen("GameScreen", gameScreen.getMission());
                return true;
            }
        });
        table2.add(textButton).padRight(5.0f);

        textButton = new TextButton("Missions", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (Static.getPopup().isOpen()) {
                    Static.getStage().addActor(Static.getPopup().loadingPopup(skin));
                }
                Static.getData().missions(new Callback() {
                    @Override
                    public void onSuccess(Object object) {
                        Static.setMissions((ArrayList<Mission>) object);
                        Gdx.app.postRunnable(() -> {
                            Static.getPopup().close();
                            Static.getScreenChanger().changeScreen("MissionScreen");
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Gdx.app.postRunnable(() -> {
                            if (!Static.getPopup().isOpen()) {
                                Static.getPopup().close();
                            }

                            if (Static.getPopup().isOpen()) {
                                Static.getStage().addActor(Static.getPopup().errorPopup(skin, errorMessage));
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

        float viewportWidth = Static.getStage().getViewport().getWorldWidth();
        float viewportHeight = Static.getStage().getViewport().getWorldHeight();

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
                width = (int) Static.getStage().getViewport().getWorldWidth();
                height = (int) Static.getStage().getViewport().getWorldHeight();
            }
            table.setPosition((width - table.getWidth()) / 2, (height - table.getHeight()) / 2);
        }
    }

    public void close() {
        Array<Actor> actors = Static.getStage().getActors();
        actors.get(actors.size - 1).remove();
        setOpen(false);
    }

    public boolean isOpen() {
        return !open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
