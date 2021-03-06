package com.dragonboat.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Random;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * --ASSESSMENT 2--
 * Represents the initial state of the game.
 */
public class StartGame extends Game {
	public DragonBoatGame game;
	public final String saveLocation = "../../gameSave.json";
	private boolean paused = false;
	private int pauseAfter = -1;

	/**
	 * Reloads a fresh game with the original values.
	 */
	public void reload() {
		game = new DragonBoatGame(this, false);
		game.init();
		setScreen(game);
		game.launch();
	}

	@Override
	public void create() {
		this.reload();
	}

	@Override
	public void render() {
		super.render();
		if (!getScreen().getClass().equals(EndGameScreen.class)) {
			game.step();
		}
	}

	/**
	 * Resizes the game screen.
	 *
	 * @param width  Width of the screen.
	 * @param height Height of the screen.
	 */
	@Override
	public void resize(int width, int height) {
		this.getScreen().resize(width, height);
	}

	/**
	 * Disposes of the current screen when it's no longer needed.
	 */
	@Override
	public void dispose() {
		this.getScreen().dispose();
	}
}
