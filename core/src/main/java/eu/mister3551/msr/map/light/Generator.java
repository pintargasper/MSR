package eu.mister3551.msr.map.light;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import eu.mister3551.msr.Constants;

public class Generator {

    private final RayHandler rayHandler;

    public Generator(RayHandler rayHandler) {
        this.rayHandler = rayHandler;
    }

    public void generate(String mapName) {
        lights(mapName);
    }

    private void lights(String mapName) {
        int rays = setShadowResolution(Constants.options.getShadowsLights());

        for (Light light : Constants.screenChanger.getGameState().getGameStates().get(mapName).getLights()) {
            Rectangle rectangle = light.getBounds();
            float centerX = rectangle.x + rectangle.width / 2f;
            float centerY = rectangle.y + rectangle.height / 2f;

            switch (light.getType()) {
                case "torch":
                    PointLight pointLightTorch = new PointLight(rayHandler, rays, Color.CORAL, light.getDistance(), centerX / Constants.PPM, centerY / Constants.PPM);
                    pointLightTorch.setSoftnessLength(light.getSoftness());
                    break;
                case "lamp":
                    ConeLight coneLight = new ConeLight(rayHandler, rays, Color.CORAL, light.getDistance(), centerX, centerY, -90, 30);
                    coneLight.setSoftnessLength(light.getSoftness());
                    break;
                case "sun":
                    PointLight pointLightSun = new PointLight(rayHandler, rays, Color.WHITE, light.getDistance(), centerX / Constants.PPM, centerY / Constants.PPM);
                    pointLightSun.setSoftnessLength(light.getSoftness());
                    break;
            }
        }
    }

    private int setShadowResolution(String resolution) {
        int rays = 4;
        switch (resolution) {
            case "Ultra low":
                rays = 20;
                break;
            case "Low":
                rays = 70;
                break;
            case "Normal":
                rays = 2000;
                break;
            case "High":
                rays = 10000;
                break;
            case "Ultra high":
                rays = 20000;
                break;
        }
        return rays;
    }
}
