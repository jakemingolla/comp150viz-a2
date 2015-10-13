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



TransitionManager transitionManager;
BarGraph barGraph;
LineGraph lineGraph;
PieGraph pieGraph;

RenderState currentRenderState;
Graph currentGraph;
String data_path = "data1.csv";


ArrayList<Data> dataPoints = new ArrayList<Data>();
String[] nameLabels;
int time;
int mfr = 100;

public void setup() {

    size(800, 600);
    frameRate(mfr);
    if(frame != null) {
        frame.setResizable(true);
    }


    ArrayList<Data> values = readData(data_path);

    barGraph  = new BarGraph(values);
    lineGraph = new LineGraph(values);
    pieGraph = new PieGraph(values);

    transitionManager = new TransitionManager(barGraph, lineGraph, pieGraph);

    currentRenderState = RenderState.LINE_RS;
    currentGraph = lineGraph;
    
    time = 0;
}

public void mousePressed() {
    for (Button b : currentGraph.getButtons()) {
        if (b.isInside(mouseX, mouseY)) {
            if (currentRenderState == RenderState.BAR_RS) {
                if (b.getRenderState() == RenderState.LINE_RS) {
                    currentRenderState = RenderState.BAR2LINE_RS;
                    currentGraph = barGraph;
                } else if (b.getRenderState() == RenderState.PIE_RS) {
                    currentRenderState = RenderState.BAR2PIE_RS;
                    currentGraph = pieGraph;
                } else {
                    currentRenderState = RenderState.BAR_RS;
                    currentGraph = barGraph;
                }
            } else if (currentRenderState == RenderState.LINE_RS) {
                if (b.getRenderState() == RenderState.BAR_RS) {
                    currentRenderState = RenderState.LINE2BAR_RS;
                    currentGraph = barGraph;
                } else if (b.getRenderState() == RenderState.PIE_RS) {
                    currentRenderState = RenderState.LINE2PIE_RS;
                    currentGraph = pieGraph;
                } else {
                    currentRenderState = RenderState.LINE_RS;
                    currentGraph = lineGraph;
                }
            } else if (currentRenderState == RenderState.PIE_RS) {
                if (b.getRenderState() == RenderState.BAR_RS) {
                    currentRenderState = RenderState.PIE2BAR_RS;
                    currentGraph = barGraph;
                } else if (b.getRenderState() == RenderState.LINE_RS) {
                    currentRenderState = RenderState.PIE2LINE_RS;
                    currentGraph = lineGraph;
                } else {
                    currentRenderState = RenderState.PIE_RS;
                    currentGraph = pieGraph;
                }
            }
        }
    }
}

public void keyPressed() {
    int k = 107;
    int j = 106;
    if(key == j && mfr >= 20) {
        mfr -= 10;
    } else if (key == k && mfr <= 150) {
        mfr += 10;
    }
}

public void draw() {
    background(200, 200, 200);
    frameRate(mfr);
    text("Max Frame Rate: " + mfr, 0 ,0, width, height);

    currentGraph.renderButtons();
    Transition t;
    switch (currentRenderState) {
    case BAR2LINE_RS:
        t = transitionManager.getTransition(BarGraph.class, LineGraph.class);
        drawTransition(t);
        break;
    case BAR2PIE_RS:
        t = transitionManager.getTransition(BarGraph.class, PieGraph.class);
        drawTransition(t);
        break;
    case LINE2BAR_RS:
        t = transitionManager.getTransition(LineGraph.class, BarGraph.class);
        drawTransition(t);
        break;
    case LINE2PIE_RS:
        t = transitionManager.getTransition(LineGraph.class, BarGraph.class);
        if (!t.isDone()) {
            drawTransition(t);
        } else {
            t = transitionManager.getTransition(BarGraph.class, PieGraph.class);
            drawTransition(t);
        }
        break;
     case PIE2BAR_RS:
        t = transitionManager.getTransition(PieGraph.class, BarGraph.class);
        drawTransition(t);
        break;
    case PIE2LINE_RS:
        t = transitionManager.getTransition(PieGraph.class, BarGraph.class);
        if (!t.isDone()) {
            drawTransition(t);
        } else {
            t = transitionManager.getTransition(BarGraph.class, LineGraph.class);
            drawTransition(t);
        }
        break;
    case LINE_RS:
        lineGraph.render();
        break;
    case BAR_RS:
        barGraph.render();
        break;
    case PIE_RS:
        pieGraph.render();
        break;
    }
}

public void drawTransition(Transition transition) {
    if (!transition.isDone()) {
        transition.render();
        transition.tick();
    } else {
        transition.resetRenderFrame();
        if (transition.getTargetClass() == BarGraph.class) {
            currentRenderState = RenderState.BAR_RS;
        } else if (transition.getTargetClass() == LineGraph.class) {
            currentRenderState = RenderState.LINE_RS;
        } else if (transition.getTargetClass() == PieGraph.class) {
            currentRenderState = RenderState.PIE_RS;
        }
    }
}

