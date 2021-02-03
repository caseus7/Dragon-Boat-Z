package com.dragonboat.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Main Game Screen class for Dragon Boat Game. This is the main game loop,
 * handling all the game logic and rendering.
 */
public class GameScreen implements Screen {
    // ENVIRONMENT VARIABLES:
    private final Random rnd;
    private final int MAX_TIREDNESS = 100;
    private DragonBoatGame game = null;

    // debug booleans
    private boolean debug_speed,debug_positions,debug_norandom,debug_verboseoutput;

    // game
    private final Player player;
    private final Course course;
    private final Lane[] lanes;
    private final ProgressBar progressBar;
    private final Leaderboard leaderboard;
    private final Opponent[] opponents;
    private boolean started = false;
    private final float penalty = 0.016f;

    // screen
    private final OrthographicCamera camera;
    private final Viewport viewport;

    // graphics
    private final SpriteBatch batch;
    private final Texture background;
    private final Texture healthBarFull;
    private final Texture healthBarEmpty;
    private final Texture staminaBarFull;
    private final Texture staminaBarEmpty;
    private final FreeTypeFontGenerator generator;
    private final FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private final BitmapFont font18,font28,font44;

    // timing
    private int backgroundOffset;
    private float totalDeltaTime = 0;

    // global parameters
    private final int WIDTH = 1080, HEIGHT = 720;

    /**
     * Sets up everything needed for a race to take place.
     *
     * @param game Represents the initial state of DragonBoatGame.
     */
    public GameScreen(DragonBoatGame game) {
        /*
         * Grab game objects from DragonBoatGame.
         */
        debug_speed = game.debug_speed;
        debug_positions = game.debug_positions;
        debug_norandom = game.debug_norandom;
        debug_verboseoutput = game.debug_verboseoutput;

        this.game = game;
        player = this.game.player;
        course = this.game.course;
        lanes = this.game.lanes;
        progressBar = this.game.progressBar;
        opponents = this.game.opponents;
        rnd = this.game.rnd;

        ArrayList<Integer> possibleBoats = new ArrayList<Integer>();
        for (int i = 0; i < lanes.length; i++) {
            if (i != game.playerChoice) {
                possibleBoats.add(i);
            }
        }
        for (Opponent o : opponents) {
            int choice = o.SetRandomBoat(possibleBoats);
            possibleBoats.remove(choice);
        }

        leaderboard = this.game.leaderboard;

        // setup view
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WIDTH, HEIGHT, camera);

