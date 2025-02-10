package eu.mister3551.msr.map.character.control;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eu.mister3551.msr.database.object.Options;
import eu.mister3551.msr.map.character.Player;
import eu.mister3551.msr.map.character.movement.Zipline;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Device {

    protected final Player player;
    protected final Options options;
    protected Zipline zipline;
    protected boolean shooting;
    protected boolean reloading;

    public Device(Player player, Options options) {
        this.player = player;
        this.options = options;
        this.shooting = true;
    }

    public abstract void show();
    public abstract void render(SpriteBatch spriteBatch, float delta);
    public abstract void update(float delta);
    public abstract void resize(int width, int height);
    public abstract void dispose();
}
