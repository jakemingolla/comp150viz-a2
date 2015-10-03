package a2;

import java.util.ArrayList;

public class A2 {

	TransitionManager transitionManager;
	BarGraph barGraph;
	LineGraph lineGraph;

	RenderState renderState;

	public void setup() {
		ArrayList<Data> values = readData();

		barGraph = new BarGraph(values);
		lineGraph = new LineGraph(values);

		transitionManager = new TransitionManager(barGraph, lineGraph);

		renderState = RenderState.LINE;
	}

	public ArrayList<Data> readData() {
		ArrayList<Data> values = new ArrayList<Data>();
		ArrayList<Float> floats = new ArrayList<Float>();
		floats.add((float) 19);
		Data data = new Data("data1", floats);
		values.add(data);
		return values;
	}

	public void draw() {
		switch (renderState) {
		case LINE:
			System.out.println("LINE");
			break;
		case BAR:
			System.out.println("BAR");
			break;
		case PIE:
			System.out.println("PIE");
		}
	}

	public static void main(String[] args) {
		A2 a2 = new A2();
		a2.setup();

		while (true) {
			a2.draw();
		}

	}

}
