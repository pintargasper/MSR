package eu.mister3551.msr.map.character.control;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.database.object.Mission;
import eu.mister3551.msr.database.object.Options;
import eu.mister3551.msr.map.character.Character;
import eu.mister3551.msr.map.character.Player;
import eu.mister3551.msr.map.character.movement.OnZipline;
import eu.mister3551.msr.map.character.movement.Zipline;
import eu.mister3551.msr.screen.GameScreen;

public class Controller extends Device implements ControllerListener {

    private com.badlogic.gdx.controllers.Controller controller;
    private boolean buttonPressed;

    public Controller(Player player, Options options) {
        super(player, options);
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
    public void render(SpriteBatch spriteBatch, float delta) {

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
    public void update(float delta) {
        if (controller == null) {
            return;
        }

        Zipline zipline = player.getMovementCollision().getZipline();
        if (zipline != null && this.zipline == null && zipline.isZiplineCollision()) {
            this.zipline = zipline;
        }

        Zipline currentZipline = getZipline();
        if (currentZipline != null) {
            this.zipline = OnZipline.movement(player, currentZipline);
            return;
        }

        player.setVelocityX(0);
        player.setVelocityY(0);

        if (controller.getAxis(0) < -0.8f && !player.isLeftSide()) {
            if (player.getMovementCollision().isWater()) {
                player.setCurrentAnimation(player.getCharacterAnimation().getSwimLeft());
            } else {
                player.setCurrentAnimation(player.getCharacterAnimation().getWalkLeft());
            }
            player.setLastMove(Character.LastMove.LEFT);
            player.setVelocityX(-1);
        } else if (controller.getAxis(0) > 0.8f && !player.isRightSide()) {
            if (player.getMovementCollision().isWater()) {
                player.setCurrentAnimation(player.getCharacterAnimation().getSwimRight());
            } else {
                player.setCurrentAnimation(player.getCharacterAnimation().getWalkRight());
            }
            player.setLastMove(Character.LastMove.RIGHT);
            player.setVelocityX(1);
        } else {
            if (player.getMovementCollision().isWater()) {
                player.setCurrentAnimation(player.getLastMove().equals(Character.LastMove.LEFT)
                    ? player.getCharacterAnimation().getSwimLeft()
                    : player.getCharacterAnimation().getSwimRight());
            } else {
                player.setCurrentAnimation(player.getLastMove().equals(Character.LastMove.LEFT)
                    ? player.getCharacterAnimation().getStandLeft()
                    : player.getCharacterAnimation().getStandRight());
            }
        }

        if (controller.getButton(0)
            && !player.isJumping()
            && !player.getMovementCollision().isWater()) {
            jump();
        }

        if (player.isJumping()) {
            player.setCurrentAnimation(player.getLastMove().equals(Character.LastMove.LEFT) ?
                player.getCharacterAnimation().getJumpLeft() :
                player.getCharacterAnimation().getJumpRight());
        }

        if ((Gdx.app.getType() == Application.ApplicationType.Desktop
            ? controller.getAxis(4) > 0.8f : controller.getButton(6))
            && player.getWeapon().getActiveMagazineCapacity() != 0 && isShooting()) {
            player.getWeapon().setActiveMagazineCapacity(player.getWeapon().getActiveMagazineCapacity() - player.getOnShoot().shoot(player, delta));
        }

        if ((Gdx.app.getType() == Application.ApplicationType.Desktop ? controller.getButton(9) : controller.getButton(4)) && player.getWeapon().getActiveMagazineCapacity() != player.getWeapon().getMagazineCapacity() && player.getWeapon().getBackupMagazinesCapacity() != 0) {
            player.getOnShoot().reload(this, player);
        }

        if (player.getMovementCollision().isLadder() && (!(controller.getAxis(0) < -0.8f)) && (!(controller.getAxis(0) > 0.8f))) {
            setShooting(false);

            if (controller.getAxis(1) < -0.8f) {
                if (player.getMovementCollision().isStopOnTopOfLadder()) {
                    player.setCurrentAnimation(player.getCharacterAnimation().getStanding());
                } else {
                    player.setCurrentAnimation(player.getCharacterAnimation().getClimb());
                    player.setVelocityY(player.getSpeedOnLadder());
                }
            } else if (controller.getAxis(1) > 0.8f) {
                if (player.getMovementCollision().isStopOnBottomOfLadder()) {
                    player.setCurrentAnimation(player.getCharacterAnimation().getStandingOnLadder());
                } else {
                    player.setCurrentAnimation(player.getCharacterAnimation().getClimb());
                    player.setVelocityY(-player.getSpeedOnLadder());
                }
            } else {
                if (player.getMovementCollision().isStopOnTopOfLadder()) {
                    player.setCurrentAnimation(player.getCharacterAnimation().getStanding());
                } else {
                    player.setCurrentAnimation(player.getCharacterAnimation().getStandingOnLadder());
                }
            }
            player.getBody().setGravityScale(0);
            player.getBody().setLinearVelocity(player.getVelocityX() * player.getSpeed(), player.getVelocityY());
        } else {
            player.getBody().setGravityScale(1);
            if (!isReloading()) {
                setShooting(true);
            }
        }

        if (player.getMovementCollision().getDoor() != null) {
            Object map = player.getMovementCollision().getDoor().getProperties().get("map");
            if (map != null) {
                if (controller.getButton(2) && !buttonPressed) {
                    if (map.toString().matches("end-game")) {
                        Constants.gameScreen.gameStats = GameScreen.GameStats.COMPLETE;
                    } else {
                        Mission mission = Constants.gameScreen.getMission();
                        mission.setMap(map.toString());
                        Constants.screenChanger.changeScreen("GameScreen", mission);
                    }
                    buttonPressed = true;
                    return;
                } else if (!controller.getButton(2)) {
                    buttonPressed = false;
                }
            }
        }
        player.getBody().setLinearVelocity(player.getVelocityX() * player.getSpeed(), player.getBody().getLinearVelocity().y);
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

    private void jump() {
        player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
        player.getBody().applyLinearImpulse(new Vector2(0, player.getBody().getMass() * 12), player.getBody().getPosition(), true);
    }
}
