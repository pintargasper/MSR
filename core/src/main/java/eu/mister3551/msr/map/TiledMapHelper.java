package eu.mister3551.msr.map;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import eu.mister3551.msr.Static;
import eu.mister3551.msr.map.character.Enemy;
import eu.mister3551.msr.map.character.Hostage;
import eu.mister3551.msr.database.object.Mission;
import eu.mister3551.msr.map.character.Item;
import eu.mister3551.msr.map.character.Player;
import eu.mister3551.msr.map.character.weapon.Weapon;
import eu.mister3551.msr.screen.GameScreen;
import eu.mister3551.msr.screen.GameState;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TiledMapHelper {

    private final World world;
    private final Mission mission;
    private final BodyHelper bodyHelper;

    public TiledMapHelper(World world,Mission mission) {
        this.world = world;
        this.mission = mission;
        this.bodyHelper = Static.bodyHelper.setWorld(world);
    }

    public MapData setupMap() {
        TiledMap tiledMap = new TmxMapLoader().load("maps/" + "training"  + "/" + mission.getMap());

        float light = tiledMap.getProperties().get("light", Float.class);

        MapGroupLayer staticComponents = (MapGroupLayer) tiledMap.getLayers().get("Static");
        MapGroupLayer components = (MapGroupLayer) tiledMap.getLayers().get("Components");

        parseStaticComponents(staticComponents);
        parseComponents(components, mission.getMap());

        return new MapData(new OrthogonalTiledMapRenderer(tiledMap), light);
    }

    private void parseStaticComponents(MapGroupLayer mapGroupLayer) {
        for (MapLayer mapLayer : mapGroupLayer.getLayers()) {
            if (mapLayer.getName().equals("Static")) {
                for (MapObject mapObject : mapLayer.getObjects()) {
                    if (mapObject instanceof PolygonMapObject) {
                        bodyHelper.staticBody((PolygonMapObject) mapObject);
                    }
                }
            }
        }
    }

    private void parseComponents(MapGroupLayer mapGroupLayer, String mapName) {
        for (MapLayer mapLayer : mapGroupLayer.getLayers()) {
            if (mapLayer.getName().equals("Characters")) {
                MapGroupLayer charactersLayer = (MapGroupLayer) mapLayer;

                ArrayList<MapObject> mapObjects = new ArrayList<>();
                for (MapLayer subLayer : charactersLayer.getLayers()) {
                    if (subLayer.getName().equals("Characters") || subLayer.getName().equals("Items")) {
                        for (MapObject mapObject : subLayer.getObjects()) {
                            mapObjects.add(mapObject);
                        }
                    } else if (subLayer.getName().equals("EnemyMovement")) {
                        parseEnemyMovement(subLayer.getObjects());
                    }
                }
                parseCharacters(mapObjects, mapName);

            } else if (mapLayer.getName().equals("Lights")) {
                MapGroupLayer charactersLayer = (MapGroupLayer) mapLayer;
                for (MapLayer subLayer : charactersLayer.getLayers()) {
                    for (MapObject mapObject : subLayer.getObjects()) {
                        parseLight(mapObject);
                    }
                }
            } else if (mapLayer.getName().equals("Collisions")) {
                MapGroupLayer charactersLayer = (MapGroupLayer) mapLayer;
                for (MapLayer subLayer : charactersLayer.getLayers()) {
                    if (subLayer.getName().equals("Doors")) {
                        for (MapObject mapObject : subLayer.getObjects()) {
                            GameScreen.doors.add(mapObject);
                        }
                    } else if (subLayer.getName().equals("Waters")) {
                        for (MapObject mapObject : subLayer.getObjects()) {
                            parseWater(mapObject);
                        }
                    } else if (subLayer.getName().equals("Ladders")) {
                        MapGroupLayer laddersLayer = (MapGroupLayer) subLayer;
                        for (MapLayer ladder : laddersLayer.getLayers()) {
                            for (MapObject mapObject : ladder.getObjects()) {
                                parseLadder(mapObject);
                            }
                        }
                    } else if (subLayer.getName().equals("Ziplines")) {
                        MapGroupLayer ziplinesLayer = (MapGroupLayer) subLayer;
                        for (MapLayer zipline : ziplinesLayer.getLayers()) {
                            parseZipline(zipline.getObjects());
                        }
                    }
                }
            }
        }
    }

    private void parseCharacters(ArrayList<MapObject> mapObjects, String mapName) {
        GameState gameState = Static.gameState.get(mapName);

        if (gameState == null) {
            gameState = new GameState();
            gameState.setWorld(world);
            gameState.setMission(mission);
            Static.gameState.put(mapName, gameState);

            for (MapObject mapObject : mapObjects) {
                if (mapObject instanceof TiledMapTileMapObject) {
                    if (mapObject.getName() != null) {

                        ObjectData objectData = objectData(mapObject);

                        if (mapObject.getName().equals("player")) {
                            if (gameState.getPlayer() == null) {
                                gameState.setPlayer(new Player(objectData));
                            }
                        } else if (mapObject.getName().startsWith("enemy")) {
                            gameState.getEnemies().add(new Enemy(objectData));
                            Static.totalEnemies += 1;
                        } else if (mapObject.getName().startsWith("hostage")) {
                            gameState.getHostages().add(new Hostage(objectData));
                            Static.totalHostages += 1;
                        } else if (mapObject.getProperties().get("group").equals("Items")) {
                            gameState.getItems().add(new Item(objectData));
                        }
                    }
                }
            }
        }

        if (Static.gameState.size() >= 2) {
            GameState secondLastEntry = Static.gameState.values().stream()
                .skip(Static.gameState.size() - 2)
                .findFirst()
                .orElse(null);

            for (GameState state : Static.gameState.values()) {
                state.getPlayer().setLife(secondLastEntry != null ? secondLastEntry.getPlayer().getLife() : 100);
                state.getPlayer().setWeapon(secondLastEntry != null ? secondLastEntry.getPlayer().getWeapon() : null);
                state.getMission().setId(secondLastEntry != null ? secondLastEntry.getMission().getId() : 0);
                state.getMission().setIdUser(secondLastEntry != null ? secondLastEntry.getMission().getIdUser() : null);
            }
        }
    }

    private void parseEnemyMovement(MapObjects mapObjects) {
        Map<String, List<Vector2>> temporaryMap = new HashMap<>();
        Map<String, List<Integer>> sortKeysMap = new HashMap<>();

        for (MapObject mapObject : mapObjects) {
            String name = mapObject.getName();
            if (name != null && name.startsWith("enemy")) {
                String[] parts = name.split("-");
                if (parts.length == 2) {
                    String key = parts[0];
                    int sortKey = Integer.parseInt(parts[1]);
                    Vector2 coordinates = Converter.coordinates(((RectangleMapObject) mapObject).getRectangle());
                    temporaryMap.computeIfAbsent(key, value -> new ArrayList<>()).add(coordinates);
                    sortKeysMap.computeIfAbsent(key, value -> new ArrayList<>()).add(sortKey);
                }
            }
        }
        temporaryMap.forEach((key, rectangles) -> {
            List<Integer> sortKeys = sortKeysMap.get(key);

            List<Vector2> sortedVectors = IntStream.range(0, rectangles.size())
                .boxed()
                .sorted(Comparator.comparing(sortKeys::get))
                .map(rectangles::get)
                .collect(Collectors.toList());
            GameScreen.enemyMovement.put(key, new ArrayList<>(sortedVectors));
        });
    }

    private void parseZipline(MapObjects mapObjects) {
        Map<String, List<Vector2>> temporaryMap = new HashMap<>();
        Map<String, List<Integer>> sortKeysMap = new HashMap<>();

        for (MapObject mapObject : mapObjects) {
            String name = mapObject.getName();
            if (name != null && name.startsWith("zipline")) {
                String[] parts = name.split("-");
                if (parts.length == 2) {
                    String key = parts[0];
                    int sortKey = Integer.parseInt(parts[1]);

                    Vector2 coordinates = Converter.coordinates(((RectangleMapObject) mapObject).getRectangle());
                    temporaryMap.computeIfAbsent(key, value -> new ArrayList<>()).add(coordinates);
                    sortKeysMap.computeIfAbsent(key, value -> new ArrayList<>()).add(sortKey);
                }
            }
        }

        temporaryMap.forEach((key, rectangles) -> {
            List<Integer> sortKeys = sortKeysMap.get(key);

            List<Vector2> sortedVectors = IntStream.range(0, rectangles.size())
                .boxed()
                .sorted(Comparator.comparing(sortKeys::get))
                .map(rectangles::get)
                .collect(Collectors.toList());

            GameScreen.ziplines.put(key, new ArrayList<>(sortedVectors));
        });
    }

    private void parseLadder(MapObject mapObject) {
        if (mapObject.getName() != null) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            if (mapObject.getName() != null && mapObject.getName().equals("ladder")) {
                float offset = 30;
                float width = rectangle.getWidth() / 2;
                GameScreen.ladders.add(new Rectangle(rectangle.getX() + (rectangle.getWidth() - width) / 2 + offset,
                    rectangle.y, width - 2 * offset, rectangle.height));
            } else if (mapObject.getName().equals("stop")) {
                GameScreen.stopOnLadders.add(rectangle);
            }
        }
    }

    private void parseWater(MapObject mapObject) {
        if (mapObject.getName() != null && mapObject.getName().equals("water")) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            GameScreen.waters.add(rectangle);
        }
    }

    private void parseLight(MapObject mapObject) {
        //GameScreen.lights.add(new Light(mapObject.getName(), ((RectangleMapObject) mapObject).getRectangle()));
    }

    private ObjectData objectData(MapObject mapObject) {
        String name = mapObject.getName();
        String group = mapObject.getProperties().get("group", String.class);
        String skin = mapObject.getProperties().get("skin", String.class);
        String type = mapObject.getProperties().get("type", String.class);
        String weaponName = mapObject.getProperties().get("weapon", String.class);

        String awardString = mapObject.getProperties().get("award", String.class);
        Float award = awardString.isEmpty() ? 0.0f : Float.parseFloat(awardString);
        String penaltyString = mapObject.getProperties().get("penalty", String.class);
        Float penalty = penaltyString.isEmpty() ? 0.0f : Float.parseFloat(penaltyString);
        int speed = mapObject.getProperties().get("speed", Integer.class);
        int speedOnLadder = mapObject.getProperties().get("speedOnLadder", Integer.class);
        int speedOnZipline = mapObject.getProperties().get("speedOnZipline", Integer.class);

        Integer jumps = mapObject.getProperties().get("jumps", Integer.class);
        Integer life = mapObject.getProperties().get("life", Integer.class);

        int height = ((TiledMapTileMapObject) mapObject).getTile().getTextureRegion().getRegionHeight();
        int width = ((TiledMapTileMapObject) mapObject).getTile().getTextureRegion().getRegionWidth();
        float positionX = ((TiledMapTileMapObject) mapObject).getX();
        float positionY = ((TiledMapTileMapObject) mapObject).getY();

        Weapon weapon = name.isEmpty() ? null : new Weapon(weaponName, 36, 43, 50, 650, 45);
        Body body = bodyHelper.body(name, width, height, positionX, positionY, false);
        body.setUserData(group.equals("Items") ? group : name);
        Rectangle bounds = new Rectangle(positionX, positionY, width, height);

        ConeLight flashlight = null;
        PointLight pointLight = null;
        /*if (name.matches("player") || name.startsWith("enemy")) {
            flashlight = new ConeLight(rayHandler, 10, Color.WHITE, 20, 200, 100, 90, 30);
            pointLight = new PointLight(rayHandler, 10, Color.CORAL, 20, 200, 100);
        }*/
        return new ObjectData(body, bounds, weapon, flashlight, pointLight, name, group, skin, type, award, penalty, speed, speedOnLadder, speedOnZipline, jumps, life, height, width, positionX, positionY);
    }
}
