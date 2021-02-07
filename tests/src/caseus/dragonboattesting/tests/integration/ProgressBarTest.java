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
public class ProgressBarTest {
	private ProgressBar progressBar;

	@Mock
    private Player player;
    @Mock
	private Opponent opponent1;
	@Mock
	private Opponent opponent2;
	@Mock
	private Opponent opponent3;

    private Opponent[] opponents = {opponent1, opponent2, opponent3};

    @BeforeEach
    public void setup() {
		progressBar = new ProgressBar(player, opponents);
    }

	/**
	 * Tests that the <i>toJSON</i> method
	 * returns the object in JSON form.
	 */
	@Test
	public void testToJSON() {
		Assertions.assertEquals(String.class, progressBar.toJSON().getClass());
	}

	/**
	 * Tests that the <i>makeProgressBar</i> method
	 * recreates the object from the data.
	 */
	@Test
	public void testToJSONAndMakeProgressBar() {
		progressBar.IncrementTimer(10f);
		float initialTimeSeconds = progressBar.getTime();
		float initialPlayerTime = progressBar.getPlayerTime();

		String jsonData = progressBar.toJSON();
		HashMap<String, Object> data = IO.hashMapFromJSON(jsonData);
		ProgressBar loadedProgressBar = ProgressBar.makeProgressBar(data, player, opponents);

		float finalTimeSeconds = loadedProgressBar.getTime();
		float finalPlayerTime = loadedProgressBar.getPlayerTime();

		Assertions.assertEquals(initialTimeSeconds, finalTimeSeconds, "Time seconds");
		Assertions.assertEquals(initialPlayerTime, finalPlayerTime, "Player time");
	}
}
