package eu.mister3551.msr.screen.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.map.character.Player;
import lombok.Getter;

@Getter
public class Camera {

    private final OrthographicCamera orthographicCamera;

    public Camera() {
        this.orthographicCamera = new OrthographicCamera();
    }

    public void update(Player player) {
        float x = player.getBody().getPosition().x * Constants.PPM;
        float y = player.getBody().getPosition().y * Constants.PPM;

        orthographicCamera.position.set(x, y, 0);
        orthographicCamera.update();
    }

    public void setZoom(float amount) {
        orthographicCamera.zoom = amount;
    }

    public boolean isVisible(Vector2 position) {
        Vector3 vector3 = new Vector3(position.x * Constants.PPM, position.y * Constants.PPM, 0);

        Vector3 cameraBottomLeft = new Vector3(orthographicCamera.position.x - orthographicCamera.viewportWidth / 2 * orthographicCamera.zoom,
            orthographicCamera.position.y - orthographicCamera.viewportHeight / 2 * orthographicCamera.zoom, 0);
        Vector3 cameraTopRight = new Vector3(orthographicCamera.position.x + orthographicCamera.viewportWidth / 2 * orthographicCamera.zoom,
            orthographicCamera.position.y + orthographicCamera.viewportHeight / 2 * orthographicCamera.zoom, 0);

        return (vector3.x > cameraBottomLeft.x && vector3.x < cameraTopRight.x) &&
            (vector3.y > cameraBottomLeft.y && vector3.y < cameraTopRight.y);
    }
}
