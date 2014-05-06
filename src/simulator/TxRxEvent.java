package simulator;

public class TxRxEvent extends Event {
	
	public enum TxRxEventType {Generate_DATA, Arrival_DATA, StopTX, StartRX, StopRX, ACK, TIMEOUT};

	public TxRxEvent(double ts, TxRxEventType type, Data data) {
		super(ts, type, data);
	}

	public boolean before(Event otherEvent) {
		/*
		 * Método para a ordenar eventos.
		 * Eventos de tipo COMPLETE_SERVICE são prioritários em relação aos do tipo ARRIVE (quando com o mesmo tempo) 
		 */
		if (timeStamp<otherEvent.timeStamp()) { return true;		};
		return false;
	}

	public boolean equal(Event otherEvent) {
		/*
		 * Método para a verificar a igualdade entre eventos.
		 * Dois eventos são iguais se tiverem o mesmo tipo. 
		 */
		if ((TxRxEventType)(otherEvent.type()) == eventType) { return true; }
		return false;
	}
	
	public String toString() {
		String s="";
		s=s+"TS:"+timeStamp+" TYPE:"+eventType.toString();
		if (eventData != null) s=s+" DATA:"+eventData.toString();
		return s;
	}
}
