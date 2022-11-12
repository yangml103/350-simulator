package src;
import java.util.*;

public class Server {
	String queueType;
	Queue<Request> queue = new LinkedList<>();
	PriorityQueue<Request> pqueue = new PriorityQueue<>(11, new RequestComparator());
	Request currR = null;
	int limit;
	
	public Server(String queueType, int limit) {
		this.queueType = queueType;
		this.limit = limit;
	}
	
	public Server(String queueType) {
		this(queueType, -1);
	}

	boolean receiveRequest(Request request, Timeline timeline, double currTime) {
		
		if (queueType.equals("FIFO")){
			if (queue.size() >= limit && limit > 0) {
				return false;
			}
			queue.add(request);
			if (queue.size() == 1) {
				Event event = new Event("START", currTime, request.name, request, this);
				timeline.addToTimeline(event);
				return true;
			}
			return true;
			
		}
		else {
			pqueue.add(request);
			if (pqueue.size() == 1) {
				Event event = new Event("START", currTime, request.name, request, this);
				timeline.addToTimeline(event);
				return true;
			}
		}
		return false;
	
	}
	
	Request removeRequest(double time, Timeline timeline){
		if (queueType.equals("FIFO")){
			Request remove = queue.remove();
			if (queue.size() != 0) {
				Event start = new Event("START", time, queue.peek().name, queue.peek(), this);
				timeline.addToTimeline(start);
				
			}
			return remove;
		}
		else {
			pqueue.remove(currR);
			if (pqueue.size() != 0) {
				Event start = new Event("START", time, pqueue.peek().name, pqueue.peek(), this);
				timeline.addToTimeline(start);
				
			}
			return currR;
		}
	}
	
	int getQueueLength() {
		if (queueType.equals("FIFO")) {
			return queue.size();
		}
		else {
			return pqueue.size();
		}
	}
	
	

}
