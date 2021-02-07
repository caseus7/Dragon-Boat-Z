package caseus.dragonboattesting.tests.integration;

// import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.Field;

import caseus.dragonboattesting.GdxTestRunner;

import com.dragonboat.game.*;

/**
 * --ASSESSMENT 2--
 * Integration test for the methods within the
 * DragonBoatGame class.
 */
 @RunWith(GdxTestRunner.class)
 @ExtendWith(MockitoExtension.class)
public class DragonBoatGameTest {
	private String testsPath = "../../tests/src/caseus/dragonboattesting/";
	// Do not modify these files
	private String exampleSaveGamePath = testsPath + "gameSaveExample.json";

	private DragonBoatGame game;

	@Mock
	private StartGame stGame;

	@BeforeEach
	public void setup() {
		game = new DragonBoatGame(stGame, false);
	}

	/**
	 * Tests that the <i>toJSON</i> method
	 * returns the object in JSON form.
	 */
	// @Test
	// public void testToJSON() {
	// 	Assertions.assertEquals(String.class, game.toJSON().getClass());
	// }

	/**
	 * Tests that the <i>makeDragonBoatGame</i> method
	 * recreates the object from the data.
	 */
	@Test
	public void testMakeDragonBoatGame() {
		String dataString = IO.readFile(exampleSaveGamePath);
		HashMap<String, Object> data = IO.hashMapFromJSON(dataString);
		DragonBoatGame result = DragonBoatGame.makeDragonBoatGame(data, this.stGame);

		int difficulty = result.getDifficulty();
		boolean madeLanes = result.lanes[0].getClass().equals(Lane.class);
		int boatNumber = result.player.getBoatNumber();

		Assertions.assertEquals(Lane.class, result.lanes[0].getClass(), "lanes' class type");
		Assertions.assertEquals(3, difficulty, "Difficulty");
		Assertions.assertEquals(3, boatNumber, "Boat number");
	}

	// @Test
	// public void testToJSONAndMakeDragonBoatGame() {
	// 	String jsonData = game.toJSON();
	// 	HashMap<String, Object> data = IO.hashMapFromJSON(jsonData);
	// 	DragonBoatGame result = game.makeDragonBoatGame(data, this.stGame);
	//  ...
	// 	Assertions.assertTrue(madeLanes && difficulty == 3 && boatNumber == 3);
	// }

	// /**
	//  * Tests that the <i>getObstacles</i> method
	//  * returns the correct amount of obstacles.
	//  */
	// @Test
	// public void testGetObstaclesReturnsCorrectAmount() {
	// 	game = new DragonBoatGame(stGame, false);
	// 	game.init();
	// 	ArrayList<Obstacle> obs = game.getObstacles();
	// 	Assertions.assertEquals(8, obs.size());
	// }
	//
	// /**
	//  * Tests that the <i>getObstacles</i> method
	//  * returns objects of type Obstacle.
	//  */
	// @Test
	// public void testGetObstaclesReturnsCorrectType() {
	// 	game = new DragonBoatGame(stGame, false);
	// 	game.init();
	// 	ArrayList<Obstacle> obs = game.getObstacles();
	// 	Assertions.assertEquals(Obstacle.class, obs.get(0).getClass());
	// }
}
