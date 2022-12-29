package ru.vsu.cs.semenov_d_s.view;

import ru.vsu.cs.semenov_d_s.game.model.Board;
import ru.vsu.cs.semenov_d_s.game.model.pieces.Alliance;
import ru.vsu.cs.semenov_d_s.game.model.pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class DeadPanel extends JPanel {

    private static final int PIECE_WIDTH = 21;
    private static final int PIECE_HEIGHT = PIECE_WIDTH;
    private static final int PADDING = 5;
    private static final Dimension SIZE = new Dimension(80, 400);

    private JPanel whitePanel;
    private JPanel blackPanel;

    public DeadPanel(GUIView view) {
        super(new BorderLayout());
        setBackground(Color.decode("0xFDF5E6"));
        setBorder(new EtchedBorder(EtchedBorder.RAISED));
        whitePanel = new JPanel(new GridLayout(8, 2));
        blackPanel = new JPanel(new GridLayout(8, 2));
        whitePanel.setOpaque(false);
        blackPanel.setOpaque(false);
        add(whitePanel, BorderLayout.NORTH);
        add(blackPanel, BorderLayout.SOUTH);
        drawAllDeadPieces(view);
    }

    @Override
    public Dimension getPreferredSize() {
        return SIZE;
    }

    public void drawAllDeadPieces(GUIView view) {
        Board board = view.getController().getBoard();
        whitePanel.removeAll();
        blackPanel.removeAll();
        List<Piece> whiteDeadPieces = board.getWhiteDeadPieces();
        List<Piece> blackDeadPieces = board.getBlackDeadPieces();
        whiteDeadPieces.sort(Comparator.comparingInt(Piece::getStrength));
        blackDeadPieces.sort(Comparator.comparingInt(Piece::getStrength));
        drawPieces(whiteDeadPieces);
        drawPieces(blackDeadPieces);
        validate();
        repaint();
    }

    private void drawPieces(List<Piece> pieces) {
        for (Piece piece : pieces) {
            String piecePath = piece.getImagePath();
            try {
                BufferedImage image = ImageIO.read(new File(piecePath));
                Image scaledImage = image.getScaledInstance(PIECE_WIDTH, PIECE_HEIGHT, Image.SCALE_SMOOTH);
                JLabel pieceIcon = new JLabel(new ImageIcon(scaledImage));
                pieceIcon.setPreferredSize(new Dimension(PIECE_WIDTH + PADDING, PIECE_HEIGHT + PADDING));
                if (piece.getAlliance() == Alliance.WHITE) {
                    whitePanel.add(pieceIcon);
                } else {
                    blackPanel.add(pieceIcon);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
