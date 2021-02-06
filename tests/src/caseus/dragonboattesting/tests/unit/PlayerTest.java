package caseus.dragonboattesting.tests.unit;

import caseus.dragonboattesting.GdxTestRunner;
import com.badlogic.gdx.Gdx;
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
public class PlayerTest {

    @Mock
    private Lane lane;
    @Mock
    private DragonBoatGame game;

    @BeforeEach
    public void setup() {
    }


}
