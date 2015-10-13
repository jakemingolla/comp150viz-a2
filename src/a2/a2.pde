import java.util.ArrayList;

TransitionManager transitionManager;
BarGraph barGraph;
LineGraph lineGraph;
PieGraph pieGraph;

RenderState currentRenderState;
Graph currentGraph;
String data_path = "data.csv";


ArrayList<Data> dataPoints = new ArrayList<Data>();
String[] nameLabels;
int time;
int mfr = 100;

void setup() {

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

void mousePressed() {
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

void keyPressed() {
    int k = 107;
    int j = 106;
    if(key == j && mfr >= 20) {
        mfr -= 10;
    } else if (key == k && mfr <= 150) {
        mfr += 10;
    }
}

void draw() {
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

void drawTransition(Transition transition) {
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
