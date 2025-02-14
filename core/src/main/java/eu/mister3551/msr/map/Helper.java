package eu.mister3551.msr.map;

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
import eu.mister3551.msr.map.character.*;
import eu.mister3551.msr.map.character.weapon.Weapon;
import eu.mister3551.msr.map.light.Light;
import eu.mister3551.msr.map.object.ObjectData;
import eu.mister3551.msr.map.object.Stop;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class Helper {

    private float id;
    private final World world;
    private Player player;
    private final ArrayList<Enemy> enemies;
    private final ArrayList<Hostage> hostages;
    private final ArrayList<Item> items;
    private final ArrayList<MapObject> doors;
    private final ArrayList<Rectangle> ladders;
    private final ArrayList<Rectangle> waters;
    private final ArrayList<Stop> stopOnLadders;
    private final ArrayList<Light> lights;
    private final HashMap<String, ArrayList<Vector2>> ziplines;
    private final HashMap<String, ArrayList<Vector2>> enemyMovement;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private final BodyHelper bodyHelper;
    private boolean diffuseLight;
    private float light;
    private int totalEnemies;
    private int totalHostages;

    public Helper() {
        this.world = new World(new Vector2(0, -25), true);
        this.enemies = new ArrayList<>();
        this.hostages = new ArrayList<>();
        this.items = new ArrayList<>();
        this.doors = new ArrayList<>();
        this.ladders = new ArrayList<>();
        this.waters = new ArrayList<>();
        this.stopOnLadders = new ArrayList<>();
        this.lights = new ArrayList<>();
        this.ziplines = new HashMap<>();
        this.enemyMovement = new HashMap<>();
        this.bodyHelper = new BodyHelper().setWorld(world);
    }

    public Helper setUp(String missionName, String mapName) {
        TiledMap tiledMap = new TmxMapLoader().load("maps" + "/" + missionName + "/" + mapName);
        id = tiledMap.getProperties().get("id", Float.class);
        diffuseLight = tiledMap.getProperties().get("diffuseLight", Boolean.class);
        light = tiledMap.getProperties().get("light", Float.class);

        MapGroupLayer staticComponents = (MapGroupLayer) tiledMap.getLayers().get("Static");
        MapGroupLayer components = (MapGroupLayer) tiledMap.getLayers().get("Components");

        parseStaticComponents(staticComponents);
        parseComponents(components);

        totalEnemies = enemies.size();
        totalHostages = hostages.size();

        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        return this;
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

    private void parseComponents(MapGroupLayer components) {
        for (MapLayer mapLayer : components.getLayers()) {
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
                parseCharacters(mapObjects);
            } else if (mapLayer.getName().equals("Lights")) {
                MapGroupLayer charactersLayer = (MapGroupLayer) mapLayer;
                for (MapLayer subLayer : charactersLayer.getLayers()) {
                    for (MapObject mapObject : subLayer.getObjects()) {
                        int distance = mapObject.getProperties().get("distance", Integer.class);
                        float softness = mapObject.getProperties().get("softness", Float.class);
                        lights.add(new Light(mapObject.getName(), ((RectangleMapObject) mapObject).getRectangle(), distance, softness));
                    }
                }
            } else if (mapLayer.getName().equals("Collisions")) {
                MapGroupLayer charactersLayer = (MapGroupLayer) mapLayer;
                for (MapLayer subLayer : charactersLayer.getLayers()) {
                    if (subLayer.getName().equals("Doors")) {
                        for (MapObject mapObject : subLayer.getObjects()) {
                            doors.add(mapObject);
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

    private void parseCharacters(ArrayList<MapObject> mapObjects) {
        for (MapObject mapObject : mapObjects) {
            if (mapObject instanceof TiledMapTileMapObject) {
                if (mapObject.getName() != null) {

                    ObjectData objectData = objectData(mapObject);

                    if (mapObject.getName().equals("player")) {
                        player = new Player(objectData);
                    } else if (mapObject.getName().startsWith("enemy")) {
                        enemies.add(new Enemy(objectData));
                    } else if (mapObject.getName().startsWith("hostage")) {
                        hostages.add(new Hostage(objectData));
                    } else if (mapObject.getProperties().get("group").equals("Items")) {
                        items.add(new Item(objectData));
                    }
                }
            }
        }
    }

    private void parseLadder(MapObject mapObject) {
        if (mapObject.getName() != null) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            if (mapObject.getName() != null && mapObject.getName().equals("ladder")) {
                float offset = 30;
                float width = rectangle.getWidth() / 2;
                ladders.add(new Rectangle(rectangle.getX() + (rectangle.getWidth() - width) / 2 + offset,
                    rectangle.y, width - 2 * offset, rectangle.height));
            } else if (mapObject.getName().equals("stop")) {
                String type = mapObject.getProperties().get("type", String.class);
                stopOnLadders.add(new Stop(type, rectangle));
            }
        }
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
            ziplines.put(key, new ArrayList<>(sortedVectors));
        });
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
            enemyMovement.put(key, new ArrayList<>(sortedVectors));
        });
    }

    private void parseWater(MapObject mapObject) {
        if (mapObject.getName() != null && mapObject.getName().equals("water")) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            waters.add(rectangle);
        }
    }

    private ObjectData objectData(MapObject mapObject) {
        int height = ((TiledMapTileMapObject) mapObject).getTile().getTextureRegion().getRegionHeight();
        int width = ((TiledMapTileMapObject) mapObject).getTile().getTextureRegion().getRegionWidth();
        int life = mapObject.getProperties().get("life", Integer.class);
        int speed = mapObject.getProperties().get("speed", Integer.class);
        int speedOnLadder = mapObject.getProperties().get("speedOnLadder", Integer.class);
        int speedOnZipLine = mapObject.getProperties().get("speedOnZipline", Integer.class);
        float positionX = ((TiledMapTileMapObject) mapObject).getX();
        float positionY = ((TiledMapTileMapObject) mapObject).getY();
        String awardString = mapObject.getProperties().get("award", String.class);
        float award = awardString.isEmpty() ? 0.0f : Float.parseFloat(awardString);
        String weaponName = mapObject.getProperties().get("weapon", String.class);
        String lastMove = mapObject.getProperties().get("lastMove", String.class);
        String type = mapObject.getProperties().get("type", String.class);
        String group = mapObject.getProperties().get("group", String.class);

        BodyUserData bodyUserData = new BodyUserData(group.equals("Items") ? group.toLowerCase() : mapObject.getName());
        String sensors = mapObject.getProperties().get("sensors", String.class);
        if (sensors != null) {
            String[] sensorArray = Arrays.stream(sensors.split(","))
                .map(sensor -> mapObject.getName() + "-" + sensor)
                .toArray(String[]::new);
            bodyUserData.setSensors(sensorArray);
        }
        Body body = bodyHelper.body(bodyUserData, width, height, positionX, positionY, false);
        Rectangle bounds = new Rectangle(positionX, positionY, width, height);
        Weapon weapon = mapObject.getName().isEmpty() ? null : new Weapon(weaponName, 36, 43, 50, 650, 45);
        return new ObjectData(body, bounds, weapon, height, width, life, speed, speedOnLadder, speedOnZipLine, award, mapObject.getName(), type, group, lastMove);
    }
}
