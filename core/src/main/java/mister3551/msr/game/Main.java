package mister3551.msr.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import mister3551.msr.game.database.Data;
import mister3551.msr.game.database.Token;
import mister3551.msr.game.database.object.Account;
import mister3551.msr.game.screen.*;
import mister3551.msr.game.screen.components.Navigation;
import mister3551.msr.game.screen.components.Popup;
import mister3551.msr.game.screen.javascript.Native;
import mister3551.msr.game.screen.link.Callback;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    private Skin skin;
    private Stage stage;

    @Override
    public void create() {
        this.skin = new Skin(Gdx.files.internal("skins/skin/skin.json"));
        this.stage = Gdx.app.getType().equals(Application.ApplicationType.Android)
            ? new Stage(new ExtendViewport(800, 480))
            : new Stage(new ScreenViewport());

        Static.setSkin(skin);
        Static.setStage(stage);
        Static.setNative(new Native());
        Static.setNavigation(new Navigation());
        Static.setPopup(new Popup());
        Static.setJson(new Json());
        Static.setData(new Data());

        Token token = new Token();
        Static.setToken(token);

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
        Static.getData().isAuthenticated(authToken, new Callback() {
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
        Static.setScreenChanger(screenChanger);
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        stage.dispose();
    }
}
