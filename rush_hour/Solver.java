package rush_hour;

import java.io.*;
import java.util.*;

public class Solver {

    public void read_file(String fileName){
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
                
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //tenho que fazer um if aqui pra ver se adicionou corretamente
                initial_state.add_car(car);

                }

            initial_state.print_state();

            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
    }

    public static void main(String [] args) {
        Solver game = new Solver();
        game.read_file("test");
    }
}