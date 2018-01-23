package rush_hour;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class collision_Test {

	Car[] cars2h;
	Car[] cars3h;
	Car[] cars2v;
	Car[] cars3v;
	
	@BeforeEach
	void setUp() throws Exception {
		cars2h = new Car[]{new Car("1 h 2 2 3"), new Car("2 h 2 1 1"), new Car("3 h 2 5 5")};
		cars3h = new Car[]{new Car("4 h 3 3 6"), new Car("10 h 3 2 5"), new Car("12 h 3 3 3")};
		cars2v = new Car[]{new Car("8 v 2 1 5"), new Car("9 v 2 5 2"), new Car("11 v 2 5 2")}; // the last pair of cars is equal, only id changes
		cars3v = new Car[]{new Car("5 v 3 6 1"), new Car("6 v 3 1 2"), new Car("7 v 3 4 2")};		
	}

	@Test
	void testCollision() {
		assertTrue(Car.collision(cars2v[1], cars2v[2])); //equal
		
		assertTrue(Car.collision(cars3h[2], cars2v[2])); //perpendicular
		
		assertTrue(Car.collision(cars3h[2], cars2h[0])); //parallel
		
		assertTrue(!Car.collision(cars2v[1], cars3v[2])); //close
		
		assertTrue(!Car.collision(cars2h[0], cars2v[2]));//perpendicular
		
		assertTrue(!Car.collision(cars3v[0], cars2v[0])); //parallel
		
		
	}


}
