package caseus.dragonboattesting.tests.integration;

import caseus.dragonboattesting.GdxTestRunner;
import com.badlogic.gdx.Gdx;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.lang.reflect.Field;

import com.dragonboat.game.*;

/**
 * --ASSESSMENT 2--
 * Integration test for the methods within the
 * Player class.
 */
@ExtendWith(MockitoExtension.class)
public class PlayerTest {
	private Player player;

	@Mock
    private DragonBoatGame game;
    @Mock
    private Lane lane;

    @BeforeEach
    public void setup() {
		player = new Player(game, 40, lane, "testPlayer");
    }

	/**
	 * Tests that the <i>toJSON</i> method
	 * returns the object in JSON form.
	 */
	@Test
	public void testToJSON() {
		Assertions.assertEquals(String.class, player.toJSON().getClass());
	}

	/**
	 * Tests that the <i>makePlayer</i> method
	 * recreates the method from the data.
	 */
	@Test
	public void testToJSONAndMakePlayer() {
		int initialBoatNumber = 2;
		String initialBoosted = "acceleration";
		float initialAcceleration;
		player.ChooseBoat(initialBoatNumber);
		player.Boost(initialBoosted);
		initialAcceleration = player.getAcceleration();

		String jsonData = player.toJSON();
		HashMap<String, Object> data = IO.hashMapFromJSON(jsonData);
		Player loadedPlayer = Player.makePlayer(data, game, lane);

		int finalBoatNumber = loadedPlayer.getBoatNumber();
		String finalBoosted = loadedPlayer.getBoosted();
		float finalAcceleration = loadedPlayer.getAcceleration();

		Assertions.assertEquals(initialBoatNumber, finalBoatNumber, "Boat number");
		Assertions.assertEquals(initialBoosted, finalBoosted, "Boosted");
		Assertions.assertEquals(initialAcceleration, finalAcceleration, "Acceleration");
	}
}
