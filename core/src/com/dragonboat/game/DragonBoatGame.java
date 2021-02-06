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

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * <p>
 * Game Class for Dragon Boat Game.
 * </p>
 * <p>
 * Initialises all the objects necessary for the game, starts music, creates
 * Lanes, randomises Obstacle spawns, initialises blank Player and Opponents,
 * initialises a Progress Bar and Leaderboard, and instantiates a Menu Screen.
 * </p>
 *
 * @see MenuScreen
 */
public class DragonBoatGame implements Screen {

	// debug booleans
	protected boolean debug_speed = false;
	protected boolean debug_positions = false;
	protected boolean debug_norandom = false;
	protected boolean debug_verboseoutput = false;

	public StartGame stGame;
	public Lane[] lanes;
	public Player player;
	public Course course;
	public Opponent[] opponents;
	public int[] boatLanes;
	public ProgressBar progressBar;
	public Leaderboard leaderboard;
	public ArrayList<Integer>[] obstacleTimes;
	public int playerChoice;
	public Music music;
	public boolean ended = false;
	public FreeTypeFontGenerator generator;
	public FreeTypeFontGenerator.FreeTypeFontParameter parameter;
	protected Random rnd;
	private boolean loadedFromSave;
	private MenuScreen menuScreen;
	private int startDifficulty = 1;
	private int difficulty = 1;
	private SpriteBatch batch;
	private BitmapFont font28;
	private Texture courseTexture;

	/**
	 * Sets up the game with settings and instantiation of objects.
	 */
	public DragonBoatGame(StartGame start, boolean loaded) {
		this.stGame = start;
		this.loadedFromSave = loaded;

		if (debug_norandom) rnd = new Random(1);
		else rnd = new Random();
		courseTexture = new Texture(Gdx.files.internal("background sprite.png"));
	}

	public void init() {
		if (!this.loadedFromSave) {
			boatLanes = new int[7];
			int noOfObstacles = 8;
			createLanes(noOfObstacles);
			generateObstacleTimes(noOfObstacles);

			player = new Player(this, 0, lanes[3], "Player");
			boatLanes[0] = 3;

			opponents = new Opponent[6];
			for (int i = 0; i < opponents.length; i++) {
				// Ensure player is in the middle lane by skipping over lane 4.
				int lane = i >= 3 ? i + 1 : i;
				opponents[i] = new Opponent(this, 0, lanes[lane], "Opponent" + (i + 1));
				boatLanes[i+1] = lane;
			}
			progressBar = new ProgressBar(player, opponents);
		}

		course = new Course(courseTexture, lanes);
		leaderboard = new Leaderboard(player, opponents);

		// Set up font.
		generator = new FreeTypeFontGenerator(Gdx.files.internal("8bitOperatorPlus-Regular.ttf"));
		parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 28;
		font28 = generator.generateFont(parameter);

		batch = new SpriteBatch();
	}

	public void launch() {
		// Display the menu screen.
		menuScreen = new MenuScreen(this);
		stGame.setScreen(menuScreen);
	}

	/**
	 * Changes the screen to a new GameScreen and resets necessary attributes.
	 */
	public void advanceLeg() {
		/*
		 * Increase difficulty and set up next leg.
		 */
		difficulty += 1;
		if(debug_norandom) rnd = new Random(1);
		else rnd = new Random();

		int noOfObstacles = 8 * difficulty;
		clearLanesObstacles();
		updateLaneObstacleLimits(noOfObstacles);
		generateObstacleTimes(noOfObstacles);
		player.Reset();

		/*
		 * Set up final leg.
		 */
		if (difficulty == startDifficulty + 3) {
			Boat[] finalists = leaderboard.getPodium();
			opponents = new Opponent[2];
			for (Boat b : finalists) {
				if (b.getName().startsWith("Opponent")) {
					// set opponents lanes so that only the middle 3 lanes are used.
					if (opponents[0] == null) {
						opponents[0] = (Opponent) b;
						b.setLane(lanes[2]);
					} else {
						opponents[1] = (Opponent) b;
						b.setLane(lanes[4]);
					}
				}
				b.ResetFastestLegTime();
			}
		}
		for (Opponent o : opponents) {
			o.Reset();
		}
		progressBar = new ProgressBar(player, opponents);
		stGame.setScreen(new GameScreen(this, false));
	}

