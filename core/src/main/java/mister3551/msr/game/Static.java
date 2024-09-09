package mister3551.msr.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import mister3551.msr.game.characters.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Static {

    public static final int PPM = 32;

    private static Player player;
    private static ArrayList<Rectangle> ladders;
    private static HashMap<String, ArrayList<Vector2>> ziplines;

    public static Player getPlayer() {
        return player;
    }

    public static void setPlayer(Player player) {
        Static.player = player;
    }

    public static ArrayList<Rectangle> getLadders() {
        return ladders;
    }

    public static void setLadders(ArrayList<Rectangle> ladders) {
        Static.ladders = ladders;
    }

    public static HashMap<String, ArrayList<Vector2>> getZiplines() {
        return ziplines;
    }

    public static void setZiplines(HashMap<String, ArrayList<Vector2>> ziplines) {
        Static.ziplines = ziplines;
    }
}
