import java.util.ArrayList;

public class LineGraph extends Graph {

    // successful push?
	LineGraph(ArrayList<Data> values) {
		super(values);
	}
	
	@Override
	public void render() {
		System.out.println("RENDERING LINE");
	}

}
