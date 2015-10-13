public class TransitionPieToBar extends Transition<PieGraph, BarGraph> {

    ArrayList<Point> points;
    ArrayList<Data> values;

	TransitionPieToBar(PieGraph base, BarGraph target) {
		super(base, target);
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
        int axis_h = (int)(height * (1 - margin_ratio));
        int y_origin = (int)(height * (1 - margin_ratio));
        int diameter = (int) ((height < width) ? (height/ (5.0/3)) : (width / (5.0/3)));

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
        
        float tmp_pos = 0.0;

        int bar_width   = (int)((x_axis_width * 0.75)/values.size());
        int space_width = (int)((x_axis_width * 0.25)/values.size());

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
                            text(name, ((diameter / 2) * 1.15), 0);
                        } else {
                            rotate((PI) + (start_angle + (rad_ratio/2)));
                            textAlign(CENTER);
                            text(name, -1 * ((diameter / 2) * 1.15), 0);
                        }
                popMatrix();
            }
   
            fill(#000000);
            if (renderFrame >= (9 * totalRenderFrame / 10)) {
                stageFrames = totalRenderFrame /10;
                frameOffset = 0;
                float fadeRatio = (float)((renderFrame - frameOffset) / (float)stageFrames);
                int col = (int)(200.0 * (1 - fadeRatio));
                fill(col, col, col);
                pushMatrix();
                    translate(bar_x, bar_y + (y_origin - bar_y) + (height * margin_ratio /8));
                    rotate(HALF_PI * 0.8);
                    textSize(12);
                    textAlign(BOTTOM);
                    text(values.get(i).getDataName(), 0, 0);
                    textAlign(LEFT);
                popMatrix();
                fill(0, 0, 0);
            }
        }
    }

    void drawLabels(int x_origin, int y_origin, float margin_ratio, float fadeRatio) {

        textAlign(LEFT);
        int col = (int) (200.0 * fadeRatio);
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

    void drawAxes(float margin_ratio, float fadeRatio) {
        int axis_h = (int)(height * (1 - margin_ratio));
        int col = (int) (200.0 * fadeRatio);
        println("axes col == " + col);
        stroke(col, col, col);
        //x axis
        line(width * margin_ratio, axis_h, (width * (1-margin_ratio)), axis_h);
        //y axis
        line(width * margin_ratio, axis_h, width * margin_ratio, height * margin_ratio);

        stroke(0, 0, 0);
    }
}
