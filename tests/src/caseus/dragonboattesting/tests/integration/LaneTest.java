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

import caseus.dragonboattesting.GdxTestRunner;

import com.dragonboat.game.Lane;
import com.dragonboat.game.Obstacle;
import com.dragonboat.game.Log;
import com.dragonboat.game.Goose;


@ExtendWith(MockitoExtension.class)
@RunWith(GdxTestRunner.class)
public class LaneTest {
	private Lane lane;

	@BeforeEach
	public void setup() {

	}

	@Test
	void testSpawnObstacleIncreasesObstacleCount() {
		int leftBoundary = 0;
		int rightBoundary = 20;
		lane = new Lane(leftBoundary, rightBoundary);

		int initialObstacleCount = lane.getObstacleCount();
		lane.SpawnObstacle(leftBoundary + 2, 10, "Goose");
		int finalObstacleCount = lane.getObstacleCount();
		Assertions.assertTrue(initialObstacleCount < finalObstacleCount);
	}

	// @Test
	// void testSpawnObstacleMakesCorrectType() {
	// 	String obstacleType = "Goose";
	// 	int leftBoundary = 0;
	// 	int rightBoundary = 20;
	// 	lane = new Lane(leftBoundary, rightBoundary);
	// 	lane.SpawnObstacle(leftBoundary + 2, rightBoundary - 2, obstacleType);
	// 	Obstacle outputObstacle = lane.getObstacles().get(lane.getObstacles().size() - 1);
	// 	System.out.println("Obstacle type: " + outputObstacle.getClass());
	// 	Assertions.assertEquals(Goose.class, outputObstacle.getClass());
	// }

	// @Test
	// void testSpawnObstacleReachedObstacleLimit() {
	//
	// }
}
