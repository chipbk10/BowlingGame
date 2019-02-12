
public class BowlingGame {

    Integer[][] rolls = new Integer[12][2];
    int cur = 0;

    public void roll(int pins) {
        if (cur == 12) return;  // can't add more
        if (pins == 10) rolls[cur++][0] = 10;   // strike
        else {
            // identify first or second attempt
            int i = (rolls[cur][0] == null) ? 0 : 1;
            rolls[cur][i] = pins;
            // the current frame is finished
            if (i == 1) cur++;
        }
    }

    public int getScore() {
        int score = 0;
        for (int i = 0; i < 10; i++) {
            score += scoreAt(i);
            if (isStrikeAt(i)) {
                score += scoreAt(i+1);
                if (isStrikeAt(i+1)) score += rolls[i+2][0];
            }
            else if (isSpareAt(i)) score += unbox(rolls[i+1][0]);
        }
        return score;
    }

    private int scoreAt(int i) {
        return unbox(rolls[i][0]) + unbox(rolls[i][1]);
    }

    private boolean isSpareAt(int i) {
        return scoreAt(i) == 10;
    }

    private boolean isStrikeAt(int i) {
        return (unbox(rolls[i][0]) == 10);
    }

    private int unbox(Integer n) {
        return (n == null) ? 0 : n;
    }
}
