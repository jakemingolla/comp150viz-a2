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
	void render() {
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

        int bar_width   = (int)((x_axis_width * 0.75)/values.size());
        int space_width = (int)((x_axis_width * 0.25)/values.size());

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

            fill(#000000);

            pushMatrix();
                translate(bar_x, bar_y + (y_origin - bar_y) + (height * margin_ratio /8));
                rotate(HALF_PI * 0.8);
                textSize(12);
                textAlign(BOTTOM);
                text(values.get(i).getDataName(), 0, 0);
                textAlign(LEFT);
            popMatrix();
        }
    }

    void drawLabels(int x_origin, int y_origin, float margin_ratio) {

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

    void drawAxes(float margin_ratio) {
        int axis_h = (int)(height * (1 - margin_ratio));
        //x axis
        line(width * margin_ratio, axis_h, (width * (1-margin_ratio)), axis_h);
        //y axis
        line(width * margin_ratio, axis_h, width * margin_ratio, height * margin_ratio);

    }
}
