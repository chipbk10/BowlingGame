# Overview
This is an interview question from BNP Paris.

# BowlingGame
Required to implement a bowling game. More info on the rules at: [how to score for bowling](https://www.topendsports.com/sport/tenpin/scoring.htm)

# How to run
We use JUnit for testing. There are several ways (maven, gradle, ant, etc.) to compile and run the tests.
Here, I show you how to use command line to run the tests.

### 1. Downloads
We need [JUnit-5.4.0](https://search.maven.org/search?q=g:org.junit.jupiter) to build and [JUnit-platform-console-standalone-1.4.0](https://search.maven.org/search?q=a:junit-platform-console-standalone) to print test execution results to the console.

### 2. Compile
Create `out` folder to store our compiled java files
```
mkdir out
```
We use [JUnit-5.4.0](https://search.maven.org/search?q=g:org.junit.jupiter) to build

```
javac -cp junit-jupiter-api-5.4.0.jar BowlingGame.java BowlingGameTest.java -nowarn -d out
```

### 3. Run TestCases

```
java -jar junit-platform-console-standalone-1.4.0.jar --class-path out --scan-class-path

```

# Results

```
╷
├─ JUnit Jupiter ✔
│  └─ BowlingGameTest ✔
│     ├─ testOneStrike() ✔
│     ├─ testAllFives() ✔
│     ├─ testAllZeros() ✔
│     ├─ testOneSpare() ✔
│     ├─ testAlternativesStrikeSpare() ✔
│     ├─ testAllStrikes() ✔
│     ├─ testAllNinesAndMisses() ✔
│     ├─ testAllSpares() ✔
│     ├─ testFinalStrike() ✔
│     ├─ testAlternativesSpareStrike() ✔
│     └─ testFinalSpare() ✔
└─ JUnit Vintage ✔

Test run finished after 76 ms
[         3 containers found      ]
[         0 containers skipped    ]
[         3 containers started    ]
[         0 containers aborted    ]
[         3 containers successful ]
[         0 containers failed     ]
[        11 tests found           ]
[         0 tests skipped         ]
[        11 tests started         ]
[         0 tests aborted         ]
[        11 tests successful      ]
[         0 tests failed          ]
```

# How to create a TestCase

A `TestCase` can be generated from [here](https://www.bowlinggenius.com/)