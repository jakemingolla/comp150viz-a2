public class TransitionBarToLine extends Transition<BarGraph, LineGraph> {

	TransitionBarToLine(BarGraph base, LineGraph target) {
		super(base, target);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void transition() {
		System.out.println("Transitioning from a BarGraph to a LineGraph");
		System.out.println("Calling BarGraph render():");
		base.render();
		System.out.println("Calling LineGraph render():");
		target.render();
	}
	
	@Override
	public void render() {
		
	}

}
