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
public class LaneTest {
	private Lane lane;

	@Mock
	private Obstacle obst;

	@BeforeEach
	public void setup() {

	}
}