	/*
	* Gets the start difficulty
	*/
	public int getStartDifficulty() {
		return this.startDifficulty;
	}

	/*
	* Sets the start difficulty
	* @param newStartDifficulty Integer greater than 0
	* @return If the difficulty was changed successfully
	*/
	public boolean setStartDifficulty(int newStartDifficulty) {
		if (newStartDifficulty > 0) {
			this.startDifficulty = newStartDifficulty;
			return true;
		}
		return false;
	}

	/*
	* Gets the difficulty
	*/
	public int getDifficulty() {
		return this.difficulty;
	}

	/*
	* Sets the difficulty
	* @param newDifficulty Integer greater than 0
	* @return If the difficulty was changed successfully
	*/
	public boolean setDifficulty(int newDifficulty) {
		if (newDifficulty > 0) {
			this.difficulty = newDifficulty;
			return true;
		}
		return false;
	}

	public ArrayList<Obstacle> getObstacles() {
		ArrayList<Obstacle> obstacles = new ArrayList<>();
		for (Lane lane : this.lanes) {
			obstacles.addAll(lane.obstacles);
		}
		return obstacles;
	}

	public void setScreen(Screen screen) {
		stGame.setScreen(screen);
	}

	public void step() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.O)){
			System.out.println("DURA BEFORE: " + player.getDurability());
			player.Boost("health");
			System.out.println("DURA AFTER: " + player.getDurability());
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.I)){
			System.out.println("ACC BEFORE: " + player.getAcceleration());
			player.Boost("acceleration");
			System.out.println("ACC AFTER: " + player.getAcceleration());
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.U)){
			System.out.println("IM BEFORE: " + player.getImmune());
			player.Boost("immune");
			System.out.println("IM AFTER: " + player.getImmune());
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.Y)){
			System.out.println("MAN BEFORE: " + player.getManeuverability());
			player.Boost("maneuverability");
			System.out.println("MAN AFTER: " + player.getManeuverability());
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.T)){
			System.out.println("SPEED BEFORE: " + player.getCurrentSpeed());
			player.Boost("speed");
			System.out.println("SPEED AFTER: " + player.getCurrentSpeed());
		}

		if (player.isBoosted()) {
			player.boostTimer += 1;
			if (player.boostTimer >= 500) {
				player.removeBoost();
			}
		}

		for (Opponent o : opponents) {
			o.boostTimer += 1;
			if (o.boostTimer >= 500) {
				o.removeBoost();
			}
		}

		/*
		 * If the game hasn't ended, just call the current screen render function.
		 */
		if (this.ended) {
			/*
			 * Else, display an end screen and appropriate text and images.
			 */
			ArrayList<Texture> textures = new ArrayList<>();
			ArrayList<String> text = new ArrayList<>();
			ArrayList<float[]> texturePositions = new ArrayList<>();
			ArrayList<float[]> textPositions = new ArrayList<>();

			textures.add(new Texture(Gdx.files.internal("end screen.png")));
			texturePositions.add(new float[] {0, 0});

			boolean playerWon = false;
			Boat[] podium = leaderboard.getPodium();
			for (int i = 0; i < podium.length; i++) {
				/*
				 * If the player is in the top 3 boats, display the player's boat and
				 * appropriate medal.
				 */
				if (podium[i].getName().startsWith("Player") && player.getDurability() > 0) {
					playerWon = true;
					batch.begin();
					textures.add(player.texture);
					texturePositions.add(
						new float[] {
							Gdx.graphics.getWidth() / 2,
							Gdx.graphics.getHeight() / 3 });
					switch (i) {
						case 0:
							textures.add(new Texture(Gdx.files.internal("medal gold.png")));
							texturePositions.add(
								new float[] {
									Gdx.graphics.getWidth() / 3,
									Gdx.graphics.getHeight() / 3 });
							break;
						case 1:
							textures.add(new Texture(Gdx.files.internal("medal silver.png")));
							texturePositions.add(
								new float[] {
									Gdx.graphics.getWidth() / 3,
									Gdx.graphics.getHeight() / 3 });
							break;
						case 2:
							textures.add(new Texture(Gdx.files.internal("medal bronze.png")));
							texturePositions.add(
								new float[] {
									Gdx.graphics.getWidth() / 3,
									Gdx.graphics.getHeight() / 3 });
							break;
					}
					text.add("Congratulations! You reached Super Saiyan!");
					textPositions.add(new float[] {140, 140});
				}
			}
			if (!playerWon) {
				text.add("Unlucky, click to return to the menu");
				textPositions.add(new float[] {140, 200});
			}

			ArrayList<float[]> positions = new ArrayList<>();
			positions.addAll(texturePositions);
			positions.addAll(textPositions);
			setScreen(new EndGameScreen(stGame, textures, text, positions));
			dispose();
			return;
		}
	}

	@Override
	public void render(float deltaTime) {

	}

	/**
	 * Resizes the game screen.
	 *
	 * @param width  Width of the screen.
	 * @param height Height of the screen.
	 */
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

	public void endGame() {
		this.ended = true;

	}

	/**
	 * Disposes of the current screen when it's no longer needed.
	 */
	@Override
	public void dispose() {
		batch.dispose();
		font28.dispose();
		courseTexture.dispose();
	}

	/**
	 * Converts data about the instance into JSON so it can be recreated later
	 * @return JSON string sotring the instance's info
	 */
	public String toJSON() {
		HashMap<String, Object> data = new HashMap<>();

		ArrayList<String> laneData = new ArrayList<>();
		for (Lane l : this.lanes) {
			laneData.add(l.toJSON());
		}
		ArrayList<String> opponentData = new ArrayList<>();
		for (Opponent op : this.opponents) {
			opponentData.add(op.toJSON());
		}

		data.put("className", "DragonBoatGame");
		data.put("startDifficulty", this.startDifficulty);
		data.put("difficulty", this.difficulty);
		data.put("player", this.player.toJSON());
		data.put("opponents", opponentData);
		data.put("lanes", laneData);
		data.put("boatLanes", this.boatLanes);
		data.put("obstacleTimes", this.obstacleTimes);
		data.put("progressBar", this.progressBar.toJSON());
		return IO.toJSON(data);
	}

	/**
	 * Creates an instance from the data passed
	 * @param data HashMap storing data about an instance, likely gained
	 * by converting an instance to JSON first
	 */
	public static DragonBoatGame makeDragonBoatGame(
			HashMap<String, Object> data, StartGame startGame) {
		String _class = (String) data.get("className");
		int _startDifficulty = (int) data.get("startDifficulty");
		int _difficulty = (int) data.get("difficulty");
		Player _player;
		Opponent[] _opponents;
		Lane[] _lanes;
		int[] _boatLanes;
		ArrayList<Integer>[] _obstacleTimes;
		ProgressBar _progressBar;

		DragonBoatGame loadedGame = new DragonBoatGame(startGame, true);

		HashMap<String, Object> playerData =
			IO.hashMapFromJSON((String) data.get("player"));
		Array<String> opponentStrings = (Array) data.get("opponents");
		Array<String> laneStrings = (Array) data.get("lanes");
		Array<Array<Integer>> obstacleTimeData = (Array) data.get("obstacleTimes");
		HashMap<String, Object> progressBarData =
			IO.hashMapFromJSON((String) data.get("progressBar"));

		_boatLanes = new int[laneStrings.size];
		Array<Float> tmp = (Array) data.get("boatLanes");
		for (int i = 0; i < laneStrings.size; i++) {
			_boatLanes[i] = Math.round(tmp.get(i));
		}

		_lanes = new Lane[laneStrings.size];
		for (int i = 0; i < laneStrings.size; i++) {
			HashMap<String, Object> laneData = IO.hashMapFromJSON(laneStrings.get(i));
			_lanes[i] = Lane.makeLane(laneData);
		}

		_opponents = new Opponent[opponentStrings.size];
		for (int i = 0; i < opponentStrings.size; i++) {
			HashMap<String, Object> opponentData = IO.hashMapFromJSON(opponentStrings.get(i));
			int laneIdx = (int) _boatLanes[i+1];
			_opponents[i] = Opponent.makeOpponent(opponentData, loadedGame, _lanes[laneIdx]);
		}

		_obstacleTimes = new ArrayList[obstacleTimeData.size];
		for (int i = 0; i < obstacleTimeData.size; i++) {
			_obstacleTimes[i] = new ArrayList<>();
			for (int j = 0; j < obstacleTimeData.get(i).size; j++) {
				_obstacleTimes[i].add(obstacleTimeData.get(i).get(j));
			}
		}

		int playerLaneIdx = _boatLanes[0];
		_player = Player.makePlayer(playerData, loadedGame, _lanes[playerLaneIdx]);

		_progressBar = ProgressBar.makeProgressBar(
			progressBarData, _player, _opponents );

		loadedGame.lanes = _lanes;
		loadedGame.player = _player;
		loadedGame.playerChoice = _player.getBoatNumber();
		loadedGame.opponents = _opponents;
		loadedGame.boatLanes = _boatLanes;
		loadedGame.startDifficulty = _startDifficulty;
		loadedGame.difficulty = _difficulty;
		loadedGame.obstacleTimes = _obstacleTimes;
		loadedGame.progressBar = _progressBar;

		return loadedGame;
	}

	/**
	 * Instantiate the lanes
	 */
	private void createLanes(int obstacleLimit) {
		lanes = new Lane[7];
		int w = Gdx.graphics.getWidth() - 80;
		for (int i = 0; i < lanes.length; i++) {
			lanes[i] = new Lane(
				(i * w / lanes.length) + 40,
				(((i + 1) * w) / lanes.length) + 40,
				obstacleLimit );
		}
	}

	private void updateLaneObstacleLimits(int newLimit) {
		for (Lane l : lanes) {
			l.setObstacleLimit(newLimit);
		}
	}

	/*
	 * Allocate obstacles to the lanes by creating a random sequence
	 * of Y values for obstacles to spawn at for each lane.
	 */
	private void generateObstacleTimes(int noOfObstacles) {
		obstacleTimes = new ArrayList[lanes.length];
		int minY = Gdx.graphics.getHeight();
		int maxY = (int) Math.round(courseTexture.getHeight() - 0.01 * courseTexture.getHeight());
		for (int i = 0; i < lanes.length; i++) {
			obstacleTimes[i] = new ArrayList<>();
			for(int y = 0; y < noOfObstacles; y++) {
				obstacleTimes[i].add(minY + rnd.nextInt(maxY - minY));
			}
			Collections.sort(obstacleTimes[i]);

			if (debug_verboseoutput) {
				System.out.println("Lane " + i + " obstacles to spawn: ");
				for (Integer num : obstacleTimes[i]) {
					System.out.print(num + ", ");
				}
				System.out.println();
			}
		}
	}

	/**
	 * Removes the obstacles from all lanes
	 */
	private void clearLanesObstacles() {
		for (int i = 0; i < lanes.length; i++) {
			lanes[i].removeAllObstacles();
		}
	}
}
