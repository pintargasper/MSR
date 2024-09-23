package mister3551.msr.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import mister3551.msr.game.characters.object.Bullet;
import mister3551.msr.game.characters.object.Enemy;
import mister3551.msr.game.characters.object.Player;
import mister3551.msr.game.map.BodyHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class Static {

    public static final int PPM = 32;

    private static Player player;
    private static ArrayList<Enemy> enemies;
    private static ArrayList<Bullet> bullets;
    private static ArrayList<Bullet> bulletsToRemove;

    private static ArrayList<Rectangle> ladders;
    private static ArrayList<Rectangle> stopOnLadders;
    private static ArrayList<Rectangle> waters;
    private static HashMap<String, ArrayList<Vector2>> ziplines;

    private static BodyHelper bodyHelper;

    public static BodyHelper getBodyHelper() {
        return bodyHelper;
    }

    public static void setBodyHelper(BodyHelper bodyHelper) {
        Static.bodyHelper = bodyHelper;
    }

    public static Player getPlayer() {
        return player;
    }

    public static void setPlayer(Player player) {
        Static.player = player;
    }

    public static ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public static void setEnemies(ArrayList<Enemy> enemies) {
        Static.enemies = enemies;
    }

    public static ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public static void setBullets(ArrayList<Bullet> bullets) {
        Static.bullets = bullets;
    }

    public static ArrayList<Bullet> getBulletsToRemove() {
        return bulletsToRemove;
    }

    public static void setBulletsToRemove(ArrayList<Bullet> bulletsToRemove) {
        Static.bulletsToRemove = bulletsToRemove;
    }

    public static ArrayList<Rectangle> getLadders() {
        return ladders;
    }

    public static void setLadders(ArrayList<Rectangle> ladders) {
        Static.ladders = ladders;
    }

    public static ArrayList<Rectangle> getStopOnLadders() {
        return stopOnLadders;
    }

    public static void setStopOnLadders(ArrayList<Rectangle> stopOnLadders) {
        Static.stopOnLadders = stopOnLadders;
    }

    public static ArrayList<Rectangle> getWaters() {
        return waters;
    }

    public static void setWaters(ArrayList<Rectangle> waters) {
        Static.waters = waters;
    }

    public static HashMap<String, ArrayList<Vector2>> getZiplines() {
        return ziplines;
    }

    public static void setZiplines(HashMap<String, ArrayList<Vector2>> ziplines) {
        Static.ziplines = ziplines;
    }
}
