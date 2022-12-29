package ru.vsu.cs.semenov_d_s.game.model;

import ru.vsu.cs.semenov_d_s.game.model.pieces.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Board {

    public static final int ROW_COUNT = 8;
    public static final int COLUMN_COUNT = 8;
    public static final int CELL_COUNT = ROW_COUNT * COLUMN_COUNT;

    private Cell[] cells;
    private Piece[] blackPieces;
    private Piece[] whitePieces;
    private Pawn enPassantPawn;

    public Board() {
        this.cells = new Cell[CELL_COUNT];
        this.blackPieces = new Piece[16];
        this.whitePieces = new Piece[16];
        reset();
    }

    public static int getIndex(int row, int column) {
        return column + row * COLUMN_COUNT;
    }

    public static int getRow(int index) {
        return index / COLUMN_COUNT;
    }

    public static int getColumn(int index) {
        return index % COLUMN_COUNT;
    }

    public static boolean isRow(int n) {
        return 0 <= n && n < ROW_COUNT;
    }

    public static boolean isColumn(int n) {
        return 0 <= n && n < COLUMN_COUNT;
    }

    public static boolean isCell(int i) {
        return 0 <= i && i < CELL_COUNT;
    }

    public static boolean isCell(int row, int column) {
        return isCell(getIndex(row, column));
    }

    public static boolean isNthRow(int n, int index) {
        if (!isCell(index) || !isRow(n)) {
            return false;
        }
        return (index / Board.COLUMN_COUNT) == n;
    }

    public static boolean isNthColumn(int n, int index) {
        if (!isCell(index) || !isColumn(n)) {
            return false;
        }
        return (index % Board.COLUMN_COUNT) == n;
    }

    public Board deepCopy() {
        Board copy = new Board();
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            copy.setCell(i, cell.hardCopy(copy));
        }
        for (int i = 0; i < blackPieces.length; i++) {
            Piece piece = blackPieces[i];
            copy.setWhitePiece(i, piece.hardCopy(copy));
        }
        for (int i = 0; i < whitePieces.length; i++) {
            Piece piece = whitePieces[i];
            copy.setBlackPiece(i, piece.hardCopy(copy));
        }
        if (enPassantPawn != null) {
            copy.setEnPassantPawn((Pawn) enPassantPawn.hardCopy(copy));
        }
        return copy;
    }

    public Piece[] getBlackPieces() {
        return blackPieces;
    }

    public Piece[] getWhitePieces() {
        return whitePieces;
    }

    public List<Piece> getBlackDeadPieces() {
        List<Piece> deadPieces = new ArrayList<>();
        for (int i = 0; i < blackPieces.length; i++) {
            if (!blackPieces[i].isAlive()) {
                deadPieces.add(blackPieces[i]);
            }
        }
        return deadPieces;
    }

    public List<Piece> getWhiteDeadPieces() {
        List<Piece> deadPieces = new ArrayList<>();
        for (int i = 0; i < whitePieces.length; i++) {
            if (!whitePieces[i].isAlive()) {
                deadPieces.add(whitePieces[i]);
            }
        }
        return deadPieces;
    }

    public Collection<Move> getAttacksOnCell(int index, Collection<Move> moves) {
        List<Move> attacks = new ArrayList<>();
        for (Move move : moves) {
            if (index == move.getDestination()) {
                attacks.add(move);
            }
        }
        return attacks;
    }

    public Collection<Move> getCastlingMoves(King king) {
        Alliance alliance = king.getAlliance();
        if (isChecked(alliance) || !king.isFirstMove()) {
            return Collections.emptyList();
        }
        List<Move> kingCastles = new ArrayList<>();
        int kingIndex = king.getIndex();
        if (!this.getCell(kingIndex + 1).isOccupied() && !this.getCell(kingIndex + 2).isOccupied()) {
            Cell rookCell = this.getCell(kingIndex + 3);
            if (rookCell.isOccupied() && rookCell.getPiece().isFirstMove()) {
                if (getAttacksOnCell(kingIndex + 1, legalAttackMoves(alliance.getOpposite())).isEmpty() && getAttacksOnCell(kingIndex + 2, legalAttackMoves(alliance.getOpposite())).isEmpty()) {
                    if (rookCell.getPiece() instanceof Rook) {
                        kingCastles.add(new Move(this, king, kingIndex + 2));
                    }
                }
            }
        }
        if (!this.getCell(kingIndex - 1).isOccupied() && !this.getCell(kingIndex - 2).isOccupied() && !this.getCell(kingIndex - 3).isOccupied()) {
            Cell rookCell = this.getCell(kingIndex - 4);
            if (rookCell.isOccupied() && rookCell.getPiece().isFirstMove()) {
                if (getAttacksOnCell(kingIndex - 2, legalAttackMoves(alliance.getOpposite())).isEmpty() && getAttacksOnCell(kingIndex - 1, legalAttackMoves(alliance.getOpposite())).isEmpty()) {
                    if (rookCell.getPiece() instanceof Rook) {
                        kingCastles.add(new Move(this, king, kingIndex - 2));
                    }
                }
            }
        }
        return kingCastles;
    }

    public Collection<Move> legalMoves(Piece[] pieces) {
        List<Move> moves = new ArrayList<>();
        for (Piece piece : pieces) {
            moves.addAll(piece.getLegalMoves(this));
        }
        return moves;
    }

    public Collection<Move> legalMoves(Alliance alliance) {
        if (alliance == Alliance.WHITE) {
            return legalMoves(whitePieces);
        }
        return legalMoves(blackPieces);
    }

    public Collection<Move> legalAttackMoves(Piece[] pieces) {
        List<Move> moves = new ArrayList<>();
        for (Piece piece : pieces) {
            moves.addAll(piece.getLegalAttackMoves(this));
        }
        return moves;
    }

    public Collection<Move> legalAttackMoves(Alliance alliance) {
        if (alliance == Alliance.WHITE) {
            return legalAttackMoves(whitePieces);
        }
        return legalAttackMoves(blackPieces);
    }

    public boolean isChecked(Alliance alliance) {
        return !getKing(alliance).getAttacksOnPiece(legalAttackMoves(alliance.getOpposite())).isEmpty();
    }

    public King getKing(Alliance alliance) {
        return (King) getPieces(alliance)[4];
    }

    public Piece[] getPieces(Alliance alliance) {
        return (alliance == Alliance.WHITE) ? whitePieces : blackPieces;
    }

    public boolean isCheckmate(Alliance alliance) {
        return isChecked(alliance) && !hasEscapeMoves(alliance);
    }

    public boolean hasEscapeMoves(Alliance alliance) {
        for (Move move : legalMoves((alliance == Alliance.WHITE) ? whitePieces : blackPieces)) {
            Move.MoveResult result = (move.getStatus() == Move.MoveResult.UNTRIED) ? move.tryOut() : move.getStatus();
            if (result == Move.MoveResult.POSSIBLE) {
                return true;
            }
        }
        return false;
    }

    public boolean isStalemate(Alliance alliance) {
        return !isChecked(alliance) && legalMoves(alliance).isEmpty();
    }

    public void initializePieces() {
        blackPieces[0] = new Rook(this, 0, Alliance.BLACK);
        blackPieces[1] = new Knight(this, 1, Alliance.BLACK);
        blackPieces[2] = new Bishop(this, 2, Alliance.BLACK);
        blackPieces[3] = new Queen(this, 3, Alliance.BLACK);
        blackPieces[4] = new King(this, 4, Alliance.BLACK);
        blackPieces[5] = new Bishop(this, 5, Alliance.BLACK);
        blackPieces[6] = new Knight(this, 6, Alliance.BLACK);
        blackPieces[7] = new Rook(this, 7, Alliance.BLACK);
        blackPieces[8] = new Pawn(this, 8, Alliance.BLACK);
        blackPieces[9] = new Pawn(this, 9, Alliance.BLACK);
        blackPieces[10] = new Pawn(this, 10, Alliance.BLACK);
        blackPieces[11] = new Pawn(this, 11, Alliance.BLACK);
        blackPieces[12] = new Pawn(this, 12, Alliance.BLACK);
        blackPieces[13] = new Pawn(this, 13, Alliance.BLACK);
        blackPieces[14] = new Pawn(this, 14, Alliance.BLACK);
        blackPieces[15] = new Pawn(this, 15, Alliance.BLACK);

        whitePieces[8] = new Pawn(this, 48, Alliance.WHITE);
        whitePieces[9] = new Pawn(this, 49, Alliance.WHITE);
        whitePieces[10] = new Pawn(this, 50, Alliance.WHITE);
        whitePieces[11] = new Pawn(this, 51, Alliance.WHITE);
        whitePieces[12] = new Pawn(this, 52, Alliance.WHITE);
        whitePieces[13] = new Pawn(this, 53, Alliance.WHITE);
        whitePieces[14] = new Pawn(this, 54, Alliance.WHITE);
        whitePieces[15] = new Pawn(this, 55, Alliance.WHITE);
        whitePieces[0] = new Rook(this, 56, Alliance.WHITE);
        whitePieces[1] = new Knight(this, 57, Alliance.WHITE);
        whitePieces[2] = new Bishop(this, 58, Alliance.WHITE);
        whitePieces[3] = new Queen(this, 59, Alliance.WHITE);
        whitePieces[4] = new King(this, 60, Alliance.WHITE);
        whitePieces[5] = new Bishop(this, 61, Alliance.WHITE);
        whitePieces[6] = new Knight(this, 62, Alliance.WHITE);
        whitePieces[7] = new Rook(this, 63, Alliance.WHITE);
    }

    public Cell getCell(int i) {
        return cells[i];
    }

    public Cell getCell(int row, int column) {
        return getCell(getIndex(row, column));
    }

    public void setCell(int i, Cell cell) {
        this.cells[i] = cell;
    }

    public void setWhitePiece(int i, Piece piece) {
        this.blackPieces[i] = piece;
    }

    public void setBlackPiece(int i, Piece piece) {
        this.whitePieces[i] = piece;
    }

    public boolean isEmptyCell(int i) {
        return !getCell(i).isOccupied();
    }

    public boolean isEmptyCell(int row, int column) {
        return isEmptyCell(getIndex(row, column));
    }

    public Cell[] getCells() {
        return cells;
    }

    public Pawn getEnPassantPawn() {
        return enPassantPawn;
    }

    public void setEnPassantPawn(Pawn enPassantPawn) {
        this.enPassantPawn = enPassantPawn;
    }

    public String toString() {
        String res = " +----+----+----+----+----+----+----+----+\n";
        for (int y = 0; y < ROW_COUNT; y++) {
            for (int x = 0; x < COLUMN_COUNT; x++) {
                res += " | " + getCell(y, x).toString();
            }
            res += " |\n";
            res += " +----+----+----+----+----+----+----+----+\n";
        }
        return res;
    }

    public void reset() {
        for (int y = 0; y < ROW_COUNT; y++) {
            for (int x = 0; x < COLUMN_COUNT; x++) {
                cells[x + y * ROW_COUNT] = new Cell(this, y, x);
            }
        }
        this.enPassantPawn = null;
        initializePieces();
    }

}
