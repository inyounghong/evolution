package fishes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Simulation extends JPanel {
	HashMap<Fish, Integer> fishes = new HashMap<Fish, Integer>();
	ArrayList<Food> foods = new ArrayList<Food>();
	ArrayList<Seed> seeds = new ArrayList<Seed>();
	
	HashMap<Integer, ArrayList<Food>> mapped_foods = new HashMap<Integer, ArrayList<Food>>();
	
	int food_consumed = 0;

	private void move() {
		for (Fish fish: fishes.keySet()){
			fish.move();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		for (Fish fish: fishes.keySet()){
			fish.paint(g2d);
		}

		g.setColor(new Color(0,0,0));
		for (Food food: copyList(foods)){
			food.paint(g2d);
		}
		
		this.displayAverageFish(g2d);
	}
	
	static ArrayList<Food> copyList (ArrayList<Food> lst){
		ArrayList<Food> copy = new ArrayList<Food>();
		for (Food food : lst){
			copy.add(food);
		}
		return copy;
	}
	
	public void fillWithFish(int n){
		for (int i = 0; i < n; i++){
			Seed s = seeds.get(Math.round(i / 2));
			fishes.put(new Fish(this, s), 0);
		}
	}
	
	public void fillWithFood(int n){
		for (int i = 0; i < n; i++){
			Food f = new Food(this);  // passing simulation as this
			foods.add(f); // Add to array list
			addFood(f); // Add to map	
		}
	}

	public ArrayList<Fish> runSim(Game game) throws InterruptedException{
		JFrame frame = new JFrame("Fishies");
		ArrayList<Fish> sortedFishes = new ArrayList<Fish>();
		
		this.seeds = game.seeds;
		
		frame.add(this);
		frame.setSize(1000, 500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.fillWithFish(20);
		this.fillWithFood(30);
		
		long t= System.currentTimeMillis();
		long end = t+15000;
		while (System.currentTimeMillis() <= end) {
			this.move();
			this.repaint();			
			Thread.sleep(5);
		}
		System.out.println("Food consumed:" + food_consumed);
		sortedFishes = sortByComparator(fishes, false);
		return sortedFishes;
	}
	
	public void displayAverageFish(Graphics2D g){
		double swivel = 0.0;
		double view = 0.0;
		double steps = 0.0;
		for (Fish i : fishes.keySet()){
			swivel += i.swivel_range;
			view += i.view;
			steps += i.steps;
		}
		int size = fishes.size();
		Color c = new Color(maxColor((int)(swivel/size), 100), 
				maxColor((int)(view/size), 50), maxColor((int)(steps/size)*5, 255));
		
		g.setColor(c);
		g.fillOval(5, 5, 20, 20);
	}
	
	public int maxColor(int i, int max){
		int c = (int) (((i * 1.0) / max) * 255);
		c = Math.max(0, c);
		return Math.min(c, 255);
	}
	
	public int flatten(int x_coor) {
		return x_coor/50 * 50;
	}
	
	/** Add food to hashmap */
	public void addFood(Food f){
		int x_coor = flatten(f.x); // Flatten x coor
		ArrayList<Food> new_array = new ArrayList<Food>();
		
		// If the map already contains key, append f to existing list in value
		if (mapped_foods.containsKey(x_coor)){
			new_array = mapped_foods.get(x_coor);
		}
		new_array.add(f);
		mapped_foods.put(x_coor, new_array);
	}
	
	public void removeFood(Food food){
		foods.remove(food);
		Food f = new Food(this);
		
		// Remove from map
		int x_coor = flatten(food.x);
		ArrayList<Food> old = mapped_foods.get(x_coor);
		old.remove(food);
		mapped_foods.put(x_coor, old);
		
		foods.add(f);
		addFood(f);
		food_consumed++;
	}
	
	public void addFoodCount(Fish fish){
		int old_value = fishes.get(fish);
		fishes.put(fish, old_value + 1);
	}
	
    private static ArrayList<Fish> sortByComparator(HashMap<Fish, Integer> unsortMap, final boolean order)
    {
        List<Entry<Fish, Integer>> list = new LinkedList<Entry<Fish, Integer>>(unsortMap.entrySet());
        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<Fish, Integer>>() {
            public int compare(Entry<Fish, Integer> o1, Entry<Fish, Integer> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });

        // Insert into array
        ArrayList<Fish> array = new ArrayList<Fish>();
        for (Entry<Fish, Integer> entry : list) {
        	array.add(entry.getKey());
        }

        return array;
    }
    
    
}
