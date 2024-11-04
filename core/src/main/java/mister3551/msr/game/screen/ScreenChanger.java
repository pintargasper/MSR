package mister3551.msr.game.screen;

import mister3551.msr.game.Main;
import mister3551.msr.game.Static;
import com.badlogic.gdx.Screen;

public class ScreenChanger {

    private final Main main;

    public ScreenChanger(Main main) {
        this.main = main;
    }

    public void changeScreen(String screenName, String... mapName) {
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
                newScreen = new GameScreen(mapName[0]);
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
