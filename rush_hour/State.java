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
            System.err.out("Out of grid");
            return false;
        }
        for(Car c: cars) {
            if(c.dir() == Car.VERTICAL && c.x == x && y - c.y >= 0 && y - c.y <= c.len()) {
                return false;
            }
            else if(c.dir() == Car.HORIZONTAL && c.y == y && x - c.x >= 0 && x - c.x <= c.len()) {
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

    public void sort(){
        Collections.sort(cars);
    }
}
