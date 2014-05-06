package simulator;

public class Data {
	private double ts;
	private int id;
	private int s;

	Data(double timestamp, int number, int size) {
		ts = timestamp;
		id = number;
		s = size;
	}

	public double getTimeStamp() {
		return ts;
	}

	public int getSize() {
		return s;
	}

	public int getID() {
		return id;
	}

	public String toString() {
		return Integer.toString(id);
	}
}