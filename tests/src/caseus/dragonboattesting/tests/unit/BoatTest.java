package caseus.dragonboattesting.tests.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

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
	public void testSteerLeftMovesLeft() {
		when(lane.getLeftBoundary()).thenReturn(0);
		when(lane.getRightBoundary()).thenReturn(100);
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.setStats(20, 1, 1, 20, 4);
		boat.IncreaseSpeed();
		float initialX = boat.getXPosition();
		boat.SteerLeft();
		float finalX = boat.getXPosition();
		Assertions.assertTrue(initialX > finalX);
	}

	@Test
	public void testSteerLeftAtLeftBoundary() {
		when(lane.getLeftBoundary()).thenReturn(0);
		when(lane.getRightBoundary()).thenReturn(100);
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.setStats(20, 1, 1, 20, 4);
		boat.IncreaseSpeed();
		float initialX = boat.getXPosition();
		// If initialX is not an integer this test won't work
		if (initialX != (int) initialX) {
			String msg = "Boats x position is not an integer. ";
			msg += "Set the mocked `lane.getRightBoundary()` to return a ";
			msg += "number that will cause the boat's position to be an ";
			msg += "integer on construction.";
			throw new java.lang.Error(msg);
		}
		boat.setLeftBound((int) initialX);
		boat.SteerLeft();
		float finalX = boat.getXPosition();
		Assertions.assertEquals(initialX, finalX);
	}

	@Test
	public void testSteerRightMovesRight() {
		when(lane.getLeftBoundary()).thenReturn(0);
		when(lane.getRightBoundary()).thenReturn(100);
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.setStats(20, 1, 1, 20, 4);
		boat.setRightBound(100);
		boat.IncreaseSpeed();
		float initialX = boat.getXPosition();
		boat.SteerRight();
		float finalX = boat.getXPosition();
		Assertions.assertTrue(initialX < finalX);
	}

	@Test
	public void testSteerRightAtRightBound() {
		when(lane.getLeftBoundary()).thenReturn(0);
		when(lane.getRightBoundary()).thenReturn(100);
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.setStats(20, 1, 1, 20, 4);
		boat.IncreaseSpeed();
		float initialX = boat.getXPosition();
		// If initialX is not an integer this test won't work
		if (initialX != (int) initialX) {
			String msg = "Boats x position is not an integer. ";
			msg += "Set the mocked `lane.getRightBoundary()` to return a ";
			msg += "number that will cause the boat's position to be an ";
			msg += "integer on construction.";
			throw new java.lang.Error(msg);
		}
		boat.setRightBound((int) initialX);
		boat.SteerRight();
		float finalX = boat.getXPosition();
		Assertions.assertEquals(initialX, finalX);
	}

	@Test
	public void testMoveForwardMovesUp() {
		when(lane.getLeftBoundary()).thenReturn(0);
		when(lane.getRightBoundary()).thenReturn(10);
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.setStats(20, 1, 1, 20, 4);
		boat.IncreaseSpeed();
		float initialY = boat.getYPosition();
		boat.MoveForward();
		float finalY = boat.getYPosition();
		Assertions.assertTrue(initialY < finalY);
	}

	@Test
	public void testIncreaseSpeedIncreasesSpeed() {
		when(lane.getLeftBoundary()).thenReturn(0);
		when(lane.getRightBoundary()).thenReturn(10);
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.setStats(20, 1, 1, 20, 4);
		float initialSpeed = boat.getCurrentSpeed();
		boat.IncreaseSpeed();
		float finalSpeed = boat.getCurrentSpeed();
		Assertions.assertTrue(initialSpeed < finalSpeed);
	}

	@Test
	public void testIncreaseSpeedAtMaxSpeed()
			throws IllegalAccessException, NoSuchFieldException {
		when(lane.getLeftBoundary()).thenReturn(0);
		when(lane.getRightBoundary()).thenReturn(10);
		int maxSpeed = 10;
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.setStats(maxSpeed, 1, 1, 20, 4);

		// Set private variable `boat.currentSpeed` to `maxSpeed`
		Field instanceCurrentSpeedField = Boat.class.getDeclaredField("currentSpeed");
		instanceCurrentSpeedField.setAccessible(true);
		instanceCurrentSpeedField.set(boat, maxSpeed);

		float initialSpeed = boat.getCurrentSpeed();
		boat.IncreaseSpeed();
		float finalSpeed = boat.getCurrentSpeed();
		Assertions.assertEquals(initialSpeed, finalSpeed);
	}

	@Test
	public void testDecreaseSpeedDecreasesSpeed() {
		when(lane.getLeftBoundary()).thenReturn(0);
		when(lane.getRightBoundary()).thenReturn(10);
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.setStats(20, 1, 1, 20, 4);
		boat.IncreaseSpeed();
		float initialSpeed = boat.getCurrentSpeed();
		boat.DecreaseSpeed();
		float finalSpeed = boat.getCurrentSpeed();
		Assertions.assertTrue(initialSpeed > finalSpeed);
	}

	@Test
	public void testDecreaseSpeedAtZeroSpeed() {
		when(lane.getLeftBoundary()).thenReturn(0);
		when(lane.getRightBoundary()).thenReturn(10);
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.setStats(20, 1, 1, 20, 4);
		float initialSpeed = boat.getCurrentSpeed();
		boat.DecreaseSpeed();
		float finalSpeed = boat.getCurrentSpeed();
		Assertions.assertEquals(initialSpeed, finalSpeed);
	}

	@Test
	public void testApplyDamageReducesDurability() {
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
	public void testApplyDamageNoDivisionByZeroError() {
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
