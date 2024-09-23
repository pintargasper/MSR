package mister3551.msr.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import mister3551.msr.game.characters.object.Player;
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
    private final Texture shootTexture;
    private final Texture reloadTexture;

    private boolean moveLeftPressed;
    private boolean moveRightPressed;
    private boolean moveUpPressed;
    private boolean moveDownPressed;
    private boolean moveJumpPressed;
    private boolean moveShootPressed;

    private final Image upImage;
    private final Image downImage;
    private final Image leftImage;
    private final Image rightImage;
    private final Image jumpImage;
    private final Image shootImage;

    private final TextureRegionDrawable shootDrawable;
    private final TextureRegionDrawable reloadDrawable;

    public Mobile(Body body, Player player) {
        super(body, player);
        this.viewport = new ExtendViewport(800, 480);
        this.stage = new Stage(viewport);

        this.upTexture = new Texture("controls/up.png");
        this.downTexture = new Texture("controls/down.png");
        this.leftTexture = new Texture("controls/left.png");
        this.rightTexture = new Texture("controls/right.png");
        this.shootTexture = new Texture("controls/shoot.png");
        this.reloadTexture = new Texture("controls/reload.png");

        this.upImage = new Image(upTexture);
        this.downImage = new Image(downTexture);
        this.leftImage = new Image(leftTexture);
        this.rightImage = new Image(rightTexture);
        this.jumpImage = new Image(upTexture);
        this.shootImage = new Image(shootTexture);

        this.shootDrawable = new TextureRegionDrawable(new TextureRegion(shootTexture));
        this.reloadDrawable = new TextureRegionDrawable(new TextureRegion(reloadTexture));

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
        shootImage.setSize(controlSize, controlSize);

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
        rightControls.add(shootImage).size(controlSize, controlSize).padBottom(100).padRight(30);
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
        addControlListener(shootImage, "shoot");
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
        shootTexture.dispose();
        reloadTexture.dispose();
    }

    @Override
    public void inputs(float delta, boolean ladderCollision, boolean stopOnLadder, boolean waterCollision, Zipline zipLine) {
        if (zipLine != null && getZipline() == null && zipLine.isZiplineCollision()) {
            setZipline(zipLine);
        }

        Zipline currentZipline = getZipline();
        if (currentZipline != null) {
            setVisibility(false, false, false, false, false, false);
            onZipline(currentZipline.getPoints());
        } else {
            walking(delta, ladderCollision, stopOnLadder, waterCollision);
        }
    }

    @Override
    public void walking(float delta, boolean ladderCollision, boolean stopOnLadder, boolean watterCollision) {
        player.setVelocityX(0);
        player.setVelocityY(0);

        if (moveLeftPressed && !player.isOnLeftSide()) {
            player.setCurrentAnimation(watterCollision ? player.getCharacterAnimation().getSwimLeft() : player.isOnFloor() ? player.getCharacterAnimation().getWalkLeft() : player.getCharacterAnimation().getZiplineLeft());
            player.setLastMove("left");
            player.setVelocityX(-1);
        } else if (moveRightPressed && !player.isOnRightSide()) {
            player.setCurrentAnimation(watterCollision ? player.getCharacterAnimation().getSwimRight() : player.isOnFloor() ? player.getCharacterAnimation().getWalkRight() : player.getCharacterAnimation().getZiplineRight());
            player.setLastMove("right");
            player.setVelocityX(1);
        } else if (player.isOnFloor()) {
            if (!watterCollision) {
                player.setCurrentAnimation(player.getLastMove().equals("left") ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
            } else {
                player.setCurrentAnimation(player.getLastMove().equals("left") ? player.getCharacterAnimation().getSwimLeft() : player.getCharacterAnimation().getSwimRight());
            }
        }

        if (moveUpPressed && ladderCollision) {
            player.setCurrentAnimation(!stopOnLadder ? player.getCharacterAnimation().getClimb() : player.getCharacterAnimation().getStanding());
            player.setVelocityY(player.getSpeedOnLadder());
        } else if (moveDownPressed && ladderCollision) {
            player.setCurrentAnimation(player.getCharacterAnimation().getClimb());
            player.setVelocityY(-player.getSpeedOnLadder());
        } else if (!stopOnLadder && ladderCollision) {
            player.setCurrentAnimation(player.getCharacterAnimation().getStandingOnLadder());
        }

        if (player.isOnFloor()) {
            player.setJumps(0);

            if (player.getVelocityX() == 0 && stopOnLadder) {
                player.setCurrentAnimation(player.getLastMove().equals("left") ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
            }
        }

        if (ladderCollision && player.getVelocityY() == 0 && !player.isOnFloor()) {
            player.setCurrentAnimation(!stopOnLadder ? player.getCharacterAnimation().getStandingOnLadder() : player.getLastMove().equals("left") ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
        }

        if (moveJumpPressed && player.getJumps() < 1 && player.isOnFloor() && !ladderCollision) {
            jump();
            body.setGravityScale(1);
        }

        if (ladderCollision) {
            body.setGravityScale(0);
            body.setLinearVelocity(player.getVelocityX() * player.getSpeed(), player.getVelocityY());
            setVisibility(true, true, true, true, false, player.isBodyOnFloor() || stopOnLadder);
        } else {
            body.setGravityScale(1);
            body.setLinearVelocity(player.getVelocityX() * player.getSpeed(), Math.min(body.getLinearVelocity().y, 25));
            setVisibility(false, false, true, true, player.isOnFloor(),
                !watterCollision
                    && (player.isOnFloor()
                    || (player.getCurrentAnimation() != player.getCharacterAnimation().getZiplineLeft()
                    && player.getCurrentAnimation() != player.getCharacterAnimation().getZiplineRight()
                    && player.getCurrentAnimation() != player.getCharacterAnimation().getSwimLeft()
                    && player.getCurrentAnimation() != player.getCharacterAnimation().getSwimRight())
                )
            );
        }

        if (moveShootPressed && shots < player.getWeapon().getMagazineCapacity() && isShooting) {
            shots += player.getOnShoot().shoot(player, delta);
        }

        if (shots >= player.getWeapon().getMagazineCapacity() && !shootImage.getDrawable().equals(reloadDrawable) && shots != 0) {
            isShooting = false;
            shootImage.setDrawable(reloadDrawable);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    isShooting = true;
                    shots = 0;
                    shootImage.setDrawable(shootDrawable);
                }
            }, 3);
        }
    }

    @Override
    public void jump() {
        body.setLinearVelocity(body.getLinearVelocity().x, 0);
        body.applyLinearImpulse(new Vector2(0, body.getMass() * 12), body.getPosition(), true);
        player.setJumps(player.getJumps() + 1);
    }

    @Override
    public void onZipline(ArrayList<Vector2> points) {
        zipline = OnZipline.movement(body, player, zipline, points, player.getSpeedOnZipline());
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
                    case "shoot": moveShootPressed = true; break;
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
                    case "shoot": moveShootPressed = false; break;
                }
            }
        });
    }

    private void setVisibility(boolean up, boolean down, boolean left, boolean right, boolean jump, boolean shoot) {
        upImage.setVisible(up);
        downImage.setVisible(down);
        leftImage.setVisible(left);
        rightImage.setVisible(right);
        jumpImage.setVisible(jump);
        shootImage.setVisible(shoot);
    }
}
