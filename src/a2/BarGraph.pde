import java.util.ArrayList;

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

/*        println("BARGRAPH------------");*/
/*        println("xo: " + x_origin);*/
/*        println("yo: " + y_origin);*/
/*        println("w: " + w);*/
/*        println("h: " + h);*/

        xName = nameLabels[0];
        yName = nameLabels[1];

        this.values = values;

/*        println("in barGraph, values are: ");*/
/*        for (Data d : values) {*/
/*            println(d.getValues().get(0));*/
/*        }*/
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

    void drawBars(int x_origin, int y_origin) {

        float max_height = findMax(values);

        int bar_width   = (int)((x_axis_width * 0.75)/values.size());
        int space_width = (int)((x_axis_width * 0.25)/values.size());

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
            fill(#000000);

            pushMatrix();
/*            translate(bar_x, bar_y + (y_axis_height * height_ratio) + (h * margin_ratio /8));*/
            if (i == 0) {
                println("bar x: " + bar_x);
                println("bar y: " +  (bar_y + (y_origin - bar_y) + (height * margin_ratio /8)));
            }

            translate(bar_x, bar_y + (y_origin - bar_y) + (height * margin_ratio /8));
            rotate(HALF_PI * 0.8);
            textSize(12);
            textAlign(BOTTOM);
            text(values.get(i).getDataName(), 0, 0);
            textAlign(LEFT);
            popMatrix();
            
/*            text(values.get(i).getDataName(), bar_x, */
/*                 bar_y + (y_axis_height * height_ratio) + (h * margin_ratio / 4));*/

        } 
    }

    void isHovering() {

        int mx = mouseX;
        int my = mouseY;

        float max_height = findMax(values);

        int bar_width   = (int)((x_axis_width * 0.75)/values.size());
        int space_width = (int)((x_axis_width * 0.25)/values.size());

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
