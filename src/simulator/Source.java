package simulator;

import java.util.Random;

public class Source {
	public enum State {
		IDLE
	};

	private State state;
	private int maxData;
	private double meanDataInterval;
	private int meanDataSize;
	private int sent = 0; // Contador do número de tramas enviadas

	public Source(int max_data, int size, double interval) {
		maxData = max_data;
		meanDataInterval = interval;
		meanDataSize = size;

		state = State.IDLE;
	}

	private String getExperience() {
		TxRxSystem s = new TxRxSystem();
		return s.getExperiencia();
	}

	public void generateData(Event e) {

		// Constant Size..
		Random generator = new Random();
		double rd = generator.nextDouble();
		int dataSize = (int) (-meanDataSize * Math.log(rd));
		Data data = new Data(Simulator.getClock(), sent, dataSize);

		TxRxEvent newEvent = new TxRxEvent(Simulator.getClock() + 0.0,
				TxRxEvent.TxRxEventType.Arrival_DATA, data);
		Simulator.addEvent(newEvent);
		sent++;
		if (sent < maxData) {
			// Constant Rate...
			//double dataInterval = meanDataInterval;
			double r = generator.nextDouble();
			double dataInterval = -meanDataInterval * Math.log(r);
			if (getExperience().equals("3")) {
				newEvent = new TxRxEvent(Simulator.getClock() + dataInterval,
						TxRxEvent.TxRxEventType.Generate_DATA, data);
				Simulator.addEvent(newEvent);
			} else {

			}
		}

		// Output
		String s = "[Source@";
		s = s + Simulator.getClock() + " Generating Data ID: " + data.getID()
				+ "]";
		Simulator.debug(s);

		s = "" + Simulator.getClock() + "\t" + "G" + "\t" + data.getID() + "\t";
		s = s + data.getTimeStamp() + "\t" + "-" + "\t" + "-" + "\t" + "-"
				+ "\t" + "-" + "\t" + "-" + "\t" + "-";
		Simulator.data(s);
	}
}
