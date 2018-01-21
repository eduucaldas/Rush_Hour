package rush_hour;

import java.io.*;
import java.util.*;

public class Solver {
    //which data structure should we use for the explored states
    Map<Codex,State> explored;
    Queue<State> border;
    Map<State,State> prec;

    public Solver(String fileName) throws Exception{
        this.explored = new HashMap<Codex, State>();
        this.border = new LinkedList<State>();
        this.border.add(new State(fileName));
        this.prec = new HashMap<State, State>();
        this.prec.put(this.border.peek(), null);
    }

    //This is why we use hashMap Why do we do it? not creating another car every time we look if a possible neighbor is on explored
    //should think, if previous explored would the next one be as well
    public boolean neighbor_explored(int delta, State actual, Car c) throws IllegalArgumentException{
        if(!actual.is_valid_state()) throw new IllegalArgumentException("the state youre coming from is not valid");//X
        if(!actual.containsCar(c)) throw new IllegalArgumentException("the car youre moving is not in the state");//X
        if(!actual.is_valid_move(delta, c)) throw new IllegalArgumentException("the move is not valid. Delta = " + delta + " " + c);// take these if they're done double
        return this.explored.containsKey(new Codex(actual.compute_code_neighbor(delta, c)));
    }

    public LinkedList<State> possible_moves(State initial){
        LinkedList<State> moves = new LinkedList<State>();
        int lim_plus, lim_minus;// limit of car freedom
        State added;
        for(Car c:initial.cars()) {
            lim_plus = 1;
            lim_minus = -1;
            while(initial.is_valid_move(lim_plus, c)) lim_plus++;
            while(initial.is_valid_move(lim_minus, c)) lim_minus--;
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

    public State evolution() {
        State actual;
        while(!this.border.isEmpty() && !this.border.peek().is_end()) {
            actual = this.border.poll();
            if(!this.explored.containsKey(actual.get_code())) {
                this.border.addAll(possible_moves(actual));
                this.explored.put(new Codex(actual),actual);
            }

        }//should put a hard limit
        System.out.println("finished with: " + this.explored.size() + " states explored");
        return this.border.peek();
    }
/*
    //Need a function file_to_state
    private static State file_to_state(String fileName){
        //we will assume that the file has the correct format
        String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            State initial_state = new State();

            //first line is the size of the board
            initial_state.set_size(Integer.parseInt(bufferedReader.readLine()));

            //second line is the number of cars
            //initial_state.set_number_of_cars(Integer.parseInt(bufferedReader.readLine()));

            StringTokenizer st;

            while((line = bufferedReader.readLine()) != null) {//read line

                Car car = new Car(line);

                if(!initial_state.add_car(car)){
                    System.err.println("Car Overlapping!");
                    return null;
                }
                initial_state.add_car(car);

            }

            bufferedReader.close();
            return initial_state;
        }
        catch(FileNotFoundException ex) {
            System.err.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.err.println("Error reading file '" + fileName + "'");
        }
        return null;
    }
*/

    public void print_prec(State last) {
        State actual = last;
        while(actual != null) {
            actual.print_state();
            actual = prec.get(actual);
        }
    }

    public static void main(String [] args) throws Exception {

        Solver rush_test = new Solver("test01.txt");
        State test = new State("test01.txt");
        Car redCar = new Car("1 h 2 2 3");
        Car one_car = new Car("7 h 2 5 5");
        State sol = rush_test.evolution();
        rush_test.print_prec(sol);
        System.out.println(rush_test.border.size());

    }
}
