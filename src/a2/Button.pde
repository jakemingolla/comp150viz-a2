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
