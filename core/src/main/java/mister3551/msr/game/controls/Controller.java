package mister3551.msr.game.controls;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import mister3551.msr.game.characters.object.Player;
import mister3551.msr.game.controls.movement.OnZipline;
import mister3551.msr.game.controls.movement.Zipline;
import mister3551.msr.game.database.object.Options;

import java.util.ArrayList;

public class Controller extends Device implements ControllerListener {

    private com.badlogic.gdx.controllers.Controller controller;

    public Controller(Body body, Player player, Options options) {
        super(body, player, options);
        Controllers.addListener(this);
    }

    @Override
    public void show() {
        if (controller == null) {
            if (Controllers.getControllers().size > 0) {
                controller = Controllers.getControllers().first();
            }
        }

        if (controller != null) {
            controller.addListener(this);
        }
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {
        if (controller != null) {
            controller.removeListener(this);
        }
        Controllers.removeListener(this);
    }

    @Override
    public void inputs(float delta, boolean ladderCollision, boolean stopOnLadder, boolean watterCollision, Zipline zipLine) {
        if (zipLine != null && getZipline() == null && zipLine.isZiplineCollision()) {
            setZipline(zipLine);
        }

        Zipline currentZipline = getZipline();
        if (currentZipline != null) {
            onZipline(currentZipline.getPoints());
        } else {
            walking(delta, ladderCollision, stopOnLadder, watterCollision);
        }
    }

    @Override
    public void walking(float delta, boolean ladderCollision, boolean stopOnLadder, boolean watterCollision) {
        player.setVelocityX(0);
        player.setVelocityY(0);

        if (controller != null) {
            if (controller.getAxis(0) < -0.8f && !player.isOnLeftSide()) {
                player.setCurrentAnimation(watterCollision ? player.getCharacterAnimation().getSwimLeft() : player.isOnFloor() ? player.getCharacterAnimation().getWalkLeft() :  player.getCharacterAnimation().getJumpLeft());
                player.setLastMove("left");
                player.setVelocityX(-1);
            } else if (controller.getAxis(0) > 0.8f && !player.isOnRightSide()) {
                player.setCurrentAnimation(watterCollision ? player.getCharacterAnimation().getSwimRight() : player.isOnFloor() ? player.getCharacterAnimation().getWalkRight() :  player.getCharacterAnimation().getJumpRight());
                player.setLastMove("right");
                player.setVelocityX(1);
            } else if (player.isOnFloor()) {
                if (!watterCollision) {
                    player.setCurrentAnimation(player.getLastMove().equals("left") ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
                } else {
                    player.setCurrentAnimation(player.getLastMove().equals("left") ? player.getCharacterAnimation().getSwimLeft() : player.getCharacterAnimation().getSwimRight());
                }
            }

            if (controller.getAxis(1) < -0.8f && ladderCollision) {
                player.setCurrentAnimation(!stopOnLadder ? player.getCharacterAnimation().getClimb() : player.getCharacterAnimation().getStanding());
                player.setVelocityY(player.getSpeedOnLadder());
            } else if (controller.getAxis(1) > 0.8f && ladderCollision) {
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

            if (controller.getButton(0) && player.getJumps() < 1 && player.isOnFloor() && !ladderCollision) {
                jump();
                body.setGravityScale(1);
            }

            if (ladderCollision) {
                body.setGravityScale(0);
                body.setLinearVelocity(player.getVelocityX() * player.getSpeed(), player.getVelocityY());
            } else {
                body.setGravityScale(1);
                body.setLinearVelocity(player.getVelocityX() * player.getSpeed(), Math.min(body.getLinearVelocity().y, 25));
            }

            if ((Gdx.app.getType() == Application.ApplicationType.Desktop ? controller.getAxis(4) > 0.8f : controller.getButton(6)) && player.getWeapon().getActiveMagazineCapacity() != 0 && isShooting) {
                player.getWeapon().setActiveMagazineCapacity(player.getWeapon().getActiveMagazineCapacity() - player.getOnShoot().shoot(player, delta));
            }

            if ((Gdx.app.getType() == Application.ApplicationType.Desktop ? controller.getButton(9) : controller.getButton(4)) && player.getWeapon().getActiveMagazineCapacity() != player.getWeapon().getMagazineCapacity() && player.getWeapon().getBackupMagazinesCapacity() != 0) {
                player.getOnShoot().reload(this, player);
            }
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

    @Override
    public void connected(com.badlogic.gdx.controllers.Controller controller) {
        if (this.controller == null) {
            this.controller = controller;
            this.controller.addListener(this);
        }
    }

    @Override
    public void disconnected(com.badlogic.gdx.controllers.Controller controller) {
        if (this.controller == controller) {
            this.controller.removeListener(this);
            this.controller = null;
        }
    }

    @Override
    public boolean buttonDown(com.badlogic.gdx.controllers.Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean buttonUp(com.badlogic.gdx.controllers.Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(com.badlogic.gdx.controllers.Controller controller, int axisCode, float value) {
        return false;
    }

    public boolean getConnected() {
        if (controller != null && controller.isConnected()) {
            return true;
        } else {
            controller = null;
            show();
            return false;
        }
    }
}
