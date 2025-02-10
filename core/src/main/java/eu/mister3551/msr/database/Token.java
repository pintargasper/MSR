package eu.mister3551.msr.database;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.screen.javascript.Native;

public class Token {

    private final Json json;
    private final Native _native;

    public Token() {
        this.json = Constants.json;
        this._native = Constants._native;
    }

    public void saveToJsonFile(String token) {
        try {
            FileHandle fileHandle = (Gdx.app.getType() == Application.ApplicationType.Android)
                ? Gdx.files.external("data/data.json")
                : Gdx.files.local("data/data.json");
            fileHandle.writeString(json.toJson(token), false);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public String readFromJsonFile() {
        String token = null;
        try {
            FileHandle jsonFile = (Gdx.app.getType() == Application.ApplicationType.Android)
                ? Gdx.files.external("data/data.json")
                : Gdx.files.local("data/data.json");
            if (jsonFile.exists()) {
                token = json.fromJson(String.class, jsonFile.readString());
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return token;
    }

    public void saveToCookie(String token) {
        _native.setCookieValue("token", token, 1);
    }

    public String readFromCookie() {
        return _native.getCookieValue("token");
    }
}
