package eu.mister3551.msr.screen;

import com.badlogic.gdx.Screen;
import eu.mister3551.msr.Main;
import eu.mister3551.msr.Static;
import eu.mister3551.msr.database.object.Mission;
import eu.mister3551.msr.database.object.Statistics;

import java.util.LinkedHashMap;

public class ScreenChanger {

    private final Main main;
    private boolean repeat;

    public ScreenChanger(Main main) {
        this.main = main;
    }

    public ScreenChanger repeat(boolean repeat) {
        this.repeat = repeat;
        return this;
    }

    public void changeScreen(String screenName, Mission... mission) {
        Screen newScreen = createScreen(screenName, mission);

        if (newScreen != null) {
            resetStaticState(newScreen instanceof GameScreen);
            main.setScreen(newScreen);
        }
    }

    private Screen createScreen(String screenName, Mission... mission) {
        Screen newScreen;
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
                if (repeat) {
                    resetGameSpecificState();
                    repeat = false;
                }
                newScreen = new GameScreen(mission != null && mission.length > 0 ? mission[0] : null);
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
            default:
                newScreen = null;
                break;
        }
        return newScreen;
    }

    private void resetStaticState(boolean isGameScreen) {
        Static.stage.clear();
        if (!isGameScreen) {
            resetGameSpecificState();
        }
    }

    private void resetGameSpecificState() {
        Static.gameState = new LinkedHashMap<>();
        Static.statistics = new Statistics();
        Static.timer.reset();
    }
}
