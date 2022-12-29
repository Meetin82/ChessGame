package ru.vsu.cs.semenov_d_s.view;

import ru.vsu.cs.semenov_d_s.game.model.Board;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardPanel extends JPanel {

    private static final Dimension SIZE = new Dimension(400, 400);

    private List<CellPanel> cellPanels;

    public BoardPanel(GUIView view) {
        super(new GridLayout(Board.ROW_COUNT, Board.COLUMN_COUNT));
        cellPanels = new ArrayList<>();
        for (int i = 0; i < Board.CELL_COUNT; i++) {
            CellPanel cellPanel = new CellPanel(view, i);
            cellPanels.add(cellPanel);
            add(cellPanel);
        }
        setBorder(new EtchedBorder(EtchedBorder.RAISED));
        validate();
    }

    @Override
    public Dimension getPreferredSize() {
        return SIZE;
    }

    public void drawBoard(GUIView view) {
        removeAll();
        for (CellPanel cellPanel : cellPanels) {
            cellPanel.drawCell(view);
            add(cellPanel);
        }
        validate();
        repaint();
    }

    public void flipBoard() {
        Collections.reverse(cellPanels);
    }

}
