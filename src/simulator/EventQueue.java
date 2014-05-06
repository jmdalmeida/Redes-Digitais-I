package simulator;

public class EventQueue {

    private int size;
    private LinkedEvent head, tail;

    public EventQueue() {
    	size=0;
    	tail=head=null;
    }

    public int   eventQueueSize() { return size; };

    public Event nextEvent() {
        Event headEvent = null;
        if (head != null) {
        	headEvent = head.getEvent();
        	head=head.getNext();
        	if (head!=null) head.setPrevious(null);
        	size--;
        }
        return headEvent;
    }
    
    public void removeEvent(Event toRemove) {
    	Event currentE;
    	LinkedEvent currentLE = head;
    	while(currentLE != null) {
    		currentE=currentLE.getEvent();
    		if(currentE.equal(toRemove)) {
    			size--;
    			if (currentLE==head) { head = currentLE.getNext(); }
    			if (currentLE==tail) { tail = currentLE.getPrevious(); }
    			
    			if(currentLE.getNext()!=null) {
    				currentLE.getNext().setPrevious(currentLE.getPrevious());
    			}
    			if(currentLE.getPrevious()!=null) {
    				currentLE.getPrevious().setNext(currentLE.getNext());
    			}
    		}
    		currentLE=currentLE.getNext();
    	}
    	return;
    }	

    public void addEvent(Event toBook) {
    	LinkedEvent toBookLE;
    	LinkedEvent iterator;
    	toBookLE = new LinkedEvent(toBook);
    	iterator = tail;
	
    	size++;
    	if (size == 1) { // only one in the Queue!
    		head = tail = toBookLE;
    		return;
    	}

    	while (toBookLE.getEvent().before(iterator.getEvent())) {
    		if (iterator.getPrevious()!=null) {
    			iterator=iterator.getPrevious();
    		} else { // at the head!
    			head.setPrevious(toBookLE);
    			toBookLE.setNext(head);
    			head = toBookLE;
    			return;
    		}
    	}

    	if (iterator.getNext()==null) { // at the tail!
    		tail.setNext(toBookLE);
    		toBookLE.setPrevious(tail);
    		tail = toBookLE;
    		return;
    	} else { // in the middle!
    		toBookLE.setNext(iterator.getNext());
    		toBookLE.getNext().setPrevious(toBookLE);
    		toBookLE.setPrevious(iterator);
    		iterator.setNext(toBookLE);
    		return;
    	}
    }
    
    public String toString() {
    	String s="";
    	LinkedEvent iterator=head;
    	while(iterator != null) {
    		s=s+"[EVENT_QUEUE] "+iterator.getEvent().toString()+"\n";
    		iterator=iterator.getNext();
    	}
    	return s;
    }
}