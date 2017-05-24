package minesweeper;

import java.util.Random;

public class Minesweeper {
    private final int SIZE = 10;
    public enum CellStatus {UNEXPOSED, EXPOSED, SEALED}
    public enum GameStatus {INPROGRESS, LOST, WON}
    private CellStatus[][] cellStatus = new CellStatus[SIZE][SIZE];
    private boolean[][] mines = new boolean[SIZE][SIZE];
    
    public Minesweeper() {
        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++) {
                cellStatus[i][j] = CellStatus.UNEXPOSED;
            }
    }

    public CellStatus getCellStatus (int row, int column) {
        return cellStatus[row][column];
    }

    public void exposeCell(int row, int column) {
        if(cellStatus[row][column] == CellStatus.UNEXPOSED) {
            cellStatus[row][column] = CellStatus.EXPOSED;

            if(!isAdjacentCell(row, column) && !isMined(row, column))
                exposeNeighbors(row, column);
        }
    }

    public void exposeNeighbors(int row, int column) {
        for(int i = row - 1; i < row + 2; i++)
            for(int j = column - 1; j < column + 2; j++)
                if(i >= 0 && i < SIZE && j >= 0 && j < SIZE && cellStatus[i][j] == CellStatus.UNEXPOSED)
                    exposeCell(i, j);
    }

    public void toggleSeal(int row, int column) {
        switch(cellStatus[row][column]) {
          case UNEXPOSED:
            cellStatus[row][column] = CellStatus.SEALED;
            break;
            
          case SEALED:
            cellStatus[row][column] = CellStatus.UNEXPOSED;
            break;
        }
    }

    public boolean isAdjacentCell(int row, int column) {
        if(isMined(row, column))
            return false;

        for(int i = row - 1; i < row + 2; i++) {
            for(int j = column - 1; j < column + 2; j++) {
                if(i >= 0 && i < SIZE && j >= 0 && j < SIZE && isMined(i, j)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isMined(int row, int column) {
        return mines[row][column];
    }

    public void setMine(int row, int column) {
        mines[row][column] = true;
    }

    public GameStatus getGameStatus() {
        int sealedMines = 0;
        boolean mineExploded = false;
        int exposedCells = 0;

        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                if(isMined(i, j) && getCellStatus(i, j) == CellStatus.SEALED)
                    sealedMines++;
                if(isMined(i, j) && getCellStatus(i, j) == CellStatus.EXPOSED)
                    mineExploded = true;
                if(!isMined(i, j) && getCellStatus(i, j) == CellStatus.EXPOSED)
                    exposedCells++;
            }
        }

        if(mineExploded)
            return GameStatus.LOST;
        else if(sealedMines == 10 && exposedCells == 90)
            return GameStatus.WON;
        else
            return GameStatus.INPROGRESS;
    }

    public void placeMines() {
        Random randomNumberGenerator = new Random();
        for(int i = 0; i < 10; i++) {
            int randomRow = randomNumberGenerator.nextInt(10);
            int randomCol = randomNumberGenerator.nextInt(10);
            if(!isMined(randomRow, randomCol))
                setMine(randomRow, randomCol);
            else
                i--;
        }
    }

    public int computeNumOfMinesAroundAdjacentCell(int row, int column) {
        int temp = 0;

        if(isAdjacentCell(row, column)){
            for(int i = row - 1; i < row + 2; i++) {
                for(int j = column - 1; j < column + 2; j++) {
                    if(i >= 0 && i < SIZE && j >= 0 && j < SIZE && isMined(i, j)) {
                        temp++;
                    }
                }
            }
        }
        return temp;
    }
}