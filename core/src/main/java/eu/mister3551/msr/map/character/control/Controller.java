package eu.mister3551.msr.map.character.control;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.Static;
import eu.mister3551.msr.database.object.Mission;
import eu.mister3551.msr.database.object.Options;
import eu.mister3551.msr.map.character.Character;
import eu.mister3551.msr.map.character.Player;
import eu.mister3551.msr.map.character.movement.MovementCollision;
import eu.mister3551.msr.map.character.movement.OnZipline;
import eu.mister3551.msr.map.character.movement.Zipline;
import eu.mister3551.msr.screen.GameState;

import java.util.ArrayList;

public class Controller extends Device implements ControllerListener {

    private com.badlogic.gdx.controllers.Controller controller;

    public Controller(Player player, Options options) {
        super(player, options);
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
    public void inputs(MovementCollision movementCollision, float delta) {
        if (movementCollision.getZipline() != null && getZipline() == null && movementCollision.getZipline().isZiplineCollision()) {
            setZipline(movementCollision.getZipline());
        }

        Zipline currentZipline = getZipline();
        if (currentZipline != null) {
            onZipline(currentZipline.getPoints());
        } else {
            walking(movementCollision, delta);
        }
    }

    @Override
    public void walking(MovementCollision movementCollision, float delta) {
        player.setVelocityX(0);
        player.setVelocityY(0);

        if (controller != null) {
            if (controller.getAxis(0) < -0.8f && !player.isOnLeftSide()) {
                player.setCurrentAnimation(movementCollision.isWater() ? player.getCharacterAnimation().getSwimLeft() : player.isOnFloor() ? player.getCharacterAnimation().getWalkLeft() :  player.getCharacterAnimation().getJumpLeft());
                player.setLastMove(Character.LastMove.LEFT);
                player.setVelocityX(-1);
            } else if (controller.getAxis(0) > 0.8f && !player.isOnRightSide()) {
                player.setCurrentAnimation(movementCollision.isWater() ? player.getCharacterAnimation().getSwimRight() : player.isOnFloor() ? player.getCharacterAnimation().getWalkRight() :  player.getCharacterAnimation().getJumpRight());
                player.setLastMove(Character.LastMove.RIGHT);
                player.setVelocityX(1);
            } else if (player.isOnFloor()) {
                if (!movementCollision.isWater()) {
                    player.setCurrentAnimation(player.getLastMove().equals(Character.LastMove.LEFT) ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
                } else {
                    player.setCurrentAnimation(player.getLastMove().equals(Character.LastMove.LEFT) ? player.getCharacterAnimation().getSwimLeft() : player.getCharacterAnimation().getSwimRight());
                }
            }

            if (controller.getAxis(1) < -0.8f && movementCollision.isLadder()) {
                player.setCurrentAnimation(!movementCollision.isStopOnLadder() ? player.getCharacterAnimation().getClimb() : player.getCharacterAnimation().getStanding());
                player.setVelocityY(player.getSpeedOnLadder());
            } else if (controller.getAxis(1) > 0.8f && movementCollision.isLadder()) {
                player.setCurrentAnimation(player.getCharacterAnimation().getClimb());
                player.setVelocityY(-player.getSpeedOnLadder());
            } else if (!movementCollision.isStopOnLadder() && movementCollision.isLadder()) {
                player.setCurrentAnimation(player.getCharacterAnimation().getStandingOnLadder());
            }

            if (player.isOnJump()) {
                player.setJumps(0);
                if (player.getVelocityX() == 0 && movementCollision.isStopOnLadder()) {
                    player.setCurrentAnimation(player.getLastMove().equals(Character.LastMove.LEFT) ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
                }
            }

            if (movementCollision.isLadder() && player.getVelocityY() == 0 && !player.isOnFloor()) {
                player.setCurrentAnimation(!movementCollision.isStopOnLadder() ? player.getCharacterAnimation().getStandingOnLadder() : player.getLastMove().equals(Character.LastMove.LEFT) ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
            }

            if (controller.getButton(0) && player.getJumps() < 1 && player.isOnJump() && player.isBodyOnFloor() && !movementCollision.isLadder()) {
                jump();
                player.getBody().setGravityScale(1);
            }

            if (movementCollision.isLadder()) {
                player.getBody().setGravityScale(0);
                player.getBody().setLinearVelocity(player.getVelocityX() * player.getSpeed(), player.getVelocityY());
            } else {
                player.getBody().setGravityScale(1);
                player.getBody().setLinearVelocity(player.getVelocityX() * player.getSpeed(), Math.min(player.getBody().getLinearVelocity().y, 25));
            }

            if ((Gdx.app.getType() == Application.ApplicationType.Desktop ? controller.getAxis(4) > 0.8f : controller.getButton(6)) && player.getWeapon().getActiveMagazineCapacity() != 0 && isShooting) {
                player.getWeapon().setActiveMagazineCapacity(player.getWeapon().getActiveMagazineCapacity() - player.getOnShoot().shoot(player, delta));
            }

            if ((Gdx.app.getType() == Application.ApplicationType.Desktop ? controller.getButton(9) : controller.getButton(4)) && player.getWeapon().getActiveMagazineCapacity() != player.getWeapon().getMagazineCapacity() && player.getWeapon().getBackupMagazinesCapacity() != 0) {
                player.getOnShoot().reload(this, player);
            }

            if (movementCollision.getDoor() != null && player.isOnFloor()) {
                Object map = movementCollision.getDoor().getProperties().get("map");
                if (map != null) {
                    if (controller.getButton(2)) {

                        GameState gameState = Static.gameState.values().stream()
                            .findFirst()
                            .orElse(null);

                        if (gameState != null) {
                            Mission mission = new Mission();
                            mission.setId(gameState.getMission().getId());
                            mission.setIdUser(gameState.getMission().getIdUser());
                            mission.setMap(map.toString());
                            Static.screenChanger.changeScreen("GameScreen", mission);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void jump() {
        player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
        player.getBody().applyLinearImpulse(new Vector2(0, player.getBody().getMass() * 12), player.getBody().getPosition(), true);
        player.setJumps(player.getJumps() + 1);
    }

    @Override
    public void onZipline(ArrayList<Vector2> points) {
        zipline = OnZipline.movement(player, zipline, points);
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
