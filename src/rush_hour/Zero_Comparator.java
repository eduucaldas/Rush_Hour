package rush_hour;

import java.util.Comparator;

public class Zero_Comparator implements Comparator<State>{
	
	
	@Override
	public String toString() {
		return "Zero Heuristics";
	}

	@Override
	public int compare(State self, State other) {
		return self.zero_heuristics() - other.zero_heuristics();
	}
}
