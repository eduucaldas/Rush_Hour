package rush_hour;

import java.io.*;
import java.util.*;

//Question. Do we still need to sort? Yeah
// TODO: constructor from file used code field before it was init, (is_vacant fault)


public class State implements Comparable<State>{

    private int size;
    private int number_of_cars;
    private Car redCar;
    private ArrayList<Car> cars;
    private Codex code;

    //Don't see why
    public State(){
        this.size = 0;
        this.number_of_cars = 0;
        this.cars = new ArrayList<Car>();
        this.redCar = null;
        this.code = new Codex();//idk
    }

    public State(State other) {//clone
        this.size = other.size;
        this.number_of_cars = other.number_of_cars;
        this.cars = new ArrayList<Car>(other.cars);
        this.redCar = other.redCar;
        this.code = new Codex(other.get_code());
    }

    // this does not become more complicated for sorted cars
    // Important lemma, with our way of comparing, we cannot change the order of the cars by moving them :)
    // doesn't verify if resulting state is obtainable from other
    public State(State other, int delta, Car c)throws IllegalArgumentException {
        //check whether state is obtainable
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
    }


    //tested
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

    //Should test, O(logn)
    //this become O(n) if cars are not sorted
    public int findCar(Car c){
        return Collections.binarySearch(this.cars, c);
    }
    /*
    public int findCar(Car c) {
        return this.cars.indexOf(c);
    }
    */
    //this become O(n) if cars are not sorted
    public boolean containsCar(Car c) {
        return ((this.cars.get(this.findCar(c))).equals(c));
    }
    //oh java...
    //test?
    private static long minus_pow2(int power) {
        if(power < 0)
            throw new IllegalArgumentException("oooops, you sent a negative number");
        else if(power == 0)
            return -1;
        else
            return 2*minus_pow2(power-1);
    }//this uses minus because we need 2^63 and it is not available for the positives

    //test
    private long code_tile(int x, int y) {
        return minus_pow2(x+this.get_size()*y);
    }

    // made by picking for every position (is position occupied)*2^(x+y*size)
    // As the grid is smaller than 9 this encoding can be done with long
    //Difficulties:
    // Need to use unsigned long, but unsigned doesn't exist
    //Problems
    // Does it need to encode the position of the red car for our application. Don't think so
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
    //////////////////This is why we use HashMap///////////////////////////
    //this is to be used in possible_moves
    //the car should be moving to a valid space already
    //test
    //
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

    // This is a relaxed compareTo
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
    public boolean equals(Object other) {
        return (this.compareTo((State)other) == 0);
    }

    @Override
    public int hashCode() {
        return this.get_code().hashCode();
    }

    //number of vacants = size^2 - Sum(len)
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

    // test. says if it is ok to add car to the actual state
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

    //TEST. Says if the tile (x,y) is vacant, O(1)
    public boolean is_vacant(int x, int y) {
        if((x >= 0) && (y >= 0) && (x < this.get_size()) && (y < this.get_size())) {
            boolean is_vacant_h = (((this.code.get_code_h()/code_tile(x,y))%2 - (Long.MAX_VALUE/code_tile(x,y))%2)%2 == 0);
            boolean is_vacant_v = (((this.code.get_code_v()/code_tile(x,y))%2 - (Long.MAX_VALUE/code_tile(x,y))%2)%2 == 0);
            return (is_vacant_h && is_vacant_v);
        }
    else return false;
    }//this is some maths, basically we try to take out the bit of info that we want without overflow

    /*
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
    */

    //test
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

    //tested
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

    private boolean[][] image(){
        boolean[][] img = new boolean[this.get_size()][this.get_size()];

        for(int x = 0; x < this.get_size(); x++) {
            for(int y = 0; y < this.get_size(); y++) {
                img[x][y] = !is_vacant(x,y);
            }
        }
        return img;
    }

    public void print_img() {
        boolean[][] img = this.image();
        for(int y = 0; y < this.get_size(); y++) {
            for(int x = 0; x < this.get_size(); x++) {
                if(img[x][y])
                    System.out.print("1\t");
                else
                    System.out.print("0\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    //test, tells if the game has ended
    public boolean is_end() {
        return (this.redCar.dir() == Car.HORIZONTAL && (this.get_size() - this.redCar.x <= this.redCar.len()) || this.redCar.dir() == Car.VERTICAL && (this.get_size() - this.redCar.y <= this.redCar.len()));
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

    //O(n)
    public void remove_car(Car car){
        cars.remove(car);
    }

    public void sort(){
        Collections.sort(cars);
    }

    //still needs testing!
    //removing -> add -> sort is O(nlogn), not good
    //Should see if the move was already discovered, we can do it very quickly if we manage to find the code of a not yet created car
    //Update it to work with code()
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
                //HERE we should use find to just swap some cars, it`ll be much quicker
                new_state.add_car_bypass(car);
                new_state.sort();

                moves.add(new_state);

                i=i+1;
            }
        }

        return moves;
    }

    public static void main(String[] args) throws Exception{
        State test = new State("test01.txt");
        test.print_state();
        test.print_img();

    }
}

