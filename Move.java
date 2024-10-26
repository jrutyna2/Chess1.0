package Chess;
import java.util.*;
import Chess.*;


public class Move {

    private Square s1;
    private Square s2;

    public Move(Square s1, Square s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    public Square getSquare1() {
        return this.s1;
    }

    public Square getSquare2() {
        return this.s2;
    }

    public Piece getPiece1() {
        if (!s1.isOccupied()) return null;
        return this.s1.getPiece();
    }

    public Piece getPiece2() {
        if (!s2.isOccupied()) return null;
        return this.s2.getPiece();
    }

    public int getRank1() {
        return this.s1.getRank();
    }

    public int getFile1() {
        return this.s1.getFile();
    }

    public int getRank2() {
        return this.s2.getRank();
    }

    public int getFile2() {
        return this.s2.getFile();
    }

    public void printMove() {
      if (s1 != null && s2 != null) {
        this.s1.printSquare();
        this.s2.printSquare();
        System.out.printf("\n");
      }
      else {
        System.out.println("null");
      }
    }

    public boolean containsKing() {
        if (!s2.isOccupied()) return false;
        return getPiece2().getName().equals("king");
    }

}