public ArrayList<Data> readData(String path) {
    ArrayList<Data> values  = new ArrayList<Data>();

    String[] lines = loadStrings(path);
    nameLabels = split(lines[0], ",");

    for (int i = 1; i < lines.length; i++) {
        String[] splitLine = split(lines[i], ",");
        ArrayList<Float> floatValues = new ArrayList<Float>();
        for (int j = 1; j < splitLine.length; j++) {
            floatValues.add(parseFloat(splitLine[j]));
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

        textAlign(LEFT);
        fill(0, 0, 0);
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
                stroke((20 * i) % 255, (30 * i) % 255, (40 * i) % 255);
                fill((20 * i) % 255, (30 * i) % 255, (40 * i) % 255);
            }

            rect(bar_x, bar_y, bar_width, (y_axis_height * height_ratio));
            fill(0xff000000);
            stroke(0, 0, 0);

            pushMatrix();
                translate(bar_x, bar_y + (y_origin - bar_y) + (height * margin_ratio /8));
                rotate(HALF_PI * 0.8f);
                textSize(12);
                textAlign(BOTTOM);
                text(values.get(i).getDataName(), 0, 0);
                textAlign(LEFT);
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
public class Button implements Renderable {
    int x, y, w, h;
    RenderState state;

    Button() {
        x = 0;
        y = 0;
        w = 0;
        h = 0;
        state = null;
    }

    Button(int a, int b, int c, int d, RenderState rs) {
        x = a;
        y = b;
        w = c;
        h = d;
        state = rs;
    }

    public boolean isInside(int mx, int my) {
        if((mx < x + w && mx > x) && (my < y + h && my > y)) {
            return true;
        }
        
        return false;
    }

    public RenderState getRenderState() {
        return state;
    }

    @Override
    public void render() {
        stroke(0, 0, 0);
        if (currentRenderState == RenderState.LINE_RS || currentRenderState == RenderState.BAR_RS || currentRenderState == RenderState.PIE_RS) {
            if (isInside(mouseX, mouseY)) {
                fill(255, 0, 0);
            } else {
                //fill (100, 100, 100);
                fill(255, 255, 255);
            }
        } else {
            fill(55, 55, 55);
        }
        rect(x, y, w, h);

        if (currentRenderState == RenderState.LINE_RS || currentRenderState == RenderState.BAR_RS || currentRenderState == RenderState.PIE_RS) {
            String t = "";
            switch(state) {
                case LINE_RS:
                    t = "LINE";
                    break;
                case BAR_RS:
                    t = "BAR";
                    break;
                case PIE_RS:
                    t = "PIE";
                    break;
                default:
            }
            textAlign(CENTER);
            //text(t, x + w/2, y + h/2, w, h);
            fill(0, 255, 0);
            text(t, x , y , w, h);
        } else {

        }
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
    private ArrayList<Button> buttons;
    final float margin_ratio = 0.2f;
	
	Graph(ArrayList<Data> values) {
		this.values = values;
        buttons = new ArrayList<Button>();
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

    public float findSum(ArrayList<Data> arr) {
        float sum = 0;
        for (Data d : arr) {
            sum += d.getValues().get(0);
        }
        return sum;
    }

    public ArrayList<Button> getButtons() {
        return buttons;
    }

    public float getMarginRatio() {
        return margin_ratio;
    }

    public void makeButtons() {
        
        buttons.clear();
        int bw, bh, sp;

        bw = (int)(width  * (2.0f/9.0f));
        bh = (int)(((margin_ratio * height) * (1.0f/4.0f)));
        sp = (int)(width *  (1.0f/9.0f));

        RenderState state;
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                state = RenderState.LINE_RS;
            } else if (i == 1) {
                state = RenderState.BAR_RS;
            } else if (i == 2) {
                state = RenderState.PIE_RS;
            } else {
                state = null;
            }
            Button b = new Button(sp/2 + (i * bw) + (sp * i),
                                  (int)(height - (height * margin_ratio)) + (3 *bh), bw, bh, state);
            buttons.add(b);
        }
    }

    public void renderButtons() {
        makeButtons();
        for (Button b : buttons) {
            b.render();
        }
    }

    public ArrayList<Data> getValues() {
        return values;
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
    int highlighted = -1;
    int radius = 8;

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

        points = new ArrayList<Point>();

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

    

        /* re-render the parts of the graph */
        points.clear();
        makePoints();
        isHovering();
        drawAxes(x_origin, y_origin);
        drawLabels(x_origin, y_origin);
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

        textAlign(LEFT);
        fill(0, 0, 0);

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
        float line_len   = (x_axis_width / (values.size())); // half for each end

        for(int i = 0; i < values.size(); i++ ) {
            float height_ratio = (values.get(i).getValues().get(0) / max_height);

            // LOL
            int bar_width   = (int)((x_axis_width * 0.75f)/values.size());
            int space_width = (int)((x_axis_width * 0.25f)/values.size()); 

            int bar_x = (x_origin + (i * bar_width) + ((i+1) * space_width));
            int px = bar_x + (bar_width/2);
            int py = (int)(y_origin - (y_axis_height * height_ratio));

            Point tmp = new Point(px, py);
            points.add(tmp);
        }
    }

    public void drawPoints() {
        int i = 0;
        for(Point p : points) {
            if (i == highlighted) {
                fill(255, 255, 255);
            } else {
                stroke((20 * i) % 255, (30 * i) % 255, (40 * i) % 255);
                fill((20 * i) % 255, (30 * i) % 255, (40 * i) % 255);
            }
            ellipse(p.getX(), p.getY(), radius, radius);
            stroke(0, 0, 0);

            // draw data labels 
            pushMatrix();
                //slanted under data
                //translate(p.getX(), p.getY() + (y_axis_height * (p.getY()/y_axis_height)) + (h * margin_ratio /8));

                int bar_width   = (int)((x_axis_width * 0.75f)/values.size());
                translate(p.getX() - (bar_width/2), p.getY() + (y_origin - p.getY()) + (height * margin_ratio /8));
                rotate(HALF_PI * 0.8f);
                textSize(12);
                textAlign(BOTTOM);
                fill(0, 0, 0);
                text(values.get(i).getDataName(), 0, 0);
                textAlign(LEFT);
            popMatrix();
            i++;
        }
    }

    public void drawLines() {
        for(int i = 1; i < points.size(); i++) {
            Point p1 = points.get(i - 1); 
            Point p2 = points.get(i); 
            line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
    }

    public void isHovering() {
        
        int my = mouseY;
        int mx = mouseX;

        int counter = 0;

        for (Point p: points) {
            if(abs(my - p.getY()) < radius && abs(mx - p.getX()) < radius) {
                highlighted = counter;
                return;
            }
            counter++;
        }
        highlighted = -1;
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

        radius = (int)(height/(5.0f/3));

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
                fill(255, 255, 255);
            } else {
                fill(((color_seed) * 20) % 255, ((color_seed) * 30) % 255, ((color_seed) * 40) % 255);
                stroke(((color_seed) * 20) % 255, ((color_seed) * 30) % 255, ((color_seed) * 40) % 255);
            }

            arc(x_origin, y_origin, radius, radius, tmp_pos, tmp_pos + rad_ratio);
    
            drawLabels(d.getDataName(), tmp_pos, rad_ratio, radius);

            tmp_pos += rad_ratio;
            tmp = (tmp_pos/(2*PI)) * 360;
            color_seed++;
        }
    }

    public void drawLabels(String name, float startAngle, float endAngle, float radius) {

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
        radius =(int) ((height < width) ? (height/ (5.0f/3)) : (width / (5.0f/3)));

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

    protected int renderFrame = 0;
    protected int totalRenderFrame = 600;
	
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

    public void setRenderFrame(int renderFrame) {
        this.renderFrame = renderFrame;
    }

    public void setTotalRenderFrame(int totalRenderFrame) {
        this.totalRenderFrame = totalRenderFrame;
    }

    public int getRenderFrame() {
        return renderFrame;
    }

    public int getTotalRenderFrame() {
        return totalRenderFrame;
    }

    public void resetRenderFrame() {
        renderFrame = 0;
    }

    public void tick() {
        renderFrame += 1;
    }

    public boolean isDone() {
        return renderFrame == totalRenderFrame;
    }
}
public class TransitionBarToLine extends Transition<BarGraph, LineGraph> {


    ArrayList<Point> points;
    ArrayList<Data> values;
    int radius = 8;

	TransitionBarToLine(BarGraph base, LineGraph target) {
		super(base, target);
		// TODO Auto-generated constructor stub
        points = new ArrayList<Point>();
        values = base.getValues();
	}
	
/*	@Override*/
	public void render() {
        /* update canvas fields */
        int w = width;
        int h = height;
        float margin_ratio = base.getMarginRatio();
        int x_axis_width  = (int)(width * (1 - 2*margin_ratio));
        int y_axis_height = (int)(height * (1 - 2*margin_ratio));
        int x_origin = (int)(width * margin_ratio);
        int y_origin = (int)(height * (1 - margin_ratio));

        /* re-render the parts of the graph */
        drawAxes(margin_ratio);
        drawLabels(x_origin, y_origin, margin_ratio);
        
        int stageFrames;
        int frameOffset;

        float max_height = base.findMax(values);

        int bar_width   = (int)((x_axis_width * 0.75f)/values.size());
        int space_width = (int)((x_axis_width * 0.25f)/values.size());

        points.clear();

        for (int i = 0; i < values.size(); i++) {

            float height_ratio = (values.get(i).getValues().get(0) / max_height);

            int bar_x = (x_origin + (i * bar_width) + ((i+1) * space_width));
            int bar_y = (int)(y_origin - (y_axis_height * height_ratio));

            // if the highlighted one, change paint color for this rectangle to white
            // else purdy colorz
            fill((20 * i) % 255, (30 * i) % 255, (40 * i) % 255);
            stroke((20 * i) % 255, (30 * i) % 255, (40 * i) % 255);
            
            if (renderFrame < (totalRenderFrame / 3)) {

                stageFrames = totalRenderFrame / 3;
                frameOffset = 0;
                float drawHeightRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);
                rect(bar_x, bar_y, bar_width, (y_axis_height * height_ratio) * (1- drawHeightRatio));

            } else if ((renderFrame >= totalRenderFrame/3) && (renderFrame < totalRenderFrame /2)) {
        
                stageFrames = totalRenderFrame / 6;
                frameOffset = totalRenderFrame / 3;
                float drawWidthRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);
                int bar_left = bar_x;
                int bar_right = bar_x + bar_width;
                line(bar_left + ((bar_width/2) * (drawWidthRatio)), bar_y, bar_right - ((bar_width/2) * (drawWidthRatio)), bar_y);
/*                line(bar_x + ((bar_width/2) * (1 - drawWidthRatio)), bar_y, (bar_x + (bar_width/2)) - ((bar_width/2) * (1 - drawWidthRatio)), bar_y);*/
/*                println("in middle third, frame = " + renderFrame);*/
            } else if ((renderFrame >= totalRenderFrame/2) && (renderFrame < 3 * totalRenderFrame / 4)) {
                stageFrames = totalRenderFrame / 4;
                frameOffset = totalRenderFrame / 2;

                int px = bar_x + bar_width/2;
                int py = bar_y;
                float radiusRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);

/*                fill(0, 0, 0);*/
                ellipse(px, py, radius * radiusRatio, radius * radiusRatio);
                stroke(0, 0, 0);
            } else if (renderFrame >= 3 * totalRenderFrame/4) {
                stageFrames = totalRenderFrame / 4;
                frameOffset = 3 * totalRenderFrame / 4;

                float lineRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);

/*                makePoints(margin_ratio);*/

                // make points
                int px = bar_x + (bar_width/2);
                int py = (int)(y_origin - (y_axis_height * height_ratio));

                Point tmp = new Point(px, py);
                points.add(tmp);

                ellipse(points.get(i).getX(), points.get(i).getY(), radius, radius);

                if(i > 0) {
                    Point p1 = points.get(i-1);
                    Point p2 = points.get(i);

                    int x1 = p1.getX();
                    int y1 = p1.getY();
                    int x2 = p2.getX();
                    int y2 = p2.getY();
                    stroke(0, 0, 0);
                    line(x1, y1, (x2 - x1) * lineRatio + x1, (y2 - y1) * lineRatio + y1);
                }
            }
   

/*        }*/

            fill(0xff000000);

            pushMatrix();
                translate(bar_x, bar_y + (y_origin - bar_y) + (height * margin_ratio /8));
                rotate(HALF_PI * 0.8f);
                textSize(12);
                textAlign(BOTTOM);
                text(values.get(i).getDataName(), 0, 0);
                textAlign(LEFT);
            popMatrix();
        }
    }

    public void drawLabels(int x_origin, int y_origin, float margin_ratio) {

        textAlign(LEFT);
        fill(0, 0, 0);

        String xName = nameLabels[0];
        String yName = nameLabels[1];

        int w = width;
        int h = height;

        int axis_h = (int)(height * (1 - margin_ratio));
        text(xName, x_origin + (w * margin_ratio / 2),
            y_origin + (h * margin_ratio / 2),
            x_origin + (w * margin_ratio / 2),
            y_origin + (h * margin_ratio / 2));
        text(yName, x_origin - (w * margin_ratio / 2),
             y_origin - (h * margin_ratio / 2), x_origin, y_origin);

    }

    public void drawAxes(float margin_ratio) {
        int axis_h = (int)(height * (1 - margin_ratio));
        //x axis
        line(width * margin_ratio, axis_h, (width * (1-margin_ratio)), axis_h);
        //y axis
        line(width * margin_ratio, axis_h, width * margin_ratio, height * margin_ratio);

    }
}
public class TransitionBarToPie extends Transition<BarGraph, PieGraph> {


