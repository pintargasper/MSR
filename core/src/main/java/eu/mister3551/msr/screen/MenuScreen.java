package eu.mister3551.msr.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.database.object.Mission;
import eu.mister3551.msr.database.object.Options;
import eu.mister3551.msr.screen.components.Navigation;
import eu.mister3551.msr.screen.components.Popup;
import eu.mister3551.msr.screen.link.Callback;

import java.util.ArrayList;

public class MenuScreen implements Screen {

    private final Skin skin;
    private final Stage stage;
    private final Navigation navigation;
    private final Popup popup;

    public MenuScreen() {
        this.skin = Constants.skin;
        this.stage = Constants.stage;
        this.navigation = Constants.navigation;
        this.popup = Constants.popup;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        Table navigationTable = navigation.menu(skin, popup);
        mainTable.add(navigationTable).growX();

        Table table = new Table();
        table.setBackground(skin.getDrawable("background"));
        table.pad(0.0f);

        Image image = new Image(skin, "logo-text");
        image.setScaling(Scaling.none);
        table.add(image).padBottom(11.0f);

        table.row();
        TextButton textButton = new TextButton("Campaign", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (popup.isOpen()) {
                    stage.addActor(popup.loadingPopup(skin));
                }
                Constants.data.missions(new Callback() {
                    @Override
                    public void onSuccess(Object object) {
                        Constants.missions = (ArrayList<Mission>) object;
                        Gdx.app.postRunnable(() -> {
                            popup.close();
                            Constants.screenChanger.changeScreen("MissionScreen");
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
        table.add(textButton).padBottom(5.0f);

        table.row();
        textButton = new TextButton("Multiplayer", skin, "navigation");
        table.add(textButton).padBottom(5.0f);

        table.row();
        textButton = new TextButton("Options", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (popup.isOpen()) {
                    stage.addActor(popup.loadingPopup(skin));
                }
                Constants.data.options(new Callback() {
                    @Override
                    public void onSuccess(Object object) {
                        Constants.options = (Options) object;
                        Gdx.app.postRunnable(() -> {
                            popup.close();
                            Constants.screenChanger.changeScreen("OptionsScreen");
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Gdx.app.postRunnable(() -> {
                            if (!popup.isOpen()) {
                                popup.close();
                                popup.setOpen(false);
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
        table.add(textButton).padBottom(5.0f);

        table.row();
        textButton = new TextButton("Credits", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Constants.screenChanger.changeScreen("CreditsScreen");
                return true;
            }
        });
        table.add(textButton);

        mainTable.row();
        mainTable.add(table).expand().center();

        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
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

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }
}
