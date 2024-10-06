package mister3551.msr.game.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MissionScreen implements Screen {

    private Skin skin;
    private Stage stage;

    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skins/skin/skin.json"));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.pad(10.0f);
        table.setFillParent(true);

        Image image = new Image(skin, "mission-text");
        image.setScaling(Scaling.none);
        table.add(image).padTop(50.0f).padBottom(20.0f);
        table.row();

        Table table1 = new Table();

        for (int i = 0; i <= 100; i++) {
            Table table2 = new Table();
            table2.setBackground(skin.getDrawable("scroll-pane-table-background"));
            table2.pad(10.0f);

            Label label = new Label("Training", skin);
            table2.add(label).padTop(5.0f).padBottom(5.0f);

            table2.row();
            image = new Image(skin, "background");
            table2.add(image).padBottom(5.0f).maxWidth(150.0f).maxHeight(120.0f);

            if (i % 7 == 0) {
                table1.row();
            }

            table2.row();
            TextButton textButton = new TextButton("Play", skin);
            table2.add(textButton);
            table1.add(table2).pad(5.0f);
        }

        ScrollPane scrollPane = new ScrollPane(table1, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollBarTouch(true);
        scrollPane.setFlickScroll(false);

        table.add(scrollPane).padTop(20.0f).padBottom(Gdx.app.getType().equals(Application.ApplicationType.WebGL) ? 70.0f : 0.0f);

        stage.addActor(table);
        stage.setScrollFocus(scrollPane);
    }

        public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