    ArrayList<Point> points;
    ArrayList<Data> values;

	TransitionBarToPie(BarGraph base, PieGraph target) {
		super(base, target);
        points = new ArrayList<Point>();
        values = base.getValues();
	}
	
/*	@Override*/
	public void render() {
        /* update canvas fields */
        int w = width;
        int h = height;
        float margin_ratio = base.getMarginRatio();
        int x_axis_width  = (int)(width * (1 - 2*margin_ratio));
        int y_axis_height = (int)(height * (1 - 2*margin_ratio));
        int x_origin = (int)(width * margin_ratio);
        int axis_h = (int)(height * (1 - margin_ratio));
        int y_origin = (int)(height * (1 - margin_ratio));
        int diameter = (int) ((height < width) ? (height/ (5.0f/3)) : (width / (5.0f/3)));

        int stageFrames;
        int frameOffset;

        if (renderFrame < (totalRenderFrame / 10)) {
            stageFrames = totalRenderFrame /10;
            frameOffset = 0;
            float fadeRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);

            drawAxes(margin_ratio, fadeRatio);
            drawLabels(x_origin, y_origin, margin_ratio, fadeRatio);
        }
        

        float max_height = base.findMax(values);
        int sum = (int)base.findSum(values);
        
        float tmp_pos = 0.0f;

