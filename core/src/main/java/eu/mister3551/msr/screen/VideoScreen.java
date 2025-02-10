package eu.mister3551.msr.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;
import eu.mister3551.msr.screen.link.Callback;

import java.io.FileNotFoundException;

public class VideoScreen implements Screen {

    private final VideoPlayer videoPlayer;
    private final SpriteBatch spriteBatch;

    public VideoScreen() {
        this.videoPlayer = VideoPlayerCreator.createVideoPlayer();
        this.spriteBatch = new SpriteBatch();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (videoPlayer != null) {
            videoPlayer.update();
        }

        spriteBatch.begin();
        Texture texture = (videoPlayer != null) ? videoPlayer.getTexture() : null;
        if (texture != null) {
            spriteBatch.draw(texture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        videoPlayer.dispose();
        spriteBatch.dispose();
    }

    public void playVideo(Callback callback) {

        videoPlayer.setOnCompletionListener(new VideoPlayer.CompletionListener() {
            @Override
            public void onCompletionListener(FileHandle file) {
                callback.onSuccess(true);
            }
        });

        try {
            FileHandle videoFile = Gdx.files.internal("videos/Intro.webm");
            videoPlayer.load(videoFile);
            videoPlayer.play();
        } catch (FileNotFoundException fileNotFoundException) {
            callback.onError("Error playing video: " + fileNotFoundException.getMessage());
        }

    }
}
