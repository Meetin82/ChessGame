package ru.vsu.cs.semenov_d_s.game.model.pieces;

import ru.vsu.cs.semenov_d_s.game.model.Board;
import ru.vsu.cs.semenov_d_s.game.model.Cell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Piece {

    private static final int[] DELTAS = new int[]{-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(Board board, int row, int column, Alliance alliance) {
        super(board, Type.KNIGHT, row, column, alliance);
    }

    public Knight(Board board, int index, Alliance alliance) {
        super(board, Type.KNIGHT, index, alliance);
    }

    public Knight(Board board, int index, Alliance alliance, int kills, boolean alive, boolean firstMove) {
        super(board, Type.KNIGHT, index, alliance, kills, alive, firstMove);
    }

    public Collection<Move> getLegalMoves(Board board) {
        int coordinate;
        List<Move> moves = new ArrayList<>();
        if (!isAlive()) {
            return moves;
        }
        for (int delta : DELTAS) {
            coordinate = this.getIndex() + delta;
            if (super.isLegalMove(this.getIndex(), coordinate)) {
                if (isFirstColumn(this.getIndex(), delta) || isSecondColumn(this.getIndex(), delta) || isSeventhColumn(this.getIndex(), delta) || isEightColumn(this.getIndex(), delta)) {
                    continue;
                }
                Cell cell = board.getCell(coordinate);
                if (!cell.isOccupied()) {
                    moves.add(new Move(board, this, coordinate));
                } else {
                    Piece piece = cell.getPiece();
                    Alliance alliance = piece.getAlliance();
                    if (this.alliance != alliance) {
                        moves.add(new Move(board, this, coordinate, piece));
                    }
                }
            }
        }
        return moves;
    }

    @Override
    public int getStrength() {
        return 3;
    }

    private boolean isFirstColumn(int position, int delta) {
        return Board.isNthColumn(0, position) && (delta == -17 || delta == -10 || delta == 6 || delta == 15);
    }

    private boolean isSecondColumn(int position, int delta) {
        return Board.isNthColumn(1, position) && (delta == -10 || delta == 6);
    }

    private boolean isSeventhColumn(int position, int delta) {
        return Board.isNthColumn(6, position) && (delta == -6 || delta == 10);
    }

    private boolean isEightColumn(int position, int delta) {
        return Board.isNthColumn(7, position) && (delta == -15 || delta == -6 || delta == 10 || delta == 17);
    }

    public Piece hardCopy(Board boardCopy) {
        return new Knight(boardCopy, getIndex(), alliance, kills, alive, firstMove);
    }

}
