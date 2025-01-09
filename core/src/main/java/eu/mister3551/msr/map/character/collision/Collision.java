package eu.mister3551.msr.map.character.collision;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.map.character.Character;
import eu.mister3551.msr.map.character.movement.Zipline;
import eu.mister3551.msr.screen.GameScreen;

import java.util.ArrayList;
import java.util.HashMap;

public class Collision {

    public eu.mister3551.msr.map.character.movement.Collision check(Character character) {
        return new eu.mister3551.msr.map.character.movement.Collision(
            ladder(character),
            false,
            water(character),
            zipline(character),
            door(character)
        );
    }

    private boolean ladder(Character character) {
        return GameScreen.ladders.stream().anyMatch(character.getBounds()::overlaps);
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
