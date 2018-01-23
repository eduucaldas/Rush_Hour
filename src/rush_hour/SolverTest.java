package rush_hour;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SolverTest {

	private static String[] files;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		//added RushHour7 with one of the most difficult problems
		//thanks http://cs.ulb.ac.be/~fservais/rushhour/index.php?window_size=20&offset=0
		int max_n_test = 47;
		int max_n_filename = 40;
		files = new String[max_n_test];
		String base = "tests/test";
		String base_RH = "tests/RushHour";
		String extension = ".txt";
		for(int i = 0; i < max_n_filename; i++) {
			files[i] = base + String.format("%02d", i+1) + extension;
		}
		for(int i = 0; i < max_n_test - max_n_filename; i++) {
			files[max_n_filename + i] = base_RH + String.format("%d", i+1) + extension;
		}
	}


	

	@BeforeEach
	void setUp() throws Exception {
	}

	
	@Test
	void testCompare() throws Exception {
		/*for(String s: files) {
			System.out.println("For:" + s);
			Solver.test_compare(s);
		}*/
		
	}
	
	@Test
	void testNonConsistant() throws Exception {
		State test1 = new State(files[9]);
		System.out.println("analyzing " + files[9]);
		test1.print_state();
		Solver.test(files[9],true, new Non_Consistant_Comparator());
		
		State test2 = new State(files[19]);
		System.out.println("analyzing " + files[19]);
		test2.print_state();
		Solver.test(files[19],true, new Non_Consistant_Comparator());
		
		State test3 = new State(files[20]);
		System.out.println("analyzing " + files[20]);
		test3.print_state();
		Solver.test(files[20],true, new Non_Consistant_Comparator());
		
		State test4 = new State(files[26]);
		System.out.println("analyzing " + files[26]);
		test4.print_state();
		Solver.test(files[26],true, new Non_Consistant_Comparator());
		
		String very_hard = "tests/RushHour7.txt";
		State test_very_hard = new State(very_hard);
		System.out.println("analyzing " + very_hard);
		test_very_hard.print_state();
		Solver.test(very_hard,true, new My_Comparator());
		
		
		
		
	}

	/*@Test
	void testNeighbour_explored() throws Exception {
		Solver rush_test = new Solver("test01.txt");
		State test = new State("test01.txt");
		Car redCar = new Car("1 h 2 2 3");
		Car one_car = new Car("7 h 2 5 5");
		
	}

	@Test
	void testPossible_moves() throws Exception {
		Solver rush_test = new Solver("test01.txt");
		State test = new State("test01.txt");
		Car redCar = new Car("1 h 2 2 3");
		Car one_car = new Car("7 h 2 5 5");
	}*/

}
