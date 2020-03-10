package moma_game_of_life;

import java.io.FileNotFoundException;
import java.io.FileReader;
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
		//Get x and y coordinates for the live cell that is being checked
		int x = coord[0];
		int y = coord[1];
		int numNeighbors = 0;
		
		//Scan all cells around the live cell to count how many live cell it is neighbors with
		for(int i=x-1; i<=x+1; i++) {
			for(int j=y-1; j<=y+1; j++) {
				//Skip checking its own position so it does not count itself as a neighbor
				if(i==x && j==y) {
					continue;
				}
				//Get key related to the cell
				String neiKey = getKey(i,j);
				//Check if it is in the cur_alive hashmap. If it is, it means it is neighbors with the current cell being checked
				if(cur_alive.containsKey(neiKey)) {
					numNeighbors++;
				}
				//Else, it is a dead cell, that will be put into a hashmap for dead cells to track how many living neighbors it has
				else {
					if(!cur_dead.containsKey(neiKey)) {
						//Skip negative indexes
						if(i<0 || j<0) {
							continue;
						}
						//If Dead cell is not currently in cur_dead, and it is neighbor with a live cell, that means it has at least ONE live cell as neighbor
						addToHashmap(i,j,1,"DEAD");
					}
					else {
						//If Dead cell is in cur_dead hashmap, then increase its number of live neighbors by one
						cur_dead.get(neiKey)[2]+=1;
						//If the number of living neighbors is 3, then this dead cell should be alive on next generation
						if(cur_dead.get(neiKey)[2]==3) {
							addToHashmap(i,j,cur_dead.get(neiKey)[2],"NEXT");
						//However, if number of living neighbors exceeds 3, then remove it from next generation
						} else if(cur_dead.get(neiKey)[2]>3 && next_alive.containsKey(neiKey)) {
							next_alive.remove(neiKey);
						}
					}
				}
			}
		}
		
		//Update number of neighbors
		cur_alive.get(key)[2]=numNeighbors;
		
		//If number of live neighbors is either 2 or 3, this cell will remain alive next generation
		if(numNeighbors==2 || numNeighbors==3) {
			addToHashmap(x,y,0,"NEXT");
		}
	}
	
	//Add cells to hashmaps associated with cell's state:
	//ALIVE: Cell is alive in current generation
	//DEAD: Cell is dead in current generation, but it is neighbor with at least one live cell
	//NEXT: Cell will be alive next generation
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
	
	//Function to print the contents of a hashmap
	public static void printHashmap( HashMap<String, int[]> map, int generation) {
		map.forEach((k, v) -> System.out.println(k+"=["+v[0]+"]"+"["+v[1]+"]. Number of neighbors: "+v[2]+"."));
		System.out.println();
	}
	
	//Function to check whether user's input for number of generations the program should run is a positive integer
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
	
	//Function that reads a text file to get what are the coordinates where live cells for the very first generation are located
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
	    inFile.close();
	}

	public static void main(String[] args) {
		Scanner userInput = new Scanner( System.in );
		int currentGeneration = 1;
		String maxGenerationsString = "";
				
		//Get a positive integer as input from user to know for how many generations algorithm will run
		while(!isInteger(maxGenerationsString)) {
			System.out.println("Please type the number of generations you want the algorithm to run: ");
			maxGenerationsString = userInput.nextLine();
		}
		int maxGenerations = Integer.parseInt(maxGenerationsString);
		userInput.close();
		
		System.out.println("Populating cells with coordinates provided on the text file \"inputcells.txt\"...");
		populateCells();
		System.out.println("Populating process complete.\n");
		
		//Each iteration of this loop is a generation of cells
		while(currentGeneration <= maxGenerations) {
			//Check number of neighbors for all live cells, and also checks if their dead cell neighbors will be alive next generation
			cur_alive.forEach((k, v) -> checkNeighborsForLiveCells(k, v));
			
			System.out.println("Printing results for generation "+currentGeneration+":");
			//Print all the cells that are currently alive if any
			if(!cur_alive.isEmpty()) {
				printHashmap(cur_alive, currentGeneration);
			} else {
			//If all cells are dead, stop program
				System.out.println("All cells are dead. Program stopping...");
				break;
			}
			
			//Prepare for next generation
			cur_alive.clear();
			cur_alive.putAll(next_alive);
			next_alive.clear();
			cur_dead.clear();
			currentGeneration++;
		}
	}
}