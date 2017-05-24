package minesweeper;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertFalse;

import minesweeper.Minesweeper.CellStatus;
import minesweeper.Minesweeper.GameStatus;

public class MinesweeperTest {

    Minesweeper minesweeper;

    @Before
    public void BeforeEach() {
        minesweeper = new Minesweeper();
    }

    @Test
    public void Canary() {
        assertTrue(true);
    }

    @Test
    public void exposeACell() {
        minesweeper.exposeCell(1, 1);

        assertEquals(CellStatus.EXPOSED, minesweeper.getCellStatus(1, 1));
    }

    @Test
    public void exposedACellThatIsAlreadyExposed() {
        minesweeper.exposeCell(2, 4);
        minesweeper.exposeCell(2, 4);

        assertEquals(CellStatus.EXPOSED, minesweeper.getCellStatus(2, 4));
    }

    @Test
    public void exposeACellOutOfRowBounds() {
        try {
            minesweeper.exposeCell(99, 1);
            fail("Expected exception for stepping out of bounds");
        } catch(ArrayIndexOutOfBoundsException ex) {}

    }

    @Test
    public void exposeACellOutOfColumnBounds() {
        try {
            minesweeper.exposeCell(1, 99);
            fail("Expected exception for stepping out of bounds");
        } catch(ArrayIndexOutOfBoundsException ex) {}
    }
    
    class MinesweeperWithExposeNeighborsStubbed extends Minesweeper {
        public boolean exposeNeighborsCalled;
      
        public void exposeNeighbors(int row, int column) {
            exposeNeighborsCalled = true;
        }
    }  
    
    @Test
    public void exposingACellCallsExposeNeighbors() {
        MinesweeperWithExposeNeighborsStubbed minesweeper = new MinesweeperWithExposeNeighborsStubbed();
      
        minesweeper.exposeCell(2, 4);

        assertTrue(minesweeper.exposeNeighborsCalled);
    }

    @Test
    public void exposingAnExposedCellDoesNotCallExposeNeighbors() {
        MinesweeperWithExposeNeighborsStubbed minesweeper = new MinesweeperWithExposeNeighborsStubbed();
      
        minesweeper.exposeCell(2, 4);
        minesweeper.exposeNeighborsCalled = false;
        minesweeper.exposeCell(2, 4);
      
        assertFalse(minesweeper.exposeNeighborsCalled);
    }

    class MinesweeperWithExposeCellStubbed extends Minesweeper {
        public List<Integer> rows = new ArrayList<>();
        public List<Integer> columns = new ArrayList<>();

        public void exposeCell(int row, int column) {
            rows.add(row);
            columns.add(column);
        }
    }

    @Test
    public void exposeNeighborsCallsExposeCellForNineCells() {
        MinesweeperWithExposeCellStubbed minesweeper = new MinesweeperWithExposeCellStubbed();

        minesweeper.exposeNeighbors(3, 5);

        assertEquals(Arrays.asList(2, 2, 2, 3, 3, 3, 4, 4, 4), minesweeper.rows);
        assertEquals(Arrays.asList(4, 5, 6, 4, 5, 6, 4, 5, 6), minesweeper.columns);
    }

    @Test
    public void exposeNeighborsCalledOnTopLeftCell() {
        MinesweeperWithExposeCellStubbed minesweeper = new MinesweeperWithExposeCellStubbed();

        minesweeper.exposeNeighbors(0, 0);

        assertEquals(Arrays.asList(0, 0, 1, 1), minesweeper.rows);
        assertEquals(Arrays.asList(0, 1, 0, 1), minesweeper.columns);
    }

    @Test
    public void exposeNeighborsCalledOnTopRightCell() {
        MinesweeperWithExposeCellStubbed minesweeper = new MinesweeperWithExposeCellStubbed();

        minesweeper.exposeNeighbors(0, 9);

        assertEquals(Arrays.asList(0, 0, 1, 1), minesweeper.rows);
        assertEquals(Arrays.asList(8, 9, 8, 9), minesweeper.columns);
    }

