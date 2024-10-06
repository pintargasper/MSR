package mister3551.msr.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SignInScreen implements Screen {

    private Skin skin;
    private Stage stage;

    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skins/skin/skin.json"));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setName("tableForm");
        table.setBackground(skin.getDrawable("background"));
        table.pad(0.0f);
        table.setFillParent(true);

        Image image = new Image(skin, "logo-text");
        image.setScaling(Scaling.none);
        table.add(image).padBottom(11.0f);

        table.row();
        TextField textField = new TextField(null, skin);
        textField.setName("textFieldUsername");
        textField.setMaxLength(16);
        textField.setMessageText("Username");
        table.add(textField).padBottom(5.0f).fillX();

        table.row();
        textField = new TextField(null, skin);
        textField.setName("textFieldPassword");
        textField.setPasswordMode(true);
        textField.setPasswordCharacter('•');
        textField.setMessageText("Password");
        table.add(textField).padBottom(5.0f).fillX();

        table.row();
        TextButton textButton = new TextButton("Sign in", skin);
        textButton.setName("textButtonSignIn");
        table.add(textButton).padBottom(5.0f);
        stage.addActor(table);

        table = new Table();
        table.setName("tableToolBar");
        table.padRight(5.0f);
        table.padTop(5.0f);
        table.align(Align.topRight);
        table.setFillParent(true);

        table.add();

        textButton = new TextButton("News", skin);
        textButton.setName("textButtonNews");
        table.add(textButton).padRight(5.0f);

        textButton = new TextButton("Sign up", skin);
        textButton.setName("textButtonSignUp");
        table.add(textButton);
        stage.addActor(table);
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
