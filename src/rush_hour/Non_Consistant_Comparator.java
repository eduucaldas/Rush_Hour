package rush_hour;

import java.util.Comparator;

public class Non_Consistant_Comparator implements Comparator<State>{
	//This just uses the natural order of the states, which favors states close to the right and bottom limit
	@Override
	public String toString() {
		return "Non Consistant Heuristics";
	}

	@Override
	public int compare(State self, State other) {
		return self.compareTo(other);
	}
}
