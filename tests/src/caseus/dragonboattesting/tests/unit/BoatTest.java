package caseus.dragonboattesting.tests.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dragonboat.game.*;


@ExtendWith(MockitoExtension.class)
public class BoatTest {
	private Boat boat;

	@Mock
	private Lane lane;
	@Mock
	private DragonBoatGame game;

	@BeforeEach
	public void setup() {

	}

	@Test
	void testApplyDamageReducesDurability() {
		when(lane.getLeftBoundary()).thenReturn(0);
		when(lane.getRightBoundary()).thenReturn(10);
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.setStats(0, 1, 1, 0, 0);

		int obstacleDamage = 20;
		int initialDurability = boat.getDurability();
		boat.ApplyDamage(obstacleDamage);
		int finalDurability = boat.getDurability();

		Assertions.assertTrue(initialDurability > finalDurability);
	}

	@Test
	void testApplyDamageNoDivisionByZeroError() {
		// On initialisation, boat's robustness is 0 and one way damage to the
		// boat is calculated is by dividing obstacle damage by the boat's
		// robustness, causing division by 0 if no checks are in place
		when(lane.getLeftBoundary()).thenReturn(0);
		when(lane.getRightBoundary()).thenReturn(10);
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		int obstacleDamage = 20;
		boat.ApplyDamage(obstacleDamage);
	}
}
