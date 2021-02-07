package com.dragonboat.game;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents a log obstacle on the course.
 */
public class Log extends Obstacle {

	/**
	 * Creates a log instance.
	 *
	 * @param xPosition X-position.
	 * @param yPosition Y-position.
	 * @param texture   Texture asset for the log.
	 * @param lane      Lane the log will spawn in.
	 */
	public Log(float xPosition, float yPosition, Texture texture, Lane lane) {
		super(60, xPosition, yPosition, texture.getWidth(), texture.getHeight(), texture, lane);
	}

	/**
	 * --ASSESSMENT 2--
	 * Converts data about the instance into JSON so it can be recreated later.
	 *
	 * @return JSON string storing the instance's info.
	 */
	public String toJSON() {
		HashMap<String, Object> data = new HashMap<>();
		data.put("className", "Log");
		data.put("xPosition", this.xPosition);
		data.put("yPosition", this.yPosition);
		return IO.toJSON(data);
	}

	/**
	 * --ASSESSMENT 2--
	 * Creates an instance from the data passed.
	 *
	 * @param data HashMap storing data about an instance, likely gained by converting an instance to JSON first.
	 * @param tex  Texture representing the asset for the log.
	 * @param l    Lane representing the lane to spawn the log in.
	 */
	public static Log makeLog(
			HashMap<String, Object> data, Texture tex, Lane l) {
		float _xPosition = (float) data.get("xPosition");
		float _yPosition = (float) data.get("yPosition");
		Log log = new Log(_xPosition, _yPosition, tex, l);
		return log;
	}
}
