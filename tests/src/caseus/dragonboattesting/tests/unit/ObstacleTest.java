package caseus.dragonboattesting.tests.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.badlogic.gdx.graphics.Texture;

import com.dragonboat.game.*;


@ExtendWith(MockitoExtension.class)
public class ObstacleTest {
	private Obstacle obstacle;

	@Mock
	private Texture tex;
	@Mock
	private Lane lane;

	private int damage = 10;
	private int xPosition = 10;
	private int yPosition = 10;
	private int width = 10;
	private int height = 10;

	@BeforeEach
	public void setup() {
		obstacle = new Obstacle(damage, xPosition, yPosition, width, height, tex, lane);
	}

	@Test
	void testGetX() {
		Assertions.assertEquals(xPosition, obstacle.getX());
	}

	@Test
	void testGetY() {
		Assertions.assertEquals(yPosition, obstacle.getY());
	}

	@Test
	void testMove() {
		float moveAmount = 20;
		float[] expectedPosition = {xPosition, yPosition - moveAmount};
		obstacle.Move(moveAmount);
		float[] finalPosition = {obstacle.getX(), obstacle.getY()};

		Assertions.assertArrayEquals(expectedPosition, finalPosition);
	}
}
