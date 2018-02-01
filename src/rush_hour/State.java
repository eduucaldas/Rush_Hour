package rush_hour;

import java.io.*;
import java.util.*;


public class State implements Comparable<State>{

	private int size;
	private int number_of_cars;
	private Car redCar;
	private ArrayList<Car> cars;
	private Codex code;
	private int dist_origin;

	public State(State other) {//clone
		this.size = other.size;
		this.number_of_cars = other.number_of_cars;
		this.cars = new ArrayList<Car>(other.cars);
		this.redCar = new Car(other.redCar);//deep copy
		this.code = new Codex(other.get_code());
		this.setDist_origin(other.dist_origin());
	}
	
	//Creates State from moving a car in another State
	// this does not become more complicated for sorted cars
	// Important lemma, with our way of comparing, we cannot change the order of the cars by moving them :)
	public State(State other, int delta, Car c)throws IllegalArgumentException {
		//checks whether state is obtainable
		if(!other.is_valid_state()) throw new IllegalArgumentException("the state youre coming from is not valid");
		if(!other.containsCar(c)) throw new IllegalArgumentException("the car youre moving is not in the state");
		if(!other.is_valid_move(delta, c)) throw new IllegalArgumentException("the move is not valid");

		this.size = other.size;
		this.number_of_cars = other.number_of_cars;
		this.code = new Codex(other.compute_code_neighbor(delta, c));
		ArrayList<Car> moved_cars = new ArrayList<Car>(other.cars);
		Car moved = new Car(c,delta);//c but moved of delta
		moved_cars.set(other.findCar(c), moved);
		this.cars = moved_cars;
		if(c.equals(other.redCar))
			this.redCar = moved;
		else
			this.redCar = other.redCar;
		this.setDist_origin(other.dist_origin()+1);
	}
	
