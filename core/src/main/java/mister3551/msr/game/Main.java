package mister3551.msr.game;

import com.badlogic.gdx.Game;
import mister3551.msr.game.screen.GameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
