package simulator;

import java.util.Random;
import java.util.Scanner;

/**
 * 
 * Esta classe implementa o comportamento de um receptor..<br>
 * <br>
 * Possuí métodos para:<br>
 * - Iniciar a recepçao de dados {@link Sender.Data}.<br>
 * - Terminar a recepçao de dados {@link Sender.Data}.<br>
 * 
 * @author Grupo de Redes do ISCTE (20012/13)
 * 
 */

public class Receiver {
	public enum State {
		WAIT, RX
	};

	private State state;

	public Receiver() {
		state = State.WAIT;
	}

	/**
	 * Inicia a recepção de dados.
	 * 
	 * @param Os
	 *            dados recebidos.
	 */
	public void startRX(Data data) {

		state = State.RX;

		// Update statistics

		// Output
		String s = "[Receiver@";
		s = s + Simulator.getClock() + " Start RX Data ID: " + data.getID()
				+ "]";
		Simulator.debug(s);

		s = "" + Simulator.getClock() + "\t" + "Srx" + "\t" + data.getID()
				+ "\t";
		s = s + data.getTimeStamp() + "\t" + "-" + "\t" + "-" + "\t"
				+ Simulator.getClock() + "\t" + "-" + "\t" + "-" + "\t" + "-";
		Simulator.data(s);
	}

	public void stopRX(Data data) {
		state = State.WAIT;

		// Update statistics
		/*
		 * Ao tempo total no sistema, delaySys é adicionado o tempo passado no
		 * sistema (tempoActual-tempoGeração) da trama que acabou de ser
		 * recebida
		 */
		TxRxSystem.delaySys += Simulator.getClock() - data.getTimeStamp();

		// Output
		String s = "[Receiver@";
		s = s + Simulator.getClock() + " Stop RX Data ID (State=" + state
				+ "): " + data.getID() + "]";
		Simulator.debug(s);
		s = "" + Simulator.getClock() + "\t" + "Erx" + "\t" + data.getID()
				+ "\t";
		s = s + data.getTimeStamp() + "\t" + "-" + "\t" + "-" + "\t" + "-"
				+ "\t" + Simulator.getClock() + "\t" + "-" + "\t" + "-";
		Simulator.data(s);
		// Verificar se a trama est� correcta de acordo com o boolean
		// demonstra�ao/simulacao;
		if (TxRxSystem.demonstracao) {
			Scanner scanner = new Scanner(System.in);
			System.out.print("Trama " + (data.getID() + 1)
					+ " is (0-correct, 1-incorrect)?: ");
			int a = scanner.nextInt();
			if (a == 0) {
				TxRxEvent newEvent2 = new TxRxEvent(Simulator.getClock()
						+ (TxRxSystem.DISTANCIA / TxRxSystem.vp),
						TxRxEvent.TxRxEventType.ACK, data);
				Simulator.addEvent(newEvent2);
			}
		}
		if (TxRxSystem.simulacao) {
			double Peb = TxRxSystem.Peb;
			int crc = 8;
			double total = 32 * Math.pow(Peb, 3)
					* Math.pow((1 - Peb), data.getSize() + (crc - 3)) + 153
					* Math.pow(Peb, 4)
					* Math.pow((1 - Peb), data.getSize() + (crc - 4)) + 1014
					* Math.pow(Peb, 5)
					* Math.pow((1 - Peb), data.getSize() + (crc - 5)) + 5420
					* Math.pow(Peb, 6)
					* Math.pow((1 - Peb), data.getSize() + (crc - 6)) + 21287
					* Math.pow(Peb, 7)
					* Math.pow((1 - Peb), data.getSize() + (crc - 7)) + 70575
					* Math.pow(Peb, 8)
					* Math.pow((1 - Peb), data.getSize() + (crc - 8)) + 204361
					* Math.pow(Peb, 9)
					* Math.pow((1 - Peb), data.getSize() + (crc - 9)) + 512312
					* Math.pow(Peb, 10)
					* Math.pow((1 - Peb), data.getSize() + (crc - 10))
					+ Math.pow((1 - Peb), data.getSize());

			Random random = new Random();
			double r = random.nextDouble();
			if (r < total) {
				TxRxEvent newEvent2 = new TxRxEvent(Simulator.getClock()
						+ (TxRxSystem.DISTANCIA / TxRxSystem.vp),
						TxRxEvent.TxRxEventType.ACK, data);
				Simulator.addEvent(newEvent2);
			}
		}

	}
}
