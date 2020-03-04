package moma_game_of_life;

import java.util.HashMap;

public class Main {
	
	static HashMap<String, int[]> cur_alive = new HashMap<String, int[]>();
	static HashMap<String, int[]> cur_dead = new HashMap<String, int[]>();
	static HashMap<String, int[]> next_alive = new HashMap<String, int[]>();
	
	public static String getKey(int x, int y) {
		return Integer.toString(x) + "," + Integer.toString(y);
	}
	
	public static void checkNeighborsForLiveCells(int[] coord) {
		int x = coord[0];
		int y = coord[1];

		int numNeighbors = 0;
		
		for(int i=x-1; i<=x+1; i++) {
			for(int j=y-1; j<=y+1; j++) {
				if(i!=x && j!=y) {
					String neiKey = getKey(i,j);
					if(cur_alive.containsKey(neiKey)) {
						numNeighbors++;
					}
					else {
						if(!cur_dead.containsKey(neiKey)) {
							int[] deadCell = {i, j};
							cur_dead.put(neiKey, deadCell);
						}
					}
				}
			}
		}

		if(numNeighbors==3) {
			next_alive.put(getKey(coord[0],coord[1]), coord);
		}
	}
	
	public static void checkNeighborsForDeadCells(int[] coord) {
		int x = coord[0];
		int y = coord[1];

		int numNeighbors = 0;
		
		for(int i=x-1; i<=x+1; i++) {
			for(int j=y-1; j<=y+1; j++) {
					if(i!=x && j!=y) {
					String neiKey = getKey(i,j);
					if(cur_alive.containsKey(neiKey)) {
						numNeighbors++;
					}
				}
			}
		}

		if(numNeighbors==2 || numNeighbors==3) {
			next_alive.put(getKey(coord[0],coord[1]), coord);
		}
	}
	
	public static void printLiveCells() {
		System.out.println("Alive Cells: "+cur_alive);
	}
	
	public static void printNextCells() {
		System.out.println("Next Gen Cells: "+next_alive);
	}

	public static void main(String[] args) {

		
		int[] coordinates = {1,1};
		String key = getKey(coordinates[0],coordinates[1]);
		cur_alive.put(key, coordinates);
		
		coordinates[0] = 2;
		coordinates[1] = 1;
		key = getKey(coordinates[0],coordinates[1]);
		cur_alive.put(key, coordinates);
		
		coordinates[0] = 1;
		coordinates[1] = 2;
		key = getKey(coordinates[0],coordinates[1]);
		cur_alive.put(key, coordinates);
		
		coordinates[0] = 3;
		coordinates[1] = 2;
		key = getKey(coordinates[0],coordinates[1]);
		cur_alive.put(key, coordinates);
		
		coordinates[0] = 1;
		coordinates[1] = 3;
		key = getKey(coordinates[0],coordinates[1]);
		cur_alive.put(key, coordinates);
		
		coordinates[0] = 4;
		coordinates[1] = 4;
		key = getKey(coordinates[0],coordinates[1]);
		cur_alive.put(key, coordinates);
		
		
		cur_alive.forEach((k, v) -> checkNeighborsForLiveCells(v));
		cur_dead.forEach((k, v) -> checkNeighborsForDeadCells(v));
		printLiveCells();
		printNextCells();
		
		
		
		//cur_alive = next_alive
		//next_alive.clear();
		//cur_dead.clear();
	}

}