    @Test
    public void exposeNeighborsCalledOnBottomLeftCell() {
        MinesweeperWithExposeCellStubbed minesweeper = new MinesweeperWithExposeCellStubbed();

        minesweeper.exposeNeighbors(9, 0);

        assertEquals(Arrays.asList(8, 8, 9, 9), minesweeper.rows);
        assertEquals(Arrays.asList(0, 1, 0, 1), minesweeper.columns);
    }

    @Test
    public void exposeNeighborsCalledOnBottomRightCell() {
        MinesweeperWithExposeCellStubbed minesweeper = new MinesweeperWithExposeCellStubbed();

        minesweeper.exposeNeighbors(9, 9);

        assertEquals(Arrays.asList(8, 8, 9, 9), minesweeper.rows);
        assertEquals(Arrays.asList(8, 9, 8, 9), minesweeper.columns);
    }

    @Test
    public void sealACell() {
        minesweeper.toggleSeal(0, 0);

        assertEquals(CellStatus.SEALED, minesweeper.getCellStatus(0, 0));
    }

    @Test
    public void toggleSealedAndUnsealedStatusOfACell() {
        minesweeper.toggleSeal(1, 1);
        minesweeper.toggleSeal(1, 1);

        assertEquals(CellStatus.UNEXPOSED, minesweeper.getCellStatus(1, 1));
    }

    @Test
    public void exposeASealedCellShouldNotExposeCell() {
        minesweeper.toggleSeal(1, 1);
        minesweeper.exposeCell(1, 1);

        assertEquals(CellStatus.SEALED, minesweeper.getCellStatus(1, 1));
    }

    @Test
    public void sealingAnExposedCellShouldNotSealTheCell() {
        minesweeper.exposeCell(1, 1);
        minesweeper.toggleSeal(1, 1);

        assertEquals(CellStatus.EXPOSED, minesweeper.getCellStatus(1, 1));
    }

    @Test
    public void exposeOnASealedCellShouldNotCallExposeNeighbors() {
        MinesweeperWithExposeNeighborsStubbed minesweeper = new MinesweeperWithExposeNeighborsStubbed();

        minesweeper.toggleSeal(1, 1);
        minesweeper.exposeCell(1, 1);

        assertFalse(minesweeper.exposeNeighborsCalled);
    }

    class MinesweeperWithIsAdjacentCellStubbed extends MinesweeperWithExposeNeighborsStubbed {
        public boolean isAdjacentCell(int row, int column) {
            return true;
        }
    }

    @Test
    public void exposeCellShouldNotCallExposeNeighborsOnAnAdjacentCell() {
        MinesweeperWithIsAdjacentCellStubbed minesweeper = new MinesweeperWithIsAdjacentCellStubbed();

        minesweeper.exposeCell(0, 0);
        assertFalse(minesweeper.exposeNeighborsCalled);
    }

    class MinesweeperWithIsMinedStubbed extends MinesweeperWithExposeNeighborsStubbed {
        public boolean isMined(int row, int column) {
            return true;
        }
    }

    @Test
    public void exposeCellShouldNotCallExposeNeighborsOnMinedCell() {
        MinesweeperWithIsMinedStubbed minesweeper = new MinesweeperWithIsMinedStubbed();

        minesweeper.exposeCell(0, 0);
        assertFalse(minesweeper.exposeNeighborsCalled);
    }

    @Test
    public void isAdjacentCellShouldReturnTrueIfAMineIsNearby() {
        minesweeper.setMine(1, 1);

        assertTrue(minesweeper.isAdjacentCell(0, 0));
    }

    @Test
    public void isAdjacentCellShouldReturnFalseIfNoMinesAreNearby() {
        assertFalse(minesweeper.isAdjacentCell(1, 1));
    }

    @Test
    public void isAdjacentCellShouldReturnFalseIfCellIsMined() {
        minesweeper.setMine(1, 1);

        assertFalse(minesweeper.isAdjacentCell(1, 1));
    }

    @Test
    public void getGameStatusShouldReturnInProgressIfNoCellsAreExposedOrSealed() {
        assertEquals(GameStatus.INPROGRESS, minesweeper.getGameStatus());
    }

