package caseus.dragonboattesting.tests.integration;

import caseus.dragonboattesting.GdxTestRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.lang.reflect.Field;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

import com.dragonboat.game.*;

@ExtendWith(MockitoExtension.class)
public class GooseTest {
	private Goose goose;

	@Mock
    private Lane lane;
    @Mock
    private Texture texture;

    @BeforeEach
    public void setup() {
		goose = new Goose(20, 105, texture, lane);
    }

	/**
	 * Tests that the <i>toJSON</i> method
	 * returns the object in JSON form.
	 */
	@Test
	public void testToJSON() {
		Assertions.assertEquals(String.class, goose.toJSON().getClass());
	}

	/**
	 * Tests that the <i>makeGoose</i> method
	 * recreates the object from the data.
	 */
	@Test
	public void testToJSONAndMakeGoose() {
		String initialDirection = goose.direction;
		float initialYPosition = goose.getY();
		float initialDamage = goose.getDamage();

		String jsonData = goose.toJSON();
		HashMap<String, Object> data = IO.hashMapFromJSON(jsonData);
		Goose loadedGoose = Goose.makeGoose(data, texture, lane);

		String finalDirection = loadedGoose.direction;
		float finalYPosition = loadedGoose.getY();
		float finalDamage = loadedGoose.getDamage();

		Assertions.assertEquals(initialDirection, finalDirection, "Direction");
		Assertions.assertEquals(initialYPosition, finalYPosition, "Y-position");
		Assertions.assertEquals(initialDamage, finalDamage, "Damage");
	}
}
