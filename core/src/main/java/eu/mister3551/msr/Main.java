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
import eu.mister3551.msr.screen.ScreenChanger;
import eu.mister3551.msr.screen.components.Navigation;
import eu.mister3551.msr.screen.components.Popup;
import eu.mister3551.msr.screen.javascript.Native;
import eu.mister3551.msr.screen.link.Callback;

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


        Static.skin = skin;
        Static.stage = stage;
        Static._native = new Native();
        Static.navigation = new Navigation();
        Static.popup = new Popup();
        Static.json = new Json();
        Static.data = new Data();

        Token token = new Token();
        Static.token = token;

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
        Static.data.isAuthenticated(authToken, new Callback() {
            @Override
            public void onSuccess(Object object) {
                Account account = (Account) object;
                Static.account = account;

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
        Static.screenChanger = screenChanger;
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        stage.dispose();
    }
}
