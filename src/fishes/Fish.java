package fishes;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

public class Fish {
	double x;
	double y;
	
	double x_move;
	double y_move;
	
	double current_angle = 0; 		// Current angle of direction
	int swivel_range; 				// Amount of variance possible in swivel
	int size = 20;
	int conflict_bounce = 0;
	int view; 						// Size of the fish's viewpoint for food
	int view_swivel;				// Amount of swivel when within view of food
	int view_cont;					// Number of steps cont when within view of food
	Ellipse2D.Double view_circle;	// Actual circle representing the fish view
	boolean food_in_view = false;	// True if food is in view
	
	int cont = 0;
	int steps; 						// Number of steps before recalculating angle, range of 3
	int food_count = 0;				// Number of food consumed
	Color color;					// Fish color
	
	private Simulation simulation;
	public static Random randomGen = new Random();

	public Fish(Simulation simulation, Seed seed) {
		this.simulation= simulation;
		this.swivel_range = Math.abs(seed.getSwivel() + randomNegative(randomGen.nextInt(10))) + 1;
		this.conflict_bounce = seed.getBounce() + randomNegative(randomGen.nextInt(10));
		this.view = Math.min(notNeg(seed.getView() + randomNegative(randomGen.nextInt(5))), 50);
		this.view_swivel = seed.view_swivel;
		this.view_cont = notNeg(seed.view_cont + randomNegative(randomGen.nextInt(5)));
		
		
		this.steps = notNeg(seed.getSteps() + plusOrMinus(10));
		this.current_angle = randomGen.nextInt(359) + 1;
		this.x = randomGen.nextInt(simulation.getWidth());
		this.y = randomGen.nextInt(simulation.getHeight());	
		this.view_circle = new Ellipse2D.Double(this.x, this.y, view, view);
		
		this.color = makeFishColor();
	}
	
	public Color makeFishColor(){
		return new Color(simulation.maxColor(swivel_range, 120), 
				simulation.maxColor((int)(view), 50), simulation.maxColor(steps, 80));
	}
	
	public static int randomNegative(int value){
		if (value % 2 == 0){
			return value * -1;
		}
		return value;
	}

	
	/** Returns 0 if n is negative, otherwise just n */
	public static int notNeg(int n){
		if (n < 0){
			return 0;
		} else {
			return n;
		}
	}
	
	/** Returns plus or minus of given n range */
	public static int plusOrMinus(int n){
		return randomNegative(randomGen.nextInt(n));
	}
	
	public void bounce(){
		current_angle += conflict_bounce;
		x_move = Math.sin(Math.toRadians(current_angle));
	    y_move = Math.cos(Math.toRadians(current_angle));
	    cont = 10;
	}

	public void fluidMove(){
		if (x > simulation.getWidth()){
			x = 1;
		} else if (x < -size){
			x = simulation.getWidth();
		}
		if (y > simulation.getHeight()){
			y = 1;
		} else if (y < -size){
			y = simulation.getHeight();
		}
	}
	
	public void setNewAngle(int angle){
		 current_angle += randomNegative( randomGen.nextInt(angle));
		    
		 // Set new movements
		 x_move = Math.sin(Math.toRadians(current_angle));
		 y_move = Math.cos(Math.toRadians(current_angle));
	    
		 // Reset cont
		 cont = view_cont;
	}
	
	public void setAngleTo(int angle){
		current_angle = angle;
		x_move = Math.sin(Math.toRadians(current_angle));
	    y_move = Math.cos(Math.toRadians(current_angle));
	}
	
	/** Returns a list of foods close to the given x coordinate */
	public ArrayList<Food> getFoodsCloseTo(int fish_x){
		ArrayList<Food> foods1 = simulation.mapped_foods.get(fish_x);
		ArrayList<Food> new_foods1 = copyList(foods1);
		
		// Add foods +/- one hash unit for view checking
		if(simulation.mapped_foods.containsKey(fish_x - 50)){
			new_foods1.addAll(simulation.mapped_foods.get(fish_x - 50));
		}
		if(simulation.mapped_foods.containsKey(fish_x + 50)){
			new_foods1.addAll(simulation.mapped_foods.get(fish_x + 50));
		}
		return new_foods1;
	}

	/** Moves fish one step after running calculations */
	void move() {
		// Wrap fish around screen
		fluidMove();
		
		// Check fish boundaries
		int fish_x = simulation.flatten((int) this.x);
		if (simulation.mapped_foods.containsKey(fish_x)){
			ArrayList<Food> foods = getFoodsCloseTo(fish_x);
			
			food_in_view = false;
			
			for (Food food: foods){
				// If food is within view
				Boolean in_view = food.getBounds().intersects(getViewCircle().getBounds2D());
				if (in_view){		
					food_in_view = true;
					// If fish eats food
					if ( food.getBounds().intersects(getBounds()) ){
						eatFood(food);
					}
				}
			}
		}	
		
		// Set new angle if at end of steps
		if (food_in_view){
			setNewAngle(view_swivel);
		}
		else if (cont == 0){
			setNewAngle(swivel_range);
		}
		else {
			cont--;
		}
		
		// Actually add coordinates and step one move
		x += x_move;
		y += y_move;
		
	}
	

	public void paint(Graphics2D g) {
		if (food_in_view){
			g.setColor(Color.RED);
		}
		else{
			g.setColor(color);
		}
		g.fillOval((int)Math.round(x), (int)Math.round(y), 20, 20);
//		g.draw(getViewRectangle());
		g.draw(getViewCircle());
	}
	
	
	
	public void eatFood(Food food){
		food_count++;
		simulation.addFoodCount(this);
		simulation.removeFood(food);
	}
	
	static ArrayList<Food> copyList (ArrayList<Food> lst){
		ArrayList<Food> copy = new ArrayList<Food>();
		for (Food food : lst){
			copy.add(food);
		}
		return copy;
	}
	

	/** Returns rectangle bounds of fish */
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, size, size);
	}
	
	public Ellipse2D.Double getViewCircle(){
		double half = view / 2;
		Ellipse2D.Double new_circle = new Ellipse2D.Double(x - half, y - half, view + size, view + size);
		view_circle = new_circle;
		return new_circle;
	}
}