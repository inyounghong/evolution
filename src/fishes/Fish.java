package fishes;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Fish {
	double x;
	double y;
	
	double x_move = 1;
	double y_move = 1;
	
	double current_angle = 0; 	// Current angle of direction
	int swivel_range; 			// Amount of variance possible in swivel
	int size = 20;
	int conflict_bounce = 0;
	int view; 					// Size of the fish's viewpoint for food
	
	int cont = 0;
	int steps; 					// Number of steps before recalculating angle, range of 3
	int food_count = 0;			// Number of food consumed
	Color color;				// Fish color
	
	private Simulation simulation;
	public static Random randomGen = new Random();

	public Fish(Simulation simulation, int swivel, int conflict, int steps, int view) {
		this.simulation= simulation;
		this.swivel_range = Math.abs(swivel + randomNegative(randomGen.nextInt(10))) + 1;
		this.conflict_bounce = conflict + randomNegative(randomGen.nextInt(10));
		this.view = notNeg(view + randomNegative(randomGen.nextInt(5)));
		
		this.steps = notNeg(steps + plusOrMinus(10));
		this.current_angle = randomGen.nextInt(359) + 1;
		this.x = randomGen.nextInt(simulation.getWidth());
		this.y = randomGen.nextInt(simulation.getHeight());	
		
		this.color = makeFishColor();
	}
	
	public Color makeFishColor(){
		return new Color(simulation.maxColor(swivel_range, 120), 
				simulation.maxColor((int)(conflict_bounce), 360), simulation.maxColor(steps, 80));
	}
	
	public static int randomNegative(int value){
		if (value % 2 == 0){
			return value * -1;
		}
		return value;
	}
	
	public void randomAngle(){
		int swivel = randomNegative( randomGen.nextInt(swivel_range));
	    current_angle += swivel;
	    x_move = Math.sin(Math.toRadians(current_angle));
	    y_move = Math.cos(Math.toRadians(current_angle));
	    cont = notNeg( steps + plusOrMinus(3) );
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

	void move() {
		fluidMove();
		if (cont == 0){
			nextMove();	
		} else {
			cont--;
		}
		
		x += x_move;
		y += y_move;
		
	}
	

	public void paint(Graphics2D g) {
		g.setColor(color);
		g.fillOval((int)Math.round(x), (int)Math.round(y), 20, 20);
	}
	
	
	
	public void eatFood(Food food){
		food_count++;
		simulation.addFood(this);
		simulation.removeFood(food);
	}
	
	static ArrayList<Food> copyList (ArrayList<Food> lst){
		ArrayList<Food> copy = new ArrayList<Food>();
		for (Food food : lst){
			copy.add(food);
		}
		return copy;
	}
	
	private void nextMove() {
		for (Food food: copyList(simulation.foods)){
			if ( food.getBounds().intersects(getBounds()) ){
				eatFood(food);
			}
			randomAngle();
		}
		for (Fish other_fish: simulation.fishes.keySet()){
			if ( other_fish != this && other_fish.getBounds().intersects(getBounds())){
				randomAngle();
			} else {
				randomAngle();
			}
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int)Math.round(x), (int)Math.round(y), size, size);
	}
	
	public Rectangle largeBounds(int n){
		int half = n / 2;
		return new Rectangle((int)x - half, (int)y - half, size, size);
	}
}