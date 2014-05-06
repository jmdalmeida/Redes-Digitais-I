package simulator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * 
 * Esta classe implementa a gest&atildeo da lista de eventos pendentes.<br>
 * <br>
 * Possu&iacute m&eacutetodos para:<br>
 * - Inserir um novo evento {@link Event}.<br>
 * - Remover evento(s).<br>
 * - <b>Retirar</b> o pr&oacuteximo evento a ocorrer.<br>
 * - Obter o valor actual do rel&oacutegio de simula&ccedil&atildeo.<br>
 * - Converter o conte&uacutedo da lista para uma cadeia de caracteres.<br>
 * 
 * <i><b>Nota:</b></i> O valor do rel&oacutegio de simula&ccedil&atildeo &eacute
 * inicializado com -1.0.
 * 
 * @author Grupo de Redes do ISCTE (2008/09) e (2012/13).
 * 
 */
public class Simulator {
	private static double simulationClock = -1.0;
	private static EventQueue pendingEvents = new EventQueue();
	private static Simulator.LogLevel logLevel = Simulator.LogLevel.DATA;
	private static PrintStream dataFile = System.out;

	public enum LogLevel {
		QUIET, DATA, INFO, DEBUG, SIMDEBUG, NEXTDEBUG, FULLDEBUG
	};

	/**
	 * Devolve o valor actual do rel&oacutegio de simula&ccedil&acuteo.
	 * 
	 * @return O valor actual do rel&oacutegio de simula&ccedil&atildeo.
	 */
	public static double getClock() {
		return simulationClock;
	}

	/**
	 * Devolve o pr&oacuteximo evento a ocorrer, <b>retira-o</b> da lista e
	 * <b>actualiza</b> o rel&oacutegio de simula&ccedil&atildeo (com o instante
	 * de tempo de ocorr&ecircncia do evento devolvido).<br>
	 * Caso a lista esteja vazia devolve <i>null</i>.<br>
	 * <b><i>Nota:</i></b>N&atildeo deve ser usado para inspeccionar a lista,
	 * pois esta &eacute alterada por este m&eacutetodo.
	 * 
	 * @return O pr&oacuteximo evento a ocorrer.
	 * @see Event
	 */
	public static Event nextEvent() {
		if ((logLevel == Simulator.LogLevel.FULLDEBUG)
				|| (logLevel == Simulator.LogLevel.SIMDEBUG))
			System.out.println(flush());

		Event e = pendingEvents.nextEvent();
		if (e != null) {
			simulationClock = e.timeStamp();
			switch (logLevel) {
			case SIMDEBUG:
			case NEXTDEBUG:
			case FULLDEBUG:
				System.out.println("[SIMULATION] Processing event:"
						+ e.toString());
				break;
			}
		}
		return e;
	}

	/**
	 * Devolve o n&uacutemero de eventos na lista.
	 * 
	 * @return O n&uacutemero de eventos na lista.
	 */
	public static int pendingEvents() {
		return pendingEvents.eventQueueSize();
	}

	/**
	 * Adiciona um novo evento &agrave lista de eventos. A ordem deste evento na
	 * lista &eacute definida pelo m&eacutetodo {@link before} da classe
	 * {@link Event}.
	 * 
	 * @param e
	 *            O novo evento a inserir.
	 * @see Event
	 */
	public static void addEvent(Event e) {
		pendingEvents.addEvent(e);
	}

	/**
	 * Remove da lista de acontecimentos o evento <b><i>e</i></b>
	 * 
	 * @param e
	 *            O evento a ser removido.
	 * @see Event
	 */
	public static void removeEvent(Event e) {
		pendingEvents.removeEvent(e);
	}

	/**
	 * Remove zero, um ou v&aacuterios eventos da lista.<br>
	 * Remove todos os eventos da lista que sejam iguais ao evento
	 * exemplo/template <b><i>e</i></b> de acordo com o crit&eacuterio de
	 * igualdade definido no m&eacutetodo {@link equal} da classe {@link Event}.
	 * 
	 * @param e
	 *            O evento exemplo (de compara&ccedil&atildeo) a usar para a
	 *            remo&ccedil&atildeo.
	 * @see Event
	 */
	public static void removeEventByTemplate(Event template) {
		pendingEvents.removeEvent(template);
	}

	/**
	 * Converte para uma cadeia de caracteres o rel&oacutegio de
	 * simula&ccedil&atildeo e a lista de acontecimentos.<br>
	 * 
	 * @return A cadeira de caracteres representando o estado da lista.
	 */
	public static String flush() {
		return "[Engine] Clock:" + simulationClock + "\n"
				+ "[ENGINE] Pending events:" + pendingEvents.eventQueueSize()
				+ "\n" + pendingEvents.toString() + "\n";
	}

	/**
	 * Define o n&iacutevel de Log pretendido para a fun&ccedil&atildeo log.<br>
	 * 
	 * @param level
	 *            O nivel de Log desejado.
	 */
	public static void setLogLevel(Simulator.LogLevel level) {
		logLevel = level;
	}

	/**
	 * Define o ficheiro para ser utilizado para armazenar os dados da
	 * simula&ccedil&atildeo.<br>
	 * 
	 * @param fileName
	 *            Nome do ficheiro a ser utilizado.
	 */
	public static void setDataFile(String fileName) {
		try {
			dataFile = new PrintStream(new FileOutputStream(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
	}

	/**
	 * Imprime em System.err a mensagem de info (logLevel >= INFO.<br>
	 * 
	 * @param infoPhrase
	 *            String a ser impressa.
	 */
	public static void info(String infoPhrase) {
		if (logLevel.ordinal() >= Simulator.LogLevel.INFO.ordinal()) {
			System.out.println(infoPhrase);
		}
	}

	/**
	 * Imprime em System.err a mensagem de debug (logLevel >= DEBUG.<br>
	 * 
	 * @param debugPhrase
	 *            String a ser impressa.
	 */
	public static void debug(String debugPhrase) {
		if (logLevel.ordinal() >= Simulator.LogLevel.DEBUG.ordinal()) {
			System.out.println(debugPhrase);
		}
	}

	/**
	 * Imprime em System.err a mensagem de data (logLevel >= DATA.<br>
	 * 
	 * @param debugPhares
	 *            String a ser impressa.
	 */
	public static void data(String data) {
		dataFile.println(data);
	}

}
