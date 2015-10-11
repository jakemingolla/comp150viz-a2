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
PieGraph pieGraph;

RenderState renderState;
String data_path = "data.csv";


ArrayList<Data> dataPoints = new ArrayList<Data>();
String[] nameLabels;

public void setup() {

    size(800, 600);
    if(frame != null) {
        frame.setResizable(true);
    }

    ArrayList<Data> values = readData(data_path);

    /* TODO add nameLabels */
    barGraph  = new BarGraph(values);
    lineGraph = new LineGraph(values);
    pieGraph = new PieGraph(values);

    transitionManager = new TransitionManager(barGraph, lineGraph);

    renderState = RenderState.LINE_RS;
}

public void draw() {

    //shit.txt
/*    pieGraph.render();*/
/*    barGraph.render();*/
    lineGraph.render();

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

    for(int i = 1; i < lines.length; i++ ) {
        println(i + ": " + lines[i]);
    }

    println("ll: " + lines.length);
    for (int i = 1; i < lines.length; i++) {
        String[] splitLine = split(lines[i], ",");
        ArrayList<Float> floatValues = new ArrayList<Float>();
        println("---------------");
        for (int j = 1; j < splitLine.length; j++) {
            floatValues.add(parseFloat(splitLine[j]));
            println(splitLine[j]);
        }
        Data d = new Data(splitLine[0], floatValues);
        values.add(d);
    }

    return values;
}

/*}*/


public class BarGraph extends Graph {
	//screen width/height
    int w, h;

    // positioning info
    int x_origin;
    int y_origin;
    int x_axis_width;
    int y_axis_height;

    // meta 
    String xName;
    String yName;
    final float margin_ratio = 0.15f;
    int highlighted = -1;

    // data
    ArrayList<Data> values;

	BarGraph(ArrayList<Data> values) {
		super(values);

        w = width;
        h = height;

        x_axis_width  = (int)(w * (1 - 2*margin_ratio));
        y_axis_height = (int)(h * (1 - 2*margin_ratio));

        x_origin = (int)(w * margin_ratio);
        y_origin = (int)(h * (1 - margin_ratio));

        xName = nameLabels[0];
        yName = nameLabels[1];

        this.values = values;

        println("in barGraph, values are: ");
        for (Data d : values) {
            println(d.getValues().get(0));
        }

	}

	@Override
	public void render() {
        /* update canvas fields */
        w = width;
        h = height;
        x_axis_width  = (int)(width * (1 - 2*margin_ratio));
        y_axis_height = (int)(height * (1 - 2*margin_ratio));
        x_origin = (int)(width * margin_ratio);
        y_origin = (int)(height * (1 - margin_ratio));

    
        isHovering();

        /* re-render the parts of the graph */
        drawAxes(x_origin, y_origin);
        drawLabels(x_origin, y_origin);
        drawBars(x_origin, y_origin);
	}

    public void drawAxes(int x_origin, int y_origin) {

        int axis_h = (int)(height * (1 - margin_ratio));

        //x axis
        line(w * margin_ratio, axis_h, (width * (1-margin_ratio)), axis_h);
        //y axis
        line(w * margin_ratio, axis_h, w * margin_ratio, height * margin_ratio);

    }

    public void drawLabels(int x_origin, int y_origin) {

        int axis_h = (int)(height * (1 - margin_ratio));
        text(xName, x_origin + (w * margin_ratio / 2),
            y_origin + (h * margin_ratio / 2),
            x_origin + (w * margin_ratio / 2),
            y_origin + (h * margin_ratio / 2));
        text(yName, x_origin - (w * margin_ratio / 2),
             y_origin - (h * margin_ratio / 2), x_origin, y_origin);

    }

    public void drawBars(int x_origin, int y_origin) {

        float max_height = findMax(values);

        int bar_width   = (int)((x_axis_width * 0.75f)/values.size());
        int space_width = (int)((x_axis_width * 0.25f)/values.size());

        for (int i = 0; i < values.size(); i++) {

            float height_ratio = (values.get(i).getValues().get(0) / max_height);

            int bar_x = (x_origin + (i * bar_width) + ((i+1) * space_width));
            int bar_y = (int)(y_origin - (y_axis_height * height_ratio));

            // if the highlighted one, change paint color for this rectangle to white
            if (i == highlighted) {
                fill (255, 255, 255);
            // else purdy colorz
            } else {
                fill((20 * i) % 255, (30 * i) % 255, (40 * i) % 255);
            }

/*
            println("drawing rectangle with x = " + bar_x + ", y = " + bar_y + ", w = " + bar_width
                    + ", h = " + (y_axis_height * height_ratio));
                    */
            rect(bar_x, bar_y, bar_width, (y_axis_height * height_ratio));
            fill(0xff000000);

            pushMatrix();
            translate(bar_x, bar_y + (y_axis_height * height_ratio) + (h * margin_ratio /8));
            rotate(HALF_PI * 0.8f);
            text(values.get(i).getDataName(), 0, 0);
            popMatrix();
            
/*            text(values.get(i).getDataName(), bar_x, */
/*                 bar_y + (y_axis_height * height_ratio) + (h * margin_ratio / 4));*/

        } 
    }

