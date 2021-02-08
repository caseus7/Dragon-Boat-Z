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
 * Opponent class.
 */
@ExtendWith(MockitoExtension.class)
public class OpponentTest {
	private Opponent opponent;

	@Mock
    private DragonBoatGame game;
    @Mock
    private Lane lane;

    @BeforeEach
    public void setup() {
		opponent = new Opponent(game, 40, lane, "testOpponent");
    }

	/**
	 * Tests that the <i>toJSON</i> method
	 * returns the object in JSON form.
	 */
	@Test
	public void testToJSON() {
		Assertions.assertEquals(String.class, opponent.toJSON().getClass());
	}

	/**
	 * Tests that the <i>makeOpponent</i> method
	 * recreates the method from the data.
	 */
	@Test
	public void testToJSONAndMakeOpponent() {
		int initialBoatNumber = 4;
		String initialBoosted = "speed";
		float initialMaxSpeed;
		opponent.ChooseBoat(initialBoatNumber);
		opponent.Boost(initialBoosted);
		initialMaxSpeed = opponent.getMaxSpeed();

		String jsonData = opponent.toJSON();
		HashMap<String, Object> data = IO.hashMapFromJSON(jsonData);
		Opponent loadedOpponent = Opponent.makeOpponent(data, game, lane);

		int finalBoatNumber = loadedOpponent.getBoatNumber();
		String finalBoosted = loadedOpponent.getBoosted();
		float finalMaxSpeed = loadedOpponent.getMaxSpeed();

		Assertions.assertEquals(initialBoatNumber, finalBoatNumber, "Boat number");
		Assertions.assertEquals(initialBoosted, finalBoosted, "Boosted");
		Assertions.assertEquals(initialMaxSpeed, finalMaxSpeed, "Max speed");
	}
}
