package eu.mister3551.msr.screen;

import com.badlogic.gdx.Screen;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.Main;
import eu.mister3551.msr.database.object.Mission;
import eu.mister3551.msr.database.object.Statistics;
import eu.mister3551.msr.map.GameState;
import eu.mister3551.msr.screen.gamescreen.Timer;
import lombok.Getter;

@Getter
public class ScreenChanger {

    private String mapName;
    private final Main main;
    private GameState gameState;
    private boolean resetGame;
    private Timer timer;

    public ScreenChanger(Main main) {
        this.main = main;
    }

    public void changeScreen(String screenName, Mission... mission) {

        if (this.mapName == null && mission.length > 0) {
            this.mapName = mission[0].getMap();
        }

        Screen newScreen = createScreen(screenName, mission);
        Constants.stage.clear();
        main.setScreen(newScreen);
    }

    public ScreenChanger reset() {
        this.resetGame = true;

        if (Constants.gameScreen != null) {
            this.gameState = new GameState(Constants.gameScreen.getMission());
        }
        return this;
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
                if (gameState == null || resetGame) {
                    this.gameState = new GameState(mission[0]);
                    this.timer = new Timer();
                    Constants.statistics = new Statistics();
                    this.resetGame = false;
                }
                newScreen = new GameScreen(mission[0], gameState);
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
}
