package rush_hour;

import java.util.Comparator;

public class Blocking_Comparator implements Comparator<State>{
	
	
	@Override
	public String toString() {
		return "Blocking Heuristics";
	}

	@Override
	public int compare(State self, State other) {
		return self.a_star_heuristics() - other.a_star_heuristics();
	}
}
