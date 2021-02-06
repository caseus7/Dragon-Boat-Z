package com.dragonboat.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.files.FileHandle;


/**
 * Used to save and load data from file
 */
public class IO {
	private static Json json = new Json();

	/**
	 * Reads from file
	 * @param filepath Path of the file to read
	 * @return Content of the file as a String
	 */
	public static String readFile(String filepath) {
		FileHandle file = Gdx.files.internal(filepath);
		if (!file.exists()) {
			return null;
		}
		String text = file.readString();
		return text;
	}

	/**
	 * Writes to file
	 * @param filepath Path of the file to read
	 * @param data Data to write to the file
	 */
	public static void writeFile(String filepath, String data) {
		FileHandle file = Gdx.files.local(filepath);
		file.writeString(data, false);
	}

	/**
	 * Returns the JSON representation of the object
	 * @param data The object to convert to JSON
	 * @return Object in JSON form
	 */
	public static String toJSON(Object data) {
		return json.toJson(data);
	}

	/**
	 * Makes the object represented by the JSON
	 * @param dataType Class of the object being created from the JSON
	 * @param jsonData JSON representing the object
	 * @return Object made from the JSON
	 */
	public static HashMap<String, Object> hashMapFromJSON(String jsonData) {
		return json.fromJson(HashMap.class, jsonData);
	}

	/**
	 * Makes the object represented by the JSON
	 * @param dataType Class of the object being created from the JSON
	 * @param jsonData JSON representing the object
	 * @return Object made from the JSON
	 */
	public static Object fromJSON(Class dataType, String jsonData) {
		return json.fromJson(dataType, jsonData);
	}
}
