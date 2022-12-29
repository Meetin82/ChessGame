package ru.vsu.cs.semenov_d_s.game.model.pieces;

import ru.vsu.cs.semenov_d_s.game.model.Board;

import java.util.Collection;

public class Move {

    private int start;
    private Piece piece;
    private int destination;
    private Piece attackedPiece;
    private MoveResult status;
    private Board board;
    private boolean checkMateMove;
    private boolean checkedMove;

    public Move(Board board, Piece piece, int destination, Piece attackedPiece) {
        this.piece = piece;
        this.start = piece.getIndex();
        this.destination = destination;
        this.attackedPiece = attackedPiece;
        this.status = MoveResult.UNTRIED;
        this.board = board;
        this.checkedMove = false;
        this.checkMateMove = false;
    }

    public Move(Board board, Piece piece, Piece attackedPiece) {
        this(board, piece, attackedPiece.getIndex(), attackedPiece);
    }

    public Move(Board board, Piece piece, int destination) {
        this(board, piece, destination, null);
    }

    public void execute() {
        tryExecute();
        checkMateMove = board.isCheckmate(piece.getAlliance().getOpposite());
        checkedMove = board.isChecked(piece.getAlliance().getOpposite());
    }

    public void tryExecute() {
        if (isPawnJump()) {
            board.setEnPassantPawn((Pawn) piece);
        } else {
            board.setEnPassantPawn(null);
        }
        board.getCell(getStart()).release();
        attack();
        board.getCell(destination).occupy(piece);
        if (isKingSideCastlingMove()) {
            Piece rook = board.getCell(getStart() + 3).release();
            board.getCell(getStart() + 1).occupy(rook);
        } else if (isQueenSideCastlingMove()) {
            Piece rook = board.getCell(getStart() - 4).release();
            board.getCell(getStart() - 1).occupy(rook);
        }
        piece.setFirstMove(false);
    }

    private void attack() {
        if (isAttack()) {
            board.getCell(attackedPiece.getIndex()).release();
            attackedPiece.kill();
            piece.incrementKills();
        }
    }

    public int getStart() {
        return start;
    }

    public int getDestination() {
        return destination;
    }

    public Piece getAttackedPiece() {
        return attackedPiece;
    }

    public Piece getPiece() {
        return piece;
    }

    public MoveResult getStatus() {
        return status;
    }

    public boolean isAttack() {
        return attackedPiece != null;
    }

    public boolean isCastlingMove() {
        return isKingSideCastlingMove() || isQueenSideCastlingMove();
    }

    public boolean isQueenSideCastlingMove() {
        if (getStart() - 2 == destination && piece instanceof King) {
            Piece rook = board.getCell(getStart() - 4).getPiece();
            if (rook instanceof Rook) {
                return true;
            }
        }
        return false;
    }

    public boolean isKingSideCastlingMove() {
        if (getStart() + 2 == destination && piece instanceof King) {
            Piece rook = board.getCell(getStart() + 3).getPiece();
            if (rook instanceof Rook) {
                return true;
            }
        }
        return false;
    }

    public boolean isPawnJump() {
        return ((piece.getAlliance().getDirection() * 16 + getStart()) == destination) && (piece instanceof Pawn);
    }

    public boolean isEnPassant() {
        return board.getEnPassantPawn() != null
                && board.getEnPassantPawn().equals(attackedPiece) && board.getEnPassantPawn().getAlliance() != piece.getAlliance();
    }

    public MoveResult tryOut() {
        status = MoveResult.TRYING;
        Board boardCopy = board.deepCopy();
        Move moveCopy;
        if (isAttack()) {
            moveCopy = new Move(boardCopy, boardCopy.getCell(getStart()).getPiece(), boardCopy.getCell(getDestination()).getPiece());
        } else {
            moveCopy = new Move(boardCopy, boardCopy.getCell(getStart()).getPiece(), getDestination());
        }
        if (!moveCopy.isLegal()) {
            status = MoveResult.IMPOSSIBLE;
            return status;
        }
        moveCopy.tryExecute();
        Alliance alliance = moveCopy.getPiece().getAlliance();
        King king = (King) ((alliance == Alliance.WHITE) ? boardCopy.getWhitePieces()[4] : boardCopy.getBlackPieces()[4]);
        Collection<Move> attacksOnKing = king.getAttacksOnPiece(boardCopy.legalMoves((alliance == Alliance.WHITE) ? boardCopy.getBlackPieces() : boardCopy.getWhitePieces()));
        if (!attacksOnKing.isEmpty()) {
            status = MoveResult.PLAYER_CHECKED;
            return status;
        }
        status = MoveResult.POSSIBLE;
        return status;
    }

    public boolean isLegal() {
        for (Move legalMove : getPiece().getLegalMoves(board)) {
            if (legalMove.getDestination() == getDestination()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String res = "";
        int a = 'a';
        res += piece.toString().substring(1);
        res += (isAttack() && piece instanceof Pawn) ? Character.toString((char) (a + Board.getColumn(start))) : "";
        res += (isAttack()) ? "x" : "";
        res += Character.toString((char) (a + Board.getColumn(destination)));
        res += Board.getRow(destination) + 1;
        if (checkMateMove) {
            res += "#";
        } else if (checkedMove) {
            res += "+";
        }
        return res;
    }

    public enum MoveResult {

        POSSIBLE(), IMPOSSIBLE(), PLAYER_CHECKED(), UNTRIED(), TRYING();

        MoveResult() {

        }

    }

}
