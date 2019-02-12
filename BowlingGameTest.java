import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BowlingGameTest {

    private BowlingGame game;

    private void rolls(int n, int ball1, int ball2) {
        for (int i = 0; i < n; i++) {
            game.roll(ball1);
            game.roll(ball2);
        }
    }

    @BeforeEach
    public void setUp() {
        game = new BowlingGame();
    }

    @Test
    public void testAllZeros() {
        rolls(10, 0, 0);
        Assertions.assertEquals(0, game.getScore());
    }

    @Test
    public void testAllThrees() {
        rolls(10, 3, 3);
        Assertions.assertEquals(60, game.getScore());
    }

    @Test
    public void testAllNinesAndMisses() {
        rolls(10, 9, 0);
        Assertions.assertEquals(90, game.getScore());
    }

    @Test
    public void testOneSpare() {
        game.roll(4);
        game.roll(6); // spare
        game.roll(3);
        game.roll(5);
        rolls(8, 0, 0);
        Assertions.assertEquals(21, game.getScore());
    }

    @Test
    public void testFinalSpare() {
        rolls(9, 0, 0);
        game.roll(4);
        game.roll(6); // final spare
        game.roll(5); // bonus
        Assertions.assertEquals(15, game.getScore());
    }

    @Test
    public void testOneStrike() {
        game.roll(10); // strike
        game.roll(5);
        game.roll(3);
        rolls(8, 0, 0);
        Assertions.assertEquals(26, game.getScore());
    }

    @Test
    public void testDoubleStrike() {
        game.roll(10); // strike
        game.roll(10); // double strike
        game.roll(5);
        game.roll(3);
        rolls(8, 0, 0);
        Assertions.assertEquals(51, game.getScore());
    }

    @Test
    public void testFinalStrike() {
        rolls(9, 0, 0);
        game.roll(10); // final strike
        game.roll(5);  // bonus1
        game.roll(3);  // bonus2
        Assertions.assertEquals(18, game.getScore());
    }

    @Test
    public void testAllStrikes() {
        rolls(12, 10, 0); // all strikes
        Assertions.assertEquals(300, game.getScore());
    }

    @Test
    public void testAlternativesStrikeSpare() {
        for (int i = 0; i < 5; i++) {
            game.roll(10);  // strike
            game.roll(4);
            game.roll(6);   // spare
        }
        game.roll(10);      // bonus
        Assertions.assertEquals(200, game.getScore());
    }

    @Test
    public void testAlternativesSpareStrike() {
        for (int i = 0; i < 5; i++) {
            game.roll(4);
            game.roll(6);   // spare
            game.roll(10);  // strike
        }
        game.roll(4);
        game.roll(6);       // bonus
        Assertions.assertEquals(200, game.getScore());
    }
}