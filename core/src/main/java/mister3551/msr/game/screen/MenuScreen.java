package mister3551.msr.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen {

    private final Skin skin;
    private final Stage stage;

    public MenuScreen() {
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("skins/skin/skin.json"));
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setBackground(skin.getDrawable("background"));
        table.pad(0.0f);
        table.setFillParent(true);

        Image image = new Image(skin, "logo-text");
        image.setScaling(Scaling.none);
        table.add(image).padBottom(11.0f);

        table.row();
        TextButton textButton = new TextButton("Campaign", skin);
        textButton.setName("textButtonCampaign");
        table.add(textButton).padBottom(5.0f);

        table.row();
        textButton = new TextButton("Multiplayer", skin);
        textButton.setName("textButtonMultiplayer");
        table.add(textButton).padBottom(5.0f);

        table.row();
        textButton = new TextButton("Options", skin);
        textButton.setName("textButtonOptions");
        table.add(textButton).padBottom(5.0f);

        table.row();
        textButton = new TextButton("Credits", skin);
        textButton.setName("textButtonCredits");
        table.add(textButton);
        stage.addActor(table);
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
