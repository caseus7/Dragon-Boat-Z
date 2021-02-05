package caseus.dragonboattesting.tests.unit;

import caseus.dragonboattesting.GdxTestRunner;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

import com.dragonboat.game.*;

@RunWith(GdxTestRunner.class)
@ExtendWith(MockitoExtension.class)
public class ObstacleTest {
	private Obstacle obstacle;

	@Mock
	private Texture tex;

	private Lane lane;

	private int damage = 10;
	private int xPosition = 10;
	private int yPosition = 10;
	private int width = 10;
	private int height = 10;

	@BeforeEach
	public void setup() {
	}

	@Test
	public void testMove() {
		obstacle = new Obstacle(damage, xPosition, yPosition, width, height, tex, lane);
		float moveAmount = 20;
		float[] expectedPosition = {xPosition, yPosition - moveAmount};
		obstacle.Move(moveAmount);
		float[] finalPosition = {obstacle.getX(), obstacle.getY()};
		Assertions.assertArrayEquals(expectedPosition, finalPosition);
	}

	@Test
	public void testRemove(){
		lane = new Lane(0,100,1);
		obstacle = new Obstacle(damage, xPosition, yPosition, width, height, tex, lane);
		lane.SpawnObstacle(0,0,"Log");
		Obstacle removeObstacle = lane.getObstacles().get(0);
		lane.RemoveObstacle(removeObstacle);
		Assertions.assertTrue(lane.getObstacles().size() == 0);
	}
}