    @Test
    public void getGameStatusShouldReturnInProgressIfAllMinesAndOneCellAreSealed() {
        for (int i = 0; i < 9; i++) {
            minesweeper.setMine(i, 0);
            minesweeper.toggleSeal(i, 0);
        }

        minesweeper.toggleSeal(1, 0);
        minesweeper.exposeCell(9, 9);

        assertEquals(GameStatus.INPROGRESS, minesweeper.getGameStatus());
    }

    @Test
    public void getGameStatusShouldReturnLostIfAMineIsExposed() {
        minesweeper.setMine(5, 5);
        minesweeper.exposeCell(5, 5);

        assertEquals(GameStatus.LOST, minesweeper.getGameStatus());
    }

    @Test
    public void getGameStatusShouldReturnWonIfAllMinesAreSealedAndAllCellsAreExposed() {
        for (int i = 0; i < 10; i++) {
            minesweeper.setMine(0, i);
            minesweeper.toggleSeal(0, i);
        }

        minesweeper.exposeCell(7, 7);

        assertEquals(GameStatus.WON, minesweeper.getGameStatus());
    }

    @Test
    public void getGameStatusShouldReturnInProgressIfTenMinesAreSealedButNotAllCellsAreExposed() {
        for (int i = 0; i < 10; i++) {
            minesweeper.setMine(0, i);
            minesweeper.toggleSeal(0, i);
        }

        assertEquals(GameStatus.INPROGRESS, minesweeper.getGameStatus());
    }

    @Test
    public void placeMinesShouldPlaceTenMines() {
        int mineCount = 0;
        minesweeper.placeMines();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if(minesweeper.isMined(i, j))
                    mineCount++;
            }
        }

        assertEquals(10, mineCount);
    }

    @Test
    public void placeMinesShouldRandomlyPlaceMines() {
        Minesweeper minesweeper2 = new Minesweeper();
        minesweeper.placeMines();
        minesweeper2.placeMines();
        boolean minesRandomlyPlaced = false;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if(minesweeper.isMined(i, j) && !minesweeper2.isMined(i, j));
                    minesRandomlyPlaced = true;
            }
        }

        assertTrue(minesRandomlyPlaced);
    }
    
    @Test
    public void testsToQuitCoverageForEnumGameStatus() {
      GameStatus.values();
      GameStatus.valueOf("WON");
    }

    @Test
    public void testsToQuitCoverageForEnumCellStatus() {
      CellStatus.values();
      CellStatus.valueOf("SEALED");
    }

    @Test
    public void computeNumOfMinesShouldReturnTwoIfTwoMinesAreNearby() {

        minesweeper.setMine(0,0);
        minesweeper.setMine(0,1);
        assertEquals(2,minesweeper.computeNumOfMinesAroundAdjacentCell(1, 1));
    }

    @Test
    public void computeNumOfMinesShouldReturnZeroIfNoMinesAreNearby() {
        assertEquals(0, minesweeper.computeNumOfMinesAroundAdjacentCell(0, 0));
    }

    @Test
    public void computeNumOfMinesShouldReturnZeroIfCalledOnAMinedCell() {
        minesweeper.setMine(1, 1);

        assertEquals(0, minesweeper.computeNumOfMinesAroundAdjacentCell(1, 1));
    }

    @Test
    public void computeNumOfMinesShouldReturnCorrectValueOnTopLeftCell() {
        minesweeper.setMine(0, 1);
        minesweeper.setMine(1, 0);
        minesweeper.setMine(1, 1);

        assertEquals(3, minesweeper.computeNumOfMinesAroundAdjacentCell(0, 0));
    }

    @Test
    public void computeNumOfMinesShouldReturnCorrectValueOnBottomRightCell() {
        minesweeper.setMine(8, 8);
        minesweeper.setMine(8, 9);
        minesweeper.setMine(9, 8);

        assertEquals(3, minesweeper.computeNumOfMinesAroundAdjacentCell(9, 9));
    }
}