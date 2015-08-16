import static org.junit.Assert.*;

import org.junit.Test;


public class GridTest {

	@Test
	public void testMovement() {
		Grid g = new Grid();
		g.place(new Block(g, 2, 0, 0), 0, 0);
		g.place(new Block(g, 2, 0, 1), 0, 1);
		g.place(new Block(g, 2, 0, 2), 0, 2);
		g.place(new Block(g, 2, 0, 3), 0, 3);
		
		/*Grid
		 * 2 X X X
		 * 2 X X X
		 * 2 X X X
		 * 2 X X X
		 */
		
		g.moveBlocksUp();
		assertEquals(4, g.blockAt(0, 3).number);
		assertEquals(4, g.blockAt(0, 2).number);
		assertNull(g.blockAt(0, 1));
		assertNull(g.blockAt(0, 0));
		
		/*Grid
		 * 4 X X X
		 * 4 X X X
		 * X X X X
		 * X X X X
		 */
		
		g.place(new Block(g, 4, 0, 1), 0, 1);
		
		/*Grid
		 * 4 X X X
		 * 4 X X X
		 * 4 X X X
		 * X X X X
		 */
		g.moveBlocksUp();
		assertEquals(8, g.blockAt(0, 3).number);
		assertEquals(4, g.blockAt(0, 2).number);
		assertNull(g.blockAt(0, 1));
		assertNull(g.blockAt(0, 0));
		
		/*Grid
		 * 8 X X X
		 * 4 X X X
		 * X X X X
		 * X X X X
		 */
		
		g.place(new Block(g, 4, 0, 1), 0, 1);
		
		/*Grid
		 * 8 X X X
		 * 4 X X X
		 * 4 X X X
		 * X X X X
		 */
		
		g.moveBlocksUp();
		assertEquals(8, g.blockAt(0, 3).number);
		assertEquals(8, g.blockAt(0, 2).number);
		assertNull(g.blockAt(0, 1));
		
		/*Grid
		 * 8 X X X
		 * 8 X X X
		 * X X X X
		 * X X X X
		 */
		
		g.moveBlocksUp();
		
		/*Grid
		 * 16 X X X
		 * X  X X X
		 * X  X X X
		 * X  X X X
		 */
		
		assertEquals(16, g.blockAt(0, 3).number);
		assertNull(g.blockAt(0, 2));
		assertNull(g.blockAt(0, 1));
		assertNull(g.blockAt(0, 0));
		
		g.moveBlocksUp();
		
		/*Grid (Still the same grid)
		 * 16 X X X
		 * X  X X X
		 * X  X X X
		 * X  X X X
		 */
		
		assertEquals(16, g.blockAt(0, 3).number);
		assertNull(g.blockAt(0, 2));
		assertNull(g.blockAt(0, 1));
		assertNull(g.blockAt(0, 0));
	}

}
