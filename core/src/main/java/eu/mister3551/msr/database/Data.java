package eu.mister3551.msr.database;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.database.object.*;
import eu.mister3551.msr.screen.link.Callback;

import java.util.ArrayList;

public class Data {

    private final Json json;
    private final JsonReader jsonReader;
    private final String API = "http://localhost:8080";

    public Data() {
        this.json = Constants.json;
        this.json.setOutputType(JsonWriter.OutputType.json);
        this.jsonReader = new JsonReader();
    }

    public void isAuthenticated(String token, Callback callback) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest request = requestBuilder
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(API + "/is-valid")
            .header("Authorization", "Bearer " + token)
            .build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                String responseContent = httpResponse.getResultAsString();

                if (status.getStatusCode() == 200) {
                    String[] result = responseContent.split(",\\s*");
                    if (result.length == 2) {
                        callback.onSuccess(new Account(result[1], token));
                    } else {
                        callback.onError("FAILED");
                    }
                } else {
                    callback.onError("FAILED");
                }
            }

            @Override
            public void failed(Throwable throwable) {
                callback.onError("Error: " + throwable.getMessage());
            }

            @Override
            public void cancelled() {
                callback.onError("Cancelled");
            }
        });
    }

    public void signIn(String usernameOrEmailAddress, String password, Callback callback) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest request = requestBuilder
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(API + "/sign-in")
            .header("Content-Type", "application/json")
            .content("{\"usernameOrEmailAddress\": \"" + usernameOrEmailAddress + "\", \"password\": \"" + password + "\"}")
            .build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                String responseContent = httpResponse.getResultAsString().replaceAll("[\\[\\]\"]", "").trim();

                if (status.getStatusCode() == 200) {
                    String[] result = responseContent.split(",\\s*");

                    if (result.length == 2) {
                        callback.onSuccess(new Account(result[1], result[0]));
                    } else {
                        callback.onError(responseContent.contentEquals("Bad Credentials") ? "Wrong username or password" : "No internet connection or connection error");
                    }
                } else {
                    callback.onError("Wrong username or password");
                }
            }

            @Override
            public void failed(Throwable throwable) {
                callback.onError("No internet connection or connection error");
            }

            @Override
            public void cancelled() {
                callback.onError("Cancelled");
            }
        });
    }

    public void options(Callback callback) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest request = requestBuilder
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(API + "/app/user/options")
            .header("Authorization", "Bearer " + Constants.account.getToken())
            .build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                String responseContent = httpResponse.getResultAsString();
                if (status.getStatusCode() == 200) {
                    Options options = new Options();
                    JsonValue jsonValue = jsonReader.parse(responseContent);
                    options.read(json, jsonValue);
                    callback.onSuccess(options);
                } else {
                    callback.onError("No internet connection or connection error");
                }
            }

            @Override
            public void failed(Throwable throwable) {
                callback.onError("No internet connection or connection error");
            }

            @Override
            public void cancelled() {
                callback.onError("Cancelled");
            }
        });
    }

    public void missions(Callback callback) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest request = requestBuilder
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(API + "/app/user/mission/statistics")
            .header("Authorization", "Bearer " + Constants.account.getToken())
            .build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                String responseContent = httpResponse.getResultAsString();
                if (status.getStatusCode() == 200) {

                    ArrayList<Mission> missions = new ArrayList<>();
                    JsonValue jsonValue = jsonReader.parse(responseContent);
                    for (JsonValue missionJson : jsonValue) {
                        Mission mission = new Mission();
                        mission.read(json, missionJson);
                        missions.add(mission);
                    }
                    callback.onSuccess(missions);
                } else {
                    callback.onError("No internet connection or connection error");
                }
            }

            @Override
            public void failed(Throwable throwable) {
                callback.onError("No internet connection or connection error");
            }

            @Override
            public void cancelled() {
                callback.onError("Cancelled");
            }
        });
    }

    public void gear(Callback callback) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest request = requestBuilder
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(API + "/app/user/gear")
            .header("Authorization", "Bearer " + Constants.account.getToken())
            .build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                String responseContent = httpResponse.getResultAsString();
                if (status.getStatusCode() == 200) {

                    ArrayList<Gear> gears = new ArrayList<>();
                    JsonValue jsonValue = jsonReader.parse(responseContent);
                    for (JsonValue gearJson : jsonValue) {
                        Gear gear = new Gear();
                        gear.read(json, gearJson);
                        gears.add(gear);
                    }
                    callback.onSuccess(gears);
                } else {
                    callback.onError("No internet connection or connection error");
                }
            }

            @Override
            public void failed(Throwable throwable) {
                callback.onError("No internet connection or connection error");
            }

            @Override
            public void cancelled() {
                callback.onError("Cancelled");
            }
        });
    }

    public void updateOptions(Options options, Callback callback) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest request = requestBuilder
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(API + "/app/user/update/options")
            .header("Authorization", "Bearer " + Constants.account.getToken())
            .header("Content-Type", "application/json")
            .content(json.toJson(options))
            .build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                String responseContent = httpResponse.getResultAsString();
                if (status.getStatusCode() == 200) {
                    callback.onSuccess(responseContent);
                } else {
                    callback.onError("No internet connection or connection error");
                }
            }

            @Override
            public void failed(Throwable throwable) {
                callback.onError("No internet connection or connection error");
            }

            @Override
            public void cancelled() {
                callback.onError("Cancelled");
            }
        });
    }

    public void updateGear(Gear gear, Callback callback) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest request = requestBuilder
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(API + "/app/user/update/gear")
            .header("Authorization", "Bearer " + Constants.account.getToken())
            .header("Content-Type", "application/json")
            .content(json.toJson(gear))
            .build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                String responseContent = httpResponse.getResultAsString();
                if (status.getStatusCode() == 200) {
                    callback.onSuccess(responseContent);
                } else {
                    callback.onError("No internet connection or connection error");
                }
            }

            @Override
            public void failed(Throwable throwable) {
                callback.onError("No internet connection or connection error");
            }

            @Override
            public void cancelled() {
                callback.onError("Cancelled");
            }
        });
    }

    public void insertMission(Statistics statistics, Callback callback) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest request = requestBuilder
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(API + "/app/user/mission/update")
            .header("Authorization", "Bearer " + Constants.account.getToken())
            .header("Content-Type", "application/json")
            .content(json.toJson(statistics))
            .build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                String responseContent = httpResponse.getResultAsString();
                if (status.getStatusCode() == 200) {
                    callback.onSuccess(responseContent);
                } else {
                    callback.onError("No internet connection or connection error");
                }
            }

            @Override
            public void failed(Throwable throwable) {
                callback.onError("No internet connection or connection error");
            }

            @Override
            public void cancelled() {
                callback.onError("Cancelled");
            }
        });
    }
}
