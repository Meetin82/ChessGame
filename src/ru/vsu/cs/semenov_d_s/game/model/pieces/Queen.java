package ru.vsu.cs.semenov_d_s.game.model.pieces;

import ru.vsu.cs.semenov_d_s.game.model.Board;
import ru.vsu.cs.semenov_d_s.game.model.Cell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece {

    private static final int[] DELTAS = new int[]{-9, -8, -7, -1, 1, 7, 8, 9};

    public Queen(Board board, int row, int column, Alliance alliance) {
        super(board, Type.QUEEN, row, column, alliance);
    }

    public Queen(Board board, int index, Alliance alliance) {
        super(board, Type.QUEEN, index, alliance);
    }

    public Queen(Board board, int index, Alliance alliance, int kills, boolean alive, boolean firstMove) {
        super(board, Type.QUEEN, index, alliance, kills, alive, firstMove);
    }

    public Collection<Move> getLegalMoves(Board board) {
        int coordinate;
        List<Move> moves = new ArrayList<>();
        if (!isAlive() || !Board.isCell(getIndex())) {
            return moves;
        }
        for (int delta : DELTAS) {
            coordinate = this.getIndex();
            do {
                if (isFirstColumn(coordinate, delta) || isEightColumn(coordinate, delta)) {
                    break;
                }
                coordinate += delta;
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
                        break;
                    }
                }
            } while (super.isLegalMove(this.getIndex(), coordinate));
        }
        return moves;
    }

    @Override
    public int getStrength() {
        return 9;
    }

    private boolean isFirstColumn(int position, int delta) {
        return Board.isNthColumn(0, position) && (delta == -9 || delta == -1 || delta == 7);
    }

    private boolean isEightColumn(int position, int delta) {
        return Board.isNthColumn(7, position) && (delta == -7 || delta == 1 || delta == 9);
    }

    public Piece hardCopy(Board boardCopy) {
        return new Queen(boardCopy, getIndex(), alliance, kills, alive, firstMove);
    }

}