        int bar_width   = (int)((x_axis_width * 0.75f)/values.size());
        int space_width = (int)((x_axis_width * 0.25f)/values.size());

        for (int i = 0; i < values.size(); i++) {

            float height_ratio = (values.get(i).getValues().get(0) / max_height);

            int bar_x = (x_origin + (i * bar_width) + ((i+1) * space_width));
            int bar_y = (int)(y_origin - (y_axis_height * height_ratio));

            fill((20 * i) % 255, (30 * i) % 255, (40 * i) % 255);
            stroke((20 * i) % 255, (30 * i) % 255, (40 * i) % 255);
            
            if (renderFrame < (totalRenderFrame / 10)) {

                stageFrames = totalRenderFrame / 10;
                frameOffset = 0;
                float pinchRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);

                int bar_left = bar_x;
                int bar_right = bar_x + bar_width;

                rect(bar_left + ((bar_width/2) * (pinchRatio)), bar_y, bar_width - (bar_width * pinchRatio), y_axis_height * height_ratio);


            } else if ((renderFrame >= totalRenderFrame/10) && (renderFrame < totalRenderFrame /5)) {
        
                stageFrames = totalRenderFrame / 10;
                frameOffset = totalRenderFrame / 10;
                float shrinkRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);

                int target_height = (int)((values.get(i).getValues().get(0) / sum) * (PI * diameter));

                int top_y    = bar_y;
                int bottom_y = (int)(bar_y + (y_axis_height * height_ratio));

                int target_top = y_origin - target_height;
                int target_diff = target_top - top_y;

                int new_top = (int)(top_y + shrinkRatio * (target_diff));

