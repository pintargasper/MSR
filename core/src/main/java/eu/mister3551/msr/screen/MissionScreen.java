package eu.mister3551.msr.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.database.object.Mission;
import eu.mister3551.msr.screen.components.Navigation;
import eu.mister3551.msr.screen.components.Popup;

import java.util.ArrayList;

public class MissionScreen implements Screen {

    private final Skin skin;
    private final Stage stage;
    private final Navigation navigation;
    private final Popup popup;
    private final ArrayList<Mission> missions;

    public MissionScreen() {
        this.skin = Constants.skin;
        this.stage = Constants.stage;
        this.navigation = Constants.navigation;
        this.popup = Constants.popup;
        this.missions = Constants.missions;
        Constants.screenChanger.reset();
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

        Image image = new Image(skin, "missions-text");
        image.setScaling(Scaling.none);
        table.add(image).padBottom(20.0f).minHeight(20.0f).maxHeight(20.0f);

        table.row();

        Table table1 = new Table();

        ScrollPane scrollPane = new ScrollPane(table1, skin);
        scrollPane.setFadeScrollBars(false);
        table.add(scrollPane).padTop(20.0f).padBottom(20.0f).minWidth(870.0f);

        table.row();
        table.add().padTop(5.0f).growY();

        mainTable.row();
        mainTable.add(table).expand().top();

        int i = 0;
        for (Mission mission : missions) {
            Table table2 = new Table();
            table2.setBackground(skin.getDrawable("table-background"));

            Label label = new Label(mission.getName(), skin);
            label.setEllipsis(true);
            label.setWrap(true);
            table2.add(label).pad(5.0f).align(Align.left).minWidth(150.0f).maxWidth(150.0f);

            table2.row();
            image = new Image(skin, mission.getImage());
            table2.add(image).minWidth(150.0f).minHeight(100.0f).maxWidth(150.0f).maxHeight(100.0f);

            table2.row();
            TextButton textButton = new TextButton(mission.getIdUser() != null ? "Play" : "Locked", skin, "navigation");
            if (mission.getIdUser() != null) {
                textButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if (popup.isOpen()) {
                            stage.addActor(popup.missionDetailsPopup(skin, mission));
                        }
                        return true;
                    }
                });
            }
            table2.add(textButton).padBottom(5);

            if (i % 5 == 0) {
                table1.row();
            }
            i++;
            table1.add(table2).pad(5.0f);
        }

        mainTable.row();
        mainTable.add().padTop(5.0f).growY();

        stage.setScrollFocus(scrollPane);
        stage.addActor(mainTable);
    }

    @Override
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
