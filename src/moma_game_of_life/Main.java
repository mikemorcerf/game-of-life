package moma_game_of_life;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
	
	static HashMap<String, int[]> cur_alive = new HashMap<String, int[]>();
	static HashMap<String, int[]> cur_dead = new HashMap<String, int[]>();
	static HashMap<String, int[]> next_alive = new HashMap<String, int[]>();
	
	public static String getKey(int x, int y) {
		return Integer.toString(x) + "," + Integer.toString(y);
	}
	
	public static void checkNeighborsForLiveCells(String key, int[] coord) {
		int x = coord[0];
		int y = coord[1];
		int numNeighbors = 0;
		
		for(int i=x-1; i<=x+1; i++) {
			for(int j=y-1; j<=y+1; j++) {
				if(i==x && j==y) {
					continue;
				}
				String neiKey = getKey(i,j);
				if(cur_alive.containsKey(neiKey)) {
					numNeighbors++;
				}
				else {
					if(!cur_dead.containsKey(neiKey)) {
						//Skip negative indexes
						if(i<0 || j<0) {
							continue;
						}
						addToHashmap(i,j,0,"DEAD");
					}
				}
			}
		}
		
		//Update number of neighbors
		cur_alive.get(key)[2]=numNeighbors;
		
		if(numNeighbors==2 || numNeighbors==3) {
			addToHashmap(x,y,0,"NEXT");
		}
	}
	
	public static void checkNeighborsForDeadCells(String key, int[] coord) {
		int x = coord[0];
		int y = coord[1];

		int numNeighbors = 0;
		
		for(int i=x-1; i<=x+1; i++) {
			for(int j=y-1; j<=y+1; j++) {
				if(i==x && j==y) {
					continue;
				}
				String neiKey = getKey(i,j);
				if(cur_alive.containsKey(neiKey)) {
					numNeighbors++;
				}
			}
		}

		//Update number of neighbors
		cur_dead.get(key)[2]=numNeighbors;
		
		if(numNeighbors==3) {
			addToHashmap(x,y,0,"NEXT");
		}
	}
	
	public static void addToHashmap(int x, int y, int numNeighbors, String state) {
		int[] coordinates = {x,y,numNeighbors};
		String key = getKey(x,y);
		switch (state) {
			case "DEAD":
				cur_dead.put(key, coordinates);
				break;
			case "ALIVE":
				cur_alive.put(key, coordinates);
				break;
			case "NEXT":
				next_alive.put(key, coordinates);
				break;
			default:
				System.out.println("Error: Wrong state argument provided");
		}
	}
	
	public static void printHashmap( HashMap<String, int[]> map, int generation) {
		System.out.println("Printing results for generation "+generation+":");
		map.forEach((k, v) -> System.out.println(k+"=["+v[0]+"]"+"["+v[1]+"]. Number of neighbors: "+v[2]+"."));
		System.out.println();
	}
	
	public static boolean isInteger(String input) {
		if(input.isEmpty()) {
			return false;
		}
		else {
			for(int i=0; i<input.length(); i++) {
				int ASCII = input.charAt(i);
				if(ASCII<48 || ASCII>57) {
					System.out.println("Error: Only positive integers allowed as input.");
					return false;
				}
			}
			return true;
		}
	}
	
	public static void populateCells() {
	    Scanner inFile = null;
		try {
			inFile = new Scanner(new FileReader("inputcells.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    while (inFile.hasNext()) {
	    	String xCoordinate=inFile.next();
	    	String yCoordinate=inFile.next();
	    	int x = Integer.parseInt(xCoordinate);
	    	int y = Integer.parseInt(yCoordinate);
	    	addToHashmap(x, y, 0, "ALIVE");
	    }
	}

	public static void main(String[] args) {
		Scanner userInput = new Scanner( System.in );
		int currentGeneration = 1;
		String maxGenerationsString = "";
				
		while(!isInteger(maxGenerationsString)) {
			System.out.println("Please type the number of generations you want the algorithm to run: ");
			maxGenerationsString = userInput.nextLine();
		}
		int maxGenerations = Integer.parseInt(maxGenerationsString);
		
		System.out.println("Populating cells with coordinates provided on the text file \"inputcells.txt\"...");
		populateCells();
		System.out.println("Populating process complete.\n");
		
		while(currentGeneration <= maxGenerations) {
			cur_alive.forEach((k, v) -> checkNeighborsForLiveCells(k, v));
			cur_dead.forEach((k, v) -> checkNeighborsForDeadCells(k, v));
			printHashmap(cur_alive, currentGeneration);
			
			//Prepare for next iteration
			cur_alive.clear();
			next_alive.forEach((k, v) -> cur_alive.put(k, v));
			next_alive.clear();
			cur_dead.clear();
			currentGeneration++;
		}
	}
}
