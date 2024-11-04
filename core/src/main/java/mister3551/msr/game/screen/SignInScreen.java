package mister3551.msr.game.screen;

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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Scaling;
import mister3551.msr.game.Static;
import mister3551.msr.game.database.Token;
import mister3551.msr.game.database.object.Account;
import mister3551.msr.game.screen.components.Navigation;
import mister3551.msr.game.screen.components.Popup;
import mister3551.msr.game.screen.link.Callback;

public class SignInScreen implements Screen {

    private final Skin skin;
    private final Stage stage;
    private final Popup popup;
    private final Token token;

    public SignInScreen() {
        this.skin = Static.getSkin();
        this.stage = Static.getStage();
        this.popup = Static.getPopup();
        this.token = Static.getToken();
        Static.setSkin(skin);
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
                Static.getData().signIn(textFieldUsername.getText(), textFieldPassword.getText(), new Callback() {
                    @Override
                    public void onSuccess(Object object) {
                        Account account = (Account) object;
                        Static.setAccount(account);

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
                            Static.getScreenChanger().changeScreen("MenuScreen");
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