                line(bar_x + bar_width/2, new_top, bar_x + bar_width/2, y_origin);

            } else if ((renderFrame >= totalRenderFrame/5) && (renderFrame < totalRenderFrame / 2)) {
                stageFrames = 3 * totalRenderFrame / 10;
                frameOffset = totalRenderFrame / 5;

                float moveRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);

                float rad_ratio = (values.get(i).getValues().get(0) / sum) * (2 * PI);
                int target_height = (int)((values.get(i).getValues().get(0) / sum) * (PI * diameter));

                float end_angle = rad_ratio + tmp_pos;
                float start_angle = tmp_pos;


                int start_x = (int)(diameter/2 * cos(start_angle) + width/2);
                int start_y = (int)(diameter/2 * sin(start_angle) + height/2);
                
                int end_x = (int)(diameter/2 * cos(end_angle) + width/2);
                int end_y = (int)(diameter/2 * sin(end_angle) + height/2);


                int top_y    = bar_y;
                int bottom_y = (int)(bar_y + (y_axis_height * height_ratio));

                int target_top = y_origin - target_height;

                int x_1 = bar_x + bar_width/2;
                int y_1 = target_top;

                int x_2 = bar_x + bar_width/2;
                int y_2 = y_origin;

                int diff_x1 = start_x - x_1;
                int diff_x2 = end_x - x_2;
                int diff_y1 = start_y - y_1;
                int diff_y2 = end_y - y_2;
                
                line(x_1 + moveRatio * diff_x1,
                     y_1 + moveRatio * diff_y1,
                     x_2 + moveRatio * diff_x2,
                     y_2 + moveRatio * diff_y2);

                tmp_pos += rad_ratio;

            } else if (renderFrame >= totalRenderFrame / 2 && (renderFrame < 9 * totalRenderFrame / 10)) {
                stageFrames = 4 * totalRenderFrame / 10;
                frameOffset = totalRenderFrame / 2;

                float moveRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);
                float rad_ratio = (values.get(i).getValues().get(0) / sum) * (2 * PI);
                int target_height = (int)((values.get(i).getValues().get(0) / sum) * (PI * diameter));

                float end_angle = rad_ratio + tmp_pos;
                float start_angle = tmp_pos;


                int start_x = (int)(diameter/2 * cos(start_angle) + width/2);
                int start_y = (int)(diameter/2 * sin(start_angle) + height/2);
                
                int end_x = (int)(diameter/2 * cos(end_angle) + width/2);
                int end_y = (int)(diameter/2 * sin(end_angle) + height/2);


                int top_y    = bar_y;
                int bottom_y = (int)(bar_y + (y_axis_height * height_ratio));

                int target_top = y_origin - target_height;

                int x_1 = bar_x + bar_width/2;
                int y_1 = target_top;

                int x_2 = bar_x + bar_width/2;
                int y_2 = y_origin;

                int diff_x1 = start_x - x_1;
                int diff_x2 = end_x - x_2;

                int diff_y1 = start_y - y_1;
                int diff_y2 = end_y - y_2;
                
                line(x_1 + diff_x1,
                     y_1 + diff_y1,
                     x_2 + diff_x2,
                     y_2 + diff_y2);

                tmp_pos += rad_ratio;

                arc(width/2, height/2, diameter * moveRatio, diameter * moveRatio,
                    start_angle, end_angle);
            } else if (renderFrame >= 9 * totalRenderFrame / 10) {
                stageFrames = totalRenderFrame / 10;
                frameOffset = 9 * totalRenderFrame / 10;
                float alphaRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);
                
                float rad_ratio = (values.get(i).getValues().get(0) / sum) * (2 * PI);
                int target_height = (int)((values.get(i).getValues().get(0) / sum) * (PI * diameter));
                float end_angle = rad_ratio + tmp_pos;
                float start_angle = tmp_pos;


                int start_x = (int)(diameter/2 * cos(start_angle) + width/2);
                int start_y = (int)(diameter/2 * sin(start_angle) + height/2);
                
                int end_x = (int)(diameter/2 * cos(end_angle) + width/2);
                int end_y = (int)(diameter/2 * sin(end_angle) + height/2);


                int top_y    = bar_y;
                int bottom_y = (int)(bar_y + (y_axis_height * height_ratio));

                int target_top = y_origin - target_height;

                int x_1 = bar_x + bar_width/2;
                int y_1 = target_top;

                int x_2 = bar_x + bar_width/2;
                int y_2 = y_origin;

                int diff_x1 = start_x - x_1;
                int diff_x2 = end_x - x_2;

                int diff_y1 = start_y - y_1;
                int diff_y2 = end_y - y_2;
                
                line(x_1 + diff_x1,
                     y_1 + diff_y1,
                     x_2 + diff_x2,
                     y_2 + diff_y2);

                tmp_pos += rad_ratio;

                arc(width/2, height/2, diameter, diameter, start_angle, end_angle);

                String name = values.get(i).getDataName();
                pushMatrix();
                    translate(width/2, height/2);
                        fill(200 - (200 * alphaRatio), 
                             200 - (200 * alphaRatio), 
                             200 - (200 * alphaRatio));
                        if (start_angle < HALF_PI || start_angle> (3 * PI / 2)) {
                        rotate(start_angle + (rad_ratio/2));
                            textAlign(CENTER);
                            text(name, ((diameter / 2) * 1.15f), 0);
                        } else {
                            rotate((PI) + (start_angle + (rad_ratio/2)));
                            textAlign(CENTER);
                            text(name, -1 * ((diameter / 2) * 1.15f), 0);
                        }
                popMatrix();
            }
   
            fill(0xff000000);
            if (renderFrame < (totalRenderFrame / 10)) {
                stageFrames = totalRenderFrame /10;
                frameOffset = 0;
                float fadeRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);
                int col = (int)(200.0f * fadeRatio);
                fill(col, col, col);
                pushMatrix();
                    translate(bar_x, bar_y + (y_origin - bar_y) + (height * margin_ratio /8));
                    rotate(HALF_PI * 0.8f);
                    textSize(12);
                    textAlign(BOTTOM);
                    text(values.get(i).getDataName(), 0, 0);
                    textAlign(LEFT);
                popMatrix();
                fill(0, 0, 0);
            }
        }
    }

    public void drawLabels(int x_origin, int y_origin, float margin_ratio, float fadeRatio) {

        textAlign(LEFT);
        int col = (int) (200.0f * fadeRatio);
        fill(col, col, col);

        String xName = nameLabels[0];
        String yName = nameLabels[1];

        int w = width;
        int h = height;

        int axis_h = (int)(height * (1 - margin_ratio));
        text(xName, x_origin + (w * margin_ratio / 2),
            y_origin + (h * margin_ratio / 2),
            x_origin + (w * margin_ratio / 2),
            y_origin + (h * margin_ratio / 2));
        text(yName, x_origin - (w * margin_ratio / 2),
             y_origin - (h * margin_ratio / 2), x_origin, y_origin);

    }

    public void drawAxes(float margin_ratio, float fadeRatio) {
        int axis_h = (int)(height * (1 - margin_ratio));
        int col = (int) (200.0f * fadeRatio);
        stroke(col, col, col);
        //x axis
        line(width * margin_ratio, axis_h, (width * (1-margin_ratio)), axis_h);
        //y axis
        line(width * margin_ratio, axis_h, width * margin_ratio, height * margin_ratio);

        stroke(0, 0, 0);
    }
}
public class TransitionLineToBar extends Transition<LineGraph, BarGraph> {


