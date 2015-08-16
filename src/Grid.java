import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * Clone version of 2048 made using the StdDrawPlus class from 
 * CS61B of Spring 2015 at UC Berkeley. I do not own any of the images
 * as they belong to their respective owners.
 * This is the grid class, which holds the blocks of the game and allows gameplay.
 * @author Thong Dinh
 *
 */

public class Grid {

    protected Block[][] grid;
    protected static int numBlocksMoved = 0;
    protected static boolean hasWonBefore = false;
    protected static int stuck = 0;

    
    /*************************************************************************
     *  GRID CONSTRUCTOR AND RANDOM BLOCK GENERATOR
     *************************************************************************/

    /**
     * Grid constructor that sets up a new game. 
     * Initializes with 2 blocks to start.
     */
    public Grid() {
        grid = new Block[4][4];
        int count = 0;
        while (count < 2) {
        	count += genRandom();
        }
    }
    
    /**
     * Generates and places a random valued block on the grid.
     * There is a 90% chance for a 2 and 10% chance for a 4.
     * @return int
     */
    private int genRandom() {
    	Random random = new Random();
    	ArrayList<Point> available = new ArrayList<Point>();
    	for (int i = 0; i < grid.length; i++) {
    		for (int j = 0; j < grid.length; j++) {
    			if (blockAt(i, j) == null) {
    				available.add(new Point(i, j));
    			}
    		}
    	}
    	if (available.isEmpty()) {
    		return 0;
    	}
    	int index = random.nextInt(available.size());
        int type = (int) Math.floor(Math.random() * 10);
        int x = (int) available.get(index).getX();
        int y = (int) available.get(index).getY();
        if (type == 0) {
            Block b4 = new Block(this, 4, x, y);
            place(b4, x, y);
        } else {
            Block b2 = new Block(this, 2, x, y);
            place(b2, x, y);
        }
        return 1;
    }
    
    /*************************************************************************
     *  GRAPHICS
     *************************************************************************/

