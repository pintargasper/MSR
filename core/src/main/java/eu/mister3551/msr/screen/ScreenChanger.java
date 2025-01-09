package eu.mister3551.msr.screen;

import com.badlogic.gdx.Screen;
import eu.mister3551.msr.Main;
import eu.mister3551.msr.Static;
import eu.mister3551.msr.database.object.Mission;

public class ScreenChanger {

    private final Main main;

    public ScreenChanger(Main main) {
        this.main = main;
    }

    public void changeScreen(String screenName, Mission... mission) {
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
                newScreen = new GameScreen(mission[0]);
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
            Static.stage.clear();
            main.setScreen(newScreen);
        }
    }
}
