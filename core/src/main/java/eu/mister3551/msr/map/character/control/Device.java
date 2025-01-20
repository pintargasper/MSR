package eu.mister3551.msr.map.character.control;

import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.database.object.Options;
import eu.mister3551.msr.map.character.Player;
import eu.mister3551.msr.map.character.movement.MovementCollision;
import eu.mister3551.msr.map.character.movement.Zipline;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public abstract class Device {

    protected final Player player;
    protected final Options options;
    protected Zipline zipline;
    protected boolean isShooting;

    public Device(Player player, Options options) {
        this.player = player;
        this.options = options;
        this.isShooting = true;
    }

    public abstract void show();
    public abstract void render(float delta);
    public abstract void resize(int width, int height);
    public abstract void dispose();
    public abstract void inputs(MovementCollision movementCollision, float delta);
    public abstract void walking(MovementCollision movementCollision, float delta);
    public abstract void jump();
    public abstract void onZipline(ArrayList<Vector2> points);
}
