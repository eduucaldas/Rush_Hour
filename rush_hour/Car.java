package rush_hour;


public class Car {
	final static int SMALL = 2;
	final static int LARGE = 3;
	final static int VERTICAL = 0;
	final static int HORIZONTAL = 1;
	private final int len;
	private final int dir;
	private final boolean red;
	public int pos
	
	public Car(int len, int dir, boolean red) {
		this.dir = dir;
		this.len = len;
		this.red = red;
	}
	
	public int len() {
		return this.len;
	}
	
	public int dir() {
		return this.dir;
	}
	
	public boolean red() {
		return this.red;
	}
	
	
		
}
