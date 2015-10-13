import java.util.ArrayList;

public abstract class Graph implements Renderable {
	private ArrayList<Data> values;
    private ArrayList<Button> buttons;
    final float margin_ratio = 0.2;
	
	Graph(ArrayList<Data> values) {
		this.values = values;
        buttons = new ArrayList<Button>();
	}

    float findMax(ArrayList<Data> arr) {
        float max = 0;
        for (Data d : arr) {
            if (d.getValues().get(0) > max) {
                max = d.getValues().get(0);
            }
        }
        return max;
    }

    float findSum(ArrayList<Data> arr) {
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

    void makeButtons() {
        
        buttons.clear();
        int bw, bh, sp;

        bw = (int)(width  * (2.0/9.0));
        bh = (int)(((margin_ratio * height) * (1.0/4.0)));
        sp = (int)(width *  (1.0/9.0));

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

    void renderButtons() {
        makeButtons();
        for (Button b : buttons) {
            b.render();
        }
    }

    ArrayList<Data> getValues() {
        return values;
    }


}
