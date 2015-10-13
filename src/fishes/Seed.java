package fishes;

public class Seed {
	private int swivel_range;
	private int conflict_bounce;
	private int steps;
	private int view;
	
	Seed(int swivel, int conflict, int steps, int view){
		swivel_range = swivel;
		conflict_bounce = conflict;
		this.steps = steps;
		this.view = view;
	}
	
	public int getSwivel(){
		return swivel_range;
	}
	
	public int getBounce(){
		return conflict_bounce;
	}
	
	public int getSteps(){
		return steps;
	}
	
	public int getView(){
		return view;
	}
	
	@Override
	public String toString() { 
	    String result = getSwivel() + "-" + getBounce() + "-" + getSteps()  + "-" + getView();
	    return result;
	} 
}
