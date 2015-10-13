package fishes;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

public class Food {
	
	int x;
	int y;
	boolean isEaten = false;
	
	private Simulation simulation;
	Random randomGen = new Random();

	public Food(Simulation simulation){
		this.x = randomGen.nextInt(simulation.getWidth());
		this.y = randomGen.nextInt(simulation.getHeight());
	}
	
	public void paint(Graphics2D g) {
		g.fillOval(x, y, 5, 5);
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, 5, 5);
	}
	
}
