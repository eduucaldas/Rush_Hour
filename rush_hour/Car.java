package rush_hour;

import java.util.StringTokenizer;

public class Car implements Comparable<Car>{
	final static int SMALL = 2;
	final static int LARGE = 3;
	final static char VERTICAL = 'v';
	final static char HORIZONTAL = 'h';
	private final int id;	//useless, we say two cars are equal regardless of this
	private final boolean red;
	private final int len;
	private final char dir;
	public int x;
	public int y;

	public Car(int id, char dir, int len, int x, int y) throws IllegalArgumentException {


		if(id>0)
			this.id = id;
		else
			throw new IllegalArgumentException("id = " + id + " Should be greater than 0");

		this.red = (id == 1);

		if(len == SMALL || len == LARGE)
			this.len = len;
		else
			throw new IllegalArgumentException("len = " + len + " Should be 2 or 3");

		if(dir == VERTICAL || dir == HORIZONTAL)
			this.dir = dir;
		else
			throw new IllegalArgumentException("dir = " + dir + " Should be v or h");

		if(x >= 0 && y >= 0){
			this.x = x;
			this.y = y;
		}
		else
			throw new IllegalArgumentException("x,y = " + dir + " Should be bigger than zero");
		}

	//ATTENTION: in input (x,y) go from 1 to len, but our att come from 0 to len-1
	public Car(String line) throws IllegalArgumentException{
		StringTokenizer st = new StringTokenizer(line);

		int id = Integer.parseInt(st.nextToken());
		char dir = st.nextToken().toString().charAt(0);
		int len = Integer.parseInt(st.nextToken());
		int x = Integer.parseInt(st.nextToken()) - 1;
		int y = Integer.parseInt(st.nextToken()) - 1;

		this.red = (id == 1);

		if(id>0)
			this.id = id;
		else
			throw new IllegalArgumentException("id = " + id + " Should be greater than 0");

		if(len == SMALL || len == LARGE)
			this.len = len;
		else
			throw new IllegalArgumentException("len = " + len + " Should be 2 or 3");

		if(dir == VERTICAL || dir == HORIZONTAL)
			this.dir = dir;
		else
			throw new IllegalArgumentException("dir = " + dir + " Should be v or h");

		if(x >= 0 && y >= 0){
			this.x = x;
			this.y = y;
		}
		else
			throw new IllegalArgumentException("x,y = " + dir + " Should be bigger than zero");
	}

	public int get_type(){
		return (this.len() - 1) + ((this.dir() == HORIZONTAL)? 0 : 2);
	}

	public int id() {
		return this.id;
	}

	public int len() {
		return this.len;
	}

	public char dir() {
		return this.dir;
	}

	public boolean red() {
		return this.red;
	}

	@Override
	public String toString() {
		return "Car(id = " + this.id() + ", red = " + this.red() + ", len = " + this.len() + ", dir = " + this.dir() + ", (x, y) = (" + this.x + ", " + this.y + "))";
	}

	@Override
	public int compareTo(Car other) {
		//IMPORTANT: This method compares two cars in the following order:
		// 1. types
		// 2. position, the most important position being the one orthogonal to the car direction.
		int comp_type = this.get_type() - other.get_type();
		if(comp_type == 0) {
			int comp_1 = 0;
			int comp_2 = 0;
			if(this.dir() == VERTICAL) {
				comp_1 = this.x - other.x;
				comp_2 = this.y - other.y;
			}
			else {
				comp_1 = this.y - other.y;
				comp_2 = this.x - other.x;
			}

			if(comp_1 == 0)
				return comp_2;
			else
				return comp_1;
		}
		else return comp_type;
	}

	@Override
	public boolean equals(Object other) {
		return this.compareTo((Car)other) == 0;
	}

	public static boolean collision(Car one, Car other) {
		if(one.dir() != other.dir()) {
			Car h, v;

			if(one.dir() == HORIZONTAL) {
				h = one;
				v = other;
			}
			else {
				h = other;
				v = one;
			}

			//draw it and see it. the axis are counted from top left
			return (h.y - v.y >= 0) && (h.y - v.y <= v.len - 1) && (v.x - h.x >= 0) && (v.x - h.x <= h.len - 1);
		}
		else {
			if(one.dir() == VERTICAL && one.x == other.x) {
				Car up, down;
				if(one.y <= other.y) {
					up = one;
					down = other;
				}
				else {
					up = other;
					down = one;
				}
				return (down.y - up.y) <= up.len - 1;
			}
			else if(one.dir() == HORIZONTAL  && one.y == other.y) {
				Car left, right;
				if(one.y <= other.y) {
					left = one;
					right = other;
				}
				else {
					left = other;
					right = one;
				}
				return (left.x - right.x) <= left.len - 1;
			}
		}
		return false;

	}

}


