package com.dragonboat.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents a power-up on the course.
 */
public class Boost extends Obstacle {

    public String type;

    /**
     * Creates a boost instance.
     *
     * @param xPosition X-position.
     * @param yPosition Y-position.
     * @param texture   Texture asset for the boost.
     * @param lane      Lane the boost will spawn in.
     */
    public Boost(float xPosition, float yPosition, Texture texture, Lane lane, String type) {
        super(0, xPosition, yPosition, texture.getWidth(), texture.getHeight(), texture, lane);
        this.type = type;
    }

    /**
     *
     * @return String representing the type of power-up.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Converts data about the instance into JSON so it can be recreated later
     * @return JSON string sotring the instance's info
     */
    public String toJSON() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("className", "Boost");
        data.put("type", this.type);
        data.put("xPosition", this.xPosition);
        data.put("yPosition", this.yPosition);
        return IO.toJSON(data);
    }

    /**
     * Creates an instance from the data passed
     * @param data HashMap storing data about an instance, likely gained
     * by converting an instance to JSON first
     */
    public static Boost makeBoost(
            HashMap<String, Object> data, Texture tex, Lane l) {
        String _type = (String) data.get("type");
        float _xPosition = (float) data.get("xPosition");
        float _yPosition = (float) data.get("yPosition");
        Boost boost = new Boost(_xPosition, _yPosition, tex, l, _type);
        return boost;
    }
}
