package rush_hour;

import java.util.Comparator;
import java.util.Scanner;

import junit.framework.Test;

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
		long startTime = System.currentTimeMillis();
		for(String s: files) {
			System.out.println("For:" + s);
			Solver.test_compare(s);
		}
		System.out.println((System.currentTimeMillis() - startTime) + " milliseconds pour tous les " + this.files.length + " tests");
	}
	
	public static String pickInput(Scanner sc) {
		System.out.println("Great! Please enter the name of your test file, it should be in the tests folder, and don't bother writing .txt");
		System.out.print("filename: ");
		String filename = sc.nextLine();
		return "tests/" + filename + ".txt";
	}
	
	public static Comparator<State> cmpSelector(Scanner sc){
		System.out.println("Choose your heuristics");
		System.out.println("1. Zero Heuristics");
		System.out.println("2. Blocking Heuristics");
		System.out.println("3. My Heuristics");
		System.out.println("4. Non Consistant Heuristics");
		String answer = "";
		while(!answer.equals("1") && !answer.equals("2") && !answer.equals("3") && !answer.equals("4")) {
			System.out.print("Answer[1, 2, 3, 4]: ");
			answer = sc.nextLine();
		}
		Comparator <State> cmp = null;
		switch (answer) {
			case "1": 
				cmp = new Zero_Comparator();
				break;
			case "2":
				cmp = new Blocking_Comparator();
				break;
			case "3":
				cmp = new My_Comparator();
				break;
			case "4":
				cmp = new Non_Consistant_Comparator();
				break;
		}
		return cmp;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("Welcome to the rush hour solver!");
		String filename, answer = "";
		Scanner sc = new Scanner(System.in);
		while(!answer.equals("y")  && !answer.equals("n")) {
			System.out.println("Do you want to see it solve your rush hour puzzle?[y/n]");
			answer = sc.nextLine().toLowerCase();
			
		}
		if(answer.equals("y")) {
			SolverTest(pickInput(sc), true, new My_Comparator());
		}
		System.out.println("Maybe you`re more of a nerd. What do you want to know?");
		System.out.println("1. What is the bitmap you told about?");
		System.out.println("2. Could I use your Solver with different heuristics?");
		System.out.println("3. Could I compare your Solver with different heuristics and do loads of tests at the same time? :)");
		System.out.println("4. Who are you calling a nerd?");
		
		while(!answer.equals("1") && !answer.equals("2") && !answer.equals("3") && !answer.equals("4")) {
			System.out.print("Answer[1, 2, 3, 4]: ");
			answer = sc.nextLine();
		}
		switch (answer) {
		case "1": 
			StateTest(pickInput(sc));
			System.out.println();
			System.out.println("See? The bit map is like a shadow!");
			break;
		case "2":
			SolverTest(pickInput(sc), true, cmpSelector(sc));
			break;
		case "3":
			System.out.println("Pleas put the tests in a tests folder and set their name to a format testXX, with XX digits");
			System.out.print("How many tests do you have?");
			Tester test = new Tester(sc.nextInt(), "tests/test", ".txt");
			
			test.CompareTest();
			
			break;
		case "4":
			System.out.println("Ok ok, no one here is a nerd... Chill...");
			break;
		}

	}
}
