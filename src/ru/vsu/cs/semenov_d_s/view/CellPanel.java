package ru.vsu.cs.semenov_d_s.view;

import ru.vsu.cs.semenov_d_s.game.model.Board;
import ru.vsu.cs.semenov_d_s.game.model.Cell;
import ru.vsu.cs.semenov_d_s.game.model.pieces.Move;
import ru.vsu.cs.semenov_d_s.game.model.pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class CellPanel extends JPanel {

    private static final Color LIGHT = Color.decode("#FFFACD");
    private static final Color DARK = Color.decode("#593E1A");
    private static final Dimension SIZE = new Dimension(50, 50);

    private int index;

    public CellPanel(GUIView view, int index) {
        super(new GridBagLayout());
        this.index = index;
        assignColor();
        assignPiece(view);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Cell cell = view.getController().getBoard().getCell(index);
                if (cell.isOccupied()) {
                    view.setSelectedCell(cell);
                    SwingUtilities.invokeLater(view::redraw);
                    validate();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Cell cell = view.getController().getBoard().getCell(index);
                if (cell.isOccupied()) {
                    view.setSelectedCell(null);
                    SwingUtilities.invokeLater(view::redraw);
                    validate();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Cell startCell = view.getStartCell();
                Board board = view.getController().getBoard();
                if (!view.getController().gameOver()) {
                    Cell cell = board.getCell(index);
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (startCell == null) {
                            boolean occupied = cell.isOccupied();
                            boolean isTurn = (occupied) && view.getController().getCurrentPlayer().getAlliance() == cell.getPiece().getAlliance();
                            startCell = (occupied && isTurn) ? cell : null;
                        } else {
                            Piece attackedPiece = cell.getPiece();
                            Piece piece = startCell.getPiece();
                            if (attackedPiece == null || attackedPiece.getAlliance() != piece.getAlliance()) {
                                Move move = new Move(board, piece, cell.getIndex(), attackedPiece);
                                Move.MoveResult result = move.tryOut();
                                if (result == Move.MoveResult.POSSIBLE) {
                                    move.execute();
                                    view.getController().changePlayer();
                                    view.getMoveLog().add(move);
                                }
                            }
                            startCell = null;
                        }
                        SwingUtilities.invokeLater(view::redraw);
                    }
                    view.setStartCell(startCell);
                }
            }
        });
        validate();
    }

    private void assignPiece(GUIView view) {
        Board board = view.getController().getBoard();
        removeAll();
        if (board.getCell(index).isOccupied()) {
            Piece piece = board.getCell(index).getPiece();
            String piecePath = piece.getImagePath();
            try {
                BufferedImage image = ImageIO.read(new File(piecePath));
                add(new JLabel(new ImageIcon(image.getScaledInstance(36, 36, Image.SCALE_SMOOTH))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void assignColor() {
        for (int i = 0; i < Board.ROW_COUNT; i++) {
            if (Board.isNthRow(i, index)) {
                if ((i & 1) == 0) {
                    setBackground(index % 2 == 0 ? LIGHT : DARK);
                } else {
                    setBackground(index % 2 == 0 ? DARK : LIGHT);
                }
            }
        }
    }

    public void drawCell(GUIView view) {
        assignColor();
        highlightLegalMoves(view);
        highlightSelectedPiece(view);
        if (!view.getMoveLog().isEmpty()) {
            highlightMove(view.getMoveLog().get(view.getMoveLog().size() - 1));
        }
        assignPiece(view);
        validate();
        repaint();
    }

    public void highlightLegalMoves(GUIView view) {
        if (view.shouldHighlightLegalMoves()) {
            for (Move move : currentLegalMoves(view)) {
                if (move.getDestination() == index) {
                    setBackground(Color.GREEN);
                }
            }
        }
    }

    public void highlightSelectedPiece(GUIView view) {
        if (view.getStartCell() != null) {
            if (view.getStartCell().getIndex() == index) {
                setBackground(Color.YELLOW);
            }
        }
    }

    public void highlightMove(Move move) {
        if (move.getStart() == index || move.getDestination() == index) {
            setBackground(Color.ORANGE);
        }
    }

    private Collection<Move> currentLegalMoves(GUIView view) {
        Piece currentPiece = (view.getStartCell() != null) ? view.getStartCell().getPiece() : null;
        if (currentPiece != null && currentPiece.getAlliance() == view.getController().getCurrentPlayer().getAlliance()) {
            return currentPiece.getLegalMoves(view.getController().getBoard());
        }
        return Collections.emptyList();
    }

    @Override
    public Dimension getPreferredSize() {
        return SIZE;
    }

}
