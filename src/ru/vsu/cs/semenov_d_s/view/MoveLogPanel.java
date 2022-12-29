package ru.vsu.cs.semenov_d_s.view;

import ru.vsu.cs.semenov_d_s.game.model.pieces.Alliance;
import ru.vsu.cs.semenov_d_s.game.model.pieces.Move;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MoveLogPanel extends JPanel {

    private static final Dimension SIZE = new Dimension(100, 400);

    private DefaultTableModel model;
    private JScrollPane scrollPane;

    public MoveLogPanel(List<Move> moves) {
        super(new BorderLayout());
        model = new DefaultTableModel(new String[]{"White", "Black"}, 32);
        JTable table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setDefaultEditor(Object.class, null);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setFocusable(false);
        scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(SIZE);
        add(scrollPane, BorderLayout.CENTER);
        showMoveLog(moves);
    }

    @Override
    public Dimension getPreferredSize() {
        return SIZE;
    }

    public void showMoveLog(List<Move> moves) {
        int currentRow = 0;
        clear();
        for (Move move : moves) {
            String moveDesc = move.toString();
            if (currentRow == model.getRowCount()) {
                model.setRowCount(model.getRowCount() + 1);
            }
            if (move.getPiece().getAlliance() == Alliance.WHITE) {
                model.setValueAt(moveDesc, currentRow, 0);
            } else {
                model.setValueAt(moveDesc, currentRow, 1);
                currentRow++;
            }
        }
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }

    public void clear() {
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            model.setValueAt("", i, 0);
            model.setValueAt("", i, 1);
        }
    }

}
