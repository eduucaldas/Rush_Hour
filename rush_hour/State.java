package rush_hour;

import java.io.*;
import java.util.*;

public class State {

	private int size;
    private int number_of_cars;

	private ArrayList<Car> type_1;//horizontal with length 2
    private ArrayList<Car> type_2;//horizontal with length 3
    private ArrayList<Car> type_3;//vertical with length 2
    private ArrayList<Car> type_4;//vertical with length 3

    public State(){
        this.size = 0;
        this.number_of_cars = 0;
        type_1 = new ArrayList<Car>();
        type_2 = new ArrayList<Car>();
        type_3 = new ArrayList<Car>();
        type_4 = new ArrayList<Car>();
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

    public void print_state(){
    	System.out.println("TODO");
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
                type_2.add(car);
                break;
            case 4:
                type_2.add(car);
                break;
    	}
    	return true;
    }
}