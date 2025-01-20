package eu.mister3551.msr.map.character.collision;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.map.character.Character;
import eu.mister3551.msr.map.character.movement.MovementCollision;
import eu.mister3551.msr.map.character.movement.Zipline;
import eu.mister3551.msr.screen.GameScreen;

import java.util.ArrayList;
import java.util.HashMap;

public class Collision {

    public MovementCollision check(Character character) {
        return new MovementCollision(
            ladder(character),
            stopOnLadder(character),
            water(character),
            zipline(character),
            door(character)
        );
    }

    private boolean ladder(Character character) {
        return GameScreen.ladders.stream().anyMatch(character.getBounds()::overlaps);
    }

    private boolean stopOnLadder(Character character) {
        float offset = 2;
        return GameScreen.stopOnLadders.stream().anyMatch(rectangle -> character.getBounds().overlaps(rectangle)
            || (character.getBounds().y <= rectangle.y + rectangle.height + offset && character.getBounds().y >= rectangle.y + rectangle.height - offset) &&
            (character.getBounds().x < rectangle.x + rectangle.width && character.getBounds().x + character.getBounds().width > rectangle.x));
    }

    private boolean water(Character character) {
        return GameScreen.waters.stream().anyMatch(character.getBounds()::overlaps);
    }

    private Zipline zipline(Character character) {
        HashMap<String, ArrayList<Vector2>> allPoints = GameScreen.ziplines;
        float threshold = 1;

        return allPoints.entrySet().stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .filter(entry -> character.getBody().getPosition().dst(entry.getValue().get(0)) < threshold)
            .findFirst()
            .map(entry -> new Zipline(entry.getValue(), true))
            .orElse(null);
    }

    private MapObject door(Character character) {
        return GameScreen.doors.stream()
            .filter(mapObject -> character.getBounds().overlaps(((RectangleMapObject) mapObject).getRectangle()))
            .findFirst()
            .orElse(null);
    }
}
