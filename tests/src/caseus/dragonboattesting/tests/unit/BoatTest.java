package caseus.dragonboattesting.tests.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dragonboat.game.Boat;
import com.dragonboat.game.Lane;


@ExtendWith(MockitoExtension.class)
public class BoatTest {
	private Boat boat;

	@Mock
	private Lane lane;

	@BeforeEach
	public void setup() {

	}

	@Test
	void testApplyDamageReducesDurability() {
		when(lane.getLeftBoundary()).thenReturn(0);
		when(lane.getRightBoundary()).thenReturn(10);
		boat = new Boat(10, 10, 10, lane, "testBoat");
		boat.setStats(0, 1, 0, 0);

		int obstacleDamage = 20;
		int initialDurability = boat.getDurability();
		boat.ApplyDamage(obstacleDamage);
		int finalDurability = boat.getDurability();

		Assertions.assertTrue(initialDurability > finalDurability);
	}

	@Test
	void testApplyDamageNoDivisionByZeroError() {
		when(lane.getLeftBoundary()).thenReturn(0);
		when(lane.getRightBoundary()).thenReturn(10);
		boat = new Boat(10, 10, 10, lane, "testBoat");
		// boat.setStats() not used so all those values will be 0, possibly causing
		// division by 0
		int obstacleDamage = 20;
		boat.ApplyDamage(obstacleDamage);
	}
}
