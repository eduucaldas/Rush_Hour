package rush_hour;

import java.util.Comparator;

public class My_Comparator implements Comparator<State>{
		
	@Override
	public String toString() {
		return "My Heuristics";
	}

	@Override
	public int compare(State self, State other) {
		if(self.my_heuristics() > other.my_heuristics()) {
			return 1;
		}
		else if(self.my_heuristics() < other.my_heuristics()) {
			return -1;
		}
		else
			return 0;
	}
	
	
}