    ArrayList<Point> points;
    ArrayList<Data> values;
    int radius = 8;

	TransitionLineToBar(LineGraph base, BarGraph target) {
		super(base, target);
		// TODO Auto-generated constructor stub
        points = new ArrayList<Point>();
        values = base.getValues();
	}
	
/*	@Override*/
	public void render() {
        println("Rendering frame = " + renderFrame);
        /* update canvas fields */
        int w = width;
        int h = height;
        float margin_ratio = base.getMarginRatio();
        int x_axis_width  = (int)(width * (1 - 2*margin_ratio));
        int y_axis_height = (int)(height * (1 - 2*margin_ratio));
        int x_origin = (int)(width * margin_ratio);
        int y_origin = (int)(height * (1 - margin_ratio));

        /* re-render the parts of the graph */
        drawAxes(margin_ratio);
        drawLabels(x_origin, y_origin, margin_ratio);
        
        int stageFrames;
        int frameOffset;

        float max_height = base.findMax(values);

        int bar_width   = (int)((x_axis_width * 0.75f)/values.size());
        int space_width = (int)((x_axis_width * 0.25f)/values.size());

        points.clear();

        for (int i = 0; i < values.size(); i++) {

            float height_ratio = (values.get(i).getValues().get(0) / max_height);

            int bar_x = (x_origin + (i * bar_width) + ((i+1) * space_width));
            int bar_y = (int)(y_origin - (y_axis_height * height_ratio));

            // if the highlighted one, change paint color for this rectangle to white
            // else purdy colorz
            fill((20 * i) % 255, (30 * i) % 255, (40 * i) % 255);
            stroke((20 * i) % 255, (30 * i) % 255, (40 * i) % 255);
            
            // recede bars
            if (renderFrame >= (2 * totalRenderFrame/3)) {

                stageFrames = totalRenderFrame / 3;
                frameOffset = 2 * totalRenderFrame / 3;
                float drawHeightRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);
                rect(bar_x, bar_y, bar_width, (y_axis_height * height_ratio) * (drawHeightRatio));
                stroke(0, 0, 0);

            } else if ((renderFrame >= totalRenderFrame/2) && (renderFrame < (2 * totalRenderFrame /3))) {
        
                stageFrames = totalRenderFrame / 6;
                frameOffset = totalRenderFrame / 2;
                float drawWidthRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);
                int bar_left = bar_x;
                int bar_right = bar_x + bar_width;
                line(bar_left + ((bar_width/2) * (1 - drawWidthRatio)), bar_y, bar_right - ((bar_width/2) * (1 - drawWidthRatio)), bar_y);

            } else if ((renderFrame >= totalRenderFrame/4) && (renderFrame < totalRenderFrame / 2)) {

                stageFrames = totalRenderFrame / 4;
                frameOffset = totalRenderFrame / 4;

                int px = bar_x + bar_width/2;
                int py = bar_y;
                float radiusRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);

                ellipse(px, py, radius * (1 - radiusRatio), radius * (1 - radiusRatio));

            } else if (renderFrame < (totalRenderFrame / 4)) {

                stageFrames = totalRenderFrame / 4;
                //frameOffset = totalRenderFrame / 4;
                frameOffset = 0;

                float lineRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);

                // make points
                int px = bar_x + (bar_width/2);
                int py = (int)(y_origin - (y_axis_height * height_ratio));

                Point tmp = new Point(px, py);
                points.add(tmp);

                ellipse(points.get(i).getX(), points.get(i).getY(), radius, radius);

                if(i > 0) {
                    Point p1 = points.get(i-1);
                    Point p2 = points.get(i);

                    int x1 = p1.getX();
                    int y1 = p1.getY();
                    int x2 = p2.getX();
                    int y2 = p2.getY();
                    stroke(0, 0, 0);
                    line(x1, y1, ((x2 - x1) * (1 - lineRatio)) + x1, ((y2 - y1) * (1 - lineRatio)) + y1);
                }
            }

            fill(0xff000000);

            pushMatrix();
                translate(bar_x, bar_y + (y_origin - bar_y) + (height * margin_ratio /8));
                rotate(HALF_PI * 0.8f);
                textSize(12);
                textAlign(BOTTOM);
                text(values.get(i).getDataName(), 0, 0);
                textAlign(LEFT);
            popMatrix();
        }
    }

    public void drawLabels(int x_origin, int y_origin, float margin_ratio) {

        textAlign(LEFT);
        fill(0, 0, 0);

        String xName = nameLabels[0];
        String yName = nameLabels[1];

        int w = width;
        int h = height;

        int axis_h = (int)(height * (1 - margin_ratio));
        text(xName, x_origin + (w * margin_ratio / 2),
            y_origin + (h * margin_ratio / 2),
            x_origin + (w * margin_ratio / 2),
            y_origin + (h * margin_ratio / 2));
        text(yName, x_origin - (w * margin_ratio / 2),
             y_origin - (h * margin_ratio / 2), x_origin, y_origin);

    }

    public void drawAxes(float margin_ratio) {
        int axis_h = (int)(height * (1 - margin_ratio));
        //x axis
        line(width * margin_ratio, axis_h, (width * (1-margin_ratio)), axis_h);
        //y axis
        line(width * margin_ratio, axis_h, width * margin_ratio, height * margin_ratio);

    }
}


