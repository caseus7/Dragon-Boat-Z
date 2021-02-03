package com.dragonboat.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

/**
 * Represents the player's boat.
 */
public class Player extends Boat {

    /**
     * Creates an instance of the player boat.
     *
     * @param yPosition Y-position of the boat.
     * @param lane      Lane for the boat.
     * @param name      Name of the boat.
     */
    public Player(DragonBoatGame game, float yPosition, Lane lane, String name) {
        super(game, yPosition, lane, name);
    }

    /**
     * Moves the player based on key pressed (W, A, S, D).
     */
    public void GetInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.IncreaseSpeed();
            this.IncreaseTiredness();
        } else
            this.DecreaseTiredness();

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            // Call method associated
            this.DecreaseSpeed();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            // Call method associated
            this.SteerLeft();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            // Call method associated
            this.SteerRight();
        }
    }

    /**
     * Converts data about the instance into JSON so it can be recreated later
     * @return JSON string sotring the instance's info
     */
    public String toJSON() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("className", "Player");
        data.put("name", this.getName());
        data.put("boatNumber", this.getBoatNumber());
        data.put("xPosition", this.xPosition);
        data.put("yPosition", this.yPosition);
        data.put("leftBound", this.leftBound);
        data.put("rightBound", this.rightBound);
        data.put("currentSpeed", this.getCurrentSpeed());
        data.put("durability", this.getDurability());
        data.put("tiredness", this.getTiredness());
        data.put("immune", this.getImmune());
        data.put("boosted", this.getBoosted());
        data.put("boostTimer", this.boostTimer);
        data.put("penalties", this.penalties);
        data.put("fastestLegTime", this.getFastestTime());
        data.put("finished", this.finished());
        return IO.toJSON(data);
    }

    /**
     * Creates an instance from the data passed
     * @param data HashMap storing data about an instance, likely gained
     * by converting an instance to JSON first
     */
    public static Player makePlayer(
            HashMap<String, Object> data, DragonBoatGame game, Lane l) {
        String _name = (String) data.get("name");
        int _boatNumber = (int) data.get("boatNumber");
        float _xPosition = (float) data.get("xPosition");
        float _yPosition = (float) data.get("yPosition");
        int _leftBound = (int) data.get("rightBound");
        int _rightBound = (int) data.get("rightBound");
        float _currentSpeed = (float) data.get("currentSpeed");
        int _durability = (int) data.get("durability");
        float _tiredness = (float) data.get("tiredness");
        boolean _immune = (boolean) data.get("immune");
        String _boosted = (String) data.get("boosted");
        int _boostTimer = (int) data.get("boostTimer");
        float _penalties = (float) data.get("penalties");
        float _fastestLegTime = (float) data.get("fastestLegTime");
        boolean _finished = (boolean) data.get("finished");

        Player player = new Player(game, _yPosition, l, _name);
        player.setName(_name);
        player.ChooseBoat(_boatNumber);
        player.setXPosition(_xPosition);
        player.setYPosition(_yPosition);
        player.setCurrentSpeed(_currentSpeed);
        player.setDurability(_durability);
        player.setTiredness(_tiredness);
        player.setImmune(_immune);
        player.setBoosted(_boosted);
        player.setBoostTimer(_boostTimer);
        player.setPenalties(_penalties);
        player.setFastestLegTime(_fastestLegTime);
        player.setFinished(_finished);
        return player;
    }
}
