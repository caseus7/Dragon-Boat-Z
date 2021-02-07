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
public class BoostTest {
	private Boost boost;

	@Mock
    private Lane lane;
    @Mock
    private Texture texture;

    @BeforeEach
    public void setup() {
		boost = new Boost(20, 105, texture, lane, "immune");
    }

	/**
	 * Tests that the <i>toJSON</i> method
	 * returns the object in JSON form.
	 */
	@Test
	public void testToJSON() {
		Assertions.assertEquals(String.class, boost.toJSON().getClass());
	}

	/**
	 * Tests that the <i>makeBoost</i> method
	 * recreates the object from the data.
	 */
	@Test
	public void testToJSONAndMakeBoost() {
		String initialType = boost.getType();
		float initialYPosition = boost.getY();
		int initialHeight = boost.getHeight();

		String jsonData = boost.toJSON();
		HashMap<String, Object> data = IO.hashMapFromJSON(jsonData);
		Boost loadedBoost = Boost.makeBoost(data, texture, lane);

		String finalType = loadedBoost.getType();
		float finalYPosition = loadedBoost.getY();
		int finalHeight = loadedBoost.getHeight();

		Assertions.assertEquals(initialType, finalType, "Type");
		Assertions.assertEquals(initialYPosition, finalYPosition, "Y-position");
		Assertions.assertEquals(initialHeight, finalHeight, "Height");
	}
}
