package eu.mister3551.msr.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Scaling;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.database.Token;
import eu.mister3551.msr.database.object.Account;
import eu.mister3551.msr.screen.components.Navigation;
import eu.mister3551.msr.screen.components.Popup;
import eu.mister3551.msr.screen.link.Callback;

public class SignInScreen implements Screen {

    private final Skin skin;
    private final Stage stage;
    private final Popup popup;
    private final Token token;

    public SignInScreen() {
        this.skin = Constants.skin;
        this.stage = Constants.stage;
        this.popup = Constants.popup;
        this.token = Constants.token;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        Table navigationTable = Navigation.signIn(skin);
        mainTable.add(navigationTable).growX();

        Table table = new Table();
        table.setBackground(skin.getDrawable("background"));
        table.pad(0.0f);

        Image image = new Image(skin, "logo-text");
        image.setScaling(Scaling.none);
        table.add(image).padBottom(11.0f);

        table.row();
        TextField textFieldUsername = new TextField(null, skin);
        textFieldUsername.setMaxLength(16);
        textFieldUsername.setMessageText("Username");
        table.add(textFieldUsername).padBottom(5.0f).fillX();

        table.row();
        TextField textFieldPassword = new TextField(null, skin);
        textFieldPassword.setPasswordMode(true);
        textFieldPassword.setMessageText("Password");
        textFieldPassword.setPasswordCharacter('•');
        table.add(textFieldPassword).padBottom(5.0f).fillX();

        table.row();
        TextButton textButton = new TextButton("Sign in", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (popup.isOpen()) {
                    stage.addActor(popup.loadingPopup(skin));
                }
                Constants.data.signIn(textFieldUsername.getText(), textFieldPassword.getText(), new Callback() {
                    @Override
                    public void onSuccess(Object object) {
                        Account account = (Account) object;
                        Constants.account = account;

                        switch (Gdx.app.getType()) {
                            case Desktop:
                            case Android:
                                token.saveToJsonFile(account.getToken());
                                break;
                            case WebGL:
                                token.saveToCookie(account.getToken());
                                break;
                        }
                        Gdx.app.postRunnable(() -> {
                            popup.close();
                            Constants.screenChanger.changeScreen("MenuScreen");
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
