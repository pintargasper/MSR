package eu.mister3551.msr.database.object;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Gear implements Json.Serializable {

    private int id;
    private String idUser;
    private String name;
    private String image;
    private float price;
    private String activeWeapon;

    @Override
    public void write(Json json) {
        json.writeValue("id", id);
        json.writeValue("idUser", idUser);
        json.writeValue("activeWeapon", activeWeapon);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.id = jsonData.getInt("id");
        this.idUser = jsonData.getString("idUser");
        this.name = jsonData.getString("name");
        this.image = jsonData.getString("image");
        this.price = jsonData.getFloat("price");
        this.activeWeapon = jsonData.getString("activeWeapon");
    }
}
