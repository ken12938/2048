import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Game {

	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		
		System.out.println("Enter grid size: ");
		
		int gridSize = in.nextInt(); //Length and width of the grid
		boolean running = true; //Will allow the game to run until this is false
		boolean initialized = false; //Determines whether the grid has any numbers (Has no numbers at the beginning)
		boolean noMoreMoves = false; //Determines whether there are any more moves (There are moves left at the beginning)
		
		/* This stores the data for the game. A value of 0 means an empty cell,
		 * and any other int value represents a cell with whatever value that is.
		 */
		int[][] grid = new int[gridSize][gridSize];
		
		int score = 0; //The score of the game. Changes when two cells are merged.
		
		while(running) { //Runs the game according to the boolean running
			
			if(!initialized) { 
				//Sets up the game board if the game board does not have any numbers
				for(int i = 0; i < gridSize; i++) {
					for(int j = 0; j < gridSize; j++) {
						if(i == 0 && j == 0) {
							grid[i][j] = 2; //Puts 2 in the top left corner
						} else {
							grid[i][j] = 0; //Everything else is an empty cell.
						}
					}
				}
				
				initialized = true; //Tells the game that the grid is set up.
			} else {
				
				//List of cells in the grid that are 0. Stores data as a string.
				ArrayList<String> coordinates = new ArrayList<String>();
				
				for(int i = 0; i < gridSize; i++) {
					for(int j = 0; j < gridSize; j++) {
						if(grid[i][j] == 0) {
							coordinates.add(i+","+j);
						}
					}
				}
				
				//Determines whether the new cell will be a 2 or 4.
				double rand = Math.random();
				
				//Finds a random cell with a 0
				int rand2 = (int) (Math.random()*coordinates.size());
				String[] rowandcol = coordinates.get(rand2).split(",");
				
				/*Uses boolean rand to determine whether the new cell is a 2 or 4.
				 * Transforms data from variable rowandcol to integer coordinates
				 * to locate where the new cell is.
				 */
				if(rand > 0.75) {
					grid[Integer.parseInt(rowandcol[0])][Integer.parseInt(rowandcol[1])] = 4;
				} else {
					grid[Integer.parseInt(rowandcol[0])][Integer.parseInt(rowandcol[1])] = 2;
				}
				
				//Ends the game if no more moves
				if(!moreMoves(grid)) {
					System.out.println("Score: " + score);
					
					//This for loop is printing out the game board.
					for(int i = 0; i < gridSize; i++) {
						for(int j = 0; j < gridSize; j++) {
							if(grid[i][j] == 0) {
								System.out.print("-   ");
							} else {
								System.out.print(grid[i][j] + "   ");
							}
						}
						
						System.out.println("");
						System.out.println("");
					}
					
					System.out.println("GAME OVER");
					break; //Exits the game
				}
			}
			
			//Prints out the score and game board.
			System.out.println("Score: " + score);
			for(int i = 0; i < gridSize; i++) {
				for(int j = 0; j < gridSize; j++) {
					if(grid[i][j] == 0) {
						System.out.print("-   ");
					} else {
						System.out.print(grid[i][j] + "   ");
					}
				}
				
				System.out.println("");
				System.out.println("");
			}
			
			//Temporary new grid to calculate the state of the grid after a move.
			int[][] newGrid = new int[gridSize][gridSize];
			
			//New command
			String input = in.next();
			
			switch(input) { 
				case "q":
					running = false;
					break;
					
				case "n":
					initialized = false;
					grid = new int[gridSize][gridSize];
					break;
					
				case "a": // Move left
					newGrid = new int[gridSize][gridSize];
					
					for(int i = 0; i < gridSize; i++) { //Row number
						
						//newIndex is the current "j" position in newGrid
						int newIndex = 0;
						for(int j = 0; j < gridSize;) { //Column number
							
							//This if loop is to prevent an empty j cell or a j cell at the end of the array
							if(j < gridSize - 1 && grid[i][j] != 0) {
								boolean foundMatch = false;
								
								/*Searches for the next cell to the right of j that is
								 * a non-empty element. Merges with j cell if h cell is equal.
								 */
								for(int h = j + 1; h < gridSize; h++) {
									if(grid[i][j] == grid[i][h]) { // h cell can merge
										newGrid[i][newIndex] = 2*grid[i][j];
										score += 2*grid[i][j];
									
										j = h + 1;
										h = gridSize;
										foundMatch = true;
									} else if(grid[i][h] != 0) { // h cell cannot merge
										h = gridSize;
									}
								}
								
								if(!foundMatch) { //There is no merge-able cell to the right of j.
									newGrid[i][newIndex] = 
											grid[i][j];
									j++;
								}
								newIndex++; //Moves newIndex when a number has been recorded in newGrid.
							} else if(grid[i][j] != 0) { //When j cell is at the end of the array
								newGrid[i][newIndex] = grid[i][j];
								j++;
								newIndex++;
							} else { //j cell is an empty cell
								j++;
							}
							
							
						}
						
						while(newIndex < gridSize) { //Fills in remaining cells of the same row of newGrid with 0
							newGrid[i][newIndex] = 0;
							newIndex++;
						}
					}
					grid = newGrid; //Replaces old grid with the new updated grid
					break;
					
				case "d": //Move right
					newGrid = new int[gridSize][gridSize];
					for(int i = 0; i < gridSize; i++) {
						int newIndex = gridSize - 1;
						
						for(int j = gridSize - 1; j >= 0;) {
							if(j > 0 && grid[i][j] != 0) {
								boolean foundMatch = false;
								
								for(int h = j - 1; h >= 0; h--) {
									if(grid[i][j] == grid[i][h]) {
										newGrid[i][newIndex] = 2*grid[i][j];
										score += 2*grid[i][j];
									
										j = h - 1;
										h = -1;
										foundMatch = true;
									} else if(grid[i][h] != 0) {
										h = -1;
									}
								}
								
								if(!foundMatch) {
									newGrid[i][newIndex] = 
											grid[i][j];
									j--;
								}
								newIndex--;
							} else if(grid[i][j] != 0) {
								newGrid[i][newIndex] = grid[i][j];
								j--;
								newIndex--;
							} else {
								j--;
							}
							
							
						}
						
						while(newIndex >= 0) {
							newGrid[i][newIndex] = 0;
							newIndex--;
						}
					}
					grid = newGrid;
					break;
					
				case "w": //Move up. Same logic as move left except with i and j swapped
					newGrid = new int[gridSize][gridSize];
					for(int i = 0; i < gridSize; i++) {
						int newIndex = 0;
						for(int j = 0; j < gridSize;) {
							if(j < gridSize - 1 && grid[j][i] != 0) {
								boolean foundMatch = false;
								
								for(int h = j + 1; h < gridSize; h++) {
									if(grid[j][i] == grid[h][i]) {
										newGrid[newIndex][i] = 2*grid[j][i];
										score += 2*grid[j][i];
									
										j = h + 1;
										h = gridSize;
										foundMatch = true;
									} else if(grid[h][i] != 0) {
										h = gridSize;
									}
								}
								
								if(!foundMatch) {
									newGrid[newIndex][i] = 
											grid[j][i];
									j++;
								}
								newIndex++;
							} else if(grid[j][i] != 0) {
								newGrid[newIndex][i] = grid[j][i];
								j++;
								newIndex++;
							} else {
								j++;
							}
							
							
						}
						
						while(newIndex < gridSize) {
							newGrid[newIndex][i] = 0;
							newIndex++;
						}
					}
					grid = newGrid;
					break;
					
				case "s": //Move down. Same logic as move right except with i and j swapped
					newGrid = new int[gridSize][gridSize];
					for(int i = 0; i < gridSize; i++) {
						int newIndex = gridSize - 1;
						
						for(int j = gridSize - 1; j >= 0;) {
							if(j > 0 && grid[j][i] != 0) {
								boolean foundMatch = false;
								
								for(int h = j - 1; h >= 0; h--) {
									if(grid[j][i] == grid[h][i]) {
										newGrid[newIndex][i] = 2*grid[j][i];
										score += 2*grid[j][i];
									
										j = h - 1;
										h = -1;
										foundMatch = true;
									} else if(grid[h][i] != 0) {
										h = -1;
									}
								}
								
								if(!foundMatch) {
									newGrid[newIndex][i] = 
											grid[j][i];
									j--;
								}
								newIndex--;
							} else if(grid[j][i] != 0) {
								newGrid[newIndex][i] = grid[j][i];
								j--;
								newIndex--;
							} else {
								j--;
							}
							
							
						}
						
						while(newIndex >= 0) {
							newGrid[newIndex][i] = 0;
							newIndex--;
						}
					}
					grid = newGrid;
					break;
					
				default:
					System.out.println("Invalid move");
					break;
					
				
			}
			
		}
		
	}
	
	public static boolean moreMoves(int[][] arr) {
		for(int i = 0; i < arr.length; i++) {
			for(int j = 0; j < arr[0].length; j++) {
				if(arr[i][j] == 0) {
					return true;
				}
				
				if(i < arr.length - 1) {
					if(arr[i][j] == arr[i + 1][j]) {
						return true;
					}
				}
				
				if(j < arr[0].length - 1) {
					if(arr[i][j] == arr[i][j + 1]) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
