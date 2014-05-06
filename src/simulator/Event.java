package simulator;

/**
 * 
 * Esta classe implementa os eventos de uma forma generica.<br>
 * <b><i>Nota:</i></b>Para cada simulador devera ser especilizada redefinindo:<br>
 * i) os atributos {@link eventType} e {@link eventData},<br>
 * ii) os metodos {@link before} e {@link equal}.<br>
 * <br>
 * <i>Exemplo:</i><br>
 * <code>public class GBNevent extends Event {...}</code><br>
 * <br>
 * 
 * <i>Nota 1:</i> O atributo <code>eventType</code> poderá ser implementado por
 * um tipo enumerado.<br>
 * <i>Nota 2:</i> O atributo <code>eventData</code> deverá ser implementado
 * pela classe Frame.<br>
 * 
 * @author Grupo de Redes do ISCTE (2008/09) e (2012/13)
 * 
 */
public class Event {
	/**
	 * Tipo do evento.<br>
	 * <b><i>Nota: </i></b>Para cada simulador, na classe que especializa a
	 * classe {@link Event}, este atributo dever&aacute ser redefinido para um
	 * tipo adequado.
	 */
	protected Object eventType;

	/**
	 * Instante de ocorr&ecircncia do evento.
	 */
	protected double timeStamp;

	/**
	 * Dados adicionais do evento.<br>
	 * <b><i>Nota: </i></b>Para cada simulador, na classe que especializa a
	 * classe {@link Event}, este atributo dever&aacute ser redefinido para um
	 * tipo adequado.<br>
	 */
	protected Object eventData;

	/**
	 * @param ts
	 *            Instante de ocorr&ecircncia do evento.
	 * @param et
	 *            Tipo do evento.
	 * @param data
	 *            Dados adicionais do evento.
	 */
	public Event(double ts, Object et, Object data) {
		eventType = et;
		timeStamp = ts;
		eventData = data;
	}

	/**
	 * Devolve o tipo do objecto de forma gen&eacuterica (como uma
	 * inst&acircncia de <code>Object</code>).<br>
	 * No simulador dever&aacute ser feito cast para o tipo específico dessa
	 * simula&ccedil&atildeo.
	 * 
	 * @return O tipo do evento.
	 */
	public Object type() {
		return eventType;
	}

	/**
	 * Devolve o instante de ocorr&ecircncia do evento.<br>
	 * 
	 * @return O instante de ocorr&ecircncia do evento.
	 */
	public double timeStamp() {
		return timeStamp;
	}

	/**
	 * Devolve os dados do objecto de forma gen&eacuterica (como uma
	 * inst&acircncia de <code>Object</code>).<br>
	 * No simulador dever&aacute ser feito cast para os tipo dos dados
	 * espec&iacutefico dessa simula&ccedil&atildeo.
	 * 
	 * @return Os dados adicionais do evento.
	 */
	public Object data() {
		return eventData;
	}

	/**
	 * Define o crit&eacuterio de ordena&ccedil&atildeo entre este evento e o
	 * evento <code>otherEvent</code>. Na especializa&ccedil&atildeo da classe
	 * <code>Event</code> este m&eacutetodo poder&aacute ser definido para lidar
	 * com situa&ccedil&otildees como por exemplo a ordenação de eventos com o
	 * mesmo instante de ocorr&ecircncia e cuja ordem &eacute relevante.
	 * 
	 * @param otherEvent
	 *            O evento a comparar para ordena&ccedil&atildeo.
	 * @return <code>true</code> se este evento ocorrer antes de
	 *         <code>otherEvent</code>; <code>false</code> no caso
	 *         contr&aacuterio.
	 */
	public boolean before(Event otherEvent) {
		if (timeStamp < otherEvent.timeStamp())
			return true;
		return false;
	}

	/**
	 * Define o crit&eacuterio de igualdade entre este evento e o evento de
	 * compara&ccedil&atildeo <code>templateEvent</code>.<br>
	 * <i><b>Nota:</i></b> &Eacute o m&eacutetodo usado como crit&eacuterio em
	 * {@link DiscreteEventEngine} para remo&ccedil&atildeo de eventos na lista.<br>
	 * Na especializa&ccedil&atildeo da classe <code>Event</code> este
	 * m&eacutetodo tem que ser redefenido.<br>
	 * 
	 * @return <code>true</code> se o evento <code>templateEvent</code>
	 *         verificar o crit&eacuterio de igualdade; false</code> no caso
	 *         contr&aacuterio.
	 */
	public boolean equal(Event templateEvent) {
		return this.equals(templateEvent);
	}
}