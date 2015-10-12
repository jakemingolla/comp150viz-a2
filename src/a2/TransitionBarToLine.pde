public class TransitionBarToLine extends Transition<BarGraph, LineGraph> {

	TransitionBarToLine(BarGraph base, LineGraph target) {
		super(base, target);
		// TODO Auto-generated constructor stub
	}
	
/*	@Override*/
	void render() {
        
/*        println("renderFrame (IN RENDER START) == " + renderFrame);*/
        
        /* update canvas fields */
        int w = width;
        int h = height;
        float margin_ratio = base.getMarginRatio();
        int x_axis_width  = (int)(width * (1 - 2*margin_ratio));
        int y_axis_height = (int)(height * (1 - 2*margin_ratio));
        int x_origin = (int)(width * margin_ratio);
        int y_origin = (int)(height * (1 - margin_ratio));

        /* re-render the parts of the graph */
        
        int stageFrames;

/*        println("rf: " + renderFrame);*/


        fill(255, 0, 0);
        ellipse(width/2, height/2, 100, 100);
        if (renderFrame < (totalRenderFrame / 3)) {
            println("in first third");
            ellipse(width/2, height/2, 100, 100);
/*            stageFrames = totalRenderFrame / 3;*/
/*            int framesPerBar = stageFrames / base.getValues().size();*/
/*            int counter = 0;*/
/**/
/*            ArrayList<Data> values = base.getValues();*/
/**/
/*            float max_height = base.findMax(values);*/
/**/
/*            int bar_width   = (int)((x_axis_width * 0.75)/values.size());*/
/*            int space_width = (int)((x_axis_width * 0.25)/values.size());*/
/**/
/*            for (int i = 0; i < values.size(); i++) {*/
/**/
/*                float height_ratio = (values.get(i).getValues().get(0) / max_height);*/
/**/
/*                int bar_x = (x_origin + (i * bar_width) + ((i+1) * space_width));*/
/*                int bar_y = (int)(y_origin - (y_axis_height * height_ratio));*/
/**/
/*                // if the highlighted one, change paint color for this rectangle to white*/
/*                // else purdy colorz*/
/*                fill((20 * i) % 255, (30 * i) % 255, (40 * i) % 255);*/
/*                */
/*                float drawHeightRatio = (float)(renderFrame / (float)stageFrames);*/
/*                println("dhr: " + drawHeightRatio);*/
/*                background(255, 0, 0);*/
/*                rect(bar_x, bar_y, bar_width, (y_axis_height * height_ratio) * drawHeightRatio);*/
/*                fill(#000000);*/
/**/
/*                pushMatrix();*/
/*                    translate(bar_x, bar_y + (y_origin - bar_y) + (height * margin_ratio /8));*/
/*                    rotate(HALF_PI * 0.8);*/
/*                    textSize(12);*/
/*                    textAlign(BOTTOM);*/
/*                    text(values.get(i).getDataName(), 0, 0);*/
/*                    textAlign(LEFT);*/
/*                popMatrix();*/
/*            }*/
        } else {
            println("in last two third");
            if (renderFrame  == 201) {
                background(200, 200, 200);
                println("clear!");
            }

        }

/*        println("renderFrame (IN RENDER END) == " + renderFrame);*/
    }
}
