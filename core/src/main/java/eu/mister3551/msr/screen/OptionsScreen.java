package eu.mister3551.msr.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.database.object.Options;
import eu.mister3551.msr.screen.components.Navigation;
import eu.mister3551.msr.screen.components.Popup;
import eu.mister3551.msr.screen.link.Callback;

public class OptionsScreen implements Screen {

    private final Skin skin;
    private final Stage stage;
    private final Navigation navigation;
    private final Popup popup;
    private final Options options;

    private TextButton textButtonMusic;
    private TextButton textButtonSoundEffect;
    private TextButton textButtonPause;
    private TextButton textButtonFootJump;
    private TextButton textButtonFootLeft;
    private TextButton textButtonFootRight;
    private TextButton textButtonFootReload;
    private TextButton textButtonFootShoot;
    private TextButton textButtonFootDoor;
    private TextButton textButtonLadderUp;
    private TextButton textButtonLadderDown;
    private TextButton textButtonLadderLeft;
    private TextButton textButtonLadderRight;
    private TextButton textButtonLadderReload;

    public OptionsScreen() {
        this.skin = Constants.skin;
        this.stage = Constants.stage;
        this.navigation = Constants.navigation;
        this.popup = Constants.popup;
        this.options = Constants.options;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        Table navigationTable = navigation.back(skin, "MenuScreen");
        mainTable.add(navigationTable).growX();

        Table table = new Table();
        table.padTop(50.0f);

        Image image = new Image(skin, "options-text");
        image.setScaling(Scaling.none);
        table.add(image).padBottom(20.0f).minHeight(20.0f).maxHeight(20.0f);

        table.row();

        Table table1 = new Table();
        Table table2 = new Table();

        Label label = new Label("General", skin, "title_label");
        table2.add(label);

        table2.row();
        Table table3 = new Table();

        label = new Label("Language", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        SelectBox<String> selectBox = new SelectBox<>(skin);
        selectBox.setItems("Slovenscina", "English", "Deutsch");
        selectBox.setSelected(options.getLanguage());

        table3.add(selectBox).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).padBottom(5.0f);

        table2.row();
        table3 = new Table();

        label = new Label("View profile", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        TextButton textButton = new TextButton("View", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.net.openURI("https://msr.gasperpintar.com/" + Constants.account.getUsername());
                return true;
            }
        });
        table3.add(textButton).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).padBottom(5.0f);

        table2.row();
        table3 = new Table();

        label = new Label("Change password", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        textButton = new TextButton("View", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.net.openURI("https://msr.gasperpintar.com/" + Constants.account.getUsername() + "/settings");
                return true;
            }
        });
        table3.add(textButton).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).padBottom(5.0f);

        table2.row();
        table3 = new Table();

        label = new Label("Sign out", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        textButton = new TextButton("Sign out", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Constants.screenChanger.changeScreen("SignInScreen");
                return true;
            }
        });
        table3.add(textButton).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3);
        table1.add(table2).padLeft(5.0f).padRight(5.0f).padTop(10.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Audio", skin, "title_label");
        table2.add(label);

        table2.row();
        table3 = new Table();

        label = new Label("Music", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        textButtonMusic = new TextButton(options.getMusic() == 1 ? "Turn off" : "Turn on", skin, "navigation");
        textButtonSound(textButtonMusic, true);
        table3.add(textButtonMusic).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).padBottom(5.0f);

        table2.row();
        table3 = new Table();

        label = new Label("Sound effect", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        textButtonSoundEffect = new TextButton(options.getSoundEffect() == 1 ? "Turn off" : "Turn on", skin, "navigation");
        textButtonSound(textButtonSoundEffect, false);
        table3.add(textButtonSoundEffect).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).padBottom(5.0f);
        table1.add(table2).padLeft(5.0f).padRight(5.0f).padTop(10.0f);

        table1.row();

        table2 = new Table();
        table2.setName("video");

        label = new Label("Video", skin, "title_label");
        table2.add(label);

        table2.row();
        table3 = new Table();
        table3.align(Align.left);

        Table table4 = new Table();

        label = new Label("Shadows/Lights", skin);
        table4.add(label).minWidth(500.0f).maxWidth(500.0f);

        SelectBox<String> selectBoxLights = new SelectBox<>(skin);
        selectBoxLights.setItems("Ultra low", "Low", "Normal", "High", "Ultra high");
        selectBoxLights.setSelected(options.getShadowsLights());

        table4.add(selectBoxLights).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table3.add(table4);
        table2.add(table3).growX();
        table1.add(table2).padLeft(5.0f).padRight(5.0f).padTop(10.0f);

        table1.row();

        table1.row();
        table2 = new Table();

        label = new Label("Controls", skin, "title_label");
        table2.add(label);

        table2.row();
        table3 = new Table();
        table3.align(Align.left);

        label = new Label("Keyboard", skin, "title_label");
        table3.add(label).align(Align.left);

        table3.row();
        table4 = new Table();

        label = new Label("Pause", skin);
        table4.add(label).minWidth(500.0f).maxWidth(500.0f);

        textButtonPause = new TextButton(Input.Keys.toString(options.getPause()), skin, "navigation");
        textButtonListener(textButtonPause);

        table4.add(textButtonPause).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table3.add(table4);

        table3.row();
        table4 = new Table();

        label = new Label("On foot", skin, "title_label");
        table4.add(label).align(Align.left);

        table4.row();
        Table table5 = new Table();

        label = new Label("Jump", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButtonFootJump = new TextButton(Input.Keys.toString(options.getKeyboardFootJump()), skin, "navigation");
        textButtonListener(textButtonFootJump);
        table5.add(textButtonFootJump).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Left", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButtonFootLeft = new TextButton(Input.Keys.toString(options.getKeyboardFootLeft()), skin, "navigation");
        textButtonListener(textButtonFootLeft);
        table5.add(textButtonFootLeft).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Right", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButtonFootRight = new TextButton(Input.Keys.toString(options.getKeyboardFootRight()), skin, "navigation");
        textButtonListener(textButtonFootRight);
        table5.add(textButtonFootRight).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Reload", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButtonFootReload = new TextButton(Input.Keys.toString(options.getKeyboardFootReload()), skin, "navigation");
        textButtonListener(textButtonFootReload);
        table5.add(textButtonFootReload).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Shoot", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButtonFootShoot = new TextButton(Input.Keys.toString(options.getKeyboardFootShoot()), skin, "navigation");
        textButtonListener(textButtonFootShoot);
        table5.add(textButtonFootShoot).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Open door", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButtonFootDoor = new TextButton(Input.Keys.toString(options.getKeyboardFootDoor()), skin, "navigation");
        textButtonListener(textButtonFootDoor);
        table5.add(textButtonFootDoor).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);
        table3.add(table4).padLeft(20.0f).align(Align.left);

        table3.row();
        table4 = new Table();

        label = new Label("On ladder", skin, "title_label");
        table4.add(label).align(Align.left);

        table4.row();
        table5 = new Table();

        label = new Label("Up", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButtonLadderUp = new TextButton(Input.Keys.toString(options.getKeyboardLadderUp()), skin, "navigation");
        textButtonListener(textButtonLadderUp);
        table5.add(textButtonLadderUp).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Down", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButtonLadderDown = new TextButton(Input.Keys.toString(options.getKeyboardLadderDown()), skin, "navigation");
        textButtonListener(textButtonLadderDown);
        table5.add(textButtonLadderDown).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Left", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButtonLadderLeft = new TextButton(Input.Keys.toString(options.getKeyboardLadderLeft()), skin, "navigation");
        textButtonListener(textButtonLadderLeft);
        table5.add(textButtonLadderLeft).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Right", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButtonLadderRight = new TextButton(Input.Keys.toString(options.getKeyboardLadderRight()), skin, "navigation");
        textButtonListener(textButtonLadderRight);
        table5.add(textButtonLadderRight).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Reload", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButtonLadderReload = new TextButton(Input.Keys.toString(options.getKeyboardLadderReload()), skin, "navigation");
        textButtonListener(textButtonLadderReload);
        table5.add(textButtonLadderReload).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table2.add(table3).growX();

        table2.row();
        table3 = new Table();
        table3.align(Align.left);

        label = new Label("Controller", skin, "title_label");
        table3.add(label).align(Align.left);

        table3.row();
        table4 = new Table();

        label = new Label("On foot", skin, "title_label");
        table4.add(label).align(Align.left);

        table4.row();
        table5 = new Table();

        label = new Label("Jump", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButton = new TextButton("B0", skin, "navigation");
        table5.add(textButton).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Left", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButton = new TextButton("Axis0", skin, "navigation");
        table5.add(textButton).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Right", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButton = new TextButton("Axis0", skin, "navigation");
        table5.add(textButton).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Reload", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButton = new TextButton("L1", skin, "navigation");
        table5.add(textButton).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Shoot", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButton = new TextButton("L2", skin, "navigation");
        table5.add(textButton).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Open door", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButton = new TextButton("B2", skin, "navigation");
        table5.add(textButton).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);
        table3.add(table4).padLeft(20.0f).align(Align.left);

        table3.row();
        table4 = new Table();

        label = new Label("On ladder", skin, "title_label");
        table4.add(label).align(Align.left);

        table4.row();
        table5 = new Table();

        label = new Label("Up", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButton = new TextButton("Axis0", skin, "navigation");
        table5.add(textButton).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Down", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButton = new TextButton("Axis0", skin, "navigation");
        table5.add(textButton).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Left", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButton = new TextButton("Axis0", skin, "navigation");
        table5.add(textButton).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Right", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButton = new TextButton("Axis0", skin, "navigation");
        table5.add(textButton).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);

        table4.row();
        table5 = new Table();

        label = new Label("Reload", skin);
        table5.add(label).padLeft(10.0f).minWidth(470.0f).minHeight(30.0f).maxWidth(470.0f).maxHeight(30.0f);

        textButton = new TextButton("L1", skin, "navigation");
        table5.add(textButton).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table4.add(table5).padBottom(5.0f);
        table3.add(table4).padLeft(20.0f).align(Align.left);
        table2.add(table3).growX();
        table1.add(table2).padLeft(5.0f).padRight(5.0f).padTop(10.0f);
        ScrollPane scrollPane = new ScrollPane(table1, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setFlickScroll(Gdx.app.getType().equals(Application.ApplicationType.Android));
        table.add(scrollPane).padTop(20.0f).padBottom(20.0f).minWidth(700.0f);

        table.row();
        textButton = new TextButton("Save options", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (popup.isOpen()) {
                    stage.addActor(popup.loadingPopup(skin));
                }

                options.setLanguage(selectBox.getSelected());
                options.setMusic(textButtonMusic.getText().toString().matches("Turn off") ? 1 : 0);
                options.setSoundEffect(textButtonSoundEffect.getText().toString().matches("Turn off") ? 1 : 0);
                options.setShadowsLights(selectBoxLights.getSelected());
                options.setPause(Input.Keys.valueOf(textButtonPause.getText().toString()));
                options.setKeyboardFootJump(Input.Keys.valueOf(textButtonFootJump.getText().toString()));
                options.setKeyboardFootLeft(Input.Keys.valueOf(textButtonFootLeft.getText().toString()));
                options.setKeyboardFootRight(Input.Keys.valueOf(textButtonFootRight.getText().toString()));
                options.setKeyboardFootReload(Input.Keys.valueOf(textButtonFootReload.getText().toString()));
                options.setKeyboardFootShoot(Input.Keys.valueOf(textButtonFootShoot.getText().toString()));
                options.setKeyboardFootDoor(Input.Keys.valueOf(textButtonFootDoor.getText().toString()));
                options.setKeyboardLadderUp(Input.Keys.valueOf(textButtonLadderUp.getText().toString()));
                options.setKeyboardLadderDown(Input.Keys.valueOf(textButtonLadderDown.getText().toString()));
                options.setKeyboardLadderLeft(Input.Keys.valueOf(textButtonLadderLeft.getText().toString()));
                options.setKeyboardLadderRight(Input.Keys.valueOf(textButtonLadderRight.getText().toString()));
                options.setKeyboardLadderReload(Input.Keys.valueOf(textButtonLadderReload.getText().toString()));

                Constants.data.updateOptions(options, new Callback() {

                    @Override
                    public void onSuccess(Object object) {
                        Gdx.app.postRunnable(() -> {
                            if (!popup.isOpen()) {
                                popup.close();
                            }
                            String message = object.equals("SUCCESS") ? "Options updated successfully" : "Something went wrong";

                            if (popup.isOpen()) {
                                stage.addActor(popup.errorPopup(skin, message));
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Gdx.app.postRunnable(() -> {
                            if (!popup.isOpen()) {
                                popup.close();
                            }

                            if (popup.isOpen()) {
                                stage.addActor(popup.errorPopup(skin, errorMessage));
                            }
                        });
                    }
                });
                return true;
            }
        });
        table.add(textButton).padTop(5.0f);

        mainTable.row();
        mainTable.add(table).expand().center();

        stage.setScrollFocus(scrollPane);
        stage.addActor(mainTable);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
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

    public void dispose() {
        skin.dispose();
        stage.dispose();
    }

    private void textButtonSound(TextButton textButton, boolean music) {
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                boolean isTurnedOff = textButton.getText().toString().equals("Turn off");

                if (isTurnedOff) {
                    textButton.setText("Turn on");
                } else {
                    textButton.setText("Turn off");
                }
                return true;
            }
        });
    }

    private void textButtonListener(TextButton textButton) {
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (popup.isOpen()) {
                    stage.addActor(popup.optionsSelectPopup(skin, textButton));
                }
                return true;
            }
        });
    }
}
