package src;

public class Exp {
	double getExp(double lambda) {		
		return -Math.log(Math.random()) / lambda;
		
	}
	
	double getPoisson(double lambda) {
		return Math.exp(-lambda);
	}
	
	public static void main(String[] args){ 
		Exp exp = new Exp();
		double lambda = Double.parseDouble(args[0]);
		int random_sample_count = Integer.parseInt(args[1]);
		//System.out.println("argument 0 is " + args[0]);
		//System.out.println("random_sample_count is " + random_sample_count);
		for(int i = 0; i < random_sample_count; i++) {
			System.out.println(exp.getExp(lambda));
		}
	}
	

}
