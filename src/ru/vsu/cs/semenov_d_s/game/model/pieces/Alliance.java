package ru.vsu.cs.semenov_d_s.game.model.pieces;

public enum Alliance {

    WHITE(), BLACK();

    Alliance() {

    }

    public int getDirection() {
        if (this.equals(WHITE)) {
            return -1;
        } else {
            return 1;
        }
    }

    public Alliance getOpposite() {
        if (this.equals(WHITE)) {
            return BLACK;
        } else {
            return WHITE;
        }
    }

    public String toString() {
        if (this.equals(WHITE)) {
            return "W";
        } else {
            return "B";
        }
    }

}
