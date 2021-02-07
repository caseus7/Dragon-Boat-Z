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
public class LogTest {
	private Log log;

	@Mock
    private Lane lane;
    @Mock
    private Texture texture;

    @BeforeEach
    public void setup() {
		log = new Log(20, 105, texture, lane);
    }

	/**
	 * Tests that the <i>toJSON</i> method
	 * returns the object in JSON form.
	 */
	@Test
	public void testToJSON() {
		Assertions.assertEquals(String.class, log.toJSON().getClass());
	}

	/**
	 * Tests that the <i>makeLog</i> method
	 * recreates the object from the data.
	 */
	@Test
	public void testToJSONAndMakeLog() {
		int initialWidth = log.width;
		float initialYPosition = log.getY();
		float initialDamage = log.getDamage();

		String jsonData = log.toJSON();
		HashMap<String, Object> data = IO.hashMapFromJSON(jsonData);
		Log loadedLog = Log.makeLog(data, texture, lane);

		int finalWidth = loadedLog.width;
		float finalYPosition = loadedLog.getY();
		float finalDamage = loadedLog.getDamage();

		Assertions.assertEquals(initialWidth, finalWidth, "Width");
		Assertions.assertEquals(initialYPosition, finalYPosition, "Y-position");
		Assertions.assertEquals(initialDamage, finalDamage, "Damage");
	}
}
