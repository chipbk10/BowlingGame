# Overview
Here, we implement the **BowlingGame** using Test-Driven Development. It means, requirements are turned into very specific test cases, then the code will be improved to pass the new tests. This process is repeated until all test cases are passed.

# Start with simple TestCases
### 1. All Zeros
We will start with a very simple test case **```All Zeros```**, the player can't make any point.

```
@Test
public void testAllZeros() {
	rolls(10, 0, 0);
	Assertions.assertEquals(0, game.getScore());
}
```

Of course, the player gains **`0`** points at the end. This is enough for us to write some code for our **```BowlingGame```** class:

```
public class BowlingGame {
	public void roll(int pins) {	
	}
	
	public int getScore() {
		return 0;
	}
}
```

Note that, in method **`getScore()`**, we return **`0`**, because **`0`** is enough for this simple case. The logic will get more complexed after we add more test cases. That's what we call ```fake it until we make it```

### 2. All Threes
Now, what if the player gains some points during the game? For example, for each roll, the player gains 3 points, so at the end the total score must be **`60`**, instead of **`0`**

```
@Test
public void testAllThrees() {
	rolls(10, 3, 3);
	Assertions.assertEquals(60, game.getScore());
}
```
Obviously, this test case fails out method **`getScore()`**. We will add some logic for it to pass the test case. Each time, the player rolls, the player gets some points, and the total score is the sum of all points the player gains for each roll. If we can store all rolls' points, so the total score will be calculated easily. An **`Fixed-Size Array`** is enough to do. 

**Question:** Why don't we use a **`Muatable List`**, instead of a **`Fixed-Size Array`**?

The reason is the memory space will be more costly. The time complexity to add an element at the end of a **`Mutable List`** is more consumed than just changing the value of an element in a **`Fixed-Size Array`**.

What is the **maximum size** of this `Array`? For **`All Threes`**, each frame, the player rolls 2 times, and the player has to do 10 frames, so the **maximum number of rolls** is 20.

```
public class BowlingGame {
	int[] rolls = new int[20];
	int cur = 0;
	
	public void roll(int pins) {	
		rolls[cur++] = pins;
	}
	
	public int getScore() {
		int score = 0;
		for (int r : rolls) score += r;
		return score;
	}
}
```

# Special Test Cases
### 1. OneSpare
`Spare` happens when the player gains `10` points for 2 rolls in a frame. The player will be awarded of whatever is scored with the next roll. Let's say, the player makes one spare during the game:

```
@Test
public void TestOneSpare() {
	game.roll(4);
	game.roll(6); // spare
	game.roll(3);
	game.roll(5); // normal one
	rolls(8, 0, 0);
	Assertions.assertEquals(21, game.getScore());
}
```
The logic seems to be simple, if we know the current `frame` is a `Spare`, we just add the points of the next roll to the total score.

```
public int getScore() {
	int score = 0;
	for (int r : rolls) {
		score += r;
		if (isSpareAt(r)) score += r;
	}
	return score;
}

private boolean isSpareAt(int roll) {
	return false;
}
```

We have a problem with the previous implementation is we don't know when the player starts or finishes a `frame`. A `roll` can be the `first attempt` or the `second attempt` of a `frame`.

Therefore, the array `rolls` should store the information of `frames` as well.

```
int[][] rolls = new int[10][2];
```

`rolls[i]` is a `frame` that contains 2 attempts of throwing `rolls[i][0]` and `rolls[i][1]`. However, we still cannot identify a `roll` is the `first attempt` or `second attempt`.

If we use `Integer` type, we can solve this problem.

```
Integer[][] rolls = new Integer[10][2];
```

A `roll` will be the `first attempt` if :

```
rolls[i][0] == null;
```

A `roll` will be the `second attempt` if :

```
rolls[i][0] != null && rolls[i][1] == null;
```

The current `frame` is finished if :

```
rolls[i][0] != null && rolls[i][1] != null;
```

Putting everything together, we can come up with the following code for **`OneSpare`**:

```
public class BowlingGame {

    Integer[][] rolls = new Integer[10][2];
    int cur = 0;

    public void roll(int pins) {
    
        // identify first or second attempt
        int i = (rolls[cur][0] == null) ? 0 : 1;
        rolls[cur][i] = pins;

        // the current frame is finished
        if (i == 1) cur++;
    }

    public int getScore() {
        int score = 0;
        for (int i = 0; i < 10; i++) {
            score += scoreAt(i);
            if (isSpareAt(i)) score += rolls[i+1][0];
        }
        return score;
    }

    private int scoreAt(int i) {
        return rolls[i][0] + rolls[i][1];
    }

    private boolean isSpareAt(int i) {
        return scoreAt(i) == 10;
    }
}
```
### 2. FinalSpare
So far, so good, but what if the `spare` happens at the final `roll`? 

```
@Test
public void testFinalSpare() {
	rolls(9, 0, 0);
	game.roll(4);
	game.roll(6); // final spare
	game.roll(5); // bonus
	Assertions.assertEquals(15, game.getScore());
}
```
The code above will come to crash, because we have no point for the next ball. Remember, the player will be awarded one more roll for the final `spare`. The bonus `roll` will be stored in the `11th frame` of our `rolls`. So, the maximum number of frames is no longer `10`, should be `11` instead.

