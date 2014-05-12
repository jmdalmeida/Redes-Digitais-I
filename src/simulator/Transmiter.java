package simulator;

import java.util.LinkedList;
import java.util.Scanner;

import simulator.TxRxEvent.TxRxEventType;

public class Transmiter {
	public enum State {
		IDLE, TX, ACK
	};

	double Rb;
	double d;
	static double vp = 200000000.0; // vp = 2e8 m/s.
	State state;
	LinkedList<Data> queue;
	int b;

	TxRxEvent eventTimeout;

	Transmiter(double binaryRate, double length) {

		Rb = binaryRate;
		d = length;
		b = 0;

		queue = new LinkedList<Data>();
		state = State.IDLE;
	}

	public void arrivalData(Data data) {
//		if (state == State.ACK) {
//			state = State.IDLE;
//		}
		// Output
		String s = "";
		s = "" + Simulator.getClock() + "\t" + "A" + "\t" + data.getID() + "\t";
		int tx = 0;
//		if (state == State.TX)
//			tx = 1;
		s = s + data.getTimeStamp() + "\t" + Simulator.getClock() + "\t" + "-"
				+ "\t" + "-" + "\t" + "-" + "\t" + queue.size() + "\t"
				+ (queue.size() + tx);
		Simulator.data(s);
		// Seccao a completar
		if (state == State.IDLE) {
			startTx(data);
			state = State.TX;
		} else if (state == State.TX) {
			queue.addLast(data);
		}
	}

	public void startTx(Data data) {
		// Update statistics
		TxRxSystem.delayQ += Simulator.getClock() - data.getTimeStamp();

		// Output
		String s = "[Transmiter@";
		s = s + Simulator.getClock() + " Start TX Data ID: " + data.getID()
				+ "]";
		Simulator.debug(s);
		s = "" + Simulator.getClock() + "\t" + "Stx" + "\t" + data.getID()
				+ "\t";
		s = s + data.getTimeStamp() + "\t" + Simulator.getClock() + "\t" + "-"
				+ "\t" + "-" + "\t" + "-" + "\t" + queue.size() + "\t"
				+ (queue.size() + 1);
		Simulator.data(s);

		// Seccao a completar
		double tProp = d / vp;
		double ttx = TxRxSystem.DATA_SIZE / Rb;
		eventTimeout = new TxRxEvent(Simulator.getClock() + ((tProp * 2) + ttx)
				* 1.01, TxRxEvent.TxRxEventType.TIMEOUT, data);
		Simulator.addEvent(eventTimeout);
		TxRxEvent newEvent = new TxRxEvent(Simulator.getClock() + tProp,
				TxRxEvent.TxRxEventType.StartRX, data);
		Simulator.addEvent(newEvent);
		System.out.println("Ttx: " + ttx);
		TxRxEvent newEvent2 = new TxRxEvent(Simulator.getClock() + ttx,
				TxRxEvent.TxRxEventType.StopTX, data);
		Simulator.addEvent(newEvent2);
		state = State.TX;

	}

	public void stopTx(Data data) {
		if (state == State.TX) {
			// Update statistics
			TxRxSystem.delayQtx += Simulator.getClock() - data.getTimeStamp();

			// Output
			String s = "";
			s = "" + Simulator.getClock() + "\t" + "Etx" + "\t" + data.getID()
					+ "\t";
			s = s + data.getTimeStamp() + "\t" + "-" + "\t"
					+ Simulator.getClock() + "\t" + "-" + "\t" + "-" + "\t"
					+ queue.size() + "\t" + queue.size();
			Simulator.data(s);
			s = "[Transmiter@";
			s = s + Simulator.getClock() + " Stop TX Data ID: " + data.getID()
					+ "]";
			Simulator.debug(s);

			// Secção a completar
			double tProp = d / vp;
			TxRxEvent newEvent = new TxRxEvent(Simulator.getClock() + tProp,
					TxRxEvent.TxRxEventType.StopRX, data);
			Simulator.addEvent(newEvent);
//			if (queue.isEmpty()) {
//				state = State.IDLE;
//			} else if (!queue.isEmpty()) {
//				Data data2 = queue.removeFirst();
//				startTx(data2);
//			}

		} else {
			Simulator.debug("ERROR - not a valid state for action stopTx");
			System.exit(1);
		}
		state = State.ACK;
	}

	public void timeout(Data data) {
		if (state == State.ACK) {
			String s = "";
			s = "" + Simulator.getClock() + "\t" + "Etx" + "\t" + data.getID()
					+ "\t";
			s = s + data.getTimeStamp() + "\t" + "-" + "\t"
					+ Simulator.getClock() + "\t" + "-" + "\t" + "-" + "\t"
					+ queue.size() + "\t" + queue.size();
			Simulator.data(s);
			s = "[Transmiter@";
			s = s + Simulator.getClock() + " Stop TX Data ID: " + data.getID()
					+ "]";
			Simulator.debug(s);
			startTx(data);

		} else {
			Simulator.debug("Error on ACK");
			System.exit(1);
		}
	}

	public void acknowledge(Data data) {
		Simulator.removeEvent(eventTimeout);
		b++;
		TxRxSystem.DSum += (Simulator.getClock() - data.getTimeStamp());
		TxRxSystem exp = new TxRxSystem();
		if (TxRxSystem.simulacao) {
			if (b < TxRxSystem.MAX_DATA) {
				if (queue.isEmpty()) {
					state = State.IDLE;
					if (!exp.getExperiencia().equals("3")) {
						TxRxEvent newEvent2 = new TxRxEvent(Simulator.getClock(),
								TxRxEvent.TxRxEventType.Generate_DATA, null);
						Simulator.addEvent(newEvent2);
						
					}
				} else if (!queue.isEmpty()) {
					Data data2 = queue.removeFirst();
					startTx(data2);
				}
			}
		} else {
			TxRxEvent newEvent2 = new TxRxEvent(Simulator.getClock(),
					TxRxEvent.TxRxEventType.Generate_DATA, null);
			Simulator.addEvent(newEvent2);
			state = State.IDLE;
		}
		
	}

}
