import java.util.ArrayList;

public class Data {

	private String name;
	private ArrayList<Float> values;

	public Data(String name, ArrayList<Float> values) {
		this.name = name;
		this.values = values;
	}

	public String getDataName() {
		return name;
	}

	public void setDataName(String name) {
		this.name = name;
	}

	public ArrayList<Float> getValues() {
		return values;
	}

	public void setValues(ArrayList<Float> values) {
		this.values = values;
	}
	
}
