package eu.mister3551.msr;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import eu.mister3551.msr.database.Data;
import eu.mister3551.msr.database.Token;
import eu.mister3551.msr.database.object.*;
import eu.mister3551.msr.screen.GameScreen;
import eu.mister3551.msr.screen.ScreenChanger;
import eu.mister3551.msr.screen.components.Navigation;
import eu.mister3551.msr.screen.components.Popup;
import eu.mister3551.msr.screen.javascript.Native;

import java.util.ArrayList;

public class Constants {

    public static final int PPM = 32;

    public static Skin skin;
    public static Stage stage;
    public static Native _native;
    public static Navigation navigation;
    public static Popup popup;
    public static Json json;
    public static Data data;
    public static Token token;
    public static Account account;
    public static Options options;
    public static Statistics statistics;
    public static ScreenChanger screenChanger;
    public static ArrayList<Mission> missions;
    public static ArrayList<Gear> gears;
    public static GameScreen gameScreen;
}
