import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.ArrayList; 
import java.util.ArrayList; 
import java.util.ArrayList; 
import java.util.ArrayList; 
import java.util.ArrayList; 
import java.util.ArrayList; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class a2 extends PApplet {



/*public class a2 {*/

TransitionManager transitionManager;
BarGraph barGraph;
LineGraph lineGraph;

RenderState renderState;
String data_path = "data.csv";


ArrayList<Data> dataPoints = new ArrayList<Data>();
String[] nameLabels;

public void setup() {

    size(800, 600);

    ArrayList<Data> values = readData(data_path);

/*    println(values);*/

    barGraph  = new BarGraph(values);
    lineGraph = new LineGraph(values);

    transitionManager = new TransitionManager(barGraph, lineGraph);

    renderState = RenderState.LINE_RS;
}

public void draw() {
    switch (renderState) {
    case LINE_RS:
        break;
    case BAR_RS:
        break;
    case PIE_RS:
        break;
    }
}

public ArrayList<Data> readData(String path) {
    ArrayList<Data> values  = new ArrayList<Data>();

    String[] lines = loadStrings(path);
    nameLabels = split(lines[0], ",");

    for(int i = 0; i < lines.length; i++ ) {
        println(i + ": " + lines[i]);
    }

    println("ll: " + lines.length);
    for (int i = 1; i < lines.length; i++) {
        String[] splitLine = split(lines[i], ",");
        ArrayList<Float> floatValues = new ArrayList<Float>();
        for (int j = 0; j < splitLine.length; j++) {
            floatValues.add(parseFloat(splitLine[j]));
        }
        Data d = new Data(splitLine[0], floatValues);
        values.add(d);
    }

    return values;
}

/*}*/


public class BarGraph extends Graph {
	
	BarGraph(ArrayList<Data> values) {
		super(values);
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
}


public class Data {

	private String name;
	private ArrayList<Float> values;

	public Data(String name, ArrayList<Float> values) {
		this.name = name;
		this.values = values;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Float> getValues() {
		return values;
	}

	public void setValues(ArrayList<Float> values) {
		this.values = values;
	}
	
}


public abstract class Graph implements Renderable {
	private ArrayList<Data> values;
	
	Graph(ArrayList<Data> values) {
		System.out.println("Calling graph constructor with " + values);
		this.values = values;
	}
}


public class LineGraph extends Graph {

	LineGraph(ArrayList<Data> values) {
		super(values);
	}
	
	@Override
	public void render() {
		System.out.println("RENDERING LINE");
	}

}
public interface Renderable {
	public void render();
}
/**
 * 
 * @author jake mingolla
 *
 * @param <A> {@link Graph}
 * @param <B> {@link Graph}
 */
public abstract class Transition<A extends Graph, B extends Graph> implements Renderable {
	protected A base;
	protected B target;
	
	Transition(A base, B target) {
		this.base = base;
		this.target = target;
	}
	
	public Class<A> getBaseClass() {
		return (Class) base.getClass();
	}
	
	public Class<B> getTargetClass() {
		return (Class) target.getClass();
	}
	
	public void transition() {
	}
}
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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "a2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
