import java.util.ArrayList;

public class TransitionManager {
	
	public ArrayList<Transition> transitions;
	
	TransitionManager(BarGraph barGraph, LineGraph lineGraph) {
		transitions = new ArrayList<Transition>();
		TransitionBarToLine barToLine = new TransitionBarToLine(barGraph, lineGraph);
		transitions.add(barToLine);
	}
	
	public void addTransition(Transition<? extends Graph, ? extends Graph> transition) {
		this.transitions.add(transition);
	}
	
	public Transition<? extends Graph, ? extends Graph> getTransition(Class<? extends Graph> base, Class<? extends Graph> target) {
		Boolean found = false;
		for (Transition transition : transitions) {
			if (transition.getBaseClass() == base && 
				transition.getTargetClass() == target) {
				return transition;
			}
		}
		
		return null;
	}
}