```
Integer[][] rolls = new Integer[11][2];
```

Notice that in the `11th frame`:

```
rolls[11][0] != null;
rolls[11][1] == null;
```

So, we can't calculate how many points are scored at `11th frame`:

```
scoreAt(11) = rolls[11][0] + rolls[11][1] = int + null;
```

If we unwrap the `Integer` value to `int` value where the `null` value will be converted to `0`. Now we can calculate the score of the `11th frame`

```
scoreAt(11) = rolls[11][0] + rolls[11][1] = int + 0 = int;
```

The `unwrap` method is easy to implement:

```
private int unbox(Integer n) {
	return (n == null) ? 0 : n;
}
```

The `FinalSpare` can be passed now:

```
Integer[][] rolls = new Integer[11][2];
int cur = 0;

public void roll(int pins) {

	// identify first or second attempt
	int i = (rolls[cur][0] == null) ? 0 : 1;
	rolls[cur][i] = pins;

	// the current frame is finished
	if (i == 1) cur++;
}

public int getScore() {
	int score = 0;
	for (int i = 0; i < 10; i++) {
		score += scoreAt(i);
		if (isSpareAt(i)) score += rolls[i+1][0];
	}
	return score;
}

private int scoreAt(int i) {
	return unbox(rolls[i][0]) + unbox(rolls[i][1]);
}

private boolean isSpareAt(int i) {
	return scoreAt(i) == 10;
}

private int unbox(Integer n) {
	return (n == null) ? 0 : n;
}

```

### 3. OneStrike
`Strike` happens when the player gains `10` points for the `first attempt` of a `frame`. The player is awarded `10` points, plus a bonus of whatever is scored with the next two balls.

```
@Test
public void testOneStrike() {
	game.roll(10); // strike
	game.roll(5);
	game.roll(3);
	rolls(8, 0, 0);
	Assertions.assertEquals(26, game.getScore());
}

```

The logic seems to be simple, if we know the current `frame` is a `Strike`, we just add the points of the 2 next rolls to the total score.

```
if (isStrikeAt(i)) {
	score += rolls[i+1][0] + rolls[i+1][1]; // = scoreAt(i+1);
}
```


A `frame` is a `Strike` if :

```
rolls[i][0] == 10 && rolls[i][1] == null;
``` 

When the player makes `strike`, the player doesn't have to do the `second attempt` of the current `frame`. So, when we add a `strike` into our `rolls`, we also increase the `current frame`.

```
public void roll(int pins) {

	if (pins == 10) { 	// strike
		rolls[cur++][0] = 10;
		return;
	}
	...
}

```

The picture now is quite clear. The logic will be:

```
public int getScore() {
	int score = 0;
	for (int i = 0; i < 10; i++) {
		score += scoreAt(i);
		if (isStrikeAt(i)) score += scoreAt(i+1);
		else if (isSpareAt(i)) score += unbox(rolls[i+1][0]);
	}
	return score;
}
```

### 4. Double Strike

Just adding the score of the next roll to the score is no longer correct when the player makes `Double Strike` (2 consecutive strikes) happened.

```
@Test
public void testDoubleStrike() {
	game.roll(10); // strike
	game.roll(10); // double strike
	game.roll(5);
	game.roll(3);
	rolls(8, 0, 0);
	Assertions.assertEquals(51, game.getScore());
}
```

The reason is the score at frame `i+1` is counted only **once**, but the score should plus a bonus of whatever is scored with the **next two balls**. In fact:

```
if (isStrikeAt(i)) {
	score += scoreAt(i+1);
	if (isStrikeAt(i+1)) score += rolls[i+2][0];
}
```

### 5. Final Strike

I expect the code above will come to crash if the `strike` happens at the final `frame`:

```
@Test
public void testFinalStrike() {
	rolls(9, 0, 0);
	game.roll(10); // final strike
	game.roll(5);  // bonus1
	game.roll(3);  // bonus2
	Assertions.assertEquals(18, game.getScore());
}
```

Suprisingly, it still works well. Note, the `final strike` will give the player 2 more rolls to play. These 2 rolls coincidently make up the `11th roll`, so that we don't have to change the size of our `rolls`.



# More Special Test Cases

### 1. All Strikes

The maximum score will be reached when all strikes happen:

```
@Test
public void testAllStrikes() {
	rolls(12, 10, 0); // all strikes
	Assertions.assertEquals(300, game.getScore());
}
```

Our code crashes, because the maximum number of frames is no longer `11`, but `12`.

```
Integer[][] rolls = new Integer[12][2];
```

While adding a `roll` into `rolls`, we need to check if the `current frame` has reached the maximum number of `possible frames`:

```
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

```

### 2. Alternatives Strike & Spare
Other test cases when `strike` and `spare` are alternatively located.

```
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
```

All test cases are passed. Happy Coding.

Any question can be sent to: [LUONG Dinh Hieu](https://www.linkedin.com/in/chipbk10/)

Thank you.








