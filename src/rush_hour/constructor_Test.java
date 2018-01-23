package rush_hour;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;

class constructor_Test {
	
	@Test
	void testCar() {
		Car one_car = new Car(2, 'h', 2, 0, 0);
		Car one_car_str = new Car("2 h 2 1 1");// the first block on the top left is 1 1 for the input
		
		assertEquals(one_car, one_car_str);
		
		Car[] basic_car_str = new Car[]{new Car("1 h 2 2 3"), new Car("2 h 2 1 1"), new Car("3 h 2 5 5"), new Car("4 h 3 3 6"), new Car("5 v 3 6 1"), new Car("6 v 3 1 2"), new Car("7 v 3 4 2"), new Car("8 v 2 1 5")};
		
		//the order in the constructor is the same as in the input
		Car[] basic_car = new Car[]{new Car(1, 'h', 2, 1, 2), new Car(2, 'h', 2, 0, 0), new Car(3, 'h', 2, 4, 4), new Car(4, 'h', 3, 2, 5), new Car(5, 'v', 3, 5, 0), new Car(6, 'v', 3, 0, 1), new Car(7, 'v', 3, 3, 1), new Car(8, 'v', 2, 0, 4)};
		
		for(int i = 0; i < basic_car.length; i++)
			assertEquals(basic_car[i], basic_car_str[i]);
		
	}
	


}
