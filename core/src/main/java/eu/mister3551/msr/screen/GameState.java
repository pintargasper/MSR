package eu.mister3551.msr.screen;

import com.badlogic.gdx.physics.box2d.World;
import eu.mister3551.msr.map.character.Enemy;
import eu.mister3551.msr.map.character.Hostage;
import eu.mister3551.msr.database.object.Mission;
import eu.mister3551.msr.map.character.Item;
import eu.mister3551.msr.map.character.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class GameState {

    private World world;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Hostage> hostages;
    private ArrayList<Item> items;
    private Mission mission;

    public GameState() {
        this.enemies = new ArrayList<>();
        this.hostages = new ArrayList<>();
        this.items = new ArrayList<>();
    }
}
