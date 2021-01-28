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

import com.dragonboat.game.Goose;
import com.dragonboat.game.Lane;


@ExtendWith(MockitoExtension.class)
public class GooseTest {
	private Goose goose;

	@Mock
	Texture tex;

	@Mock
	Lane lane;

	int xPosition = 10;
	int yPosition = 10;

	@BeforeEach
	public void setup() {
		goose = new Goose(xPosition, yPosition, tex, lane);
	}

	@Test
	void testChangeDirection() {
		String initialDirection = goose.direction;
		goose.changeDirection();
		String finalDirection = goose.direction;
		Assertions.assertNotEquals(initialDirection, finalDirection);
	}

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
