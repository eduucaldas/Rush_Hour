package rush_hour;

import java.io.*;
import java.util.*;

public class State {

	private int size;
    private int number_of_cars;

	private ArrayList<Car> cars;

    public State(){
        this.size = 0;
        this.number_of_cars = 0;
        cars = new ArrayList<Car>();
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

    public boolean is_valid(Car car){
    	//TODO
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
    		this.print_add(car, board, 1, car.id());
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
    	if(!is_valid(car)) return false;
    	switch(car.get_type()){
            case 1: 
                type_1.add(car);
                break;
            case 2:
                type_2.add(car);
                break;
            case 3:
                type_3.add(car);
                break;
            case 4:
                type_4.add(car);
                break;
    	}
    	return true;
    }
}