package ru.vsu.cs.semenov_d_s.game.model.pieces;

import ru.vsu.cs.semenov_d_s.game.model.Board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    private static final int[] DELTAS = new int[]{7, 8, 9, 16};

    public Pawn(Board board, int row, int column, Alliance alliance) {
        super(board, Type.PAWN, row, column, alliance);
    }

    public Pawn(Board board, int index, Alliance alliance) {
        super(board, Type.PAWN, index, alliance);
    }

    public Pawn(Board board, int index, Alliance alliance, int kills, boolean alive, boolean firstMove) {
        super(board, Type.PAWN, index, alliance, kills, alive, firstMove);
    }

    public Collection<Move> getLegalMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        if (!isAlive()) {
            return moves;
        }
        int coordinate;
        for (int delta : DELTAS) {
            coordinate = this.getIndex() + (this.alliance.getDirection() * delta);
            if (!super.isLegalMove(this.getIndex(), coordinate)) {
                continue;
            }
            if (delta == 8 && !board.getCell(coordinate).isOccupied()) {
                moves.add(new Move(board, this, coordinate));
            } else if (delta == 16 && this.isFirstMove()) {
                int firstCoordinate = this.getIndex() + (this.alliance.getDirection() * 8);
                if (!board.getCell(firstCoordinate).isOccupied() && !board.getCell(coordinate).isOccupied()) {
                    moves.add(new Move(board, this, coordinate));
                }
            } else if (delta == 7 && !((Board.isNthColumn(0, coordinate) && this.alliance == Alliance.WHITE) || (Board.isNthColumn(Board.COLUMN_COUNT - 1, coordinate) && this.alliance == Alliance.BLACK))) {
                moves.addAll(getAttacks(board, coordinate));
            } else if (delta == 9 && !((Board.isNthColumn(0, coordinate) && this.alliance == Alliance.BLACK) || (Board.isNthColumn(Board.COLUMN_COUNT - 1, coordinate) && this.alliance == Alliance.WHITE))) {
                moves.addAll(getAttacks(board, coordinate));
            }
        }
        return moves;
    }

    @Override
    public int getStrength() {
        return 1;
    }

    private List<Move> getEnpassantMoves(Board board, int coordinate) {
        List<Move> enpassantMoves = new ArrayList<>();
        Move move1 = new Move(board, this, coordinate, board.getCell(getIndex() - 1).getPiece());
        if (move1.isEnPassant()) {
            enpassantMoves.add(move1);
        }
        Move move2 = new Move(board, this, coordinate, board.getCell(getIndex() + 1).getPiece());
        if (move2.isEnPassant()) {
            enpassantMoves.add(move2);
        }
        return enpassantMoves;
    }

    private List<Move> getAttacks(Board board, int coordinate) {
        List<Move> moves = new ArrayList<>();
        if (board.getCell(coordinate).isOccupied()) {
            Piece piece = board.getCell(coordinate).getPiece();
            Alliance alliance = piece.getAlliance();
            if (this.alliance != alliance) {
                moves.add(new Move(board, this, coordinate, piece));
            }
        } else {
            moves.addAll(getEnpassantMoves(board, coordinate));
        }
        return moves;
    }

    public Piece hardCopy(Board boardCopy) {
        return new Pawn(boardCopy, getIndex(), alliance, kills, alive, firstMove);
    }

}