	//State from file
	public State(String fileName) throws Exception{
		//we will assume that the file has the correct format
        String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //first line is the size of the board
            this.size = Integer.parseInt(bufferedReader.readLine());
        
            //second line is the number of cars
            this.number_of_cars = Integer.parseInt(bufferedReader.readLine());
            this.setDist_origin(0);
            this.cars = new ArrayList<Car>(this.number_of_cars);
            Car read;
            
            while((line = bufferedReader.readLine()) != null) {//read line
                read = new Car(line);
                this.cars.add(read);
            }

            this.code = this.compute_code();
            
            if(!this.is_valid_state()) {
            	bufferedReader.close();
            	throw new IllegalArgumentException("Cars added had a collision");
            }
            this.redCar = this.cars.get(0);
            Collections.sort(this.cars);
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.err.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.err.println("Error reading file '" + fileName + "'");
        }
	}

	public void set_size(int size){
		this.size = size;
	}

	public int get_size(){
		return this.size;
	}

	private void set_number_of_cars(int number_of_cars){
		this.number_of_cars = number_of_cars;
	}

	public int get_number_of_cars(){
		return this.number_of_cars;
	}
	
	private void set_code() {
		this.code = new Codex(this.compute_code());
	}
	
	public Codex get_code() {
		return new Codex(this.code);//otherwise it is not isolated
	}
	
	//attention this is NOT isolated
	public ArrayList<Car> cars(){
		return this.cars;
	}
	
	//O(logn)
    public int findCar(Car c){
    	return Collections.binarySearch(this.cars, c);
    }
    
	//O(logn)
    public boolean containsCar(Car c) {
    	return ((this.cars.get(this.findCar(c))).equals(c));
    }
    
    public void sort(){
		Collections.sort(cars);
	}
    
    //oh java...
    private static long minus_pow2(int power) {
		if(power < 0)
			throw new IllegalArgumentException("oooops, you sent a negative number");
		else if(power == 0) 
			return -1;
		else 
			return 2*minus_pow2(power-1);
	}//this uses minus because we'll need 2^63 and it is not available for the positives
    
    //basis of our Codex. In the end it'll be just a bit map
    private long code_tile(int x, int y) {
    	return minus_pow2(x+this.get_size()*y);
    }
    
	// made by picking for every position (is position occupied)*2^(x+y*size)
    // As the grid is smaller than 9 this encoding can be done with long
	//Difficulties:
    // Need to use unsigned long, but unsigned doesn't exist in java.
	public Codex compute_code() {
		long[] code = new long[] {Long.MAX_VALUE, Long.MAX_VALUE}; // so we can use the all the 64 bits
		for(Car c: this.cars) {
			if(c.dir() == Car.HORIZONTAL) {
				for(int d_x = 0 ; d_x < c.len(); d_x++)
					code[0] += code_tile(c.x+d_x, c.y);
			} 
			else {
				for(int d_y = 0 ; d_y < c.len(); d_y++)
					code[1] += code_tile(c.x, c.y+d_y);
			}
		}
		return new Codex(code);
	}
	//////////////////This is why we use HashMap in Solver///////////////////////////
	//this is to be used in possible_moves
	//the car should be moving to a valid space already
	public Codex compute_code_neighbor(int delta, Car c) throws IllegalArgumentException{
		if(!this.containsCar(c)) throw new IllegalArgumentException("The car is out of the State");
		Codex code_moved = this.get_code();
		if(c.dir() == Car.HORIZONTAL) {
			for(int i = 0; i < c.len(); i++)
				code_moved.update_h(code_tile(c.x + i + delta, c.y) - code_tile(c.x + i, c.y));//if it is the last tile we might get error, if it turns negative then adds
		}
		else {
			for(int i = 0; i < c.len(); i++)
				code_moved.update_v(code_tile(c.x, c.y + i + delta) - code_tile(c.x, c.y + i));
		}
		return code_moved;
	}
	////////////////////////////////////////////////////////////////
	
	// Basically, this < other if this is at left of other, and then below
	//TODO: Adjust so it works identically for vertical and horizontal redCar
	@Override
	public int compareTo(State other) {
		if(this.code.get_code_h() > other.code.get_code_h())
			return 1;
		else if(this.code.get_code_h() < other.code.get_code_h())
			return -1;
		else {
			if(this.code.get_code_v() > other.code.get_code_v())
				return 1;
			else if(this.code.get_code_v() < other.code.get_code_v())
				return -1;
			else return 0;
		}
	}//Can't use this.code - other.code because of overflow
	
	@Override
	public int hashCode() {
		return ((code == null) ? 0 : code.hashCode());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	
    //number of vacants = size^2 - Sum(len_of_cars)
	public boolean is_valid_state() {
		//number of vacants
		int n_of_vac = 0;
		for(int x = 0; x < this.get_size(); x++) {
			for(int y = 0; y < this.get_size(); y++) {
				if(is_vacant(x, y)) n_of_vac++;
			}
		}
		
		//sum of len
		int sum_len = 0;
		for(Car c: this.cars) sum_len += c.len();
		
		return (n_of_vac + sum_len == this.get_size() * this.get_size());
	}

	// Says if it is ok to add car to the actual state
	public boolean is_valid(Car car){
		int x = car.x;
		int y = car.y;
		switch(car.get_type()){
			case 1:
				return (is_vacant(x,y) && is_vacant(x+1,y));
			case 2:
				return (is_vacant(x,y) && is_vacant(x+1,y) && is_vacant(x+2,y));
			case 3:
				return (is_vacant(x,y) && is_vacant(x,y+1));
			case 4:
				return (is_vacant(x,y) && is_vacant(x,y+1) && is_vacant(x,y+2));
		}
		return true;
	}
	
	//Says if the tile (x,y) is vacant, O(1) 
	public boolean is_vacant(int x, int y) {
		if((x >= 0) && (y >= 0) && (x < this.get_size()) && (y < this.get_size())) {
			boolean is_vacant_h = (((this.code.get_code_h()/code_tile(x,y))%2 - (Long.MAX_VALUE/code_tile(x,y))%2)%2 == 0);
			boolean is_vacant_v = (((this.code.get_code_v()/code_tile(x,y))%2 - (Long.MAX_VALUE/code_tile(x,y))%2)%2 == 0);
			return (is_vacant_h && is_vacant_v);
		}
		else return false;
	}//this is some maths, basically we try to take out the bit of info that we want without overflow
	
	public boolean is_valid_move(int delta, Car c) {
		int offset = (delta >= 0) ? c.len() : -1;
		int sign_delta = (delta >= 0) ? 1 : -1;
		int mod_delta = sign_delta*delta;
		
		if(c.dir() == Car.HORIZONTAL) {
			for(int d_x = 0; d_x < mod_delta; d_x++) {
				if(!is_vacant(c.x + offset + sign_delta*d_x, c.y))
					return false;
			}
		}
		else {
			for(int d_y = 0; d_y < mod_delta; d_y++) {
				if(!is_vacant(c.x, c.y + offset + sign_delta*d_y))
					return false;
			}
		}
		
		return true;
	}
	
	private void print_add(Car car, int[][] board, int type, int id){
		int inc_x = ((type == 1 || type == 2) ? 1 : 0);
		int inc_y = ((inc_x == 0) ? 1 : 0);

		for(int i=0; i<car.len(); i++){
			board[car.x + inc_x*i][car.y + inc_y*i] = id;
		}
	}
	
	public void print_state(){
		int[][] board = new int[size][size];

		for(Car car : cars){
			this.print_add(car, board, car.get_type(), car.id());
		}

		for(int j=0; j<size; j++){
			for(int i=0; i<size; i++){
				if(board[i][j] == 0)System.out.print(" \t");
				else System.out.print(board[i][j]+"\t");
			}
			System.out.println("");
		}
		System.out.println("#############################################\n");
	}
	
	//Better show the information in Codex
	private boolean[][] image(){
		boolean[][] img = new boolean[this.get_size()][this.get_size()];
		
		for(int x = 0; x < this.get_size(); x++) {
			for(int y = 0; y < this.get_size(); y++) {
				img[x][y] = !is_vacant(x,y);
			}
		}
		return img;
	}
	
	//As Codex has no info on the ID's this gives only a shadow where the cars are
	public void print_img() {
		boolean[][] img = this.image(); 
		for(int y = 0; y < this.get_size(); y++) {
			for(int x = 0; x < this.get_size(); x++) {
				if(img[x][y])
					System.out.print("1\t");
				else
					System.out.print(" \t");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	//tells if the game has ended
	public boolean is_end() {
		return (this.redCar.dir() == Car.HORIZONTAL && (this.get_size() - this.redCar.x <= this.redCar.len()) || this.redCar.dir() == Car.VERTICAL && (this.get_size() - this.redCar.y <= this.redCar.len()));
	}
	
	public int zero_heuristics() {
		return this.dist_origin();
	}
	
	public int a_star_heuristics() {
		int n_between = 0;
		if(this.redCar.dir() == Car.HORIZONTAL) {
			for(int d_x = this.redCar.x + this.redCar.len(); d_x < this.get_size(); d_x++) {
				if(!this.is_vacant(d_x, this.redCar.y)) n_between ++;
			}
		}
		else {
			for(int d_y = this.redCar.y + this.redCar.len(); d_y < this.get_size(); d_y++) {
				if(!this.is_vacant(this.redCar.x, d_y)) n_between ++;
			}
		}
		return n_between + this.dist_origin();
	}
	
	//Idea Added to the blockingCars heuristics let's push cars to where there is more space :)
	public long my_heuristics() {
		int a = this.a_star_heuristics();
		long reward_v = (this.get_code().get_code_v()/(long)(8*this.number_of_cars));
		long reward_h = (this.get_code().get_code_h()/(long)(8*this.number_of_cars));
		if(this.redCar.dir() == Car.HORIZONTAL && this.redCar.y > this.get_size()/2) reward_h = -reward_h;
		if(this.redCar.dir() == Car.VERTICAL && this.redCar.x > this.get_size()/2) reward_v = -reward_v;//puts other cars where there is more space
		long max_multiplier = Long.MAX_VALUE/(2*this.number_of_cars);
		if(a != 0) {
			return (a*max_multiplier + reward_h + reward_v) + this.dist_origin();
		}
		else return this.dist_origin();
	}//Some complicated math but basically gives a reward for searching solutions closer to red's objective and with other cars where there is more space
	
	
	//Test our construction and showing of the State
	public static void test_print(String filename) throws Exception{
		State test = new State(filename);
		test.print_state();
		test.print_img();
	}

	public int dist_origin() {
		return dist_origin;
	}

	public void setDist_origin(int dist_origin) {
		this.dist_origin = dist_origin;
	}
}

