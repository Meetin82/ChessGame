package ru.vsu.cs.semenov_d_s.game.controller;

import ru.vsu.cs.semenov_d_s.game.model.Board;
import ru.vsu.cs.semenov_d_s.game.model.pieces.*;
import ru.vsu.cs.semenov_d_s.view.GUIView;
import ru.vsu.cs.semenov_d_s.view.View;

import java.util.Scanner;

public class Game {

    private Player[] players = new Player[2];
    private Board board;
    private int current;
    private View view;

    public Game(Board board) {
        this.board = board;
        players[0] = new Player(this, Alliance.WHITE);
        players[1] = new Player(this, Alliance.BLACK);
        current = 0;
        view = new GUIView(this);
    }

    public Game() {
        this(new Board());
    }

    public Player getBlack() {
        return players[1];
    }
    public Player getWhite() {
        return players[0];
    }
    public Board getBoard() {
        return board;
    }

    public boolean gameOver() {
        return isStaleMate() || isCheckMate();
    }

    public boolean isStaleMate() {
        return getWhite().isStalemate() || getBlack().isStalemate();
    }

    public boolean isCheckMate() {
        return getWhite().isCheckmate() || getBlack().isCheckmate();
    }

    public void start() {
        view.start();
    }

    public boolean readBoolean(String prompt, String yes, String no) {
        String answer = null;
        do {
            System.out.print(prompt);
            Scanner in = new Scanner(System.in);
            answer = in.hasNextLine() ? in.nextLine() : null;
        } while (answer == null || (!answer.equals(yes) && !answer.equals(no)));
        return answer.equals(yes);
    }

    public void reset() {
        current = 0;
        board.reset();
    }

    public void play() {
        update();
        while (!gameOver()) {
            getCurrentPlayer().doMove();
            changePlayer();
            update();
        }
        printResult();
    }

    public void update() {
        System.out.println("\nCurrent Game Situation: \n\n" + board.toString() + "\n");
    }

    public void printResult() {
        if (isCheckMate()) {
            System.out.println("There is a winner!");
        } else {
            System.out.println("Draw. There is no winner!");
        }
    }

    public void changePlayer() {
        current = ++current % 2;
    }

    public Player getCurrentPlayer() {
        return players[current];
    }

}
