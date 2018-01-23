package rush_hour;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StateTest {
	
	
	
	@Test
	void test_is_vacant() throws Exception {
		State test = new State("test01.txt");
		//State testC = new State("test01C.txt")//this should give error
		assertNotNull(test);
		assertTrue(test.is_vacant(1, 1));
		assertFalse(test.is_vacant(0, 3));

	}
	
	@Test
	void test_print() throws Exception{
		State test = new State("test01.txt");
		//test.print_state();
	}
	
	@Test
	void test_containsCar() throws Exception{
		State test = new State("test01.txt");
		Car one_car = new Car("1 h 2 2 3");
		assertTrue(test.containsCar(one_car));
	}
	
	@Test
	void test_move() throws Exception{
		State test = new State("test01.txt");
		
		test.print_state();
		test.print_img();
		
		
		Car one_car = new Car("7 h 2 5 5");
		
		assertTrue(test.is_valid_move(-1, one_car));
		State moved = new State(test,-1, one_car);
		
		moved.print_state();
		moved.print_img();
		
		assertFalse(test.is_valid_move(1, one_car)); // doesn't let cross the wall
		assertFalse(test.is_valid_move(-4, one_car));// doesn't let collide
		State test_jump = new State("test01_jump.txt");
		assertFalse(test_jump.is_valid_move(3, one_car));// doesn't let jump
		
	}

}
