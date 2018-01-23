# Project Rush_Hour

## Base Classes:
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



Codex: Bitmap representation of a State, used mainly for performance reasons
Solver: Represents the solver of the game:
1. neighbor_explored: performance reasons

2. possible_moves: From a given state finds every State that is 1 move appart:...

3. evolution: Search the tree until it finds a solution

4. get_solution: Use precedents to derive the way it took to the solution

5.  print_solution: prints a series of steps to arrive in the solution
   

## Heuristic_Comparator Classes:
They represent the Heuristics as classes of Comparator<State>. Used as the priorities in the border priority queue.
1. Zero_Comparator: with h = 0. Used mostly for comparison with other methods

2. Blocking_Comparator: h = #(vehicules between red car and exit)

3. Non_Consistant_Comparator: function that decreases with the distance to the exiting right and bottom wall

4. My_Comparator: A hybrid of the past two, giving the most importance to Blocking_Comparator as to continue a consistent heuristic


## Tester:
To help you test and understand the most important fonctionalities in the project. Ps.: You should probably head directly here!
1. StateTest: Gives the basic in and outs of a State (constructor, print)

2. SolverTest: Solves one problem, with a given heuristics, optionally printing all intermediary States.

3. CompareTest: Compare the 4 heuristics implemented in:
    a. Execution time
    
    b. Number of intermediary States
    
    c. Number of states

