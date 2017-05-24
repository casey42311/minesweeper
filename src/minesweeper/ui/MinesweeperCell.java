package minesweeper.ui;

import javax.swing.*;

public class MinesweeperCell extends JButton {
    public final int row;
    public final int column;

    public MinesweeperCell(int theRow, int theColumn) {
        row = theRow;
        column = theColumn;
        setSize(5, 5);
    }
}

