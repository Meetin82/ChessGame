package ru.vsu.cs.semenov_d_s.game.controller;

import ru.vsu.cs.semenov_d_s.game.model.pieces.Alliance;
import ru.vsu.cs.semenov_d_s.game.model.pieces.King;
import ru.vsu.cs.semenov_d_s.game.model.pieces.Move;
import ru.vsu.cs.semenov_d_s.game.model.pieces.Piece;

import java.util.Collection;
import java.util.Scanner;

public class Player {

    private static final int PAWNS = 8;
    private static final int BISHOPS = 2;
    private static final int ROOKS = 2;
    private static final int KNIGHTS = 2;

    private Alliance alliance;

    private Game game;

    public Player(Game game, Alliance alliance) {
        this.alliance = alliance;
        this.game = game;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public Player getOpponent() {
        return (getAlliance() == Alliance.WHITE) ? game.getBlack() : game.getWhite();
    }

    public Piece[] getPieces() {
        return (getAlliance() == Alliance.WHITE) ? game.getBoard().getBlackPieces() : game.getBoard().getWhitePieces();
    }

    public Piece getPiece(int index) {
        return getPieces()[index];
    }

    public King getKing() {
        return (King) getPieces()[4];
    }

    public Collection<Move> getLegalMoves() {
        return game.getBoard().legalMoves(alliance);
    }

    public void doMove() {
        boolean finished = false;
        while (!finished) {
            int pieceIndex = readInt("[" + alliance + "]: " + "What piece to move? ");
            Piece piece = getPiece(pieceIndex);
            System.out.println("Piece to move: " + piece);
            if (!piece.isAlive()) {
                System.out.println("Can't move a dead piece!");
                continue;
            }
            System.out.print("Possible moves: ");
            for (Move move : piece.getLegalMoves(game.getBoard())) {
                System.out.print(move.getDestination() + ", ");
            }
            System.out.println();
            int destination = readInt("[" + alliance + "]: " + "Where to move to? ");
            Piece attackedPiece = game.getBoard().getCell(destination).getPiece();
            Move move = new Move(game.getBoard(), piece, destination, attackedPiece);
            Move.MoveResult result = move.tryOut();
            if (result == Move.MoveResult.POSSIBLE) {
                finished = true;
                move.execute();
            }
        }

    }

    public boolean isChecked() {
        return game.getBoard().isChecked(alliance);
    }

    public boolean isCheckmate() {
        return game.getBoard().isCheckmate(alliance);
    }

    public boolean hasEscapeMoves() {
        return game.getBoard().hasEscapeMoves(alliance);
    }

    public boolean isStalemate() {
        return game.getBoard().isStalemate(alliance);
    }

    public boolean isCastled() {
        // TODO
        return false;
    }

    private int readInt(String prompt) {
        int value = 0;
        boolean intRead = false;
        Scanner in = new Scanner(System.in);
        do {
            System.out.print(prompt);
            String line = in.nextLine();
            Scanner scannerLine = new Scanner(line);
            if (scannerLine.hasNextInt()) {
                intRead = true;
                value = scannerLine.nextInt();
            }
            scannerLine.close();
        } while (!intRead);

        return value;
    }

}
