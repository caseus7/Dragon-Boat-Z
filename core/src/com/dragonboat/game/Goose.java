package com.dragonboat.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents a goose obstacle on the course.
 */
public class Goose extends Obstacle {

	public String direction = "South"; // Facing south by default.

	/**
	 * <p>
	 * Creates a goose instance.
	 * </p>
	 * <p>
	 * Geese can face North, East, South or West. Width and height switch when
	 * changing between North or South and East or West.
	 * </p>
	 *
	 * @param xPosition X-position.
	 * @param yPosition Y-position.
	 * @param texture   Texture asset for the goose.
	 * @param lane      Lane the goose will spawn in.
	 */
	public Goose(float xPosition, float yPosition, Texture texture, Lane lane) {
		super(40, xPosition, yPosition, texture.getWidth(), texture.getHeight(), texture, lane);
	}

	/**
	 * Changes the direction of the Goose to an appropriate, random cardinal
	 * direction.
	 */
	public void changeDirection() {
		HashMap<String, ArrayList<String>> cardinals = new HashMap<String, ArrayList<String>>();

		cardinals.put("East", new ArrayList<String>(Arrays.asList("South")));
		cardinals.put("South", new ArrayList<String>(Arrays.asList("East", "West")));
		cardinals.put("West", new ArrayList<String>(Arrays.asList("South")));

		direction = cardinals.get(direction).get(new Random().nextInt(cardinals.get(direction).size()));
	}

	/**
	 * Moves the goose.
	 *
	 * @param moveVal          Distance to move Goose by.
	 * @param backgroundOffset Offset from screen to course coordinates.
	 */
	public void Move(float moveVal, int backgroundOffset) {

		boolean canGoEast, canGoWest;

		if (this.getX() > this.lane.getLeftBoundary() && this.getX() + this.width < this.lane.getRightBoundary()) {
			// Goose is within the lane boundaries.
			canGoEast = true;
			canGoWest = true;
		} else if (this.getX() <= this.lane.getLeftBoundary()) {
			// Goose is on left boundary.
			canGoEast = true;
			canGoWest = false;
		} else {
			// Goose is on right boundary.
			canGoEast = false;
			canGoWest = true;
		}

		// Chance of goose changing direction.
		int randomMove = 20;
		if (new Random().nextInt(randomMove) == randomMove - 1) {
			changeDirection();
		}

		if (canGoEast && this.direction == "East") {
			this.setX(this.getX() + moveVal);
			if (backgroundOffset > 0 && backgroundOffset < 2160) {
				this.setY(this.getY() - moveVal);
			}
		} else if (canGoWest && this.direction == "West") {
			this.setX(this.getX() - moveVal);
			if (backgroundOffset > 0 && backgroundOffset < 2160) {
				this.setY(this.getY() - moveVal);
			}
		} else {
			this.setY(this.getY() - moveVal);
		}
	}

	/**
	 * --ASSESSMENT 2--
	 * Converts data about the instance into a JSON file so it can be recreated later.
	 *
	 * @return JSON string storing the instance's info.
	 */
	public String toJSON() {
		HashMap<String, Object> data = new HashMap<>();
		data.put("className", "Goose");
		data.put("direction", this.direction);
		data.put("xPosition", this.xPosition);
		data.put("yPosition", this.yPosition);
		return IO.toJSON(data);
	}

	/**
	 * --ASSESSMENT 2--
	 * Creates an instance from the data passed.
	 *
	 * @param data HashMap storing data about an instance, likely gained by converting an instance to JSON first.
	 * @param tex Texture representing the asset for the goose.
	 * @param l Lane representing the lane to spawn the goose.
	 */
	public static Goose makeGoose(
			HashMap<String, Object> data, Texture tex, Lane l) {
		String _direction = (String) data.get("direction");
		float _xPosition = (float) data.get("xPosition");
		float _yPosition = (float) data.get("yPosition");
		Goose goose = new Goose(_xPosition, _yPosition, tex, l);
		goose.direction = _direction;
		return goose;
	}
}
