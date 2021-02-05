package caseus.dragonboattesting.tests.unit;

import caseus.dragonboattesting.GdxTestRunner;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Arrays;

import com.dragonboat.game.*;

@RunWith(GdxTestRunner.class)
@ExtendWith(MockitoExtension.class)
public class LeaderboardTest {

    private Leaderboard lb;

    @Mock
    private DragonBoatGame game;

    private Lane lane = new Lane(0,100);
    private Player player = new Player(game, 0, 0, 0, lane, "");

    private Opponent[] opponents = new Opponent[6];

    @Test
    public void getTimesTest(){
        lb = new Leaderboard(player, opponents);

    }

}