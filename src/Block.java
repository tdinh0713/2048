/**
 * Clone version of 2048 made using the StdDrawPlus class from 
 * CS61B of Spring 2015 at UC Berkeley. I do not own any of the images
 * as they belong to their respective owners.
 * This is the block class, which creates single valued block objects that go on the grid.
 * @author Thong Dinh
 *
 */

public class Block {

    protected int number;
    protected int x;
    protected int y;
    protected Grid g;

    /**
     * Block constructor that creates new blocks with appropriate values (2, 4, 8, etc).
     * @param grid Current grid that the block belongs to.
     * @param value Value of the block (2, 4, 8, etc).
     * @param xPos Position x of the block on the grid.
     * @param yPos Position y of the block on the grid.
     */
    public Block(Grid grid, int value, int xPos, int yPos) {
        g = grid;
        number = value;
        x = xPos;
        y = yPos;
    }
}