public class TransitionManager {
	
	public ArrayList<Transition> transitions;

    public int maxFrameCounter, frameCounter;
	
	TransitionManager(BarGraph barGraph, LineGraph lineGraph, PieGraph pieGraph) {
		transitions = new ArrayList<Transition>();

		TransitionBarToLine barToLine = new TransitionBarToLine(barGraph, lineGraph);
		transitions.add(barToLine);

        TransitionLineToBar lineToBar = new TransitionLineToBar(lineGraph, barGraph);
        transitions.add(lineToBar);

        TransitionBarToPie barToPie = new TransitionBarToPie(barGraph, pieGraph);
        transitions.add(barToPie);

        TransitionPieToBar pieToBar = new TransitionPieToBar(pieGraph, barGraph);
        transitions.add(pieToBar);
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
public class TransitionPieToBar extends Transition<PieGraph, BarGraph> {

    ArrayList<Point> points;
    ArrayList<Data> values;

	TransitionPieToBar(PieGraph base, BarGraph target) {
		super(base, target);
        points = new ArrayList<Point>();
        values = base.getValues();
	}
	
/*	@Override*/
	public void render() {
        /* update canvas fields */
        int w = width;
        int h = height;
        float margin_ratio = base.getMarginRatio();
        int x_axis_width  = (int)(width * (1 - 2*margin_ratio));
        int y_axis_height = (int)(height * (1 - 2*margin_ratio));
        int x_origin = (int)(width * margin_ratio);
        int axis_h = (int)(height * (1 - margin_ratio));
        int y_origin = (int)(height * (1 - margin_ratio));
        int diameter = (int) ((height < width) ? (height/ (5.0f/3)) : (width / (5.0f/3)));

        int stageFrames;
        int frameOffset;

        if (renderFrame >= (9 * totalRenderFrame / 10)) {
            stageFrames = totalRenderFrame /10;
            frameOffset = 9 * totalRenderFrame / 10;
            float fadeRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);

            drawAxes(margin_ratio, (1 - fadeRatio));
            drawLabels(x_origin, y_origin, margin_ratio, (1 - fadeRatio));
        }
        

        float max_height = base.findMax(values);
        int sum = (int)base.findSum(values);
        
        float tmp_pos = 0.0f;

        int bar_width   = (int)((x_axis_width * 0.75f)/values.size());
        int space_width = (int)((x_axis_width * 0.25f)/values.size());

        for (int i = 0; i < values.size(); i++) {

            float height_ratio = (values.get(i).getValues().get(0) / max_height);

            int bar_x = (x_origin + (i * bar_width) + ((i+1) * space_width));
            int bar_y = (int)(y_origin - (y_axis_height * height_ratio));

            fill((20 * i) % 255, (30 * i) % 255, (40 * i) % 255);
            stroke((20 * i) % 255, (30 * i) % 255, (40 * i) % 255);
            
            if (renderFrame >= (9 * totalRenderFrame / 10)) {

                stageFrames = totalRenderFrame / 10;
                frameOffset = 9 * totalRenderFrame / 10;
                float pinchRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);

                int bar_left = bar_x;
                int bar_right = bar_x + bar_width;

                rect(bar_left + ((bar_width/2) * (1 - pinchRatio)), bar_y, bar_width - (bar_width * (1 - pinchRatio)), y_axis_height * height_ratio);


            } else if ((renderFrame >= 8 * totalRenderFrame/10) && (renderFrame < 9 * totalRenderFrame /10)) {
        
                stageFrames = totalRenderFrame / 10;
                frameOffset = 8 * totalRenderFrame / 10;
                float shrinkRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);

                int target_height = (int)((values.get(i).getValues().get(0) / sum) * (PI * diameter));

                int top_y    = bar_y;
                int bottom_y = (int)(bar_y + (y_axis_height * height_ratio));

                int target_top = y_origin - target_height;
                int target_diff = target_top - top_y;

                int new_top = (int)(top_y + (1 - shrinkRatio) * (target_diff));

                line(bar_x + bar_width/2, new_top, bar_x + bar_width/2, y_origin);

            } else if ((renderFrame >= totalRenderFrame/2) && (renderFrame < 4 * totalRenderFrame / 5)) {
                stageFrames = 3 * totalRenderFrame / 10;
                frameOffset = totalRenderFrame / 2;

                float moveRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);

                float rad_ratio = (values.get(i).getValues().get(0) / sum) * (2 * PI);
                int target_height = (int)((values.get(i).getValues().get(0) / sum) * (PI * diameter));

                float end_angle = rad_ratio + tmp_pos;
                float start_angle = tmp_pos;


                int start_x = (int)(diameter/2 * cos(start_angle) + width/2);
                int start_y = (int)(diameter/2 * sin(start_angle) + height/2);
                
                int end_x = (int)(diameter/2 * cos(end_angle) + width/2);
                int end_y = (int)(diameter/2 * sin(end_angle) + height/2);


                int top_y    = bar_y;
                int bottom_y = (int)(bar_y + (y_axis_height * height_ratio));

                int target_top = y_origin - target_height;

                int x_1 = bar_x + bar_width/2;
                int y_1 = target_top;

                int x_2 = bar_x + bar_width/2;
                int y_2 = y_origin;

                int diff_x1 = start_x - x_1;
                int diff_x2 = end_x - x_2;
                int diff_y1 = start_y - y_1;
                int diff_y2 = end_y - y_2;
                
                line(x_1 + (1 - moveRatio) * diff_x1,
                     y_1 + (1 - moveRatio) * diff_y1,
                     x_2 + (1 - moveRatio) * diff_x2,
                     y_2 + (1 - moveRatio) * diff_y2);

                tmp_pos += rad_ratio;

            } else if (renderFrame >= totalRenderFrame / 10 && (renderFrame <  totalRenderFrame / 2)) {
                stageFrames = 4 * totalRenderFrame / 10;
                frameOffset = totalRenderFrame / 10;

                float moveRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);
                float rad_ratio = (values.get(i).getValues().get(0) / sum) * (2 * PI);
                int target_height = (int)((values.get(i).getValues().get(0) / sum) * (PI * diameter));

