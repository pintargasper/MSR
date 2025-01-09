package eu.mister3551.msr.map;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MapData {

    public final OrthogonalTiledMapRenderer renderer;
    public final float light;

    public MapData(OrthogonalTiledMapRenderer renderer, float light) {
        this.renderer = renderer;
        this.light = light;
    }
}
