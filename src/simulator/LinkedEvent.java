package simulator;

public class LinkedEvent {
	private LinkedEvent next, previous;
	private Event event;

	public LinkedEvent(Event e) {
		next = previous = null;
		event = e;
	}

	public Event getEvent() {
		return event;
	}

	public LinkedEvent getNext() {
		return next;
	}

	public LinkedEvent getPrevious() {
		return previous;
	}

	public void setNext(LinkedEvent ne) {
		next = ne;
	}

	public void setPrevious(LinkedEvent pe) {
		previous = pe;
	}

}
