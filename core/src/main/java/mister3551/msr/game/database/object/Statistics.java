package mister3551.msr.game.database.object;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import mister3551.msr.game.Static;
import mister3551.msr.game.characters.object.Enemy;

import java.util.HashMap;
import java.util.Map;

public class Statistics implements Json.Serializable {

    private Long idUser;
    private Long idMission;
    private int score;
    private Map<String, Float> money;
    private boolean win;
    private String usedTime;
    private double distanceTraveled;
    private int shotsFired;
    private int missedShots;
    private double accuracy;
    private int criticalHits;
    private int headshots;
    private int enemiesAlerted;
    private Map<String, Integer> enemyTypesKilled;
    private Map<String, Integer> hostageTypesKilled;
    private Map<String, Integer> itemsCollected;

    public Statistics() {
        this.money = new HashMap<>();
        this.enemyTypesKilled = new HashMap<>();
        this.hostageTypesKilled = new HashMap<>();
        this.itemsCollected = new HashMap<>();
    }

    @Override
    public void write(Json json) {
        json.writeValue("idUser", idUser);
        json.writeValue("idMission", idMission);
        json.writeValue("score", score);

        json.writeObjectStart("money");
        for (Map.Entry<String, Float> entry : money.entrySet()) {
            json.writeValue(entry.getKey(), entry.getValue());
        }
        json.writeObjectEnd();

        json.writeValue("win", win);
        json.writeValue("usedTime", usedTime);
        json.writeValue("distanceTraveled", distanceTraveled);
        json.writeValue("shotsFired", shotsFired);
        json.writeValue("missedShots", missedShots);
        json.writeValue("accuracy", accuracy);
        json.writeValue("criticalHits", criticalHits);
        json.writeValue("headshots", headshots);
        json.writeValue("enemiesAlerted", enemiesAlerted);
        writeMap(json, "enemyTypesKilled", enemyTypesKilled);
        writeMap(json, "hostageTypesKilled", hostageTypesKilled);
        writeMap(json, "itemsCollected", itemsCollected);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.idUser = jsonData.getLong("idUser");
        this.idMission = jsonData.getLong("idMission");
        this.score = jsonData.getInt("score");
        this.money = readFloatMap(jsonData.get("money"));
        this.win = jsonData.getBoolean("win");
        this.usedTime = jsonData.getString("usedTime");
        this.distanceTraveled = jsonData.getDouble("distanceTraveled");
        this.shotsFired = jsonData.getInt("shotsFired");
        this.missedShots = jsonData.getInt("missedShots");
        this.accuracy = jsonData.getDouble("accuracy");
        this.criticalHits = jsonData.getInt("criticalHits");
        this.headshots = jsonData.getInt("headshots");
        this.enemiesAlerted = jsonData.getInt("enemiesAlerted");

        this.enemyTypesKilled = readIntMap(jsonData.get("enemyTypesKilled"));
        this.hostageTypesKilled = readIntMap(jsonData.get("hostageTypesKilled"));
        this.itemsCollected = readIntMap(jsonData.get("itemsCollected"));
    }

    public void initialize() {
        for (Enemy enemy : Static.getEnemies()) {
            String enemyType = enemy.getType().replaceAll("\\d+$", "").toLowerCase();
            this.enemyTypesKilled.put(enemyType, 0);
        }
    }

    private void writeMap(Json json, String name, Map<String, Integer> map) {
        json.writeObjectStart(name);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            json.writeValue(entry.getKey(), entry.getValue());
        }
        json.writeObjectEnd();
    }

    private Map<String, Float> readFloatMap(JsonValue jsonValue) {
        Map<String, Float> map = new HashMap<>();
        for (JsonValue entry = jsonValue.child; entry != null; entry = entry.next) {
            map.put(entry.name, entry.asFloat());
        }
        return map;
    }

    private Map<String, Integer> readIntMap(JsonValue jsonValue) {
        Map<String, Integer> map = new HashMap<>();
        for (JsonValue entry = jsonValue.child; entry != null; entry = entry.next) {
            map.put(entry.name, entry.asInt());
        }
        return map;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public void setIdMission(Long idMission) {
        this.idMission = idMission;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Map<String, Float> getMoney() {
        return money;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public String getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(String usedTime) {
        this.usedTime = usedTime;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(double distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public int getShotsFired() {
        return shotsFired;
    }

    public void setShotsFired(int shotsFired) {
        this.shotsFired = shotsFired;
    }

    public int getMissedShots() {
        return missedShots;
    }

    public void setMissedShots(int missedShots) {
        this.missedShots = missedShots;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy() {
        if (shotsFired > 0) {
            this.accuracy = Math.round(((this.shotsFired - this.missedShots) / (double) this.shotsFired) * 100 * 100) / 100.0;
        } else {
            this.accuracy = 0;
        }
    }

    public int getCriticalHits() {
        return criticalHits;
    }

    public void setCriticalHits(int criticalHits) {
        this.criticalHits = criticalHits;
    }

    public int getHeadshots() {
        return headshots;
    }

    public void setHeadshots(int headshots) {
        this.headshots = headshots;
    }

    public int getEnemiesAlerted() {
        return enemiesAlerted;
    }

    public void setEnemiesAlerted(int enemiesAlerted) {
        this.enemiesAlerted = enemiesAlerted;
    }

    public Map<String, Integer> getEnemyTypesKilled() {
        return enemyTypesKilled;
    }

    public void setEnemyTypesKilled(Map<String, Integer> enemyTypesKilled) {
        this.enemyTypesKilled = enemyTypesKilled;
    }

    public Map<String, Integer> getHostageTypesKilled() {
        return hostageTypesKilled;
    }

    public void setHostageTypesKilled(Map<String, Integer> hostageTypesKilled) {
        this.hostageTypesKilled = hostageTypesKilled;
    }

    public Map<String, Integer> getItemsCollected() {
        return itemsCollected;
    }

    public void setItemsCollected(Map<String, Integer> itemsCollected) {
        this.itemsCollected = itemsCollected;
    }
}
