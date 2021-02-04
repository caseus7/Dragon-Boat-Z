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


public class StartGame extends Game {
	public DragonBoatGame game;
	public final String saveLocation = "../../gameSave.txt";
	private boolean paused = false;
	private int pauseAfter = -1;

	public void reload() {
		game = new DragonBoatGame(this, false);
		game.init();
		setScreen(game);
		game.launch();
	}

	public void pause() {
		if (getScreen().getClass().equals(GameScreen.class)) {
			this.paused = true;
		}
	}

	public void resume() {
		if (getScreen().getClass().equals(GameScreen.class)) {
			this.paused = false;
		}
	}

	public void pauseAfter(int frameCount) {
		this.pauseAfter = frameCount;
	}

	public void togglePause() {
		if (getScreen().getClass().equals(GameScreen.class)) {
			this.paused = !this.paused;
		}
	}

	@Override
	public void create() {
		this.reload();
	}

	@Override
	public void render() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            togglePause();
        }
		if (this.pauseAfter > 0) {
			this.pauseAfter -= 1;
		}
		else if (this.pauseAfter == 0) {
			pause();
			this.pauseAfter = -1;
		}
		if (this.paused && !getScreen().getClass().equals(MenuScreen.class)) {
			return;
		}
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
