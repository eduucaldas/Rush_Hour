package rush_hour;

import java.io.*;
import java.util.*;

public class Solver {
    //which data structure should we use for the explored states
    Set<State> explored;
    Queue<State> border;

    public Solver(String fileName) {
        this.explored = new TreeSet<State>();
        this.border = new LinkedList<State>();
        this.border.add(Solver.file_to_state(fileName));
    }

    public void evolution() {

    }

    //need a function possible states
    //will just put all valid states to border
    private LinkedState possible_moves(State actual) {

    }

    //Need a function file_to_state
    private State file_to_state(String fileName){
        //we will assume that the file has the correct format
        String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            State initial_state = new State();

            //first line is the size of the board
            initial_state.set_size(Integer.parseInt(bufferedReader.readLine()));

            //second line is the number of cars
            initial_state.set_number_of_cars(Integer.parseInt(bufferedReader.readLine()));

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
        }
        catch(FileNotFoundException ex) {
            System.err.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.err.println("Error reading file '" + fileName + "'");
        }
        return initial_state;
    }

    public static void main(String [] args) {
        Solver game = new Solver();
        game.read_file("test");
    }
}
