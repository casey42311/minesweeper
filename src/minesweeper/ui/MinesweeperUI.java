package minesweeper.ui;

import minesweeper.Minesweeper;
import minesweeper.Minesweeper.CellStatus;
import minesweeper.Minesweeper.GameStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MinesweeperUI extends JFrame {
    private static final int SIZE = 10;
    Minesweeper minesweeper;

    @Override
    protected void frameInit() {
        super.frameInit();
        minesweeper = new Minesweeper();
        minesweeper.placeMines();
        setLayout(new GridLayout(SIZE, SIZE));
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                MinesweeperCell cell = new MinesweeperCell(i, j);
                getContentPane().add(cell);

                cell.addMouseListener(new MouseClickedHandler());
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new MinesweeperUI();
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    private class MouseClickedHandler implements MouseListener {

        public void mousePressed(MouseEvent event) {
        }

        public void mouseReleased(MouseEvent event) {
        }

        public void mouseClicked(MouseEvent event) {
            MinesweeperCell cell = (MinesweeperCell) event.getSource();

            if(SwingUtilities.isRightMouseButton(event)) {
                minesweeper.toggleSeal(cell.row, cell.column);
                CellStatus cellStatus = minesweeper.getCellStatus(cell.row, cell.column);
                if(cellStatus == CellStatus.SEALED)
                    cell.setText("S");
                else
                    cell.setText("");

                checkGameStatus(cell);
            }
            else if(SwingUtilities.isLeftMouseButton(event)) {
                CellStatus cellStatus = minesweeper.getCellStatus(cell.row, cell.column);
                if(cellStatus == CellStatus.UNEXPOSED) {
                    minesweeper.exposeCell(cell.row, cell.column);
                }
                exposeCell(cell);
                checkGameStatus(cell);
            }
        }


        public void mouseEntered(MouseEvent event) {
        }

        public void mouseExited(MouseEvent event) {
        }
    }

    public void checkGameStatus(MinesweeperCell cell) {
        if(minesweeper.getGameStatus() == GameStatus.WON)
            JOptionPane.showMessageDialog(cell, "Congratulations, you won!");
        else if(minesweeper.getGameStatus() == GameStatus.LOST)
            JOptionPane.showMessageDialog(cell, "BOOM!");
    }

    public void exposeCell(MinesweeperCell cell) {
        if(minesweeper.getCellStatus(cell.row, cell.column) == CellStatus.EXPOSED) {
            if(minesweeper.isMined(cell.row, cell.column)) {
                cell.setBackground(Color.red);
                cell.setText("X");
            }
            else if(minesweeper.isAdjacentCell(cell.row, cell.column)) {
                int adjacency = minesweeper.computeNumOfMinesAroundAdjacentCell(cell.row, cell.column);
                cell.setBackground(Color.lightGray);
                cell.setText(Integer.toString(adjacency));
            }
            else {
                cell.setBackground(Color.lightGray);
            }
        }
    }
}
