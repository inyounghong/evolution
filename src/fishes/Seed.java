package fishes;

public class Seed {
	private int swivel_range;
	private int conflict_bounce;
	private int steps;
	private int view;
	public int view_swivel;
	public int view_cont;
	
	Seed(int swivel, int conflict, int steps, int view, int view_swivel, int view_cont){
		swivel_range = swivel;
		conflict_bounce = conflict;
		this.steps = steps;
		this.view = view;
		this.view_swivel = view_swivel;
		this.view_cont = view_cont;
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
	    String result = getSwivel() + "-" + getView() + "-" + getSteps() + "-" + view_swivel
	    		+ "-" + view_cont;
	    return result;
	} 
}
