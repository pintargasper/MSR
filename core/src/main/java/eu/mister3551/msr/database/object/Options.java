package eu.mister3551.msr.database.object;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Options implements Json.Serializable {

    private int id;
    private int idUser;
    private int music;
    private int soundEffect;
    private String language;
    private int pause;
    private int keyboardFootJump;
    private int keyboardFootLeft;
    private int keyboardFootRight;
    private int keyboardFootReload;
    private int keyboardFootShoot;
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
    private int controllerLadderUp;
    private int controllerLadderDown;
    private int controllerLadderLeft;
    private int controllerLadderRight;
    private int controllerLadderReload;

    public Options() {

    }

    @Override
    public void write(Json json) {
        json.writeValue("id", id);
        json.writeValue("idUser", idUser);
        json.writeValue("music", music);
        json.writeValue("soundEffect", soundEffect);
        json.writeValue("language", language);
        json.writeValue("pause", pause);
        json.writeValue("keyboardFootJump", keyboardFootJump);
        json.writeValue("keyboardFootLeft", keyboardFootLeft);
        json.writeValue("keyboardFootRight", keyboardFootRight);
        json.writeValue("keyboardFootReload", keyboardFootReload);
        json.writeValue("keyboardFootShoot", keyboardFootShoot);
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
        this.pause = jsonData.getInt("pause");
        this.keyboardFootJump = jsonData.getInt("keyboardFootJump");
        this.keyboardFootLeft = jsonData.getInt("keyboardFootLeft");
        this.keyboardFootRight = jsonData.getInt("keyboardFootRight");
        this.keyboardFootReload = jsonData.getInt("keyboardFootReload");
        this.keyboardFootShoot = jsonData.getInt("keyboardFootShoot");
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
        this.controllerLadderUp = jsonData.getInt("controllerLadderUp");
        this.controllerLadderDown = jsonData.getInt("controllerLadderDown");
        this.controllerLadderLeft = jsonData.getInt("controllerLadderLeft");
        this.controllerLadderRight = jsonData.getInt("controllerLadderRight");
        this.controllerLadderReload = jsonData.getInt("controllerLadderReload");
    }

    public int getMusic() {
        return music;
    }

    public void setMusic(int music) {
        this.music = music;
    }

    public int getSoundEffect() {
        return soundEffect;
    }

    public void setSoundEffect(int soundEffect) {
        this.soundEffect = soundEffect;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getPause() {
        return pause;
    }

    public void setPause(int pause) {
        this.pause = pause;
    }

    public int getKeyboardFootJump() {
        return keyboardFootJump;
    }

    public void setKeyboardFootJump(int keyboardFootJump) {
        this.keyboardFootJump = keyboardFootJump;
    }

    public int getKeyboardFootLeft() {
        return keyboardFootLeft;
    }

    public void setKeyboardFootLeft(int keyboardFootLeft) {
        this.keyboardFootLeft = keyboardFootLeft;
    }

    public int getKeyboardFootRight() {
        return keyboardFootRight;
    }

    public void setKeyboardFootRight(int keyboardFootRight) {
        this.keyboardFootRight = keyboardFootRight;
    }

    public int getKeyboardFootReload() {
        return keyboardFootReload;
    }

    public void setKeyboardFootReload(int keyboardFootReload) {
        this.keyboardFootReload = keyboardFootReload;
    }

    public int getKeyboardFootShoot() {
        return keyboardFootShoot;
    }

    public void setKeyboardFootShoot(int keyboardFootShoot) {
        this.keyboardFootShoot = keyboardFootShoot;
    }

    public int getKeyboardLadderUp() {
        return keyboardLadderUp;
    }

    public void setKeyboardLadderUp(int keyboardLadderUp) {
        this.keyboardLadderUp = keyboardLadderUp;
    }

    public int getKeyboardLadderDown() {
        return keyboardLadderDown;
    }

    public void setKeyboardLadderDown(int keyboardLadderDown) {
        this.keyboardLadderDown = keyboardLadderDown;
    }

    public int getKeyboardLadderLeft() {
        return keyboardLadderLeft;
    }

    public void setKeyboardLadderLeft(int keyboardLadderLeft) {
        this.keyboardLadderLeft = keyboardLadderLeft;
    }

    public int getKeyboardLadderRight() {
        return keyboardLadderRight;
    }

    public void setKeyboardLadderRight(int keyboardLadderRight) {
        this.keyboardLadderRight = keyboardLadderRight;
    }

    public int getKeyboardLadderReload() {
        return keyboardLadderReload;
    }

    public void setKeyboardLadderReload(int keyboardLadderReload) {
        this.keyboardLadderReload = keyboardLadderReload;
    }

    public int getControllerFootJump() {
        return controllerFootJump;
    }

    public void setControllerFootJump(int controllerFootJump) {
        this.controllerFootJump = controllerFootJump;
    }

    public int getControllerFootLeft() {
        return controllerFootLeft;
    }

    public void setControllerFootLeft(int controllerFootLeft) {
        this.controllerFootLeft = controllerFootLeft;
    }

    public int getControllerFootRight() {
        return controllerFootRight;
    }

    public void setControllerFootRight(int controllerFootRight) {
        this.controllerFootRight = controllerFootRight;
    }

    public int getControllerFootReload() {
        return controllerFootReload;
    }

    public void setControllerFootReload(int controllerFootReload) {
        this.controllerFootReload = controllerFootReload;
    }

    public int getControllerFootShoot() {
        return controllerFootShoot;
    }

    public void setControllerFootShoot(int controllerFootShoot) {
        this.controllerFootShoot = controllerFootShoot;
    }

    public int getControllerLadderUp() {
        return controllerLadderUp;
    }

    public void setControllerLadderUp(int controllerLadderUp) {
        this.controllerLadderUp = controllerLadderUp;
    }

    public int getControllerLadderDown() {
        return controllerLadderDown;
    }

    public void setControllerLadderDown(int controllerLadderDown) {
        this.controllerLadderDown = controllerLadderDown;
    }

    public int getControllerLadderLeft() {
        return controllerLadderLeft;
    }

    public void setControllerLadderLeft(int controllerLadderLeft) {
        this.controllerLadderLeft = controllerLadderLeft;
    }

    public int getControllerLadderRight() {
        return controllerLadderRight;
    }

    public void setControllerLadderRight(int controllerLadderRight) {
        this.controllerLadderRight = controllerLadderRight;
    }

    public int getControllerLadderReload() {
        return controllerLadderReload;
    }

    public void setControllerLadderReload(int controllerLadderReload) {
        this.controllerLadderReload = controllerLadderReload;
    }
}
