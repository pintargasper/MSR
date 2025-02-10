package eu.mister3551.msr.map.character;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.map.character.movement.Zipline;
import eu.mister3551.msr.map.object.Stop;

import java.util.ArrayList;
import java.util.HashMap;

public class Collision {

    public MovementCollision check(Character character, String mapName) {
        return new MovementCollision(
            door(character, mapName),
            zipline(character, mapName),
            ladder(character, mapName),
            stopOnTopOfLadder(character, mapName),
            stopOnBottomOfLadder(character, mapName),
            water(character, mapName)
        );
    }

    private MapObject door(Character character, String mapName) {
        return Constants.screenChanger.getGameState().getGameStates().get(mapName).getDoors().stream()
            .filter(mapObject -> character.getBounds().overlaps(((RectangleMapObject) mapObject).getRectangle()))
            .findFirst()
            .orElse(null);
    }

    private Zipline zipline(Character character, String mapName) {
        HashMap<String, ArrayList<Vector2>> allPoints = Constants.screenChanger.getGameState().getGameStates().get(mapName).getZiplines();
        float threshold = 1;

        return allPoints.entrySet().stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .filter(entry -> character.getBody().getPosition().dst(entry.getValue().get(0)) < threshold)
            .findFirst()
            .map(entry -> new Zipline(entry.getValue(), true))
            .orElse(null);
    }

    private boolean ladder(Character character, String mapName) {
        return Constants.screenChanger.getGameState().getGameStates().get(mapName).getLadders().stream().anyMatch(character.getBounds()::overlaps);
    }

    private boolean stopOnTopOfLadder(Character character, String mapName) {
        float offset = 2;
        return Constants.screenChanger.getGameState().getGameStates().get(mapName)
            .getStopOnLadders().stream()
            .filter(stop -> "top".equals(stop.getType()))
            .map(Stop::getBounds)
            .anyMatch(rectangle -> character.getBounds().overlaps(rectangle)
                || (character.getBounds().y <= rectangle.y + rectangle.height + offset
                && character.getBounds().y >= rectangle.y + rectangle.height - offset)
                && (character.getBounds().x < rectangle.x + rectangle.width
                && character.getBounds().x + character.getBounds().width > rectangle.x));
    }

    private boolean stopOnBottomOfLadder(Character character, String mapName) {
        float offset = 2;
        return Constants.screenChanger.getGameState().getGameStates().get(mapName)
            .getStopOnLadders().stream()
            .filter(stop -> "bottom".equals(stop.getType()))
            .map(Stop::getBounds)
            .anyMatch(rectangle -> character.getBounds().overlaps(rectangle)
                || (character.getBounds().y <= rectangle.y + rectangle.height + offset
                && character.getBounds().y >= rectangle.y + rectangle.height - offset)
                && (character.getBounds().x < rectangle.x + rectangle.width
                && character.getBounds().x + character.getBounds().width > rectangle.x));
    }

    private boolean water(Character character, String mapName) {
        return Constants.screenChanger.getGameState().getGameStates().get(mapName).getWaters().stream().anyMatch(character.getBounds()::overlaps);
    }
}
