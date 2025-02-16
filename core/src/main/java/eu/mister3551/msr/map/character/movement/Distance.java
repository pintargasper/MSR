package eu.mister3551.msr.map.character.movement;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.map.Converter;
import eu.mister3551.msr.map.character.Player;

import java.util.List;
import java.util.stream.Collectors;

public class Distance {

    public String calculate(Player player) {
        Vector2 playerPosition = player.getBody().getPosition();
        List<RectangleMapObject> doors = Constants.screenChanger.getGameState()
            .getGameStates()
            .get(Constants.gameScreen.getMission().getMap())
            .getDoors()
            .stream()
            .map(object -> (RectangleMapObject) object)
            .collect(Collectors.toList());

        Rectangle targetRectangle = doors.stream()
            .filter(object -> "end".equals(object.getName()))
            .map(RectangleMapObject::getRectangle)
            .findFirst()
            .orElseGet(() -> doors.stream()
                .filter(object -> "exit".equals(object.getName()))
                .map(RectangleMapObject::getRectangle)
                .findFirst()
                .orElse(null));

        if (targetRectangle == null) {
            return "Not visible";
        }

        float distance = new Vector2(Converter.coordinates(targetRectangle)).dst(playerPosition);
        distance = Math.round(distance * 10) / 10f;
        int integerPart = (int) distance;
        int decimalPart = (int) ((distance - integerPart) * 10);
        return integerPart + "." + decimalPart + " m";
    }

    public double calculateProgress() {
        int main = Constants.screenChanger.getGameState().getGameStates().values().stream()
            .mapToInt(helper -> helper.getEnemies().size() + helper.getHostages().size())
            .sum();
        int backup = Constants.screenChanger.getGameState().getGameStatesBackup().values().stream()
            .mapToInt(helper -> helper.getEnemies().size() + helper.getHostages().size())
            .sum();
        return (backup == 0) ? 0 : (1.0 - ((double) main / backup)) * 100;
    }
}
