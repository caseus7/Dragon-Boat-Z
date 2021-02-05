package caseus.dragonboattesting.tests.unit;

import caseus.dragonboattesting.GdxTestRunner;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import com.dragonboat.game.*;

@RunWith(GdxTestRunner.class)
@ExtendWith(MockitoExtension.class)
public class BoatTest {
	private Boat boat;

	private Lane lane = new Lane(0,100);

	@Mock
	private DragonBoatGame game;

	@BeforeEach
	public void setup() {

	}

	@Test
	public void testSteerLeftMovesLeft() {
//		when(lane.getLeftBoundary()).thenReturn(0);
//		when(lane.getRightBoundary()).thenReturn(100);
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
//		when(lane.getLeftBoundary()).thenReturn(0);
//		when(lane.getRightBoundary()).thenReturn(100);
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
//		when(lane.getLeftBoundary()).thenReturn(0);
//		when(lane.getRightBoundary()).thenReturn(100);
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
//		when(lane.getLeftBoundary()).thenReturn(0);
//		when(lane.getRightBoundary()).thenReturn(100);
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
//		when(lane.getLeftBoundary()).thenReturn(0);
//		when(lane.getRightBoundary()).thenReturn(10);
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
//		when(lane.getLeftBoundary()).thenReturn(0);
//		when(lane.getRightBoundary()).thenReturn(10);
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
//		when(lane.getLeftBoundary()).thenReturn(0);
//		when(lane.getRightBoundary()).thenReturn(10);
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
//		when(lane.getLeftBoundary()).thenReturn(0);
//		when(lane.getRightBoundary()).thenReturn(10);
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
//		when(lane.getLeftBoundary()).thenReturn(0);
//		when(lane.getRightBoundary()).thenReturn(10);
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.setStats(20, 1, 1, 20, 4);
		float initialSpeed = boat.getCurrentSpeed();
		boat.DecreaseSpeed();
		float finalSpeed = boat.getCurrentSpeed();
		Assertions.assertEquals(initialSpeed, finalSpeed);
	}

	@Test
	public void testApplyDamageReducesDurability() {
//		when(lane.getLeftBoundary()).thenReturn(0);
//		when(lane.getRightBoundary()).thenReturn(10);
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
//		when(lane.getLeftBoundary()).thenReturn(0);
//		when(lane.getRightBoundary()).thenReturn(10);
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		int obstacleDamage = 20;
		boat.ApplyDamage(obstacleDamage);
	}

	@Test
	public void BoostAppliedCorrectly(){
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		int startDura = boat.getDurability();
		boat.Boost("health");
		Assertions.assertTrue(startDura < boat.getDurability());
		float startAcc = boat.getAcceleration();
		boat.Boost("acceleration");
		Assertions.assertTrue(startAcc < boat.getAcceleration() && boat.isBoosted());
		boat.removeBoost();
		boat.Boost("immune");
		Assertions.assertTrue(boat.getImmune() && boat.isBoosted());
		boat.removeBoost();
		float startMan = boat.getManeuverability();
		boat.Boost("maneuverability");
		Assertions.assertTrue(startMan < boat.getManeuverability() && boat.isBoosted());
		boat.removeBoost();
		int startMaxSpeed = boat.getMaxSpeed();
		float startCurrentSpeed = boat.getCurrentSpeed();
		boat.Boost("speed");
		Assertions.assertTrue(startCurrentSpeed < boat.getCurrentSpeed() && startMaxSpeed < boat.getMaxSpeed() && boat.isBoosted());
	}

	@Test
	public void removeBoostWhenBoosted(){
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.Boost("speed");
		boat.removeBoost();
		Assertions.assertTrue(boat.isBoosted() == false);
	}

	@Test
	public void removeBoostWhenNotBoosted(){
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.removeBoost();
		Assertions.assertTrue(boat.isBoosted() == false);
	}

	@Test
	public void LaneCheckInLane(){
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
//		this.xPosition = lane.getRightBoundary() - (lane.getRightBoundary() - lane.getLeftBoundary()) / 2 - width / 2;
		Assertions.assertTrue(boat.CheckIfInLane());
	}

	@Test
	public void LaneCheckInLaneRightBoundaryLimit(){
		lane = new Lane(0,100);
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.setXPosition(94);
		Assertions.assertTrue(boat.CheckIfInLane());
	}

	@Test
	public void LaneCheckInLaneLeftBoundaryLimit(){
		lane = new Lane(0,100);
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.setXPosition(-4);
		Assertions.assertTrue(boat.CheckIfInLane());
	}

	@Test
	public void LaneCheckOutsideLaneRightBoundaryLimit(){
		lane = new Lane(0,100);
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.setXPosition(95);
		Assertions.assertFalse(boat.CheckIfInLane());
	}

	@Test
	public void LaneCheckOutsideLaneLeftBoundaryLimit(){
		lane = new Lane(0,100);
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.setXPosition(-5);
		Assertions.assertFalse(boat.CheckIfInLane());
	}

	@Test
	public void SuccessfulFastestTimeZeroUpdateTest(){
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		float beforeUpdate = boat.getFastestTime();
		boat.UpdateFastestTime(10);
		Assertions.assertTrue(beforeUpdate < boat.getFastestTime());
	}
	@Test
	public void SuccessfulFastestTimeNonZeroUpdateTest(){
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.UpdateFastestTime(10);
		float beforeUpdate = boat.getFastestTime();
		boat.UpdateFastestTime(9);
		Assertions.assertTrue(beforeUpdate > boat.getFastestTime());
	}

	@Test
	public void UnsuccessfulFastestTimeNonZeroUpdateTest(){
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.UpdateFastestTime(10);
		float beforeUpdate = boat.getFastestTime();
		boat.UpdateFastestTime(11);
		Assertions.assertFalse(beforeUpdate > boat.getFastestTime());
	}

	@Test
	public void SuccessfulFastestTimeNonZeroUpdateTestWithPenalties(){
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.UpdateFastestTime(10);
		float beforeUpdate = boat.getFastestTime();
		boat.applyPenalty(2);
		boat.UpdateFastestTime(7);
		Assertions.assertTrue(beforeUpdate > boat.getFastestTime());
	}

	@Test
	public void ResetTest(){
		boat = new Boat(game, 10, 10, 10, lane, "testBoat");
		boat.setXPosition(10);
		boat.IncreaseSpeed();
		boat.applyPenalty(10);
		boat.ApplyDamage(1);
		boat.IncreaseTiredness();
		boat.setFinished(true);
		boat.Reset();
		Assertions.assertTrue(boat.getXPosition() == 45 && boat.getYPosition() == 0 && boat.getCurrentSpeed() == 0f && boat.getPenalty() == 0 && boat.getDurability() == boat.getMaxDurability() && boat.getTiredness() == 0f && boat.finished() == false);
	}
}
