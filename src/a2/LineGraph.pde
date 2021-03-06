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

    void drawAxes(int x_origin, int y_origin) {

        int axis_h = (int)(height * (1 - margin_ratio));

        //x axis
        line(w * margin_ratio, axis_h, (width * (1-margin_ratio)), axis_h);
        //y axis
        line(w * margin_ratio, axis_h, w * margin_ratio, height * margin_ratio);

    }

    void drawLabels(int x_origin, int y_origin) {

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

    void makePoints() {

        float max_height = findMax(values);
        float line_len   = (x_axis_width / (values.size())); // half for each end

        for(int i = 0; i < values.size(); i++ ) {
            float height_ratio = (values.get(i).getValues().get(0) / max_height);

            // LOL
            int bar_width   = (int)((x_axis_width * 0.75)/values.size());
            int space_width = (int)((x_axis_width * 0.25)/values.size()); 

            int bar_x = (x_origin + (i * bar_width) + ((i+1) * space_width));
            int px = bar_x + (bar_width/2);
            int py = (int)(y_origin - (y_axis_height * height_ratio));

            Point tmp = new Point(px, py);
            points.add(tmp);
        }
    }

    void drawPoints() {
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

                int bar_width   = (int)((x_axis_width * 0.75)/values.size());
                translate(p.getX() - (bar_width/2), p.getY() + (y_origin - p.getY()) + (height * margin_ratio /8));
                rotate(HALF_PI * 0.8);
                textSize(12);
                textAlign(BOTTOM);
                fill(0, 0, 0);
                text(values.get(i).getDataName(), 0, 0);
                textAlign(LEFT);
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

    void isHovering() {
        
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
