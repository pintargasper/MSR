package mister3551.msr.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import mister3551.msr.game.characters.object.*;
import mister3551.msr.game.database.Data;
import mister3551.msr.game.database.Token;
import mister3551.msr.game.database.object.*;
import mister3551.msr.game.map.BodyHelper;
import mister3551.msr.game.screen.ScreenChanger;
import mister3551.msr.game.screen.components.Navigation;
import mister3551.msr.game.screen.components.Popup;
import mister3551.msr.game.screen.javascript.Native;

import java.util.ArrayList;
import java.util.HashMap;

public class Static {

    public static final int PPM = 32;

    private static ScreenChanger screenChanger;
    private static Stage stage;
    private static Skin skin;
    private static Native _native;
    private static Navigation navigation;
    private static Popup popup;
    private static Json json;
    private static Data data;
    private static Account account;
    private static Options options;
    private static Statistics statistics;
    public static Token token;
    private static ArrayList<Mission> missions;
    private static ArrayList<Gear> gears;

    private static Rectangle end;
    private static Player player;

    private static ArrayList<Enemy> enemies;
    private static ArrayList<Enemy> enemiesToRemove;
    private static ArrayList<Hostage> hostages;
    private static ArrayList<Hostage> hostagesToRemove;
    private static ArrayList<Bullet> bullets;
    private static ArrayList<Bullet> bulletsToRemove;

    private static ArrayList<Rectangle> ladders;
    private static ArrayList<Rectangle> stopOnLadders;
    private static ArrayList<Rectangle> waters;
    private static ArrayList<Item> items;
    private static ArrayList<Item> itemsToRemove;
    private static HashMap<String, ArrayList<Vector2>> ziplines;
    private static HashMap<String, ArrayList<Vector2>> enemyMovement;
    private static BodyHelper bodyHelper;

    private static int totalEnemies;
    private static int totalHostages;

    public static ScreenChanger getScreenChanger() {
        return screenChanger;
    }

    public static void setScreenChanger(ScreenChanger screenChanger) {
        Static.screenChanger = screenChanger;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        Static.stage = stage;
    }

    public static Skin getSkin() {
        return skin;
    }

    public static void setSkin(Skin skin) {
        Static.skin = skin;
    }

    public static Native getNative() {
        return _native;
    }

    public static void setNative(Native _native) {
        Static._native = _native;
    }

    public static Navigation getNavigation() {
        return navigation;
    }

    public static void setNavigation(Navigation navigation) {
        Static.navigation = navigation;
    }

    public static Popup getPopup() {
        return popup;
    }

    public static void setPopup(Popup popup) {
        Static.popup = popup;
    }

    public static Json getJson() {
        return json;
    }

    public static void setJson(Json json) {
        Static.json = json;
    }

    public static Data getData() {
        return data;
    }

    public static void setData(Data data) {
        Static.data = data;
    }

    public static Account getAccount() {
        return account;
    }

    public static void setAccount(Account account) {
        Static.account = account;
    }

    public static Token getToken() {
        return token;
    }

    public static void setToken(Token token) {
        Static.token = token;
    }

    public static Statistics getStatistics() {
        return statistics;
    }

    public static void setStatistics(Statistics statistics) {
        Static.statistics = statistics;
    }

    public static Options getOptions() {
        return options;
    }

    public static void setOptions(Options options) {
        Static.options = options;
    }

    public static ArrayList<Mission> getMissions() {
        return missions;
    }

    public static void setMissions(ArrayList<Mission> missions) {
        Static.missions = missions;
    }

    public static ArrayList<Gear> getGears() {
        return gears;
    }

    public static void setGears(ArrayList<Gear> gears) {
        Static.gears = gears;
    }

    public static Rectangle getEnd() {
        return end;
    }

    public static void setEnd(Rectangle end) {
        Static.end = end;
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

    public static ArrayList<Enemy> getEnemiesToRemove() {
        return enemiesToRemove;
    }

    public static void setEnemiesToRemove(ArrayList<Enemy> enemiesToRemove) {
        Static.enemiesToRemove = enemiesToRemove;
    }

    public static ArrayList<Hostage> getHostages() {
        return hostages;
    }

    public static void setHostages(ArrayList<Hostage> hostages) {
        Static.hostages = hostages;
    }

    public static ArrayList<Hostage> getHostagesToRemove() {
        return hostagesToRemove;
    }

    public static void setHostagesToRemove(ArrayList<Hostage> hostagesToRemove) {
        Static.hostagesToRemove = hostagesToRemove;
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

    public static ArrayList<Item> getItems() {
        return items;
    }

    public static void setItems(ArrayList<Item> items) {
        Static.items = items;
    }

    public static ArrayList<Item> getItemsToRemove() {
        return itemsToRemove;
    }

    public static void setItemsToRemove(ArrayList<Item> itemsToRemove) {
        Static.itemsToRemove = itemsToRemove;
    }

    public static HashMap<String, ArrayList<Vector2>> getZiplines() {
        return ziplines;
    }

    public static void setZiplines(HashMap<String, ArrayList<Vector2>> ziplines) {
        Static.ziplines = ziplines;
    }

    public static HashMap<String, ArrayList<Vector2>> getEnemyMovement() {
        return enemyMovement;
    }

    public static void setEnemyMovement(HashMap<String, ArrayList<Vector2>> enemyMovement) {
        Static.enemyMovement = enemyMovement;
    }

    public static BodyHelper getBodyHelper() {
        return bodyHelper;
    }

    public static void setBodyHelper(BodyHelper bodyHelper) {
        Static.bodyHelper = bodyHelper;
    }

    public static int getTotalEnemies() {
        return totalEnemies;
    }

    public static void setTotalEnemies(int totalEnemies) {
        Static.totalEnemies = totalEnemies;
    }

    public static int getTotalHostages() {
        return totalHostages;
    }

    public static void setTotalHostages(int totalHostages) {
        Static.totalHostages = totalHostages;
    }
}
