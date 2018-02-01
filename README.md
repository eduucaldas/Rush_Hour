# Project Rush_Hour

## How to use?
1. Download jar file and optionaly tests folder in the same directory

2. run : "java -jar rush_hour.jar" from terminal and follow in screen instructions

3. Want to solve your puzzle? Write it in the format described in the rush_hour_final.pdf and put the file, in the folder tests

## Source Code Quick view
### Base Classes:
Car: Represents a car in the game

State: Represents a given instant in the game.
Sections:

1. Constructors: State, State with moving car, From file

2. Getters, Setters:...

3. Car Ordering: FindCar, ContainsCar, sort

4. Codifying: Methods for Computing Codex

5. is_valid: Tests validity of: State, move, add_Car, based on is_vacant(x,y) that tells if a tile is not occupied

6. print: State and Codex

7. heuristics: Functions with the heuristic to be applied by the Comparators


Codex: Bitmask representation of a State, used mainly for performance reasons


Solver: Represents the solver of the game:
1. neighbor_explored: performance reasons

2. possible_moves: From a given state finds every State that is 1 move appart:...

3. evolution: Search the tree until it finds a solution

4. get_solution: Use precedents to derive the way it took to the solution

5.  print_solution: prints a series of steps to arrive in the solution
   

### Heuristic_Comparator Classes:
They represent the Heuristics as classes of Comparator<State>. Used as the priorities in the border priority queue.
1. Zero_Comparator: with h = 0. Used mostly for comparison with other methods

2. Blocking_Comparator: h = #(vehicules between red car and exit)

3. Non_Consistant_Comparator: function that decreases with the distance to the exiting right and bottom wall

4. My_Comparator: A hybrid of the past two, giving the most importance to Blocking_Comparator as to continue a consistent heuristic. We made it optimized for 6x6 games


### Tester:
To help you test and understand the most important fonctionalities in the project. Ps.: You should probably head directly here!
1. StateTest: Gives the basic in and outs of a State (constructor, print)

2. SolverTest: Solves one problem, with a given heuristics, optionally printing all intermediary States.

3. CompareTest: Compare the 4 heuristics implemented in:
    a. Execution time
    
    b. Number of intermediary States
    
    c. Number of states

## Additional Stuff
1. RushHour_Final.pdf: Project Description. Made by Marie Albenque
2. tests: Folder with plenty of tests. test47 is one of the most difficult tests found for a 6x6 board, all the other tests were given by Marie
3. download_tests: Tool for downloading the tests

## Aknowledgements
Thanks Marie Albenque for the beautiful project proposal :)

Thanks Ian for the great discussions we had to improve this ;)
