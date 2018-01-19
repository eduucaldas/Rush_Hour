package rush_hour;

import java.io.*;
import java.util.*;

public class State {

	private int size;
	private int number_of_cars;
	private Car redCar;
	private ArrayList<Car> cars;

	public State(){
		this.size = 0;
		this.number_of_cars = 0;
		cars = new ArrayList<Car>();
	}

	public State(State other) {//clone
		this.size = other.size;
		this.number_of_cars = other.number_of_cars;
		this.cars = new ArrayList<Car>(other.cars);
	}

	public void set_size(int size){
		this.size = size;
	}

	public int get_size(){
		return this.size;
	}

	public void set_number_of_cars(int number_of_cars){
		this.number_of_cars = number_of_cars;
	}

	public int get_number_of_cars(){
		return this.number_of_cars;
	}
	// says if it is ok to add car to the actual state
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
	//Says if the tile (x,y) is vacant, i.e. has no car passing through it
	public boolean is_vacant(int x, int y) {
		if(x >= this.get_size() || y >= this.get_size()){
			//System.err.println("Out of grid");
			return false;
		}
		for(Car c: cars) {
			if(c.dir() == Car.VERTICAL && c.x == x && y - c.y >= 0 && y - c.y < c.len()) {
				return false;
			}
			else if(c.dir() == Car.HORIZONTAL && c.y == y && x - c.x >= 0 && x - c.x < c.len()) {
				return false;
			}
		}
		return true;
	}

	//this is O(n) shame on me
	//Not tested
	public void redCar() {
		if(!(this.redCar != null && this.redCar.red())){
			for (int i = 0; i < cars.size(); i++) {
				if(cars.get(i).red()) this.redCar = cars.get(i);
			}
		}
	}
	//Not tested
	public boolean is_end() {
		return (redCar.dir() == Car.HORIZONTAL && (this.get_size() - redCar.x <= redCar.len()) || redCar.dir() == Car.VERTICAL && (this.get_size() - redCar.y <= redCar.len()));
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
				System.out.print(board[i][j]+"\t");
			}
			System.out.println("");
		}
		System.out.println("");
	}

	public boolean add_car(Car car){
		if(!is_valid(car)) return false;//Should print err
		cars.add(car);
		return true;
	}

	private boolean add_car_bypass(Car car){//bypasses verification
		cars.add(car);
		return true;
	}

	public void remove_car(Car car){
		cars.remove(car);
	}

	public void sort(){
		Collections.sort(cars);
	}

	//still needs testing!
	public LinkedList<State> possible_moves(){
		LinkedList<State> moves = new LinkedList<State>();

		//auxiliary variables (we will only need 2 while statements with this)
		int dir_x;
		int dir_y;

		for(Car car : this.cars){
			if(car.get_type()==1 || car.get_type()==2){
				dir_x = 1;
				dir_y = 0;
			}
			else if(car.get_type()==3 || car.get_type()==4){
				dir_x = 0;
				dir_y = 1;
			}
			else{
				return null;
			}

			//let's see if the car can go left/up
			int i = 1;
			while(!this.is_vacant(car.x - i*dir_x, car.y - i*dir_y)){
				//It can!
				//Add a new state
				State new_state = new State(this);
				new_state.remove_car(car);

				car.x = car.x - i*dir_x;
				car.y = car.y - i*dir_y; 

				//bypassing verification of validness
				new_state.add_car_bypass(car);
				new_state.sort();

				moves.add(new_state);

				i=i+1;
			}

			//let's see if the car can go right/down
			i = 1;
			while(!this.is_vacant(car.x + (car.len() - 1 + i)*dir_x, car.y + (car.len() - 1 + i)*dir_y)){
				//It can!
				//Add a new state
				State new_state = new State(this);
				new_state.remove_car(car);

				car.x = car.x + i*dir_x;
				car.y = car.y + i*dir_y;

				//bypassing verification of validness
				new_state.add_car_bypass(car);
				new_state.sort();

				moves.add(new_state);

				i=i+1;
			}
		}

		return moves;
	}
}
