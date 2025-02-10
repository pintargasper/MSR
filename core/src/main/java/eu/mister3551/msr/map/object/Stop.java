package eu.mister3551.msr.map.object;

import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;

@Getter
public class Stop {

    private final String type;
    private final Rectangle bounds;

    public Stop(String type, Rectangle bounds) {
        this.type = type;
        this.bounds = bounds;
    }
}
