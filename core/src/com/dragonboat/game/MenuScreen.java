package com.dragonboat.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Screen class for the Menu Screen. Allows the user to select a Boat, and shows
 * the controls of the game. Once the user clicks within set boundaries, the
 * game starts within GameScreen.
 *
 * @see GameScreen
 * @see Screen
 */
public class MenuScreen implements Screen {
    Texture startScreen;
    Texture easyScreen;
    Texture mediumScreen;
    Texture hardScreen;
    final DragonBoatGame game;
    private final SpriteBatch batch;

    private final FreeTypeFontGenerator generator;
    private final FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private final BitmapFont font24;
    private final GlyphLayout glyphLayout;

    private String menuHint;
    private String pauseHint;
    private String loadText;
    private float[][] menuHintBounds;
    private float[][] pauseHintBounds;
    private float[][] loadTextBounds;

    /**
     * Creates an Input Processor to listen for a mouse click within set boundaries.
     *
     * @param Game represents the initial state of DragonBoatGame.
     * @see com.badlogic.gdx.InputProcessor
     */
    public MenuScreen(DragonBoatGame Game) {
        game = Game;
        batch = new SpriteBatch();
        easyScreen = new Texture(Gdx.files.internal("start screen w fade w controls w difficulty easy.png"));
        mediumScreen = new Texture(Gdx.files.internal("start screen w fade w controls w difficulty normal.png"));
        hardScreen = new Texture(Gdx.files.internal("start screen w fade w controls w difficulty hard.png"));
        startScreen = easyScreen;

        generator = game.generator;
        parameter = game.parameter;
        parameter.size = 24;
        font24 = generator.generateFont(parameter);
        glyphLayout = new GlyphLayout();

        menuHint = "ESCAPE: open/close menu";
        pauseHint = "P: pause/unpause";
        loadText = "Load save game";
        menuHintBounds = new float[2][2];
        pauseHintBounds = new float[2][2];
        loadTextBounds = new float[2][2];

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
                /*
                 * First check whether the cursor is in the correct y-bounds, as all of the
                 * difficulties are have the same y-coordinates.
                 */
                if (screenY >= 309 && screenY <= 335) {
                    /*
                     * Then check if the cursor is in each set of x-bounds and set the
                     * difficulty based on the corresponding word.
                     */
                    if (screenX >= 419 && screenX <= 472) {
                        game.setDifficulty(1);
                        game.setStartDifficulty(1);
                        startScreen = easyScreen;
                    }
                    if (screenX >= 502 && screenX <= 579) {
                        game.setDifficulty(2);
                        game.setStartDifficulty(2);
                        startScreen = mediumScreen;
                    }
                    if (screenX >= 609 && screenX <= 655) {
                        game.setDifficulty(3);
                        game.setStartDifficulty(3);
                        startScreen = hardScreen;
                    }
                }
                /*
                 * First check whether the cursor is in right y-bounds, as these are all the
                 * same for all boats.
                 */
                if (screenY >= 397 && screenY <= 655) {
                    /*
                     * Then check if the mouse is in each set of x-bounds, if so, set the player
                     * boat to the corresponding boat, and initialise the game.
                     *
                     * - NOTE - These values don't work if the window is made to be resizable, and
                     * is then resized by the user.
                     */
                    if (screenX >= 44 && screenX <= 177) {
                        game.player.ChooseBoat(0);
                        game.playerChoice = 0;
                        dispose();
                        game.setScreen(new GameScreen(game, false));
                    }
                    if (screenX >= 187 && screenX <= 320) {
                        game.player.ChooseBoat(1);
                        game.playerChoice = 1;
                        dispose();
                        game.setScreen(new GameScreen(game, false));
                    }
                    if (screenX >= 330 && screenX <= 463) {
                        game.player.ChooseBoat(2);
                        game.playerChoice = 2;
                        dispose();
                        game.setScreen(new GameScreen(game, false));
                    }
                    if (screenX >= 473 && screenX <= 606) {
                        game.player.ChooseBoat(3);
                        game.playerChoice = 3;
                        dispose();
                        game.setScreen(new GameScreen(game, false));
                    }
                    if (screenX >= 616 && screenX <= 749) {
                        game.player.ChooseBoat(4);
                        game.playerChoice = 4;
                        dispose();
                        game.setScreen(new GameScreen(game, false));
                    }
                    if (screenX >= 759 && screenX <= 892) {
                        game.player.ChooseBoat(5);
                        game.playerChoice = 5;
                        dispose();
                        game.setScreen(new GameScreen(game, false));
                    }
                    if (screenX >= 902 && screenX <= 1035) {
                        game.player.ChooseBoat(6);
                        game.playerChoice = 6;
                        dispose();
                        game.setScreen(new GameScreen(game, false));
                    }
                }

                if (
                        (screenX >= loadTextBounds[0][0] && screenX <= loadTextBounds[1][0])
                        && (screenY >= loadTextBounds[0][1] && screenY <= loadTextBounds[1][1])
                ) {
                    boolean loaded = loadGame();
                    if (!loaded) {
                        loadText = "Load failed";
                    }
                }
                return super.touchUp(screenX, screenY, pointer, button);
            }
        });
    }

    /**
     * Loads the saved game from file
     *
     * @return whether or not loading the game was successful
     */
    public boolean loadGame() {
        String loadedData = IO.readFile(game.stGame.saveLocation);
        if (loadedData == null) {
            return false;
        }
        HashMap<String, Object> gameData = IO.hashMapFromJSON(loadedData);
        DragonBoatGame loadedGame = DragonBoatGame.makeDragonBoatGame(gameData, game.stGame);
        game.stGame.game = loadedGame;
        loadedGame.init();
        GameScreen gScreen = new GameScreen(loadedGame, true);
        game.setScreen(gScreen);
        gScreen.setPausedAfter(2);
        game.dispose();
        dispose();
        return true;
    }

    /**
     * Rendering function for the menu screen.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(startScreen, 0, 0);

        float height = startScreen.getHeight();
        float width = startScreen.getWidth();

        glyphLayout.setText(font24, menuHint);
        menuHintBounds[0] = new float[] {
            10,
            height / 2 + 4 * glyphLayout.height };
        menuHintBounds[1] = new float[] {
            10 + glyphLayout.width,
            height / 2 + 3 * glyphLayout.height };
        font24.draw(batch, glyphLayout, menuHintBounds[0][0], menuHintBounds[1][1]);

        glyphLayout.setText(font24, pauseHint);
        pauseHintBounds[0] = new float[] {
            width - 10 - glyphLayout.width,
            height / 2 + 4 * glyphLayout.height };
        pauseHintBounds[1] = new float[] {
            width - 10,
            height / 2 + 3 * glyphLayout.height };
        font24.draw(batch, glyphLayout, pauseHintBounds[0][0], pauseHintBounds[1][1]);

        glyphLayout.setText(font24, loadText);
        loadTextBounds[0] = new float[] {
            width / 2 - glyphLayout.width / 2,
            height - 20 - glyphLayout.height};
        loadTextBounds[1] = new float[] {
            width / 2 + glyphLayout.width / 2,
            height - 20 };
        font24.draw(batch, glyphLayout, loadTextBounds[0][0], height - loadTextBounds[1][1] + 20);
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
     * Disposes of the screen when it is no longer needed.
     */
    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        startScreen.dispose();
        batch.dispose();
    }
}