    /**
     * Draws the graphical representation of the grid and blocks.
     */
    private void drawGrid() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                StdDrawPlus.picture(i + .5, j + .5, "img/0.png", 1.1, 1.1);
            }
        }
    }

    /**
     * Graphically updates and redraws the grid and blocks during gameplay.
     */
    private void update() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (blockAt(i, j) != null) {
                    StdDrawPlus.picture(i + .5, j + .5, imgType(blockAt(i, j), false), 1.1, 1.1);
                }
            }
        }
    }
    
    /**
     * Graphically updates board to let the player know that they have lost.
     */
    private void updateLost() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (blockAt(i, j) != null) {
                    StdDrawPlus.picture(i + .5, j + .5, imgType(blockAt(i, j), true), 1.1, 1.1);
                }
            }
        }
        StdDrawPlus.text(1, 1, "GAME OVER");
    }

    /**
     * Returns the string pathway of the appropriate block to be used
     * when drawing the grid of the game.
     * @param block Block of the image to be retrieved.
     * @return String
     */
    private String imgType(Block block, boolean hasLost) {
        StringBuilder img = new StringBuilder("img/");
        img.append(block.number);
        if (hasLost) {
        	img.append("-lost");
        }
        img.append(".png");
        return img.toString();
    }
    
    /*************************************************************************
     *  BLOCK PLACEMENT/REMOVAL
     *************************************************************************/

    /**
     * Physically places the block specified on the grid at designated positions.
     * @param b Block to be placed
     * @param x Position x of the block to be placed.
     * @param y Position y of the block to be placed.
     */
    public void place(Block b, int x, int y) {
		if (!isOutOfBounds(x, y) && (b != null)) {
			grid[x][y] = b;
		}
    }

    /**
     * Physically removes and returns the block at the designated position if there
     * is one, and null otherwise.
     * @param x Position x of the block to be removed.
     * @param y Position y of the block to be removed.
     * @return Block
     */
    public Block remove(int x, int y) {
        if (blockAt(x, y) == null) {
            return null;
        } else {
            Block block = blockAt(x, y);
            grid[x][y] = null;
            return block;
        }
    }

    /*************************************************************************
     *  GAMEPLAY MECHANICS - MOVEMENT/MERGING
     *************************************************************************/

    /**
     * Moves the blocks upwards and merges if valid.
     * @param block Block to be moved/merged.
     * @param x Position x of the current block to be moved/merged.
     * @param y Position y of the current block to be moved/merged.
     */
    private void mergeUp(Block block, int x, int y) {
        if (y == 3 || isOutOfBounds(x, y)) {
            return;
        }
        if (blockAt(x, y + 1) == null) {
            place(blockAt(x, y), x, y + 1);
            remove(x, y);
            numBlocksMoved += 1;
            mergeUp(blockAt(x, y + 1), x, y + 1);
        } else if (blockAt(x, y + 1) != null && blockAt(x, y).number == blockAt(x, y + 1).number) {
            int current = blockAt(x, y).number;
            int other = blockAt(x, y + 1).number;
            Block merged = new Block(this, current + other, x, y + 1);
            remove(x, y);
            remove(x, y + 1);
            place(merged, x, y + 1);
            numBlocksMoved += 1;
        }
    }

    /**
     * Iterates through each of the block on the grid and passes it into
     * the helper merge method to be moved in the specified direction.
     */
    public void moveBlocksUp() {
    	for (int j = grid.length - 1; j >= 0; j--) {
    		for (int i = 0; i < grid.length; i++) {
    			if (blockAt(i, j) != null && j != grid.length - 1) {
    				mergeUp(blockAt(i, j), i, j);
    			}
    		}
    	}
    }

    /**
     * Moves the blocks downwards and merges if valid.
     * @param block Block to be moved/merged.
     * @param x Position x of the current block to be moved/merged.
     * @param y Position y of the current block to be moved/merged.
     */
    private void mergeDown(Block block, int x, int y) {
        if (y == 0 || isOutOfBounds(x, y)) {
            return;
        }
        if (blockAt(x, y - 1) == null) {
            place(blockAt(x, y), x, y - 1);
            remove(x, y);
            numBlocksMoved += 1;
            mergeDown(blockAt(x, y - 1), x, y - 1);
        } else if (blockAt(x, y - 1) != null && blockAt(x, y).number == blockAt(x, y - 1).number) {
            int current = blockAt(x, y).number;
            int other = blockAt(x, y - 1).number;
            Block merged = new Block(this, current + other, x, y - 1);
            remove(x, y);
            remove(x, y - 1);
            place(merged, x, y - 1);
            numBlocksMoved += 1;
        }
    }

    /**
     * Iterates through each of the block on the grid and passes it into
     * the helper merge method to be moved in the specified direction.
     */
    public void moveBlocksDown() {
    	for (int j = 0; j < grid.length; j++) {
    		for (int i = grid.length - 1; i >= 0; i--) {
    			if (blockAt(i, j) != null && j != 0) {
    				mergeDown(blockAt(i, j), i, j);
    			}
    		}
    	}
    }

    /**
     * Moves the blocks to the left and merges if valid.
     * @param block Block to be moved/merged.
     * @param x Position x of the current block to be moved/merged.
     * @param y Position y of the current block to be moved/merged.
     */
    private void mergeLeft(Block block, int x, int y) {
        if (x == 0 || isOutOfBounds(x, y)) {
            return;
        }
        if (blockAt(x - 1, y) == null) {
            place(blockAt(x, y), x - 1, y);
            remove(x, y);
            numBlocksMoved += 1;
            mergeLeft(blockAt(x - 1, y), x - 1, y);
        } else if (blockAt(x - 1, y) != null && blockAt(x, y).number == blockAt(x - 1, y).number) {
            int current = blockAt(x, y).number;
            int other = blockAt(x - 1, y).number;
            Block merged = new Block(this, current + other, x - 1, y);
            remove(x, y);
            remove(x - 1, y);
            place(merged, x - 1, y);
            numBlocksMoved += 1;
        }
    }

    /**
     * Iterates through each of the block on the grid and passes it into
     * the helper merge method to be moved in the specified direction.
     */
    public void moveBlocksLeft() {
    	for (int i = 0; i < grid.length; i++) {
    		for (int j = 0; j < grid.length; j++) {
    			if (blockAt(i, j) != null && i != 0) {
    				mergeLeft(blockAt(i, j), i, j);
    			}
    		}
    	}
    }

    /**
     * Moves the blocks to the right and merges if valid.
     * @param block Block to be moved/merged.
     * @param x Position x of the current block to be moved/merged.
     * @param y Position y of the current block to be moved/merged.
     */
    private void mergeRight(Block block, int x, int y)  {
        if (x == 3 || isOutOfBounds(x, y)) {
            return;
        }
        if (blockAt(x + 1, y) == null) {
            place(blockAt(x, y), x + 1, y);
            remove(x, y);
            numBlocksMoved += 1;
            mergeRight(blockAt(x + 1, y), x + 1, y);
        } else if (blockAt(x + 1, y) != null && blockAt(x, y).number == blockAt(x + 1, y).number) {
            int current = blockAt(x, y).number;
            int other = blockAt(x + 1, y).number;
            Block merged = new Block(this, current + other, x + 1, y);
            remove(x, y);
            remove(x + 1, y);
            place(merged, x + 1, y);
            numBlocksMoved += 1;
        }
    }

    /**
     * Iterates through each of the block on the grid and passes it into
     * the helper merge method to be moved in the specified direction.
     */
    public void moveBlocksRight() {
    	for (int i = grid.length - 1; i >= 0; i--) {
    		for (int j = grid.length - 1; j >= 0; j--) {
    			if (blockAt(i, j) != null && i != 3) {
    				mergeRight(blockAt(i, j), i, j);
    			}
    		}
    	}
    }
    
    /*************************************************************************
     *  UTILITY METHODS
     *************************************************************************/

    /**
     * Checks whether the grid is full. Will return false at first available spot.
     * @return boolean
     */
    public boolean isFull() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (blockAt(i, j) == null) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Checks whether the grid is empty. Will return false at first occupied spot.
     * @return boolean
     */
    public boolean isEmpty() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (blockAt(i, j) != null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the block at the designated position if there is one and null otherwise.
     * @param x Position x of the block to be retrieved.
     * @param y Position y of the block to be retrieved.
     * @return Block
     */
    public Block blockAt(int x, int y) {
        if (isOutOfBounds(x, y) || grid[x][y] == null) {
            return null;
        } else {
            return grid[x][y];
        }
    }

    /**
     * Checks whether to see the designated position is within the boundaries of the grid.
     * @param x Position x of the spot to be checked.
     * @param y Position y of the spot to be checked.
     * @return boolean
     */
    private boolean isOutOfBounds(int x, int y) {
        if (x < 0 || x > 3 || y < 0 || y > 3) {
        	return true;
        }
        return false;
    }
    
    /**
     * Return the int associated with whether the player hasMoved or not.
     * Utility for game over detection.
     */
    private int hasMoved() {
    	return numBlocksMoved;
    }

    /*************************************************************************
     *  VICTORY/DEFEAT
     *************************************************************************/
    
    /**
     * Returns whether the player has beaten the game. Player has won when
     * the player has a 2048 tile present on the grid.
     * @return boolean
     */
    private boolean hasWon() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (blockAt(i, j) != null && blockAt(i, j).number == 2048) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Returns whether the player has lost by running a board check. Player has lost
     * when the board is full and there are no possible moves left.
     * @return boolean
     */
    private boolean hasLost() {
    	Grid temp = new Grid();
    	for (int i = 0; i < grid.length; i++) {
    		for (int j = 0; j < grid.length; j++) {
    			temp.grid[i][j] = new Block(this, this.grid[i][j].number, i, j);
    		}
    	}
        temp.moveBlocksUp();
        if (temp.hasMoved() > 0) {
        	return false;
        }
        temp.moveBlocksDown();
        if (temp.hasMoved() > 0) {
        	return false;
        }
        temp.moveBlocksLeft();
        if (temp.hasMoved() > 0) {
        	return false;
        }
        temp.moveBlocksRight();
        if (temp.hasMoved() > 0) {
        	return false;
        }
        return true;
    }

    /*************************************************************************
     *  MAIN METHOD TO START THE GAME
     *************************************************************************/
    public static void main(String args[]) {
        StdDrawPlus.setXscale(0, 4);
        StdDrawPlus.setYscale(0, 4);
        Grid g = new Grid();
        while (true) {
            g.drawGrid();
            g.update();
            if (g.isFull()) {
            	if (g.hasLost()) {
            		g.updateLost();
            		StdDrawPlus.show(100);
            		return;
            	}
            }
            if (StdDrawPlus.isUpPressed()) {
            	numBlocksMoved = 0;
                g.moveBlocksUp();
                if (numBlocksMoved > 0) {
                	g.genRandom();
                }
            } else if (StdDrawPlus.isDownPressed()) {
            	numBlocksMoved = 0;
                g.moveBlocksDown();
                if (numBlocksMoved > 0) {
                	g.genRandom();
                }
            } else if (StdDrawPlus.isRightPressed()) {
            	numBlocksMoved = 0;
                g.moveBlocksRight();
                if (numBlocksMoved > 0) {
                	g.genRandom();
                }
            } else if (StdDrawPlus.isLeftPressed()) {
            	numBlocksMoved = 0;
                g.moveBlocksLeft();
                if (numBlocksMoved > 0) {
                	g.genRandom();
                }
            }
            if (g.hasWon() && !hasWonBefore){
                System.out.println("CONGRATULATIONS, YOU'VE BEATEN THE GAME!");
                System.out.println("ENTERING ZEN MODE");
                hasWonBefore = true;
            }
            StdDrawPlus.show(125);
        }
    }
}