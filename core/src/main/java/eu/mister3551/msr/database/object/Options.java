package eu.mister3551.msr.database.object;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Options implements Json.Serializable {

    private int id;
    private int idUser;
    private int music;
    private int soundEffect;
    private String language;
    private String shadowsLights;
    private int pause;
    private int keyboardFootJump;
    private int keyboardFootLeft;
    private int keyboardFootRight;
    private int keyboardFootReload;
    private int keyboardFootShoot;
    private int keyboardFootDoor;
    private int keyboardLadderUp;
    private int keyboardLadderDown;
    private int keyboardLadderLeft;
    private int keyboardLadderRight;
    private int keyboardLadderReload;
    private int controllerFootJump;
    private int controllerFootLeft;
    private int controllerFootRight;
    private int controllerFootReload;
    private int controllerFootShoot;
    private int controllerFootDoor;
    private int controllerLadderUp;
    private int controllerLadderDown;
    private int controllerLadderLeft;
    private int controllerLadderRight;
    private int controllerLadderReload;

    @Override
    public void write(Json json) {
        json.writeValue("id", id);
        json.writeValue("idUser", idUser);
        json.writeValue("music", music);
        json.writeValue("soundEffect", soundEffect);
        json.writeValue("language", language);
        json.writeValue("shadowsLights", shadowsLights);
        json.writeValue("pause", pause);
        json.writeValue("keyboardFootJump", keyboardFootJump);
        json.writeValue("keyboardFootLeft", keyboardFootLeft);
        json.writeValue("keyboardFootRight", keyboardFootRight);
        json.writeValue("keyboardFootReload", keyboardFootReload);
        json.writeValue("keyboardFootShoot", keyboardFootShoot);
        json.writeValue("keyboardFootDoor", keyboardFootDoor);
        json.writeValue("keyboardLadderUp", keyboardLadderUp);
        json.writeValue("keyboardLadderDown", keyboardLadderDown);
        json.writeValue("keyboardLadderLeft", keyboardLadderLeft);
        json.writeValue("keyboardLadderRight", keyboardLadderRight);
        json.writeValue("keyboardLadderReload", keyboardLadderReload);
        json.writeValue("controllerFootJump", controllerFootJump);
        json.writeValue("controllerFootLeft", controllerFootLeft);
        json.writeValue("controllerFootRight", controllerFootRight);
        json.writeValue("controllerFootReload", controllerFootReload);
        json.writeValue("controllerFootShoot", controllerFootShoot);
        json.writeValue("controllerFootDoor", controllerFootDoor);
        json.writeValue("controllerLadderUp", controllerLadderUp);
        json.writeValue("controllerLadderDown", controllerLadderDown);
        json.writeValue("controllerLadderLeft", controllerLadderLeft);
        json.writeValue("controllerLadderRight", controllerLadderRight);
        json.writeValue("controllerLadderReload", controllerLadderReload);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.id = jsonData.getInt("id");
        this.idUser = jsonData.getInt("idUser");
        this.music = jsonData.getInt("music");
        this.soundEffect = jsonData.getInt("soundEffect");
        this.language = jsonData.getString("language");
        this.shadowsLights = jsonData.getString("shadowsLights");
        this.pause = jsonData.getInt("pause");
        this.keyboardFootJump = jsonData.getInt("keyboardFootJump");
        this.keyboardFootLeft = jsonData.getInt("keyboardFootLeft");
        this.keyboardFootRight = jsonData.getInt("keyboardFootRight");
        this.keyboardFootReload = jsonData.getInt("keyboardFootReload");
        this.keyboardFootShoot = jsonData.getInt("keyboardFootShoot");
        this.keyboardFootDoor = jsonData.getInt("keyboardFootDoor");
        this.keyboardLadderUp = jsonData.getInt("keyboardLadderUp");
        this.keyboardLadderDown = jsonData.getInt("keyboardLadderDown");
        this.keyboardLadderLeft = jsonData.getInt("keyboardLadderLeft");
        this.keyboardLadderRight = jsonData.getInt("keyboardLadderRight");
        this.keyboardLadderReload = jsonData.getInt("keyboardLadderReload");
        this.controllerFootJump = jsonData.getInt("controllerFootJump");
        this.controllerFootLeft = jsonData.getInt("controllerFootLeft");
        this.controllerFootRight = jsonData.getInt("controllerFootRight");
        this.controllerFootReload = jsonData.getInt("controllerFootReload");
        this.controllerFootShoot = jsonData.getInt("controllerFootShoot");
        this.controllerFootDoor = jsonData.getInt("controllerFootDoor");
        this.controllerLadderUp = jsonData.getInt("controllerLadderUp");
        this.controllerLadderDown = jsonData.getInt("controllerLadderDown");
        this.controllerLadderLeft = jsonData.getInt("controllerLadderLeft");
        this.controllerLadderRight = jsonData.getInt("controllerLadderRight");
        this.controllerLadderReload = jsonData.getInt("controllerLadderReload");
    }
}
