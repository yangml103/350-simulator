package src;

import java.util.Comparator;

public class Request {
	public double processTime;
	public double arrivalTime;
	public String name;
	public double totalProcessTime;
	public double timeEnteredQueue;
	
	public Request(String name, double arrivalTime, double processTime) {
		this.name = name;
		this.processTime = processTime;
		this.arrivalTime = arrivalTime;
		this.totalProcessTime = processTime;
		this.timeEnteredQueue = arrivalTime;
		
	}
	
	public boolean isX() {
		return this.name.charAt(0) == 'X';
	}
	

}

class RequestComparator implements Comparator<Request>{
	
	public int compare(Request a, Request b){
		if (a.processTime < b.processTime) {
			return -1;
		}
		else if (a.processTime == b.processTime) {
			return 0;
		}
		else {
			return 1;
		}
	}
}
