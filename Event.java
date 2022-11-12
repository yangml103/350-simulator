package src;

import java.util.Comparator;

public class Event {
	String type;
	public Double timestamp;
	public String ID;
	String name;
	Request request;
	Server serverType;
	
	public Event(String type, Double _timestamp, String name, Request request, Server serverType) {
		this.timestamp = _timestamp;
		this.type = type;
		this.name = name;
		this.request = request;
		this.serverType = serverType;
		
		
	}
}
class EventComparator implements Comparator<Event>{
	
	public int compare(Event a, Event b){
		if (a.timestamp < b.timestamp) {
			return -1;
		}
		else if (a.timestamp == b.timestamp) {
			return 0;
		}
		else {
			return 1;
		}
	}
}
	
	
	//public static void main(String[] args) {
		
		//Event e0 = new Event("A",0);

		//Event e1 = new Event("A",1);
		//System.out.println("e0.ID = " + e0.ID);
		//System.out.println("e0.Instant = " + e0.timestamp);
		//System.out.println("e1.ID = " + e1.ID);
		//System.out.println("e1.Instant = " + e1.timestamp);
	//}
	//public String toString() {
		//return "[type = " + type + " timestamp = " + timestamp + "]";
	//}

//}
