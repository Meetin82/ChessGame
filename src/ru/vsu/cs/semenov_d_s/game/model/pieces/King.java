package ru.vsu.cs.semenov_d_s.game.model.pieces;

import ru.vsu.cs.semenov_d_s.game.model.Board;
import ru.vsu.cs.semenov_d_s.game.model.Cell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends Piece {

    private static final int[] DELTAS = new int[]{-9, -8, -7, -1, 1, 7, 8, 9};

    public King(Board board, int row, int column, Alliance alliance) {
        super(board, Type.KING, row, column, alliance);
    }

    public King(Board board, int index, Alliance alliance) {
        super(board, Type.KING, index, alliance);
    }

    public King(Board board, int index, Alliance alliance, int kills, boolean alive, boolean firstMove) {
        super(board, Type.KING, index, alliance, kills, alive, firstMove);
    }

    public Collection<Move> getLegalAttackMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        if (!isAlive()) {
            return moves;
        }
        int coordinate;
        for (int delta : DELTAS) {
            coordinate = this.getIndex() + delta;
            if (isFirstColumn(this.getIndex(), delta) || isEightColumn(this.getIndex(), delta)) {
                continue;
            }
            if (super.isLegalMove(this.getIndex(), coordinate)) {
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
    public Collection<Move> getLegalMoves(Board board) {
        Collection<Move> moves = getLegalAttackMoves(board);
        moves.addAll(board.getCastlingMoves(this));
        return moves;
    }

    @Override
    public int getStrength() {
        return 100;
    }

    private boolean isFirstColumn(int position, int delta) {
        return Board.isNthColumn(0, position) && (delta == -9 || delta == -1 || delta == 7);
    }

    private boolean isEightColumn(int position, int delta) {
        return Board.isNthColumn(7, position) && (delta == -7 || delta == 1 || delta == 9);
    }

    public Piece hardCopy(Board boardCopy) {
        return new King(boardCopy, getIndex(), alliance, kills, alive, firstMove);
    }

}
