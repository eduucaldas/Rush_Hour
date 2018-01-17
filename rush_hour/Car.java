package rush_hour;

import java.util.StringTokenizer;

public class Car {
	final static int SMALL = 2;
	final static int LARGE = 3;
	final static char VERTICAL = 'v';
	final static char HORIZONTAL = 'h';
	private final int len;
	private final char dir;
	private final boolean red;
	public int x;
	public int y;

	public Car(String line){
		st = new StringTokenizer(line);

        int id = Integer.parseInt(st.nextToken());
        String dir = st.nextToken();
        int len = Integer.parseInt(st.nextToken());
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());

        this((id == 1), len, dir, x, y);
	}

	public Car(boolean red, int len, char dir, int x, int y) {
		this.red = red;

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
