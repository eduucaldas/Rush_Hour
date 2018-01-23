package rush_hour;

import java.util.Comparator;

public class Tester {
	String[] files;
	
	public Tester(int max_n_test, String base, String extension) {
		int n_of_digits = (int)Math.log10(max_n_test);

		this.files = new String[max_n_test];

		for(int i = 0; i < max_n_test; i++) {
			files[i] = base + String.format("%02d", i+1) + extension;
		}
	}
	
	public static void StateTest(String filename) throws Exception {
		System.out.println("Testing State for test case in: " + filename);
		State test = new State(filename);
		System.out.println("print state");
		test.print_state();
		System.out.println("print shadow");
		test.print_img();
	}
	
	public static void SolverTest(String filename,boolean shall_print, Comparator<State> cmp) throws Exception {
		System.out.println("Testing the solver for test case in: " + filename + " with heuristics: " + cmp);
		Solver.test(filename,shall_print, cmp);
	}
	
	public void CompareTest() throws Exception {
		System.out.println("Comparing heuristics...");
		for(String s: files) {
			System.out.println("For:" + s);
			Solver.test_compare(s);
		}
	}
	
	public static void main(String[] args) throws Exception {
		String fileTest = "tests/RushHour1.txt";
		boolean see_intermediary_solutions = false;
		//uncomment lines below to tru other heuristics
		Comparator<State> cmp;
		cmp = new My_Comparator();
		// cmp = new Blocking_Comparator();
		// cmp = new Non_Consistant_Comparator();
		// cmp = new Zero_Comparator();
		
		Tester.StateTest(fileTest);
		
		Tester.SolverTest(fileTest, see_intermediary_solutions, new My_Comparator());
		//If it's too slow for you go to Solver and change time_sleep_print
		
		String base = "tests/test";
		int number_of_tests = 40;
		String extension = ".txt";
		Tester test = new Tester(number_of_tests, base, extension);
		test.CompareTest();
	}
}
