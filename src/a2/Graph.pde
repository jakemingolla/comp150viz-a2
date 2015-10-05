import java.util.ArrayList;

public abstract class Graph implements Renderable {
	private ArrayList<Data> values;
	
	Graph(ArrayList<Data> values) {
		System.out.println("Calling graph constructor with " + values);
		this.values = values;
	}
}
