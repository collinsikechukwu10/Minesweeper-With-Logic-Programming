
## CS5011 A3: Obscured MineSweeper Using Logic Programming
Implementation checklist
- [x] P1: The basic sweeper agent which probes cells in order from left to right and from top to bottom.
- [x] P2: The beginner obscured sweeper agent using the single point reasoning strategy (SPS).
- [x] P3: The intermediate obscured sweeper agent using the satisfiability test reasoning strategy (DNF) along with SPS using the DNF encoding.
- [x] P4: Agent using the satisfiability test reasoning strategy(CNF).
- [x] P5: Pattern Strategy using both the One-One and One-Two pattern strategy along with SPS to solve the sweeper world. Worked with tests but entered some infinite loops with some tests.

## Compilation and Running
To perform a search using the implemented game strategies, the following command below must be run
```
./playSweeper.sh <Strategy> <World> <additionalParameters>
```

| Parameter                | Description                                                                                                                                                                                         |
|--------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Strategy**             | The Strategy parameter should be replaced with any of the implemented strategies: `P1`,`P2`,`P3`,`P4`,`P5`,                                                                                         |
| **World**                | The World should be replaced with any of the implemented configurations:<br>- TEST1–TEST6 (used for testing)<br>- SMALL1–SMALL10, <br>- MEDIUM1–MEDIUM10,<br>- LARGE1–LARGE10 (used for evaluation) |
| **additionalParameters** | Additional parameters implemented are: <br> `verbose`: prints out the agents view of the world at each probe step                                                                                   | 


An example for running a basic obscured sweeper agent for world SMALL2 would be:
```
./playSweeper.sh P1 SMALL2
```

<br>
To perform an evaluation on all game strategies against all provided boards, the command below must
be run, this would generate a csv file (eval.csv) that can be explored using excel.

```
./playSweeper.sh eval
```

