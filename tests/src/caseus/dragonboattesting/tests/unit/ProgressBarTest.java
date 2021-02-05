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
public class ProgressBarTest {
    private ProgressBar pb;

    @Mock
    private DragonBoatGame game;

    private Lane lane = new Lane(0,100);
    private Player player = new Player(game, 0, 0, 0, lane, "");

    private Opponent[] opponents = new Opponent[6];


    @BeforeEach
    public void setup() {
    }

    @Test
    public void ResetTest(){
        pb = new ProgressBar(player, opponents);
        pb.StartTimer();
        Assertions.assertEquals(0, pb.getTime());
        Assertions.assertEquals(0, pb.getPlayerTime());
    }

    @Test
    public void IncrementTest(){
        pb = new ProgressBar(player, opponents);
        float start = pb.getTime();
        pb.IncrementTimer(100);
        Assertions.assertTrue(pb.getTime() > start);
        Assertions.assertTrue(pb.getPlayerTime() == pb.getTime());
    }

    @Test
    public void AllFinishedTest(){
        player = new Player(game, 10, 0, 0, lane, "");
        for (int j = 0; j<6; j++){
            opponents[j] = new Opponent(game, 10, 0, 0, lane, "");
        }
        pb = new ProgressBar(player, opponents);
        Assertions.assertTrue(pb.allFinished(9));
    }

    @Test
    public void PlayerNotFinishedTest(){
        player = new Player(game, 5, 0, 0, lane, "");
        for (int j = 0; j<6; j++){
            opponents[j] = new Opponent(game, 10, 0, 0, lane, "");
        }
        pb = new ProgressBar(player, opponents);
        Assertions.assertFalse(pb.allFinished(9));
    }

    @Test
    public void OpponentsNotFinishedTest(){
        player = new Player(game, 10, 0, 0, lane, "");
        for (int j = 0; j<6; j++){
            opponents[j] = new Opponent(game, 5, 0, 0, lane, "");
        }
        pb = new ProgressBar(player, opponents);
        Assertions.assertFalse(pb.allFinished(9));
    }

    @Test
    public void AllNotFinishedTest(){
        player = new Player(game, 10, 0, 0, lane, "");
        for (int j = 0; j<6; j++){
            opponents[j] = new Opponent(game, 5, 0, 0, lane, "");
        }
        pb = new ProgressBar(player, opponents);
        Assertions.assertFalse(pb.allFinished(9));
    }

    @Test
    public void GetAllFinishedProgressTest(){
        player = new Player(game, 10, 0, 0, lane, "");
        for (int j = 0; j<6; j++){
            opponents[j] = new Opponent(game, 10, 0, 0, lane, "");
        }
        pb = new ProgressBar(player, opponents);
        float[] other = {1.0F,1.0F,1.0F,1.0F,1.0F,1.0F,1.0F};
        Assertions.assertArrayEquals(pb.getProgress(10), other);
    }

    @Test
    public void GetOpponentFinishedProgressTest(){
        player = new Player(game, 5, 0, 0, lane, "");
        for (int j = 0; j<6; j++){
            opponents[j] = new Opponent(game, 10, 0, 0, lane, "");
        }
        pb = new ProgressBar(player, opponents);
        float[] other = {0.5F,1.0F,1.0F,1.0F,1.0F,1.0F,1.0F};
        Assertions.assertArrayEquals(pb.getProgress(10), other);
    }

    @Test
    public void GetPlayerFinishedProgressTest(){
        player = new Player(game, 10, 0, 0, lane, "");
        for (int j = 0; j<6; j++){
            opponents[j] = new Opponent(game, 5, 0, 0, lane, "");
        }
        pb = new ProgressBar(player, opponents);
        float[] other = {1.0F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F};
        Assertions.assertArrayEquals(pb.getProgress(10), other);
    }

    @Test
    public void GetNoneFinishedProgressTest(){
        player = new Player(game, 5, 0, 0, lane, "");
        for (int j = 0; j<6; j++){
            opponents[j] = new Opponent(game, 5, 0, 0, lane, "");
        }
        pb = new ProgressBar(player, opponents);
        float[] other = {1.0F,1.0F,1.0F,1.0F,1.0F,1.0F,1.0F};
        Assertions.assertFalse(Arrays.equals(pb.getProgress(10), other));
    }

}
