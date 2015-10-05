import java.util.ArrayList;

/*public class a2 {*/

TransitionManager transitionManager;
BarGraph barGraph;
LineGraph lineGraph;

RenderState renderState;
String data_path = "data.csv";


ArrayList<Data> dataPoints = new ArrayList<Data>();
String[] nameLabels;

void setup() {

    size(800, 600);

    ArrayList<Data> values = readData(data_path);

    /* TODO add nameLabels */
    barGraph  = new BarGraph(values);
    lineGraph = new LineGraph(values);

    transitionManager = new TransitionManager(barGraph, lineGraph);

    renderState = RenderState.LINE_RS;
}

void draw() {
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
        for (int j = 0; j < splitLine.length; j++) {
            floatValues.add(parseFloat(splitLine[j]));
        }
        Data d = new Data(splitLine[0], floatValues);
        values.add(d);
    }

    return values;
}

/*}*/
