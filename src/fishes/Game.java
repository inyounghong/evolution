package fishes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


@SuppressWarnings("serial")
public class Game extends TimerTask {
	
	int round;
	ArrayList<Fish> winners = new ArrayList<Fish>();
	ArrayList<Seed> seeds = new ArrayList<Seed>();
	
	public Game(){
		setRound();
	}
	
	public void run() {
		round += 1;
		System.out.println("Round: " + round);
		
		if (round == 1 ) writeRandom(this);

		this.readFromFile();
		
		//System.out.println("Seeds: " + seeds);
		this.printAverages();
		
		Simulation simulation = new Simulation();
		try {
			winners = simulation.runSim(this);
			try{
				int w = simulation.fishes.get(winners.get(0));
			}
			catch (IndexOutOfBoundsException e){
				Thread.sleep(1000);
				System.out.println("Running again");
				winners = simulation.runSim(this);
			}
			writeToFile(this);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
    }

	public static void main(String[] args){
        Timer timer = new Timer();
        timer.schedule(new Game(), 0, 10000);
    }
	
	public static void writeToFile(Game game){
    	try {

			String content = "";
			for (int i = 0; i < 10; i++){
				Fish fish = game.winners.get(i);
				content += fish.swivel_range;
				content += " " + fish.conflict_bounce;
				content += " " + fish.steps;
				content += " " + fish.view;
				content += " " + fish.view_swivel;
				content += " " + fish.view_cont;
				content += "\n";
				System.out.print(fish.food_count + " ");
			}

			File file = new File("round" + game.round + ".txt");

			// if file doesn't exist, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			
			

		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public static void writeRandom(Game game){
    	try {

			String content = "";
			Random randomGen = new Random();
			for (int i = 0; i < 10; i++){
				content += randomGen.nextInt(40);		 // Swivel range
				content += " " + randomGen.nextInt(360); // Bounce
				content += " " + randomGen.nextInt(60);	// Steps
				content += " " + randomGen.nextInt(50);	 // View
				content += " " + randomGen.nextInt(50);  // View swivel
				content += " " + randomGen.nextInt(30);  // View continue
				content += "\n";
			}

			File file = new File("round0.txt");

			// if file doesn't exist, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public void readFromFile(){
		
        String fileName = "round" + (round -1) + ".txt";
        String line = null;
        
        // Reset seeds
        this.seeds = new ArrayList<Seed>();

        try {
            FileReader fileReader =  new FileReader(fileName);
            BufferedReader bufferedReader =  new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	String[] splitted = line.split(" ");
            	
            	int new_swivel = Integer.parseInt(splitted[0]);
            	int new_bounce = Integer.parseInt(splitted[1]);
            	int new_steps = Integer.parseInt(splitted[2]);
            	int new_view = Integer.parseInt(splitted[3]);
            	int new_view_swivel = Integer.parseInt(splitted[4]);
            	int new_view_cont = Integer.parseInt(splitted[5]);
            			
            	Seed s = new Seed(new_swivel, new_bounce, new_steps, new_view, new_view_swivel,
            			new_view_cont);
                seeds.add(s);
            }   
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println( "Unable to open file '" +  fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println( "Error reading file '"   + fileName + "'");                  
        }
	}
	
	/** Reads round from current_round.txt */
	public void setRound(){
		
        String fileName = "current_round.txt";
        String line = null;
       
        try {
            FileReader fileReader =  new FileReader(fileName);
            BufferedReader bufferedReader =  new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	round = Integer.parseInt(line);
            }   
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println( "Unable to open file '" +  fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println( "Error reading file '"   + fileName + "'");                  
        }
	}
	
	public void printAverages(){
		double swivel = 0.0;
		double view = 0.0;
		double steps = 0.0;
		double view_swivel = 0.0;
		double view_cont = 0.0;
		for (Seed i : seeds){
			swivel += i.getSwivel();
			view += i.getView();
			steps += i.getSteps();
			view_swivel += i.view_swivel;
			view_cont += i.view_cont;
		}
		int size = seeds.size();
		System.out.println (
				"Swivel angle: " + (swivel / size) + 
				" View: " + (view / size) + 
				" Steps: " + (steps / size) + 
				" View_swivel: " + (view_swivel / size) + 
				" View_cont: "+ (view_cont / size));
	}

}