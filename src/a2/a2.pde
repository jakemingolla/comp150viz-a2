import java.util.ArrayList;

/*public class a2 {*/

TransitionManager transitionManager;
BarGraph barGraph;
LineGraph lineGraph;
PieGraph pieGraph;

RenderState renderState;
String data_path = "data.csv";


ArrayList<Data> dataPoints = new ArrayList<Data>();
String[] nameLabels;

void setup() {

    size(800, 600);
    if(frame != null) {
        frame.setResizable(true);
    }

    ArrayList<Data> values = readData(data_path);

    /* TODO add nameLabels */
    barGraph  = new BarGraph(values);
    lineGraph = new LineGraph(values);
    pieGraph = new PieGraph(values);

    transitionManager = new TransitionManager(barGraph, lineGraph);

    renderState = RenderState.LINE_RS;
}

void draw() {

    //shit.txt
/*    pieGraph.render();*/
    barGraph.render();

    switch (renderState) {
    case LINE_RS:
        break;
    case BAR_RS:
        break;
    case PIE_RS:
        break;
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
