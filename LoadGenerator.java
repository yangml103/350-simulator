package src;


class LoadGenerator {
	String type;
	Double lambda;
	Exp exp;
	Event evt;
	int x = -1;
	String xory;
	Server serverType;

	
	static Timeline timeline;
	public LoadGenerator(String _type, Double _lambda, String xory, Server serverType) {
		this.type = _type;
		this.lambda = _lambda;
		exp = new Exp();
		this.xory = xory;
		this.serverType = serverType;
	}
	
	
	
	Event generateEvent(double time) {
		double newTime = time + exp.getExp(lambda);
		//double newTime = time + lambda;


		x += 1;
		return new Event(type, newTime, xory+x,null, serverType);
		
	}
	
	/*
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("must have 3 arguments");
			System.exit(0);
		}
		timeline = new Timeline();
		double lambdaA = Double.parseDouble(args[0]);
		double lambdaB = Double.parseDouble(args[1]);
		double T = Double.parseDouble(args[2]);
		
		timeline.addToTimeline(new Event ("A",0.0));
		timeline.addToTimeline(new Event ("B",0.0));

		LoadGenerator LGA = new LoadGenerator("A",lambdaA);
		LoadGenerator LGB = new LoadGenerator("B",lambdaB);
		Double time = 0.0;
		while(time < T) {
			Event curr = timeline.popNext();
			if (curr.type == "A") {
				time = curr.timestamp;
				LGA.releaseRequest(curr);
			}
			else {
				time = curr.timestamp;
				LGB.releaseRequest(curr);
			}
			
		}
	}*/

}