    public void isHovering() {

        int mx = mouseX;
        int my = mouseY;

        float max_height = findMax(values);

        int bar_width   = (int)((x_axis_width * 0.75f)/values.size());
        int space_width = (int)((x_axis_width * 0.25f)/values.size());

        for (int i = 0; i < values.size(); i++) {

            float height_ratio = (values.get(i).getValues().get(0) / max_height);

            int bar_x = (x_origin + (i * bar_width) + ((i+1) * space_width));
            int bar_y = (int)(y_origin - (y_axis_height * height_ratio));

            if (mx >= bar_x && mx <= bar_x + bar_width &&
                my >= bar_y && my <= bar_y + (y_axis_height * height_ratio)) {
                highlighted = i;
                return;
            }
        } 

        highlighted = -1;
    }
}


public class Data {

	private String name;
	private ArrayList<Float> values;

	public Data(String name, ArrayList<Float> values) {
		this.name = name;
		this.values = values;
	}

	public String getDataName() {
		return name;
	}

	public void setDataName(String name) {
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
		this.values = values;
	}

    public float findMax(ArrayList<Data> arr) {
        float max = 0;
        for (Data d : arr) {
            if (d.getValues().get(0) > max) {
                max = d.getValues().get(0);
            }
        }
        return max;
    }


}


public class LineGraph extends Graph {

	//screen width/height
    int w, h;

    // positioning info
    int x_origin;
    int y_origin;
    int x_axis_width;
    int y_axis_height;

    // meta 
    String xName;
    String yName;
    final float margin_ratio = 0.15f;
    int highlighted = -1;

    // data
    ArrayList<Data>  values;
    ArrayList<Point> points;

	LineGraph(ArrayList<Data> values) {
		super(values);

        w = width;
        h = height;

        x_axis_width  = (int)(w * (1 - 2*margin_ratio));
        y_axis_height = (int)(h * (1 - 2*margin_ratio));

        x_origin = (int)(w * margin_ratio);
        y_origin = (int)(h * (1 - margin_ratio));

        xName = nameLabels[0];
        yName = nameLabels[1];

        this.values = values;

        println("in line graph, values are: ");
        for (Data d : values) {
            println(d.getValues().get(0));
        }

        points = new ArrayList<Point>();
        makePoints();

	}

	@Override
	public void render() {
        /* update canvas fields */
        w = width;
        h = height;
        x_axis_width  = (int)(width * (1 - 2*margin_ratio));
        y_axis_height = (int)(height * (1 - 2*margin_ratio));
        x_origin = (int)(width * margin_ratio);
        y_origin = (int)(height * (1 - margin_ratio));

    
/*        isHovering();*/

        /* re-render the parts of the graph */
        drawAxes(x_origin, y_origin);
        drawLabels(x_origin, y_origin);
/*        drawBars(x_origin, y_origin);*/
        drawPoints();
        drawLines();
        
	}

    public void drawAxes(int x_origin, int y_origin) {

        int axis_h = (int)(height * (1 - margin_ratio));

        //x axis
        line(w * margin_ratio, axis_h, (width * (1-margin_ratio)), axis_h);
        //y axis
        line(w * margin_ratio, axis_h, w * margin_ratio, height * margin_ratio);

    }

    public void drawLabels(int x_origin, int y_origin) {

        int axis_h = (int)(height * (1 - margin_ratio));
        text(xName, x_origin + (w * margin_ratio / 2),
            y_origin + (h * margin_ratio / 2),
            x_origin + (w * margin_ratio / 2),
            y_origin + (h * margin_ratio / 2));
        text(yName, x_origin - (w * margin_ratio / 2),
             y_origin - (h * margin_ratio / 2), x_origin, y_origin);

    }

    public void makePoints() {

        float max_height = findMax(values);
        float line_len   = (x_axis_width / (values.size() + 1)); // half for each end

        for(int i = 0; i < values.size(); i++ ) {
            float height_ratio = (values.get(i).getValues().get(0) / max_height);

            int px = (int)((i * line_len) + (line_len/2) + x_origin);
            int py = (int)(y_origin - (y_axis_height * height_ratio));

            Point tmp = new Point(px, py);
            points.add(tmp);
            println("adding new point at: " + px + ", " + py);
        }
    }

    public void drawPoints() {
        for(Point p : points) {
            fill(0, 0, 0);
            ellipse(p.getX(), p.getY(), 5, 5);
        }
    }

