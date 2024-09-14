package mister3551.msr.game.map;

import com.badlogic.gdx.maps.MapGroupLayer;
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
import mister3551.msr.game.Static;
import mister3551.msr.game.characters.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TiledMapHelper {

    private final BodyHelper bodyHelper;

    public TiledMapHelper(World world) {
        this.bodyHelper = new BodyHelper(world);
    }

    public OrthogonalTiledMapRenderer setupMap() {
        TiledMap tiledMap = new TmxMapLoader().load("maps/training.tmx");

        MapGroupLayer components = (MapGroupLayer) tiledMap.getLayers().get("Components");
        MapObjects mapObjects = tiledMap.getLayers().get("Objects").getObjects();
        MapObjects characters = tiledMap.getLayers().get("Characters").getObjects();
        MapObjects ladders = components.getLayers().get("Ladder").getObjects();
        MapObjects ziplines = components.getLayers().get("Zipline").getObjects();
        MapObjects water = components.getLayers().get("Water").getObjects();

        parseMapObject(mapObjects);
        parseCharacters(characters);
        parseLadders(ladders);
        parseZipline(ziplines);
        parseWater(water);
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    private void parseMapObject(MapObjects mapObjects) {

        for (MapObject mapObject : mapObjects) {
            if (mapObject instanceof PolygonMapObject) {
                bodyHelper.staticBody((PolygonMapObject) mapObject);
            }
        }
    }

    private void parseCharacters(MapObjects mapObjects) {
        for (MapObject mapObject : mapObjects) {
            if (mapObject instanceof TiledMapTileMapObject) {
                if (mapObject.getName() != null && mapObject.getName().equals("Player")) {
                    ObjectData objectData = objectData(mapObject);

                    Body body = bodyHelper.body(objectData.getWidth(), objectData.getHeight(), objectData.getPositionX(), objectData.getPositionY(), false);
                    body.setUserData(mapObject.getName());
                    Rectangle rectangle = new Rectangle(objectData.getPositionX(), objectData.getPositionY(), objectData.getWidth(), objectData.getHeight());
                    Static.setPlayer(new Player(body, rectangle, objectData));
                }
            }
        }
    }

    private void parseLadders(MapObjects mapObjects) {
        for (MapObject mapObject : mapObjects) {
            if (mapObject.getName() != null && mapObject.getName().equals("Ladder")) {
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

                float offset = 30;
                float width = rectangle.getWidth() / 2;
                float x = rectangle.getX() + (rectangle.getWidth() - width) / 2;
                Static.getLadders().add(new Rectangle(x + offset, rectangle.y, width - 2 * offset, rectangle.height));
            } else if (mapObject.getName() != null && mapObject.getName().equals("Stop")) {
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
                Static.getStopOnLadders().add(rectangle);
            }
        }
    }

    private void parseZipline(MapObjects mapObjects) {
        Map<String, List<Vector2>> temporaryMap = new HashMap<>();
        Map<String, List<Integer>> sortKeysMap = new HashMap<>();

        for (MapObject mapObject : mapObjects) {
            String name = mapObject.getName();
            if (name != null && name.startsWith("Zipline")) {
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

            Static.getZiplines().put(key, new ArrayList<>(sortedVectors));
        });
    }

    private void parseWater(MapObjects mapObjects) {
        for (MapObject mapObject : mapObjects) {
            if (mapObject.getName() != null && mapObject.getName().equals("Water")) {
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
                Static.getWaters().add(rectangle);
            }
        }
    }

    private ObjectData objectData(MapObject mapObject) {
        int height = ((TiledMapTileMapObject) mapObject).getTile().getTextureRegion().getRegionHeight();
        int width = ((TiledMapTileMapObject) mapObject).getTile().getTextureRegion().getRegionWidth();
        float positionX = ((TiledMapTileMapObject) mapObject).getX();
        float positionY = ((TiledMapTileMapObject) mapObject).getY();
        return new ObjectData(height, width, positionX, positionY);
    }
}