                float end_angle = rad_ratio + tmp_pos;
                float start_angle = tmp_pos;


                int start_x = (int)(diameter/2 * cos(start_angle) + width/2);
                int start_y = (int)(diameter/2 * sin(start_angle) + height/2);
                
                int end_x = (int)(diameter/2 * cos(end_angle) + width/2);
                int end_y = (int)(diameter/2 * sin(end_angle) + height/2);


                int top_y    = bar_y;
                int bottom_y = (int)(bar_y + (y_axis_height * height_ratio));

                int target_top = y_origin - target_height;

                int x_1 = bar_x + bar_width/2;
                int y_1 = target_top;

                int x_2 = bar_x + bar_width/2;
                int y_2 = y_origin;

                int diff_x1 = start_x - x_1;
                int diff_x2 = end_x - x_2;

                int diff_y1 = start_y - y_1;
                int diff_y2 = end_y - y_2;
                
                line(x_1 + diff_x1,
                     y_1 + diff_y1,
                     x_2 + diff_x2,
                     y_2 + diff_y2);

                tmp_pos += rad_ratio;

                arc(width/2, height/2, diameter * (1 - moveRatio), diameter * (1 - moveRatio),
                    start_angle, end_angle);
            } else if (renderFrame < totalRenderFrame / 10) {
                stageFrames = totalRenderFrame / 10;
                frameOffset = 0;
                float alphaRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);
                
                float rad_ratio = (values.get(i).getValues().get(0) / sum) * (2 * PI);
                int target_height = (int)((values.get(i).getValues().get(0) / sum) * (PI * diameter));
                float end_angle = rad_ratio + tmp_pos;
                float start_angle = tmp_pos;


                int start_x = (int)(diameter/2 * cos(start_angle) + width/2);
                int start_y = (int)(diameter/2 * sin(start_angle) + height/2);
                
                int end_x = (int)(diameter/2 * cos(end_angle) + width/2);
                int end_y = (int)(diameter/2 * sin(end_angle) + height/2);


                int top_y    = bar_y;
                int bottom_y = (int)(bar_y + (y_axis_height * height_ratio));

                int target_top = y_origin - target_height;

                int x_1 = bar_x + bar_width/2;
                int y_1 = target_top;

                int x_2 = bar_x + bar_width/2;
                int y_2 = y_origin;

                int diff_x1 = start_x - x_1;
                int diff_x2 = end_x - x_2;

                int diff_y1 = start_y - y_1;
                int diff_y2 = end_y - y_2;
                
                line(x_1 + diff_x1,
                     y_1 + diff_y1,
                     x_2 + diff_x2,
                     y_2 + diff_y2);

                tmp_pos += rad_ratio;

                arc(width/2, height/2, diameter, diameter, start_angle, end_angle);

                String name = values.get(i).getDataName();
                pushMatrix();
                    translate(width/2, height/2);
                        fill(200 * alphaRatio, 
                             200 * alphaRatio, 
                             200 * alphaRatio);
                        if (start_angle < HALF_PI || start_angle> (3 * PI / 2)) {
                        rotate(start_angle + (rad_ratio/2));
                            textAlign(CENTER);
                            text(name, ((diameter / 2) * 1.15f), 0);
                        } else {
                            rotate((PI) + (start_angle + (rad_ratio/2)));
                            textAlign(CENTER);
                            text(name, -1 * ((diameter / 2) * 1.15f), 0);
                        }
                popMatrix();
            }
   
            fill(0xff000000);
            if (renderFrame >= (9 * totalRenderFrame / 10)) {
                stageFrames = totalRenderFrame /10;
                frameOffset = 0;
                float fadeRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);
                int col = (int)(200.0f * (1 - fadeRatio));
                fill(col, col, col);
                pushMatrix();
                    translate(bar_x, bar_y + (y_origin - bar_y) + (height * margin_ratio /8));
                    rotate(HALF_PI * 0.8f);
                    textSize(12);
                    textAlign(BOTTOM);
                    text(values.get(i).getDataName(), 0, 0);
                    textAlign(LEFT);
                popMatrix();
                fill(0, 0, 0);
            }
        }
    }

    public void drawLabels(int x_origin, int y_origin, float margin_ratio, float fadeRatio) {

        textAlign(LEFT);
        int col = (int) (200.0f * fadeRatio);
        println("col == " + col);
        fill(col, col, col);

        String xName = nameLabels[0];
        String yName = nameLabels[1];

        int w = width;
        int h = height;

        int axis_h = (int)(height * (1 - margin_ratio));
        text(xName, x_origin + (w * margin_ratio / 2),
            y_origin + (h * margin_ratio / 2),
            x_origin + (w * margin_ratio / 2),
            y_origin + (h * margin_ratio / 2));
        text(yName, x_origin - (w * margin_ratio / 2),
             y_origin - (h * margin_ratio / 2), x_origin, y_origin);

    }

    public void drawAxes(float margin_ratio, float fadeRatio) {
        int axis_h = (int)(height * (1 - margin_ratio));
        int col = (int) (200.0f * fadeRatio);
        println("axes col == " + col);
        stroke(col, col, col);
        //x axis
        line(width * margin_ratio, axis_h, (width * (1-margin_ratio)), axis_h);
        //y axis
        line(width * margin_ratio, axis_h, width * margin_ratio, height * margin_ratio);

        stroke(0, 0, 0);
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
