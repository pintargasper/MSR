package eu.mister3551.msr;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import eu.mister3551.msr.database.Data;
import eu.mister3551.msr.database.Token;
import eu.mister3551.msr.database.object.Account;
import eu.mister3551.msr.database.object.Statistics;
import eu.mister3551.msr.screen.ScreenChanger;
import eu.mister3551.msr.screen.components.Navigation;
import eu.mister3551.msr.screen.components.Popup;
import eu.mister3551.msr.screen.javascript.Native;
import eu.mister3551.msr.screen.link.Callback;

public class Main extends Game {

    private Skin skin;
    private Stage stage;

    @Override
    public void create() {
        this.skin = new Skin(Gdx.files.internal("skins/skin/skin.json"));
        this.stage = Gdx.app.getType().equals(Application.ApplicationType.Android)
            ? new Stage(new ExtendViewport(800, 480))
            : new Stage(new ScreenViewport());

        Constants.skin = skin;
        Constants.stage = stage;
        Constants._native = new Native();
        Constants.navigation = new Navigation();
        Constants.popup = new Popup();
        Constants.json = new Json();
        Constants.data = new Data();
        Token token = new Token();
        Constants.token = token;
        Constants.statistics = new Statistics();
        Constants.screenChanger = new ScreenChanger(this);

        /*VideoScreen videoScreen = new VideoScreen();
        videoScreen.playVideo(new Callback() {

            @Override
            public void onSuccess(Object object) {
                load(token);
            }

            @Override
            public void onError(String errorMessage) {
                Gdx.app.error("Main", errorMessage);
            }
        });
        setScreen(videoScreen);*/
        load(token);
    }

    /*@Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
    }*/

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        stage.dispose();
    }

    private void load(Token token) {
        String authToken = "";

        switch (Gdx.app.getType()) {
            case Desktop:
            case Android:
                authToken = token.readFromJsonFile();
                break;
            case WebGL:
                authToken = token.readFromCookie();
                break;
        }

        ScreenChanger screenChanger = new ScreenChanger(this);
        Constants.data.isAuthenticated(authToken, new Callback() {
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
                    screenChanger.changeScreen("MenuScreen");
                });
            }

            @Override
            public void onError(String errorMessage) {
                Gdx.app.postRunnable(() -> {
                    screenChanger.changeScreen("SignInScreen");
                });
            }
        });
        Constants.screenChanger = screenChanger;
    }
}
