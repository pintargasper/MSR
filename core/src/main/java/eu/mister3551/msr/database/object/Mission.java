package eu.mister3551.msr.database.object;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mission implements Json.Serializable {

    private int id;
    private String idUser;
    private String name;
    private String description;
    private String image;
    private String map;
    private String maps;
    private Long score;
    private String usedTime;
    private Long playedCount;

    @Override
    public void write(Json json) {
        json.writeValue("id", id);
        json.writeValue("idUser", idUser);
        json.writeValue("name", name);
        json.writeValue("description", description);
        json.writeValue("image", image);
        json.writeValue("map", map);
        json.writeValue("maps", maps);
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
        this.maps = jsonData.getString("maps");
    }
}
