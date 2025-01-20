package eu.mister3551.msr.map;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.map.character.Player;
import eu.mister3551.msr.screen.GameScreen;
import eu.mister3551.msr.screen.GameState;

public class Distance {

    public String calculate(Player player) {
        Vector2 playerPosition = player.getBody().getPosition();
        Rectangle endRectangle = GameScreen.doors.stream()
            .filter(object -> "end".equals(object.getName()))
            .map(object -> (RectangleMapObject) object)
            .findFirst()
            .map(RectangleMapObject::getRectangle)
            .orElse(null);

        if (endRectangle == null) {
            return "Not visible";
        }

        float distance = new Vector2(Converter.coordinates(endRectangle)).dst(playerPosition);
        distance = Math.round(distance * 10) / 10f;
        int integerPart = (int) distance;
        int decimalPart = (int) ((distance - integerPart) * 10);
        return integerPart + "." + (decimalPart + "0") + " m";
    }

    public double calculateProgress(Player player, GameState gameState) {
        Vector2 playerPosition = player.getBody().getPosition();

        Rectangle startRectangle = GameScreen.doors.stream()
            .filter(object -> "start".equals(object.getName()))
            .map(object -> (RectangleMapObject) object)
            .findFirst()
            .map(RectangleMapObject::getRectangle)
            .orElse(null);

        Rectangle endRectangle = GameScreen.doors.stream()
            .filter(object -> "end".equals(object.getName()))
            .map(object -> (RectangleMapObject) object)
            .findFirst()
            .map(RectangleMapObject::getRectangle)
            .orElse(null);

        if (startRectangle == null || endRectangle == null) {
            return 0;
        }

        float totalDistance = new Vector2(Converter.coordinates(endRectangle)).dst(Converter.coordinates(startRectangle));
        float traveledDistance = new Vector2(playerPosition).dst(Converter.coordinates(startRectangle));
        double progress = Math.round((traveledDistance / totalDistance * 100));

        return (gameState.getEnemies().isEmpty() && gameState.getHostages().isEmpty() && progress >= 98 && progress <= 102) ? 100 : progress;
    }
}
