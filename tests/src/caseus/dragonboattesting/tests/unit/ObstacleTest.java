package caseus.dragonboattesting.tests.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.badlogic.gdx.graphics.Texture;

import com.dragonboat.game.Obstacle;


@ExtendWith(MockitoExtension.class)
public class ObstacleTest {
	private Obstacle obstacle;

	@Mock
	Texture tex;

	int damage = 10;
	int xPosition = 10;
	int yPosition = 10;
	int width = 10;
	int height = 10;

	@BeforeEach
	public void setup() {
		obstacle = new Obstacle(damage, xPosition, yPosition, width, height, tex);
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
