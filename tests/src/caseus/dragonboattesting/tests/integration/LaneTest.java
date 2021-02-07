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
 * Lane class.
 */
@RunWith(GdxTestRunner.class)
@ExtendWith(MockitoExtension.class)
public class LaneTest {
	private Lane lane;

	@BeforeEach
	public void setup() {

	}

	/**
	 * Tests that the <i>SpawnObstacle</i> method increases
	 * the number of obstacles.
	 *
	 */
	@Test
	public void testSpawnObstacleIncreasesObstacleCount() {
		int leftBoundary = 0;
		int rightBoundary = 20;
		lane = new Lane(leftBoundary, rightBoundary);

		int initialObstacleCount = lane.getObstacleCount();
		lane.SpawnObstacle(leftBoundary + 2, 10, "Goose");
		int finalObstacleCount = lane.getObstacleCount();
		Assertions.assertTrue(initialObstacleCount < finalObstacleCount);
	}

	/**
	 * Tests that the <i>SpawnObstacle</i> method can
	 * spawn a goose.
	 *
	 */
	@Test
	public void testSpawnObstacleCreatesGoose() {
		String obstacleType = "Goose";
		int leftBoundary = 0;
		int rightBoundary = 20;
		lane = new Lane(leftBoundary, rightBoundary);

		lane.SpawnObstacle(leftBoundary + 2, rightBoundary - 2, obstacleType);
		Obstacle outputObstacle = lane.getObstacles().get(lane.getObstacles().size() - 1);
		Assertions.assertEquals(Goose.class, outputObstacle.getClass());
	}

	/**
	 * Tests that the <i>SpawnObstacle</i> method can
	 * spawn a log.
	 *
	 */
	@Test
	public void testSpawnObstacleCreatesLog() {
		String obstacleType = "Log";
		int leftBoundary = 0;
		int rightBoundary = 20;
		lane = new Lane(leftBoundary, rightBoundary);

		lane.SpawnObstacle(leftBoundary + 2, rightBoundary - 2, obstacleType);
		Obstacle outputObstacle = lane.getObstacles().get(lane.getObstacles().size() - 1);
		Assertions.assertEquals(Log.class, outputObstacle.getClass());
	}

	/**
	 * Tests that the <i>SpawnObstacle</i> method can
	 * spawn a boost.
	 *
	 */
	@Test
	public void testSpawnObstacleCreatesBoost() {
		String obstacleType = "Boost";
		int leftBoundary = 0;
		int rightBoundary = 20;
		lane = new Lane(leftBoundary, rightBoundary);

		lane.SpawnObstacle(leftBoundary + 2, rightBoundary - 2, obstacleType);
		Obstacle outputObstacle = lane.getObstacles().get(lane.getObstacles().size() - 1);
		Assertions.assertEquals(Boost.class, outputObstacle.getClass());
	}

	/**
	 * Tests that the <i>SpawnObstacle</i> method will
	 * not spawn more obstacles than the limit.
	 *
	 */
	@Test
	public void testSpawnObstacleReachedObstacleLimit()
			throws IllegalAccessException, NoSuchFieldException {
		String obstacleType = "Log";
		int leftBoundary = 0;
		int rightBoundary = 20;
		lane = new Lane(leftBoundary, rightBoundary);

		for (int count = 0; count < lane.getObstacleLimit() + 4; count++) {
			lane.SpawnObstacle(leftBoundary + 2, rightBoundary - 2, obstacleType);
		}
		Assertions.assertEquals(lane.getObstacleLimit(), lane.getObstacleCount());
	}

	/**
	 * Tests that the <i>toJSON</i> method
	 * returns the object in JSON form.
	 */
	@Test
	public void testToJSON() {
		int leftBoundary = 15;
		int rightBoundary = 30;
		Lane lane = new Lane(leftBoundary, rightBoundary);
		Assertions.assertEquals(String.class, lane.toJSON().getClass());
	}

	/**
	 * Tests that the <i>makeLane</i> method
	 * recreates the object from the data.
	 */
	@Test
	public void testToJSONAndMakeLane() {
		int initialLeftBoundary = 15;
		int initialRightBoundary = 30;
		int initialObstacleLimit = 12;
		ArrayList<Obstacle> initialObstacles;
		Lane lane = new Lane(initialLeftBoundary, initialRightBoundary, initialObstacleLimit);
		lane.SpawnObstacle(17, 11, "Goose");
		lane.SpawnObstacle(22, 43, "Log");
		initialObstacles = lane.getObstacles();

		String jsonData = lane.toJSON();
		HashMap<String, Object> data = IO.hashMapFromJSON(jsonData);
		Lane loadedLane = Lane.makeLane(data);

		int finalLeftBoundary = loadedLane.getLeftBoundary();
		int finalRightBoundary = loadedLane.getRightBoundary();
		int finalObstacleLimit = loadedLane.getObstacleLimit();
		ArrayList<Obstacle> finalObstacles = loadedLane.getObstacles();

		Assertions.assertEquals(initialLeftBoundary, finalLeftBoundary, "Left boundary");
		Assertions.assertEquals(initialRightBoundary, finalRightBoundary, "Right boundary");
		Assertions.assertEquals(initialObstacleLimit, finalObstacleLimit, "Obstacle limit");
		Assertions.assertEquals(initialObstacles.size(), finalObstacles.size(), "Obstacle count");
		Assertions.assertTrue(
			initialObstacles.get(0).getClass().equals(finalObstacles.get(0).getClass())
			|| initialObstacles.get(0).getClass().equals(finalObstacles.get(1).getClass()),
			"Contains an obstacle with class equal to the first");
		Assertions.assertTrue(
			initialObstacles.get(1).getClass().equals(finalObstacles.get(0).getClass())
			|| initialObstacles.get(1).getClass().equals(finalObstacles.get(1).getClass()),
			"Contains an obstacle with class equal to the second");
	}
}
