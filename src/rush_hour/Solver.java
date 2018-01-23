package rush_hour;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

//Weird, non_consistant is having much better results


public class Solver {
    Map<Codex,State> explored;
    Queue<State> border;
    Map<State,State> prec;
    private static final int time_sleep_print = 1500;
    
    public Solver(String filename, Comparator<State> comp) throws Exception{
        this.explored = new HashMap<Codex, State>();
        this.border = new PriorityQueue<State>(comp);
        this.border.add(new State(filename));
        this.prec = new HashMap<State, State>();
        this.prec.put(this.border.peek(), null);
    }
    
    public Solver(String filename) throws Exception {
    	this.explored = new HashMap<Codex, State>();
        this.border = new LinkedList<State>();
        this.border.add(new State(filename));
        this.prec = new HashMap<State, State>();
        this.prec.put(this.border.peek(), null);
    }
    

    //This is why we use hashMap
    //We don't want to create a state every time we look if a possible neighbor is on explored
    //Thus we search keep the unique Codex for every state and compare them
  	private boolean neighbor_explored(int delta, State actual, Car c) throws IllegalArgumentException{
  		if(!actual.is_valid_state()) throw new IllegalArgumentException("the state youre coming from is not valid");//X
		if(!actual.containsCar(c)) throw new IllegalArgumentException("the car youre moving is not in the state");//X
		if(!actual.is_valid_move(delta, c)) throw new IllegalArgumentException("the move is not valid. Delta = " + delta + " " + c);// take these if they're done double
  		return this.explored.containsKey(new Codex(actual.compute_code_neighbor(delta, c)));
  	}
  	
  	
  	private LinkedList<State> possible_moves(State initial){
  		LinkedList<State> moves = new LinkedList<State>();
  		int lim_plus, lim_minus;// limit of car freedom
  		State added;
  		for(Car c:initial.cars()) {
  			//Finding the limit of where the car can move without collision
  			lim_plus = 1;
  			lim_minus = -1;
  			while(initial.is_valid_move(lim_plus, c)) lim_plus++;
  			while(initial.is_valid_move(lim_minus, c)) lim_minus--;
  			//See if is in neighbor then add to moves
  			for(int i = 1; i < lim_plus; i++) {
  				if(!neighbor_explored(i, initial, c)) {
  					added = new State(initial, i, c);
  					moves.add(added);
  					this.prec.put(added, initial);
  				}
  			}
  			for(int i = -1; i > lim_minus; i--) {
  				if(!neighbor_explored(i, initial, c)) {
  					added = new State(initial, i, c);
  					moves.add(added);
  					this.prec.put(added, initial);
  				}	
  			}
  		}
  		
  		return moves;
  	}
  	
  	private void evolution() {
        State actual;
    	while(!this.border.isEmpty() && !this.border.peek().is_end()) {    
    		actual = this.border.poll();
    		if(!this.explored.containsKey(actual.get_code())) {
    			this.border.addAll(possible_moves(actual));
                this.explored.put(new Codex(actual),actual);
    		}
        }
    }
  	
  	//must be applied after evolution
  	private LinkedList<State> get_solution(){
  		LinkedList<State> sol = new LinkedList<State>();
  		State actual = this.border.peek();//Remember the stopping condition is that border.peek is end
  		while(actual != null) {
  			sol.addFirst(actual);
  			actual = this.prec.get(actual);
  		}//For the first one we have prec = null
  		return sol;
  	}
  	
  	public void print_solution(boolean print_inter_sol) throws InterruptedException {
  		LinkedList<State> sol = this.get_solution();
  		System.out.println("Solution found has " + sol.size() + " intermediary states");//intermediary states != moves
  		if(print_inter_sol) {
  			for(State s: sol) {
  	  			s.print_state();
  	  			TimeUnit.MILLISECONDS.sleep(time_sleep_print);
  	  		}
  		}
  		
  		System.out.println("Finished with: " + this.explored.size() + " states explored");
  	}
  	
  	
  	//Use this to test solving with an specific heuristics
  	public static void test(String filename, boolean shall_print, Comparator<State> comp) throws Exception {
  		System.out.println(comp);
  		
  		long startTime = System.currentTimeMillis();
  		Solver rush_test = new Solver(filename, comp);
  		long constructTime = System.currentTimeMillis() - startTime;
  		System.out.println("Time taken by constructor: " + constructTime + " miliseconds");
  		
  		rush_test.evolution();
  		long evolutionTime = System.currentTimeMillis() - constructTime - startTime;
  		System.out.println("Time taken by evolution: " + evolutionTime + " miliseconds");		
		
  		rush_test.print_solution(shall_print);
		System.out.println();
  	}
  	
  	//use this to compare performance of methods
  	public static void test_compare(String filename) throws Exception {
  		test(filename, false, new Zero_Comparator());

    	test(filename, false, new Blocking_Comparator());

    	test(filename, false, new My_Comparator());

    	test(filename, false, new Non_Consistant_Comparator());
  	}
}
