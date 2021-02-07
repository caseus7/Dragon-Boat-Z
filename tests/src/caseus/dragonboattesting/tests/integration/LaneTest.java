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
// @RunWith(GdxTestRunner.class)
public class LaneTest {
	private Lane lane;

	@BeforeEach
	public void setup() {

	}

	/**
	 * Tests that the <i>SpawnObstacle</i> method increases
	 * the number of obstacles.
	 *
 	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	@Test
	public void testSpawnObstacleIncreasesObstacleCount()
			throws IllegalAccessException, NoSuchFieldException {
		int leftBoundary = 0;
		int rightBoundary = 20;
		lane = new Lane(leftBoundary, rightBoundary);
		// setSpritePaths(lane);

		int initialObstacleCount = lane.getObstacleCount();
		lane.SpawnObstacle(leftBoundary + 2, 10, "Goose");
		int finalObstacleCount = lane.getObstacleCount();
		Assertions.assertTrue(initialObstacleCount < finalObstacleCount);
	}

	/**
	 * Tests that the <i>SpawnObstacle</i> method can
	 * spawn a goose.
	 *
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	@Test
	public void testSpawnObstacleCreatesGoose()
			throws IllegalAccessException, NoSuchFieldException {
		String obstacleType = "Goose";
		int leftBoundary = 0;
		int rightBoundary = 20;
		lane = new Lane(leftBoundary, rightBoundary);
		// setSpritePaths(lane);

		lane.SpawnObstacle(leftBoundary + 2, rightBoundary - 2, obstacleType);
		Obstacle outputObstacle = lane.getObstacles().get(lane.getObstacles().size() - 1);
		Assertions.assertEquals(Goose.class, outputObstacle.getClass());
	}

	/**
	 * Tests that the <i>SpawnObstacle</i> method can
	 * spawn a log.
	 *
 	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	@Test
	public void testSpawnObstacleCreatesLog()
			throws IllegalAccessException, NoSuchFieldException {
		String obstacleType = "Log";
		int leftBoundary = 0;
		int rightBoundary = 20;
		lane = new Lane(leftBoundary, rightBoundary);
		// setSpritePaths(lane);

		lane.SpawnObstacle(leftBoundary + 2, rightBoundary - 2, obstacleType);
		Obstacle outputObstacle = lane.getObstacles().get(lane.getObstacles().size() - 1);
		Assertions.assertEquals(Log.class, outputObstacle.getClass());
	}

	/**
	 * Tests that the <i>SpawnObstacle</i> method can
	 * spawn a boost.
	 *
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	@Test
	public void testSpawnObstacleCreatesBoost()
			throws IllegalAccessException, NoSuchFieldException {
		String obstacleType = "Boost";
		int leftBoundary = 0;
		int rightBoundary = 20;
		lane = new Lane(leftBoundary, rightBoundary);
		// setSpritePaths(lane);

		lane.SpawnObstacle(leftBoundary + 2, rightBoundary - 2, obstacleType);
		Obstacle outputObstacle = lane.getObstacles().get(lane.getObstacles().size() - 1);
		Assertions.assertEquals(Boost.class, outputObstacle.getClass());
	}

	/**
	 * Tests that the <i>SpawnObstacle</i> method will
	 * not spawn more obstacles than the limit.
	 *
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	@Test
	public void testSpawnObstacleReachedObstacleLimit()
			throws IllegalAccessException, NoSuchFieldException {
		String obstacleType = "Log";
		int leftBoundary = 0;
		int rightBoundary = 20;
		lane = new Lane(leftBoundary, rightBoundary);
		// setSpritePaths(lane);

		for (int count = 0; count < lane.getObstacleLimit() + 4; count++) {
			lane.SpawnObstacle(leftBoundary + 2, rightBoundary - 2, obstacleType);
		}
		Assertions.assertEquals(lane.getObstacleLimit(), lane.getObstacleCount());
	}

	/**
	 * Sets the paths to the sprites, since the relative
	 * paths are different during testing.
	 *
	 * @param lane The lane object.
	 */
	private void setSpritePaths(Lane lane)
			throws IllegalAccessException, NoSuchFieldException {
		String newGooseSpritePath = "../core/assets/gooseSouthsprite.png";
		Field instanceGooseSpritePathField =
			Lane.class.getDeclaredField("gooseSpritePath");
		instanceGooseSpritePathField.setAccessible(true);
		instanceGooseSpritePathField.set(lane, newGooseSpritePath);

		String newLogSpritePath = "../core/assets/logBig sprite.png";
		Field instanceLogSpritePathField =
			Lane.class.getDeclaredField("logSpritePath");
		instanceLogSpritePathField.setAccessible(true);
		instanceLogSpritePathField.set(lane, newLogSpritePath);

		String[] newBoostPrefixes = {
			"../core/assets/acceleration",
	        "../core/assets/health",
	        "../core/assets/immune",
	        "../core/assets/maneuverability",
	        "../core/assets/speed" };
		Field instanceBoostPrefixesField =
			Lane.class.getDeclaredField("boostSpritePathPrefixes");
		instanceBoostPrefixesField.setAccessible(true);
		instanceBoostPrefixesField.set(lane, newBoostPrefixes);
	}
}
