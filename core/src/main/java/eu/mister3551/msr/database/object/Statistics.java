package eu.mister3551.msr.database.object;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

//TODO enemies alerted
@Getter
@Setter
public class Statistics implements Json.Serializable {

    private Long idUser;
    private Long idMission;
    private int score;
    private Map<String, Float> money;
    private boolean win;
    private String usedTime;
    private double distanceTraveled;
    private int shotsFired;
    private int shotsHit;
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

    public void setAccuracy() {
        missedShots = shotsFired - shotsHit;
        accuracy = shotsFired > 0 ? Math.round(((this.shotsFired - this.missedShots) / (double) this.shotsFired) * 100 * 100) / 100.0 : 0;
    }

    public void setTotalMoney() {
        double total = money.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().matches("ammoCosts") ? -entry.getValue() : entry.getValue())
            .sum();
        money.put("total", (float) total);
    }
}