    public void drawLines() {
        for(int i = 1; i < points.size(); i++) {
            Point p1 = points.get(i - 1); 
            Point p2 = points.get(i); 

            line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
/*            println("drawing from: " + p1.getX() + ", " + p1.getY());*/
/*            println("drawing to  : " + p2.getX() + ", " + p2.getY());*/
        }
    }


}
 public class PieGraph extends Graph {

    
    //screen width/height
    int w, h;

    // positioning info
    int x_origin;
    int y_origin;

    // meta 
    String xName;
    String yName;
    final float margin_ratio = 0.15f;
    int highlighted = -1;

    ///data
    float sum = 0;
    int radius;

    float scale = 0.0f;
    
    ArrayList<Data> values;

	PieGraph(ArrayList<Data> values) {
		super(values);
        
        w = width;
        h = height;

        x_origin = (int)(w / 2);
        y_origin = (int)(h / 2);

        xName = nameLabels[0];
        yName = nameLabels[1];

        radius = (int)(height/(4.0f/3));

        this.values = values;

        for (Data d : values) {
            sum += d.getValues().get(0);
        }
	}
	
    public void drawSlices() {

        float tmp_pos = 0.0f;
        int color_seed = 0;
        for (Data d : values) {
            float rad_ratio = (d.getValues().get(0)/sum) * 2 * PI;
            float tmp = (rad_ratio/(2*PI)) * 360;
            if( color_seed == highlighted) {
                fill(255, 0, 0);
            } else {
                fill(((color_seed + 1) * 50) % 255, ((color_seed + 2) * 40) % 255, ((color_seed + 3) * 30) % 255);
            }

            arc(x_origin, y_origin, radius, radius, tmp_pos, tmp_pos + rad_ratio);
    
            drawLabels(d.getDataName(), tmp_pos, rad_ratio, radius);

            tmp_pos += rad_ratio;
            tmp = (tmp_pos/(2*PI)) * 360;
            color_seed++;
        }
    }

    public void drawLabels(String name, float startAngle, float endAngle, float radius) {

/*        println(name + " at angle: " + startAngle );*/
        fill(0xff000000);

        pushMatrix();
        translate(w/2, h/2); //move to origin

        if((startAngle < HALF_PI) || (startAngle > 3 * PI / 2)) {
            rotate(startAngle + (endAngle/2)); //rotate about origin
            textAlign(CENTER);
            text(name, (radius / 2) * 1.15f, 0);
        } else {
            rotate((PI) + (startAngle + (endAngle/2))); //rotate about origin
            textAlign(CENTER);
            text(name, -1 * ((radius / 2) * 1.15f), 0);
        }

        popMatrix();
    }


    public void render() {
        /* update canvas fields */
        w = width;
        h = height;
        x_origin = (int)(width / 2);
        y_origin = (int)(height / 2);

        /* set radii appropriately */
        radius =(int) ((height < width) ? (height/ (4.0f/3)) : (width / (4.0f/3)));

        /* re-render the parts of the graph */
        drawSlices();
        is_hovering();
    }

   
    public void is_hovering() {

        int x_dif  = mouseX - x_origin;
        int y_dif  = mouseY - y_origin;
        
        // if mouse is in bounds of outer circle
        x_dif = ((x_dif < 0) ? (-1 * x_dif) : x_dif);
        y_dif = ((y_dif < 0) ? (-1 * y_dif) : y_dif);

        float dist = sqrt(pow(x_dif, 2) + pow(y_dif, 2));
 
        boolean inRadius = (dist < radius/2); //&& (d > hole_radius/2);

        int counter = 0;
        if (inRadius) {
            float tmp_pos = 0;
            for (Data d : values) {
                float rad_ratio = (d.getValues().get(0)/sum) * 2 * PI;
                float _start = tmp_pos;
                float _end = tmp_pos += rad_ratio;
                
                float new_x = (((float)mouseX/width)  - 0.5f) * width;
                float new_y = -1 * (((float)mouseY/height) - 0.5f) * height;

                float angle = 0.0f;
                if (new_x < 0.0f) {
                     angle = atan((new_y)/new_x) + PI/2 ;
                     angle += PI/2;
                } else {
                     angle = atan((new_y)/new_x);
                }

                if( angle < 0 ) {
                    angle = (angle) + (PI* 2);
                }

                //convert to angle bc i'm dum and rads r hard
                float angle_d = (angle/(2 * PI)) * 360;
                float start_d = (_start/(2 * PI)) * 360;
                float end_d = (_end/(2 * PI)) * 360;

                if(angle_d < (360.0f - start_d) && angle_d > (360.0f - end_d)) {
                    highlighted = counter;
                    break;
                } else {
                    counter++;
                }
            }
        } else {
           highlighted = -1;
           return;
        }


    }
    public void setHighlighted(int h) {
        highlighted = h;
    }

    public void setScale(float f) {
        scale = f;
    }

    public float getScale() {
        return scale;
    }
}
public class Point {
    private int x,y;

    Point() {
        x = 0;
        y = 0;
    }
    
    Point(int _x, int _y) {
        x = _x;
        y = _y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int _x) {
        x = _x;
    }

    public void setY(int _y) {
        y = _y;
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
