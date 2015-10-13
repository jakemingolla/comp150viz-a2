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

    float scale = 0.0;
    
    ArrayList<Data> values;

	PieGraph(ArrayList<Data> values) {
		super(values);
        
        w = width;
        h = height;

        x_origin = (int)(w / 2);
        y_origin = (int)(h / 2);

        xName = nameLabels[0];
        yName = nameLabels[1];

        radius = (int)(height/(5.0/3));

        this.values = values;

        for (Data d : values) {
            sum += d.getValues().get(0);
        }
	}
	
    void drawSlices() {

        float tmp_pos = 0.0;
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

    void drawLabels(String name, float startAngle, float endAngle, float radius) {

/*        println(name + " at angle: " + startAngle );*/
        fill(#000000);

        pushMatrix();
        translate(w/2, h/2); //move to origin

        if((startAngle < HALF_PI) || (startAngle > 3 * PI / 2)) {
            rotate(startAngle + (endAngle/2)); //rotate about origin
            textAlign(CENTER);
            text(name, (radius / 2) * 1.15, 0);
        } else {
            rotate((PI) + (startAngle + (endAngle/2))); //rotate about origin
            textAlign(CENTER);
            text(name, -1 * ((radius / 2) * 1.15), 0);
        }

        popMatrix();
    }


    void render() {
        /* update canvas fields */
        w = width;
        h = height;
        x_origin = (int)(width / 2);
        y_origin = (int)(height / 2);

        /* set radii appropriately */
        radius =(int) ((height < width) ? (height/ (5.0/3)) : (width / (5.0/3)));

        /* re-render the parts of the graph */
        drawSlices();
        is_hovering();

    }

   
    void is_hovering() {

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
                
                float new_x = (((float)mouseX/width)  - 0.5) * width;
                float new_y = -1 * (((float)mouseY/height) - 0.5) * height;

                float angle = 0.0;
                if (new_x < 0.0) {
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

                if(angle_d < (360.0 - start_d) && angle_d > (360.0 - end_d)) {
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
    void setHighlighted(int h) {
        highlighted = h;
    }

    void setScale(float f) {
        scale = f;
    }

    float getScale() {
        return scale;
    }
}
