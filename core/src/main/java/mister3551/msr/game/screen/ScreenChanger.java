package mister3551.msr.game.screen;

import mister3551.msr.game.Main;
import mister3551.msr.game.Static;
import com.badlogic.gdx.Screen;
import mister3551.msr.game.database.object.Mission;

public class ScreenChanger {

    private final Main main;

    public ScreenChanger(Main main) {
        this.main = main;
    }

    public void changeScreen(String screenName, Mission... missions) {
        Screen newScreen = null;

        switch (screenName) {
            case "SignInScreen":
                newScreen = new SignInScreen();
                break;
            case "MenuScreen":
                newScreen = new MenuScreen();
                break;
            case "MissionScreen":
                newScreen = new MissionScreen();
                break;
            case "GameScreen":
                newScreen = new GameScreen(missions[0]);
                break;
            case "OptionsScreen":
                newScreen = new OptionsScreen();
                break;
            case "CreditsScreen":
                newScreen = new CreditsScreen();
                break;
            case "GearScreen":
                newScreen = new GearScreen();
                break;
        }

        if (newScreen != null) {
            Static.getStage().clear();
            main.setScreen(newScreen);
        }
    }
}
