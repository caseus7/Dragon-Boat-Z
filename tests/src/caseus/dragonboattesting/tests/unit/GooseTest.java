package caseus.dragonboattesting.tests.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import com.badlogic.gdx.graphics.Texture;

import com.dragonboat.game.*;

/**
 * --ASSESSMENT 2--
 * Integration test for the methods within the
 * Goose class.
 */
@ExtendWith(MockitoExtension.class)
public class GooseTest {
	private Goose goose;

	@Mock
	private Texture tex;
	@Mock
	private Lane lane;

	private int xPosition = 10;
	private int yPosition = 10;

	@BeforeEach
	public void setup() {
		goose = new Goose(xPosition, yPosition, tex, lane);
	}

	/**
	 * Tests that the <i>ChangeDirection</i> method
	 * successfully changes the direction of a goose
	 * obstacle.
	 */
	@Test
	void testChangeDirection() {
		String initialDirection = goose.direction;
		goose.changeDirection();
		String finalDirection = goose.direction;
		Assertions.assertNotEquals(initialDirection, finalDirection);
	}

	/**
	 * Tests that the <i>Move</i> method successfully
	 * moves a goose obstacle.
	 */
	@Test
	void testMove() {
		float moveVal = 20;
		int backgroundOffset = 10;
		float[] currentPosition = {goose.getX(), goose.getY()};
		goose.Move(moveVal, backgroundOffset);
		float[] finalPosition = {goose.getX(), goose.getY()};
		Assertions.assertFalse(Arrays.equals(currentPosition, finalPosition));
	}
}
