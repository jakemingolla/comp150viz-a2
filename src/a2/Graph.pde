import java.util.ArrayList;

public abstract class Graph implements Renderable {
	private ArrayList<Data> values;
	
	Graph(ArrayList<Data> values) {
		this.values = values;
	}

    float findMax(ArrayList<Data> arr) {
        float max = 0;
        for (Data d : arr) {
            if (d.getValues().get(0) > max) {
                max = d.getValues().get(0);
            }
        }
        return max;
    }


}
