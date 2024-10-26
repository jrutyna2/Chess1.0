package Chess;
import java.util.*;
import Chess.*;

public class Square {

    private Board b;
    private Piece piece;
    private int rank;
    private int file;

    public Square(Board b, int rank, int file) {
        this.b = b;
        this.rank = rank;
        this.file = file;
    }

    public Piece getPiece() {
        return piece;
    }

    public String getPieceName() {
        return piece.getName();
    }

    public int getPieceColor() {
        return this.piece.getColor();
    }

    public boolean isOccupied() {
        return (this.piece != null);
    }

    public int getRank() {
        return this.rank;
    }

    public int getFile() {
        return this.file;
    }

    public void put(Piece p) {
        this.piece = p;
        p.setPosition(this);
    }

    public Piece removePiece() {
        Piece p = this.piece;
        this.piece = null;
        return p;
    }

    public void capture(Piece p) {
        Piece k = getPiece();
        if (k.getColor() == 0) b.Wpieces.remove(k);
        if (k.getColor() == 1) b.Bpieces.remove(k);
        this.piece = p;
    }

    public void captureTest(Piece p) {
        Piece k = getPiece();
        this.piece = p;
    }

    public void printSquare() {
        System.out.printf("%c%d ", b.toFile(getFile()), (getRank()+1));
    }

}
