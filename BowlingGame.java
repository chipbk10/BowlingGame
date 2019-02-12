
public class BowlingGame {

    private Integer[][] rolls = new Integer[12][2];
    private int cur = 0;

    public void roll(int pins) {
        if (cur == 12) return;  // can't add more
        if (pins == 10) rolls[cur++][0] = 10;   // strike
        else {
            int i = (rolls[cur][0] == null) ? 0 : 1;
            rolls[cur][i] = pins;
            if (i == 1) cur++;
        }
    }

    public int getScore() {
        int score = 0;
        for (int i = 0; i < 12; i++) {
            score += scoreAt(i);
            if (i >= 9) continue; // last frame
            if (strikeAt(i)) {
                score += scoreAt(i+1);
                if (strikeAt(i+1)) score += 10;
            }
            else if (spareAt(i)) score += rolls[i+1][0];
        }
        return score;
    }

    public int scoreAt(int frame) {
        return unbox(rolls[frame][0]) + unbox(rolls[frame][1]);
    }

    public boolean spareAt(int frame) {
        return (rolls[frame][0] != null && scoreAt(frame) == 10);
    }

    public boolean strikeAt(int frame) {
        return (unbox(rolls[frame][0]) == 10);
    }

    private int unbox(Integer n) {
        return (n == null) ? 0 : n;
    }
}
