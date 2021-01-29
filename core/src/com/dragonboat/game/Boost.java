package com.dragonboat.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents a goose obstacle on the course.
 */
public class Boost extends Obstacle {

    public String type;

    /**
     * <p>
     * Creates a boost instance.
     * </p>
     *
     * @param xPosition X-position.
     * @param yPosition Y-position.
     * @param texture   Texture asset for the boost.
     * @param lane      Lane the boost will spawn in.
     */
    public Boost(int xPosition, int yPosition, Texture texture, Lane lane, String type) {
        super(0, xPosition, yPosition, texture.getWidth(), texture.getHeight(), texture, lane);
        this.type = type;
    }

    public String getType() {
        return this.type;
    }





}
