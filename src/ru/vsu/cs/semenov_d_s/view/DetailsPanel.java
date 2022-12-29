package ru.vsu.cs.semenov_d_s.view;

import ru.vsu.cs.semenov_d_s.game.controller.Player;
import ru.vsu.cs.semenov_d_s.game.model.Cell;
import ru.vsu.cs.semenov_d_s.game.model.pieces.Alliance;
import ru.vsu.cs.semenov_d_s.game.model.pieces.Piece;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class DetailsPanel extends JPanel {

    private static final Dimension SIZE = new Dimension(800, 70);

    private JPanel currentPlayerPanel;
    private JPanel selectedPiecePanel;
    private JPanel timerPanel;

    public DetailsPanel(GUIView view) {
        super(new BorderLayout());
        setBorder(new EtchedBorder(EtchedBorder.RAISED));
        currentPlayerPanel = new JPanel(new BorderLayout());
        selectedPiecePanel = new JPanel(new BorderLayout());
        timerPanel = new JPanel(new BorderLayout());
        selectedPiecePanel.setPreferredSize(new Dimension((int) (SIZE.getWidth() / 3), (int) SIZE.getHeight()));
        currentPlayerPanel.setPreferredSize(new Dimension((int) (SIZE.getWidth() / 3), (int) SIZE.getHeight()));
        timerPanel.setPreferredSize(new Dimension((int) (SIZE.getWidth() / 3), (int) SIZE.getHeight()));
        add(currentPlayerPanel, BorderLayout.CENTER);
        add(selectedPiecePanel, BorderLayout.WEST);
        add(timerPanel, BorderLayout.EAST);
        showCurrentPlayer(view);
        showSelectedPiece(view);
        showTimer();
        validate();
    }

    @Override
    public Dimension getPreferredSize() {
        return SIZE;
    }

    public void update(GUIView view) {
        selectedPiecePanel.removeAll();
        currentPlayerPanel.removeAll();
        timerPanel.removeAll();
        showCurrentPlayer(view);
        showSelectedPiece(view);
        showTimer();
        validate();
        repaint();
    }

    public void showCurrentPlayer(GUIView view) {
        JLabel currentPlayerLabel = new JLabel();
        String text = "";
        Alliance alliance = view.getController().getCurrentPlayer().getAlliance();
        text += (alliance == Alliance.WHITE) ? "White" : "Black";
        text += "'s turn!";
        currentPlayerLabel.setText(text);
        currentPlayerPanel.add(currentPlayerLabel, BorderLayout.CENTER);
    }

    public void showSelectedPiece(GUIView view) {
        Cell selectedCell = view.getStartCell();
        if (selectedCell == null) {
            selectedCell = view.getSelectedCell();
        }
        if (selectedCell != null) {
            Piece selectedPiece = selectedCell.getPiece();
            JPanel selectedPieceDetails = new JPanel(new GridLayout(4, 1));
            selectedPieceDetails.add(new JLabel("Details of selected piece:"));
            selectedPieceDetails.add(new JLabel("Type: " + selectedPiece.getType().fullString()));
            selectedPieceDetails.add(new JLabel("Position: " + selectedPiece.getPosition()));
            selectedPieceDetails.add(new JLabel("Kills: " + selectedPiece.getKills()));
            selectedPiecePanel.add(selectedPieceDetails, BorderLayout.CENTER);
        } else {
            selectedPiecePanel.add(new JLabel("No piece is selected."), BorderLayout.CENTER);
        }
    }

    public void showTimer() {
        JLabel timerLabel = new JLabel();
        timerLabel.setText("No timer is used.");
        timerPanel.add(timerLabel, BorderLayout.CENTER);
    }

    public void showWinner(Player player) {
        currentPlayerPanel.removeAll();
        JLabel currentPlayerLabel = new JLabel();
        String text = "";
        Alliance alliance = player.getAlliance();
        text += (alliance == Alliance.WHITE) ? "White" : "Black";
        text += " has won!";
        currentPlayerLabel.setText(text);
        currentPlayerPanel.add(currentPlayerLabel, BorderLayout.CENTER);
        validate();
        repaint();
    }

}
