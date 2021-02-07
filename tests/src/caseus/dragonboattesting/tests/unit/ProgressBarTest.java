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

/**
 * --ASSESSMENT 2--
 * Unit test for the methods within the
 * ProgressBar class.
 */
@RunWith(GdxTestRunner.class)
@ExtendWith(MockitoExtension.class)
public class ProgressBarTest {
    private ProgressBar pb;

    @Mock
    private DragonBoatGame game;

    private Lane lane = new Lane(0,100);
    private Player player = new Player(game, 0, lane, "");

    private Opponent[] opponents = new Opponent[6];


    @BeforeEach
    public void setup() {
    }

    @Test
    public void testReset(){
        pb = new ProgressBar(player, opponents);
        pb.StartTimer();
        Assertions.assertEquals(0, pb.getTime());
        Assertions.assertEquals(0, pb.getPlayerTime());
    }

    /**
     * Tests that the <i>IncrementTimer</i> method can
     * successfully increment the timer.
     */
    @Test
    public void testIncrement(){
        pb = new ProgressBar(player, opponents);
        float start = pb.getTime();
        pb.IncrementTimer(100);
        Assertions.assertTrue(pb.getTime() > start);
        Assertions.assertTrue(pb.getPlayerTime() == pb.getTime());
    }

    /**
     * Tests that the <i>allFinished</i> method
     * returns true if all of the boats have finished
     * the race.
     */
    @Test
    public void testAllFinished(){
        player = new Player(game, 10, lane, "");
        for (int j = 0; j<6; j++){
            opponents[j] = new Opponent(game, 10, lane, "");
        }
        pb = new ProgressBar(player, opponents);
        Assertions.assertTrue(pb.allFinished(9));
    }

    /**
     * Tests that the <i>allFinished</i> method can
     * determine when the player is not finished.
     */
    @Test
    public void testPlayerNotFinished(){
        player = new Player(game, 5, lane, "");
        for (int j = 0; j<6; j++){
            opponents[j] = new Opponent(game, 10, lane, "");
        }
        pb = new ProgressBar(player, opponents);
        Assertions.assertFalse(pb.allFinished(9));
    }

    /**
     * Tests that the <i>allFinished</i> method can
     * determine when the opponents are not finished.
     */
    @Test
    public void testOpponentsNotFinished(){
        player = new Player(game, 10, lane, "");
        for (int j = 0; j<6; j++){
            opponents[j] = new Opponent(game, 5, lane, "");
        }
        pb = new ProgressBar(player, opponents);
        Assertions.assertFalse(pb.allFinished(9));
    }

    /**
     * Tests that the <i>allFinished</i> method can
     * determine when none of the boats are finished.
     */
    @Test
    public void testAllNotFinished(){
        player = new Player(game, 10, lane, "");
        for (int j = 0; j<6; j++){
            opponents[j] = new Opponent(game, 5, lane, "");
        }
        pb = new ProgressBar(player, opponents);
        Assertions.assertFalse(pb.allFinished(9));
    }

    /**
     * Tests that the <i>getProgress</i> method can
     * determine when every boat is finished.
     */
    @Test
    public void testAllFinishedProgress(){
        player = new Player(game, 10, lane, "");
        for (int j = 0; j<6; j++){
            opponents[j] = new Opponent(game, 10, lane, "");
        }
        pb = new ProgressBar(player, opponents);
        float[] other = {1.0F,1.0F,1.0F,1.0F,1.0F,1.0F,1.0F};
        Assertions.assertArrayEquals(pb.getProgress(10), other);
    }

    /**
     * Tests that the <i>getProgress</i> method can
     * determine when all the opponents are finished.
     */
    @Test
    public void testOpponentsFinishedProgress(){
        player = new Player(game, 5, lane, "");
        for (int j = 0; j<6; j++){
            opponents[j] = new Opponent(game, 10, lane, "");
        }
        pb = new ProgressBar(player, opponents);
        float[] other = {0.5F,1.0F,1.0F,1.0F,1.0F,1.0F,1.0F};
        Assertions.assertArrayEquals(pb.getProgress(10), other);
    }

    /**
     * Tests that the <i>getProgress</i> method can
     * determine when the player is finished.
     */
    @Test
    public void testPlayerFinishedProgress(){
        player = new Player(game, 10, lane, "");
        for (int j = 0; j<6; j++){
            opponents[j] = new Opponent(game, 5, lane, "");
        }
        pb = new ProgressBar(player, opponents);
        float[] other = {1.0F,0.5F,0.5F,0.5F,0.5F,0.5F,0.5F};
        Assertions.assertArrayEquals(pb.getProgress(10), other);
    }

    /**
     * Tests that the <i>getProgress</i> method can
     * determine when none of the boats are finished.
     */
    @Test
    public void testNoFinishedProgress(){
        player = new Player(game, 5, lane, "");
        for (int j = 0; j<6; j++){
            opponents[j] = new Opponent(game, 5, lane, "");
        }
        pb = new ProgressBar(player, opponents);
        float[] other = {1.0F,1.0F,1.0F,1.0F,1.0F,1.0F,1.0F};
        Assertions.assertFalse(Arrays.equals(pb.getProgress(10), other));
    }

}
