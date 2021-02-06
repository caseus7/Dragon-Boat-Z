package com.dragonboat.game;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * --ASSESSMENT 2--
 * Shows the end screen.
 * This will be shown whenever the player wins or loses.
 *
 * @see EndGameScreen
 * @see Screen
 */
public class EndGameScreen implements Screen {
    private Texture endScreen;
	private StartGame stGame;
	private ArrayList<Texture> textures;
	private ArrayList<String> textBlocks;
	private BitmapFont font28;
	private ArrayList<float[]> drawPositions;
    private final SpriteBatch batch;

    /**
     * Creates an Input Processor to listen for a mouse click within set boundaries.
     *
     * @param startGame Represents the initial state of DragonBoatGame.
     * @param texs ArrayList of textures containing the required assets for the screen.
     * @param text ArrayList of strings containing the necessary text for the screen.
     * @param positions ArrayList of floats representing the positions to draw everything.
     * @see com.badlogic.gdx.InputProcessor
     */
    public EndGameScreen(StartGame startGame, ArrayList<Texture> texs, ArrayList<String> text, ArrayList<float[]> positions) {
        this.stGame = startGame;
		this.textures = texs;
		this.textBlocks = text;
		this.drawPositions = positions;
        this.batch = new SpriteBatch();
        this.endScreen = new Texture(Gdx.files.internal("end screen.png"));
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("8bitOperatorPlus-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 28;
		font28 = generator.generateFont(parameter);


        /*
         * Defines how to handle mouse inputs.
         */
        Gdx.input.setInputProcessor(new InputAdapter() {

            /**
             * Used to receive input events from the mouse.
             *
             * @param screenX X-position of the cursor.
             * @param screenY Y-position of the cursor (top left is 0,0).
             * @param pointer Pointer object.
             * @param button  Number representing mouse button clicked (0 = left click, 1 =
             *                right click, 2 = middle mouse button, etc.).
             * @return The output of touchUp(...), a boolean representing whether the input
             *         was processed (unused in this scenario).
             * @see InputAdapter
             */
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                stGame.reload();
                return super.touchUp(screenX, screenY, pointer, button);
            }
        });
    }

    /**
     * --ASSESSMENT 2--
     * Rendering function for the menu screen.
     *
     * @param delta Float representing the time passed since the render function was last run.
     */
    @Override
    public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for (int i = 0; i < textures.size(); i++) {
			batch.draw(textures.get(i), this.drawPositions.get(i)[0], this.drawPositions.get(i)[1]);
		}
		for (int i = 0; i < textBlocks.size(); i++) {
			int positionIdx = textures.size() + i;
			font28.draw(batch, textBlocks.get(i), this.drawPositions.get(positionIdx)[0], this.drawPositions.get(positionIdx)[1]);
		}
		batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    /**
     * --ASSESSMENT 2--
     * Disposes of the screen when it is no longer needed.
     */
    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        endScreen.dispose();
        batch.dispose();
		font28.dispose();
    }
}
