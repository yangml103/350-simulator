package src;
import java.util.PriorityQueue;
//import java.util.Stack;


public class Timeline {
	//TODO: determine whether incoming events will always be the last event
	static Timeline timeline = new Timeline();
	
	PriorityQueue<Event> tpqueue = new PriorityQueue<>(11, new EventComparator());

	//Stack<Event> events = new Stack<Event>();
	
	public static Timeline getInstance() {

		return timeline;
	}
	
	void addToTimeline(Event evtToAdd){
		//Integer index = 0;
		//if (tpqueue.size() == 0) {
		tpqueue.add(evtToAdd);
			
		//}
		//else {
			//while(tpqueue.elementAt(index).timestamp > evtToAdd.timestamp) {
				
				//index += 1;
				
				//if (index == tpqueue.size()) {
				//	break;
				//}
				
			//}
			//tpqueue.add(index, evtToAdd);
		}//
	//}
	
	Event popNext() {
		return tpqueue.remove();
	}
	
	boolean isEmpty() {
		return tpqueue.isEmpty();
	}

	
	

}
