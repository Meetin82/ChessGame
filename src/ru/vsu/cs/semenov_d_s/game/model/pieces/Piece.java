package ru.vsu.cs.semenov_d_s.game.model.pieces;

import ru.vsu.cs.semenov_d_s.game.model.Board;
import ru.vsu.cs.semenov_d_s.game.model.Cell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Piece {

    private static final String IMAGE_DIR = "res/nice/";
    private static final String IMAGE_TYPE = ".png";

    protected int kills;
    protected boolean alive;
    protected Alliance alliance;
    protected int index;
    protected boolean firstMove;
    protected Type type;

    public Piece(Board board, Type type, int row, int column, Alliance alliance) {
        this.kills = 0;
        this.alive = true;
        this.type = type;
        this.alliance = alliance;
        this.firstMove = true;
        board.getCells()[Board.getIndex(row, column)].occupy(this);
    }

    public Piece(Board board, Type type, int index, Alliance alliance) {
        this(board, type, Board.getRow(index), Board.getColumn(index), alliance);
    }

    public Piece(Board board, Type type, int row, int column, Alliance alliance, int kills, boolean alive, boolean firstMove) {
        this.kills = kills;
        this.alive = alive;
        this.type = type;
        this.alliance = alliance;
        this.firstMove = firstMove;
        if (alive) {
            board.getCells()[Board.getIndex(row, column)].occupy(this);
        }
    }

    public Piece(Board board, Type type, int index, Alliance alliance, int kills, boolean alive, boolean firstMove) {
        this(board, type, Board.getRow(index), Board.getColumn(index), alliance, kills, alive, firstMove);
    }

    public abstract Piece hardCopy(Board boardCopy);

    public boolean isLegalMove(int fromColumn, int fromRow, int toColumn, int toRow) {
        if (toColumn == fromColumn && toRow == fromRow) {
            return false;
        }
        if (toColumn < 0 || toColumn >= Board.COLUMN_COUNT || fromColumn < 0 || fromColumn >= Board.COLUMN_COUNT || toRow < 0 || toRow >= Board.ROW_COUNT || fromRow < 0 || fromRow >= Board.ROW_COUNT) {
            return false;
        }
        return true;
    }

    public boolean isLegalMove(Cell fromCell, Cell toCell) {
        return isLegalMove(fromCell.getColumn(), fromCell.getRow(), toCell.getColumn(), toCell.getRow());
    }

    public boolean isLegalMove(int fromIndex, int toIndex) {
        if (fromIndex == toIndex) {
            return false;
        }
        if (!Board.isCell(fromIndex) || !Board.isCell(toIndex)) {
            return false;
        }
        return true;
    }

    public Collection<Move> getLegalAttackMoves(Board board) {
        return getLegalMoves(board);
    }

    public abstract Collection<Move> getLegalMoves(Board board);

    public Collection<Move> getAttacksOnPiece(Collection<Move> moves) {
        List<Move> attacks = new ArrayList<>();
        for (Move move : moves) {
            if (index == move.getDestination()) {
                attacks.add(move);
            }
        }
        return attacks;
    }

    public Collection<Move> getAttacks(Board board) {
        List<Move> attacks = new ArrayList<>();
        for (Move move : getLegalMoves(board)) {
            if (move.isAttack()) {
                attacks.add(move);
            }
        }
        return attacks;
    }

    public void kill() {
        this.alive = false;
    }

    public void incrementKills() {
        this.kills++;
    }

    public int getKills() {
        return kills;
    }
    public void setKills(int kills) {
        this.kills = kills;
    }
    public boolean isAlive() {
        return alive;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public int getIndex() {
        return index;
    }
    public Type getType() {
        return type;
    }
    public boolean isKing() {
        return getType() == Type.KING;
    }
    public Alliance getAlliance() {
        return alliance;
    }
    public boolean isFirstMove() {
        return firstMove;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }

    public abstract int getStrength();

    public String getPosition() {
        String res = "";
        res += Character.toString((char) ('a' + Board.getColumn(index)));
        res += Board.getRow(index) + 1;
        return res;
    }

    public enum Type {

        BISHOP() {
            public String toString() {
                return "B";
            }
            public String fullString() {
                return "Bishop";
            }
        },
        KING() {
            public String toString() {
                return "K";
            }
            public String fullString() {
                return "King";
            }
        },
        KNIGHT() {
            public String toString() {
                return "N";
            }
            public String fullString() {
                return "Knight";
            }
        },
        PAWN() {
            public String toString() {
                return "";
            }
            public String fullString() {
                return "Pawn";
            }
        },
        QUEEN() {
            public String toString() {
                return "Q";
            }
            public String fullString() {
                return "Queen";
            }
        },
        ROOK() {
            public String toString() {
                return "R";
            }
            public String fullString() {
                return "Rook";
            }
        },
        UNKNOWN() {
            public String toString() {
                return "-";
            }
            public String fullString() {
                return "Unknown";
            }
        };

        Type() {

        }

        public abstract String fullString();

    }

    public String toString() {
        return alliance.toString() + type.toString();
    }

    public String getImagePath() {
        return IMAGE_DIR + toString() + IMAGE_TYPE;
    }

}
