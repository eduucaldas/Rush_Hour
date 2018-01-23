Project Rush_Hour

Base Classes:
    Car: Represents a car in the game

    State: Represents a given instant in the game.
    Sections:
        Constructors: State, State with moving car, From file
        Getters, Setters:...
        Car Ordering: FindCar, ContainsCar, sort
        Codifying: Methods for Computing Codex
        is_valid: Tests validity of: State, move, add_Car, based on is_vacant(x,y) that tells if a tile is not occupied
        print: State and Codex
        heuristics: Functions with the heuristic to be applied by the Comparators

    Codex: Bitmap representation of a State, used mainly for performance reasons
    Solver: Represents the solver of the game:
        neighbor_explored: performance reasons
        possible_moves: From a given state finds every State that is 1 move appart
        evolution: Search the tree until it finds a solution
        get_solution: Use precedents to derive the way it took to the solution
        print_solution: prints a series of steps to arrive in the solution

Heuristic_Comparator Classes:
    They represent the Heuristics as classes of Comparator<State>. Used as the priorities in the border priority queue.
    Zero_Comparator: with h = 0. Used mostly for comparison with other methods
    Blocking_Comparator: h = #(vehicules between red car and exit)
    My_Comparator:
    Non_Consistant_Comparator: function that decreases with the distance to the exiting right and bottom wall
    My_Comparator: A hybrid of the past two, giving the most importance to Blocking_Comparator as to continue a consistent heuristic


Tester:
To help you test and understand the most important fonctionalities in the project. Ps.: You should probably head directly here!
    StateTest: Gives the basic in and outs of a State (constructor, print)
    SolverTest: Solves one problem, with a given heuristics, optionally printing all intermediary States.
    CompareTest: Compare the 4 heuristics implemented in:
        Execution time
        Number of intermediary States
        Number of states

