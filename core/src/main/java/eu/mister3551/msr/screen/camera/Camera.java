package eu.mister3551.msr.screen.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import eu.mister3551.msr.Static;
import eu.mister3551.msr.map.character.Player;
import lombok.Data;

@Data
public class Camera {

    private final OrthographicCamera camera;

    public Camera() {
        camera = new OrthographicCamera();
    }

    public void update(Player player) {
        float x = player.getBody().getPosition().x * Static.PPM;
        float y = player.getBody().getPosition().y * Static.PPM;

        camera.position.set(x, y, 0);
        camera.update();
    }

    public void setZoom(float amount) {
        camera.zoom = amount;
    }

    public boolean isVisible(Vector2 position) {
        Vector3 enemyPosition = new Vector3(position.x * Static.PPM, position.y * Static.PPM, 0);

        Vector3 cameraBottomLeft = new Vector3(camera.position.x - camera.viewportWidth / 2 * camera.zoom,
            camera.position.y - camera.viewportHeight / 2 * camera.zoom, 0);
        Vector3 cameraTopRight = new Vector3(camera.position.x + camera.viewportWidth / 2 * camera.zoom,
            camera.position.y + camera.viewportHeight / 2 * camera.zoom, 0);

        return (enemyPosition.x > cameraBottomLeft.x && enemyPosition.x < cameraTopRight.x) &&
            (enemyPosition.y > cameraBottomLeft.y && enemyPosition.y < cameraTopRight.y);
    }
}
