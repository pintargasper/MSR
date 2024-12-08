package mister3551.msr.game.database.object;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Mission implements Json.Serializable {

    private int id;
    private String idUser;
    private String name;
    private String description;
    private String image;
    private String map;
    private Long score;
    private String usedTime;
    private Long playedCount;

    public Mission() {

    }

    @Override
    public void write(Json json) {
        json.writeValue("id", id);
        json.writeValue("idUser", idUser);
        json.writeValue("name", name);
        json.writeValue("description", description);
        json.writeValue("image", image);
        json.writeValue("map", map);
        json.writeValue("playedCount", playedCount);
        json.writeValue("score", score);
        json.writeValue("usedTime", usedTime);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.id = jsonData.getInt("id");
        this.idUser = jsonData.getString("idUser");
        this.name = jsonData.getString("name");
        this.description = jsonData.getString("description");
        this.image = jsonData.getString("image");
        this.playedCount = jsonData.getLong("playedCount");
        this.score = jsonData.getLong("score");
        this.usedTime = jsonData.getString("usedTime");
        this.map = jsonData.getString("map");
    }

    public Long getId() {
        return (long) id;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getMap() {
        return map;
    }

    public Long getScore() {
        return score;
    }

    public String getUsedTime() {
        return usedTime;
    }

    public Long getPlayedCount() {
        return playedCount;
    }
}
