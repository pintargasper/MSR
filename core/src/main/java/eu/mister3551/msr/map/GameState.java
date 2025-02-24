package eu.mister3551.msr.map;

import eu.mister3551.msr.database.object.Mission;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

@Getter
public class GameState {

    LinkedHashMap<String, Helper> gameStates;

    public GameState(Mission mission) {
        this.gameStates = new LinkedHashMap<>();

        String missionName = mission.getName().toLowerCase();

        ArrayList<String> maps = new ArrayList<>();
        maps.add(mission.getMap());
        Collections.addAll(maps, mission.getMaps().split(","));

        for (String map : maps) {
            gameStates.put(map, new Helper().setUp(missionName, map));
        }
    }
}
