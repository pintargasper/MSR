package mister3551.msr.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import mister3551.msr.game.controls.zipline.OnZipline;
import mister3551.msr.game.controls.zipline.Zipline;

import java.util.ArrayList;

public class Mobile extends Device {

    private final Viewport viewport;
    private final Stage stage;

    private final Texture upTexture;
    private final Texture downTexture;
    private final Texture leftTexture;
    private final Texture rightTexture;

    private boolean moveLeftPressed;
    private boolean moveRightPressed;
    private boolean moveUpPressed;
    private boolean moveDownPressed;
    private boolean moveJumpPressed;

    private final Image upImage;
    private final Image downImage;
    private final Image leftImage;
    private final Image rightImage;
    private final Image jumpImage;

    public Mobile(Body body, float speed, float speedOnLadder, float speedOnZipline) {
        super(body, speed, speedOnLadder, speedOnZipline);
        this.viewport = new ExtendViewport(800, 480);
        this.stage = new Stage(viewport);

        this.upTexture = new Texture("controls/up.png");
        this.downTexture = new Texture("controls/down.png");
        this.leftTexture = new Texture("controls/left.png");
        this.rightTexture = new Texture("controls/right.png");

        this.upImage = new Image(upTexture);
        this.downImage = new Image(downTexture);
        this.leftImage = new Image(leftTexture);
        this.rightImage = new Image(rightTexture);
        this.jumpImage = new Image(upTexture);

        upImage.setVisible(false);
        downImage.setVisible(false);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Table controls = new Table();
        controls.left().bottom();

        float controlSize = 80f;
        upImage.setSize(controlSize, controlSize);
        downImage.setSize(controlSize, controlSize);
        leftImage.setSize(controlSize, controlSize);
        rightImage.setSize(controlSize, controlSize);
        jumpImage.setSize(controlSize, controlSize);

        controls.add();
        controls.add(upImage).size(controlSize, controlSize);
        controls.add();
        controls.row().pad(5);
        controls.add(leftImage).size(controlSize, controlSize);
        controls.add();
        controls.add(rightImage).size(controlSize, controlSize);
        controls.row().padBottom(5);
        controls.add();
        controls.add(downImage).size(controlSize, controlSize);
        controls.add();
        controls.row().padBottom(5);
        controls.add();

        Table rightControls = new Table();
        rightControls.right().bottom();
        rightControls.add(jumpImage).size(controlSize, controlSize).padBottom(100).padRight(10);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.add(controls).expand().fill();
        mainTable.add(rightControls).right().bottom();

        stage.addActor(mainTable);

        addControlListener(upImage, "up");
        addControlListener(downImage, "down");
        addControlListener(leftImage, "left");
        addControlListener(rightImage, "right");
        addControlListener(jumpImage, "jump");
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        upTexture.dispose();
        downTexture.dispose();
        leftTexture.dispose();
        rightTexture.dispose();
    }

    @Override
    public void inputs(boolean ladderCollision, Zipline zipLine) {
        if (zipLine != null && getZipline() == null && zipLine.isZiplineCollision()) {
            setZipline(zipLine);
        }

        if (getZipline() != null) {
            onZipLine(getZipline().getPoints());
            setVisibility(false, false, false, false, false);
        } else {
            if (ladderCollision) {
                onLadder();
                setVisibility(true, true, true, true, false);
            } else {
                body.setGravityScale(1);
                jump();
                setVisibility(false, false, true, true, true);
            }
            normal();

            if (body.getLinearVelocity().y == 0) {
                jumps = 0;
            }
        }
    }

    @Override
    public void normal() {
        velocityX = 0;

        if (moveRightPressed) {
            velocityX++;
        }

        if (moveLeftPressed) {
            velocityX--;
        }
        body.setLinearVelocity(velocityX * speed, Math.min(body.getLinearVelocity().y, 25));
    }

    @Override
    public void jump() {
        if (moveJumpPressed && jumps < 1) {
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(new Vector2(0, body.getMass() * 12), body.getPosition(), true);
            jumps++;
        }
    }

    @Override
    public void onLadder() {
        velocityY = 0;
        body.setGravityScale(0);

        if (moveUpPressed) {
            velocityY++;
        }

        if (moveDownPressed) {
            velocityY--;
        }
        body.setLinearVelocity(velocityX * speedOnLadder, velocityY * speedOnLadder);
    }

    @Override
    public void onZipLine(ArrayList<Vector2> points) {
        zipline = OnZipline.movement(body, zipline, points, speedOnZipLine);
    }

    private void addControlListener(Image control, final String direction) {
        control.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                switch (direction) {
                    case "up": moveUpPressed = true; break;
                    case "down": moveDownPressed = true; break;
                    case "left": moveLeftPressed = true; break;
                    case "right": moveRightPressed = true; break;
                    case "jump": moveJumpPressed = true; break;
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                switch (direction) {
                    case "up": moveUpPressed = false; break;
                    case "down": moveDownPressed = false; break;
                    case "left": moveLeftPressed = false; break;
                    case "right": moveRightPressed = false; break;
                    case "jump": moveJumpPressed = false; break;
                }
            }
        });
    }

    private void setVisibility(boolean up, boolean down, boolean left, boolean right, boolean jump) {
        upImage.setVisible(up);
        downImage.setVisible(down);
        leftImage.setVisible(left);
        rightImage.setVisible(right);
        jumpImage.setVisible(jump);
    }
}
