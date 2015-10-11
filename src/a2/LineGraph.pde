import java.util.ArrayList;

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
    final float margin_ratio = 0.15;
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
        drawPoints();
        drawLines();
        
	}

    void drawAxes(int x_origin, int y_origin) {

        int axis_h = (int)(height * (1 - margin_ratio));

        //x axis
        line(w * margin_ratio, axis_h, (width * (1-margin_ratio)), axis_h);
        //y axis
        line(w * margin_ratio, axis_h, w * margin_ratio, height * margin_ratio);

    }

    void drawLabels(int x_origin, int y_origin) {

        int axis_h = (int)(height * (1 - margin_ratio));
        text(xName, x_origin + (w * margin_ratio / 2),
            y_origin + (h * margin_ratio / 2),
            x_origin + (w * margin_ratio / 2),
            y_origin + (h * margin_ratio / 2));
        text(yName, x_origin - (w * margin_ratio / 2),
             y_origin - (h * margin_ratio / 2), x_origin, y_origin);

    }

    void makePoints() {

        float max_height = findMax(values);
        float line_len   = (x_axis_width / (values.size() + 1)); // half for each end

        for(int i = 0; i < values.size(); i++ ) {
            float height_ratio = (values.get(i).getValues().get(0) / max_height);

            int px = (int)((i * line_len) + (line_len/2) + x_origin);
            int py = (int)(y_origin - (y_axis_height * height_ratio));

            Point tmp = new Point(px, py);
            points.add(tmp);
        }
    }

    void drawPoints() {
        int i = 0;
        for(Point p : points) {
            fill(0, 0, 0);
            ellipse(p.getX(), p.getY(), 5, 5);

            // draw data labels 
            pushMatrix();
            //slanted under data
            //translate(p.getX(), p.getY() + (y_axis_height * (p.getY()/y_axis_height)) + (h * margin_ratio /8));
            translate(p.getX(), p.getY() + (y_origin - p.getY()) + (height * margin_ratio /8));
            rotate(HALF_PI * 0.8);
            text(values.get(i).getDataName(), 0, 0);
            popMatrix();
            i++;
        }
    }

    void drawLines() {
        for(int i = 1; i < points.size(); i++) {
            Point p1 = points.get(i - 1); 
            Point p2 = points.get(i); 

            line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
    }


}