        // texture setting
        background = course.getTexture();
        backgroundOffset = 0;
        batch = new SpriteBatch();
        generator = game.generator;
        parameter = game.parameter;
        parameter.size = 18;
        font18 = generator.generateFont(parameter);
        parameter.size = 28;
        font28 = generator.generateFont(parameter);
        parameter.size = 44;
        font44 = generator.generateFont(parameter);
        staminaBarFull = new Texture(Gdx.files.internal("bar stamina yellow.png"));
        staminaBarEmpty = new Texture(Gdx.files.internal("bar stamina grey.png"));
        healthBarFull = new Texture(Gdx.files.internal("bar health yellow.png"));
        healthBarEmpty = new Texture(Gdx.files.internal("bar health grey.png"));
    }

    /**
     * <p>
     * Rendering function for the game loop, handling all game logic and displaying
     * graphics.
     * </p>
     *
     * <p>
     * GAME LOOP
     * </p>
     *
     * <p>
     * - Spawns any Obstacles that need spawning.
     * </p>
     * <p>
     * - Update Player and Opponent positions.
     * </p>
     * <p>
     * - Check for collisions with Obstacles.
     * </p>
     * <p>
     * - Display Background and Obstacles
     * </p>
     * <p>
     * - End the game if the durability reaches 0.
     * </p>
     * <p>
     * - Update Obstacle positions.
     * </p>
     * <p>
     * - Display Player, Player UI and Opponents.
     * </p>
     * <p>
     * - Display Progress Bar and checks which boats have finished.
     * </p>
     * <p>
     * - Display Player timer.
     * </p>
     * <p>
     * - Checks if all boats have finished, and displays a Leaderboard if so.
     * </p>
     *
     * @param deltaTime Time passed since render function was last run.
     */
    @Override
    public void render(float deltaTime) {



        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
			String gameData = game.toJSON();
			IO.writeFile("../../gameSave.txt", gameData);
            System.out.println("SAVED");
            game.stGame.reload();
			dispose();
            return;
		}




        String debug = "";

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        /*
         * If the game has started, start incrementing time.
         */
        totalDeltaTime += started ? deltaTime : 0;

        /*
         * Check whether obstacles need to be spawned, and spawns them if so. Breaks
         * instantly if the game hasn't started, if the player has finished, or if there
         * are no more obstacles to be spawned.
         *
         * - IMPORTANT -
         * It should be noted that the obstacles currently use a
         * coordinate system relative to the screen, as they are always spawned at
         * HEIGHT + 40 (y = 760). This means all collision checking methods need to be
         * passed backgroundOffset to translate the object's y position.
         */
        for (int i = 0; i < course.getNoLanes(); i++) {
            if (!started || player.finished() || this.game.obstacleTimes[i].size() == 0)
                break;
            if (this.game.obstacleTimes[i].get(0) - player.getY() + player.getHeight() < 1) {
                String[] obstacleTypes = { "Goose", "Log","Goose", "Log","Goose", "Log","Goose", "Log","Goose", "Log","Boost" };
                // spawn an obstacle in lane i.
                int xCoord = lanes[i].getLeftBoundary()
                        + rnd.nextInt(lanes[i].getRightBoundary() - lanes[i].getLeftBoundary() - 15);
                lanes[i].SpawnObstacle(xCoord, HEIGHT + 40, obstacleTypes[rnd.nextInt(obstacleTypes.length)]);
                // make sure obstacle is only spawned once.
                this.game.obstacleTimes[i].remove(0);
            }
        }

        /*
         * Move player. Advance animation.
         */
        player.GetInput();
        player.MoveForward();
        if (player.getCurrentSpeed() > 0 && !started) {
            // detect start of game (might change this to a countdown)
            started = true;
            progressBar.StartTimer();
        }
        player.ProgressAnimation();

        /*
         * Move opponents. Advance animation.
         */
        for (Opponent o : opponents) {
            if (!started)
                break;
            o.MoveForward();
            o.CheckCollisions(backgroundOffset);
            if (Math.round(totalDeltaTime) % 2 == 0) {
                o.ai(backgroundOffset);
            }
            o.ProgressAnimation();
        }

        /*
         * Increase the background offset so the player is centered.
         */
        if (player.getY() + HEIGHT / 2 + player.getHeight() / 2 > course.getTexture().getHeight()) {
            // Stop increasing the background offset when the player reaches the end of the
            // course.
        } else if (player.getY() + player.getHeight() / 2 > HEIGHT / 2) {
            // Start increasing the background offset when the player is above half the
            // window height.
            backgroundOffset = player.getY() + player.getHeight() / 2 - HEIGHT / 2;
        }

        player.CheckCollisions(backgroundOffset);

        /*
         * Display background.
         */
        batch.begin();
        batch.draw(background, 0, 0, 0, background.getHeight() - HEIGHT - backgroundOffset, WIDTH, HEIGHT);
        batch.end();

        /*
         * Display and move obstacles.
         */
        for (Lane lane : lanes) {
            if (!started)
                break;
            for (int j = 0; j < lane.obstacles.size(); j++) {
                Obstacle o = lane.obstacles.get(j);
                // If the background hasn't started moving yet, or if the player has reached the
                // top of the course, move obstacle at set speed.
                // Else add the player speed to the obstacle speed.
                o.Move(0.4f + (backgroundOffset > 0
                        && player.getY() + HEIGHT / 2 + player.getHeight() / 2 < course.getTexture().getHeight()
                                ? player.getCurrentSpeed()
                                : 0));
                if (o.getY() < -o.getHeight()) {
                    lane.RemoveObstacle(o);
                }
                batch.begin();
                batch.draw(o.getTexture(), o.getX(), o.getY());
                batch.end();
            }
        }

        /*
         * If the durability of the boat has reached 0, end the game.
         */
        if (player.getDurability() <= 0) {
            game.endGame();
        }

        /*
         * Display player and player UI.
         */
        batch.begin();
        batch.draw(player.texture, player.getX(), player.getY() - backgroundOffset);
        batch.draw(staminaBarEmpty, player.lane.getLeftBoundary(), player.getY() - 20 - backgroundOffset);
        batch.draw(healthBarEmpty, player.lane.getLeftBoundary(), player.getY() - 40 - backgroundOffset);
        batch.draw(staminaBarFull, player.lane.getLeftBoundary(), player.getY() - 20 - backgroundOffset, 0, 0,
                Math.round(staminaBarFull.getWidth() * player.getTiredness() / MAX_TIREDNESS),
                staminaBarFull.getHeight());
        batch.draw(
            healthBarFull,
            player.lane.getLeftBoundary(),
            player.getY() - 40 - backgroundOffset,
            0,
            0,
            Math.round(healthBarFull.getWidth() * player.getDurability() / player.getMaxDurability()),
            healthBarFull.getHeight() );
        batch.end();

        if(debug_positions) debug += player.getName() + " pos: (" + player.getX() + "," + player.getY() +")\n";
        if(debug_speed) debug += player.getName() + " speed: " + player.getCurrentSpeed() + " / " + player.getMaxSpeed() + "\n\n";

        /*
         * Display opponents.
         */
        for (Opponent o : opponents) {
            batch.begin();
            batch.draw(o.texture, o.getX(), o.getY() - backgroundOffset);
            batch.end();
            if(debug_positions) debug += o.getName() + " pos: (" + o.getX() + "," + o.getY() +")\n";
            if(debug_speed) debug += o.getName() + " speed: " + o.getCurrentSpeed() + " / " + o.getMaxSpeed() + "\n\n";
        }

        /*
         * Display progress bar.
         */
        batch.begin();
        batch.draw(progressBar.getTexture(), WIDTH - progressBar.getTexture().getWidth() - 60,
                HEIGHT - progressBar.getTexture().getHeight() - 20);
        batch.end();

        /*
         * Get progress for each boat. Draw player and opponent icons on progress bar
         * with x coordinates respective to their progress.
         */
        float[] progress = progressBar.getProgress(course.getTexture().getHeight());
        for (int i = 1; i < progress.length; i++) {
            batch.begin();
            batch.draw(progressBar.getOpponentIcon(),
                    WIDTH - progressBar.getTexture().getWidth() - 50
                            + progress[i] * (progressBar.getTexture().getWidth() - 214),
                    HEIGHT - progressBar.getTexture().getHeight() / 2 - 10);
            batch.end();
        }

        batch.begin();
        batch.draw(progressBar.getPlayerIcon(),
                WIDTH - progressBar.getTexture().getWidth() - 50
                        + progress[0] * (progressBar.getTexture().getWidth() - 214),
                HEIGHT - progressBar.getTexture().getHeight() / 2 - 10);
        batch.end();

        updateFinishedBoats();
        // End the race if the player has finished
        if (player.finished()) {
            float[] predictedFinishTimes = predictBoatFinishTimes(progress);
            for (int i=0; i<predictedFinishTimes.length-1; i++) {
                if (!opponents[i].finished()) {
                    opponents[i].setFinished(true);
                    opponents[i].UpdateFastestTime(predictedFinishTimes[i+1]);
                }
            }
        }

        /*
         * Display player time.
         */
        progressBar.IncrementTimer(deltaTime);
        batch.begin();
        font28.draw(batch, started ? progressBar.getPlayerTimeString() : "", WIDTH - 230, HEIGHT - 40);
        batch.end();

        /*
         * Check player boat is in their lane, if not apply penalties.
         */
        if (!player.CheckIfInLane() && !player.finished()) {
            player.applyPenalty(penalty);
            font28.setColor(Color.RED);
            batch.begin();
            font28.draw(batch, "Warning! Penalty applied for leaving lane", 240, 100);
            batch.end();
            font28.setColor(Color.WHITE);
        }
        /*
         * Check opponent boats are in their lanes, if not apply penalties.
         */
        for (Opponent opponent : opponents) {
            if (!opponent.CheckIfInLane() && !opponent.finished()) {
                opponent.applyPenalty(penalty);
            }
        }

        /*
         * Display debug stats.
         */
        if(debug_positions || debug_speed) {
            batch.begin();
            font18.draw(batch,debug,5,HEIGHT-60);
            batch.end();
        }

        if(debug_verboseoutput) {
            System.out.println("-----------------------");
            System.out.println("Total time: " + totalDeltaTime + "\nDelta time: " + deltaTime);
            System.out.println("-----------------------");
            System.out.println(" -- Variables --\n"
                    + "backgroundOffset: " + backgroundOffset);
            for(int i = 0; i < lanes.length; i++) {
                System.out.println("Lane " + i + " obstacles: " + lanes[i].obstacles.size());
            }
            System.out.println("\n\n\n");
        }


        /*
         * Check if the player has passed the finish line, if so, generate the
         * leaderboard.
         */
        if (player.finished()) {
            // Display leaderboard, if on the third leg, display top 3 boats.
            if (game.getDifficulty() < game.getStartDifficulty() + 2) {
                batch.begin();
                batch.draw(leaderboard.getTexture(), WIDTH / 2 - leaderboard.getTexture().getWidth() / 2,
                        HEIGHT / 2 - leaderboard.getTexture().getHeight() / 2);
                batch.end();
                String[] times = leaderboard.getTimes(opponents.length + 1);
                for (int i = 0; i < opponents.length + 1; i++) {
                    batch.begin();
                    font44.draw(batch, times[i], WIDTH / 2 - leaderboard.getTexture().getWidth() / 3,
                            620 - (75 * i));
                    batch.end();
                }
                batch.begin();
                font28.draw(batch, "Click anywhere to progress to next leg.", 200, 40);
                batch.end();
                /*
                 * Defines how to handle keyboard and mouse inputs.
                 */
                Gdx.input.setInputProcessor(new InputAdapter() {
                    @Override
                    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                        game.advanceLeg();
                        Gdx.input.setInputProcessor(null);
                        return super.touchUp(screenX, screenY, pointer, button);
                    }
                });
            } else if (game.getDifficulty() == game.getStartDifficulty() + 2) {
                batch.begin();
                batch.draw(leaderboard.getTexture(), WIDTH / 2 - leaderboard.getTexture().getWidth() / 2,
                        HEIGHT / 2 - leaderboard.getTexture().getHeight() / 2);
                batch.end();
                String[] times = leaderboard.getTimes(opponents.length + 1);
                for (int i = 0; i < opponents.length + 1; i++) {
                    if (i < 3)
                        font44.setColor(Color.GOLD);
                    else
                        font44.setColor(Color.WHITE);

                    batch.begin();
                    font44.draw(batch, times[i], WIDTH / 2 - leaderboard.getTexture().getWidth() / 3,
                            620 - (75 * i));
                    batch.end();
                }
                if (times[0].startsWith("Player") || times[1].startsWith("Player")
                        || times[2].startsWith("Player")) {
                    batch.begin();
                    font28.draw(batch, "Click anywhere to progress to the final!", 200, 40);
                    batch.end();
                    /*
                     * Defines how to handle keyboard and mouse inputs.
                     */
                    Gdx.input.setInputProcessor(new InputAdapter() {
                        @Override
                        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                            game.advanceLeg();
                            Gdx.input.setInputProcessor(null);
                            return super.touchUp(screenX, screenY, pointer, button);
                        }
                    });
                } else {
                    game.endGame();
                }
            } else {
                game.endGame();
            }
        }
    }

    /*
    * Check if player and each opponent has finished, and update their finished
    * booleans and fastestLegTime variables respectively.
    *
    */
    private void updateFinishedBoats() {
        int courseHeight = course.getTexture().getHeight();
        if (player.getProgress(courseHeight) == 1 && !player.finished()) {
            player.setFinished(true);
            player.UpdateFastestTime(progressBar.getPlayerTime());
        }
        for (int i = 0; i < opponents.length; i++) {
            if (opponents[i].getProgress(courseHeight) == 1 && !opponents[i].finished()) {
                opponents[i].setFinished(true);
                opponents[i].UpdateFastestTime(progressBar.getTime());
            }
        }
    }

    /*
    * Returns the predicted time each boat will finish the race.
    *
    * @param progress The distance along the race each boat is, as a float from 0 to 1
    */
    private float[] predictBoatFinishTimes(float[] progress) {
        float[] finishTimes = new float[opponents.length + 1];
        if (player.finished()) {
            finishTimes[0] = player.getFastestTime();
        }
        else {
            finishTimes[0] = predictBoatFinishTime(
                progress[0],
                progressBar.getTime() );
        }
        for (int i=0; i<opponents.length; i++) {
            if (opponents[i].finished()) {
                finishTimes[i + 1] = opponents[i].getFastestTime();
            }
            else {
                finishTimes[i + 1] = predictBoatFinishTime(
                    progress[i + 1],
                    progressBar.getTime() );
            }
        }
        return finishTimes;
    }

    /*
    * Returns the predicted time the boat will finish the race.
    * The prediction is based on the time it took for the boat to reach its
    * current distance along the race
    *
    * @param progress The distance along the race as a float from 0 to 1
    * @param time The time it took for the boat to reach its current distance
    */
    private float predictBoatFinishTime(float progress, float time) {
        if (progress >= 1.0) {
            return time;
        }
        return time / progress;
    }

    /**
     * Resizes the game screen.
     *
     * @param width  Width of the screen.
     * @param height Height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
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

    /**
     * Disposes of the screen when it is no longer needed.
     */
    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        player.texture.dispose();
        for (Lane lane : lanes) {
            for (int j = 0; j < lane.obstacles.size(); j++) {
                lane.obstacles.get(j).getTexture().dispose();
            }
        }
        progressBar.getTexture().dispose();
        progressBar.getOpponentIcon().dispose();
        progressBar.getPlayerIcon().dispose();
        staminaBarFull.dispose();
        staminaBarEmpty.dispose();
        healthBarEmpty.dispose();
        healthBarFull.dispose();
        generator.dispose();
        font28.dispose();
        font44.dispose();
        leaderboard.getTexture().dispose();

    }

    @Override
    public void show() {

    }
}
