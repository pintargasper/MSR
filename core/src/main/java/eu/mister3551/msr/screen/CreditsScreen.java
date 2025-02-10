package eu.mister3551.msr.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.screen.components.Navigation;

public class CreditsScreen implements Screen {

    private final Skin skin;
    private final Stage stage;
    private final Navigation navigation;

    public CreditsScreen() {
        this.skin = Constants.skin;
        this.stage = Constants.stage;
        this.navigation = Constants.navigation;
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        Table navigationTable = navigation.back(skin, "MenuScreen");
        mainTable.add(navigationTable).growX();

        Table table = new Table();
        table.padTop(50.0f);

        Image image = new Image(skin, "credits-text");
        image.setScaling(Scaling.none);
        table.add(image).padBottom(20.0f).minHeight(20.0f).maxHeight(20.0f);

        table.row();

        Table table1 = new Table();
        Table table2 = new Table();

        Label label = new Label("Design", skin, "title_label");
        table2.add(label);

        table2.row();
        Table table3 = new Table();

        label = new Label("Game Designers", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        label = new Label("Gasper", skin);
        table3.add(label).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).padBottom(5.0f);

        table2.row();
        table3 = new Table();

        label = new Label("Missions Designers", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        label = new Label("Gasper", skin);
        table3.add(label).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).padBottom(5.0f);
        table1.add(table2).padLeft(5.0f).padRight(5.0f).padTop(10.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Code", skin, "title_label");
        table2.add(label);

        table2.row();
        table3 = new Table();

        label = new Label("Gameplay Programmers", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        label = new Label("Gasper", skin);
        table3.add(label).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).padBottom(5.0f);

        table2.row();
        table3 = new Table();

        label = new Label("UI Programmers", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        label = new Label("Gasper", skin);
        table3.add(label).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).padBottom(5.0f);

        table2.row();
        table3 = new Table();

        label = new Label("Online Programmers", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        label = new Label("Gasper", skin);
        table3.add(label).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).padBottom(5.0f);
        table1.add(table2).padLeft(5.0f).padRight(5.0f).padTop(10.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Art", skin, "title_label");
        table2.add(label);

        table2.row();
        table3 = new Table();

        label = new Label("Character Artists", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        label = new Label("Gasper", skin);
        table3.add(label).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3);

        table2.row();
        table3 = new Table();

        label = new Label("Urban", skin);
        table3.add(label).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).padBottom(5.0f).align(Align.right);

        table2.row();
        table3 = new Table();

        label = new Label("Mission Artists", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        label = new Label("Gasper", skin);
        table3.add(label).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).padBottom(5.0f);

        table2.row();
        table3 = new Table();

        label = new Label("Weapon Artists", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        label = new Label("Urban", skin);
        table3.add(label).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).padBottom(5.0f);
        table1.add(table2).padLeft(5.0f).padRight(5.0f).padTop(10.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Audio", skin, "title_label");
        table2.add(label);

        table2.row();
        table3 = new Table();

        label = new Label("Music Designers", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        label = new Label("Gasper", skin);
        table3.add(label).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).padBottom(5.0f);

        table2.row();
        table3 = new Table();

        label = new Label("Sound Designers", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        label = new Label("Gasper", skin);
        table3.add(label).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).padBottom(5.0f);
        table1.add(table2).padLeft(5.0f).padRight(5.0f).padTop(10.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Debugging", skin, "title_label");
        table2.add(label);

        table2.row();
        table3 = new Table();

        label = new Label("Testers", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        label = new Label("Gasper", skin);
        table3.add(label).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3);

        table2.row();
        table3 = new Table();

        label = new Label("Urban", skin);
        table3.add(label).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).align(Align.right);

        table2.row();
        table3 = new Table();

        label = new Label("Anze", skin);
        table3.add(label).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3).align(Align.right);
        table1.add(table2).padLeft(5.0f).padRight(5.0f).padTop(10.0f);

        table1.row();
        table2 = new Table();

        label = new Label("Localization", skin, "title_label");
        table2.add(label);

        table2.row();
        table3 = new Table();

        label = new Label("Language", skin);
        table3.add(label).minWidth(500.0f).maxWidth(500.0f).colspan(3);

        label = new Label("Gasper", skin);
        table3.add(label).minWidth(100.0f).minHeight(30.0f).maxWidth(100.0f).maxHeight(30.0f);
        table2.add(table3);
        table1.add(table2).padLeft(5.0f).padRight(5.0f).padTop(10.0f);
        ScrollPane scrollPane = new ScrollPane(table1, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setFlickScroll(Gdx.app.getType().equals(Application.ApplicationType.Android));
        table.add(scrollPane).padTop(20.0f).padBottom(20.0f).minWidth(700.0f);

        mainTable.row();
        mainTable.add(table).expand().top();

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
