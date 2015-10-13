import java.util.ArrayList;

/*public class a2 {*/

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

void setup() {

    size(800, 600);
    frameRate(60);
    if(frame != null) {
        frame.setResizable(true);
    }

    ArrayList<Data> values = readData(data_path);

    /* TODO add nameLabels */
    barGraph  = new BarGraph(values);
    lineGraph = new LineGraph(values);
    pieGraph = new PieGraph(values);

    transitionManager = new TransitionManager(barGraph, lineGraph, pieGraph);

    currentRenderState = RenderState.BAR_RS;
    currentGraph = barGraph;
    
    time = 0;
}

void mousePressed() {
    println("mouse pressed");
    for (Button b : currentGraph.getButtons()) {
        if (b.isInside(mouseX, mouseY)) {
            println("clicked on button");
            if (currentRenderState == RenderState.BAR_RS) {
                println("currently BAR");
                if (b.getRenderState() == RenderState.LINE_RS) {
                    println("want LINE");
                    currentRenderState = RenderState.BAR2LINE_RS;
                    currentGraph = barGraph;
                } else if (b.getRenderState() == RenderState.PIE_RS) {
                    println("want PIE");
                    currentRenderState = RenderState.BAR2PIE_RS;
                    currentGraph = pieGraph;
                } else {
                    println("want BAR");
                    currentRenderState = RenderState.BAR_RS;
                    currentGraph = barGraph;
                }
            } else if (currentRenderState == RenderState.LINE_RS) {
                println("currently LINE");
                if (b.getRenderState() == RenderState.BAR_RS) {
                    println("want BAR");
                    currentRenderState = RenderState.LINE2BAR_RS;
                    currentGraph = barGraph;
                } else {
                // no line to pie
                    println("want LINE");
                    currentRenderState = RenderState.LINE_RS;
                    currentGraph = lineGraph;
                }
            } else if (currentRenderState == RenderState.PIE_RS) {
                println("currently PIE");
                if (b.getRenderState() == RenderState.BAR_RS) {
                    println("want BAR");
                    currentRenderState = RenderState.PIE2BAR_RS;
                    currentGraph = barGraph;
                } else {
                    println("want PIE");
                    currentRenderState = RenderState.PIE_RS;
                    currentGraph = pieGraph;
                }
            }
        }
    }
}

void draw() {
    background(200, 200, 200);
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
     case PIE2BAR_RS:
        currentRenderState = RenderState.BAR_RS;
        break;
    case LINE_RS:
        lineGraph.render();
/*        lineGraph.renderButtons();*/
        break;
    case BAR_RS:
        barGraph.render();
/*        barGraph.renderButtons();*/
        break;
    case PIE_RS:
        pieGraph.render();
/*        pieGraph.renderButtons();*/
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

    for(int i = 1; i < lines.length; i++ ) {
        println(i + ": " + lines[i]);
    }

    println("ll: " + lines.length);
    for (int i = 1; i < lines.length; i++) {
        String[] splitLine = split(lines[i], ",");
        ArrayList<Float> floatValues = new ArrayList<Float>();
        println("---------------");
        for (int j = 1; j < splitLine.length; j++) {
            floatValues.add(parseFloat(splitLine[j]));
            println(splitLine[j]);
        }
        Data d = new Data(splitLine[0], floatValues);
        values.add(d);
    }

    return values;
}

/*}*/
