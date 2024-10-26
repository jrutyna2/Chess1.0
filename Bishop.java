package Chess;
import java.util.*;
import Chess.*;

public class Bishop extends Piece {

    public Bishop(int color, Square initSq, String name, int value) {
        super(color, initSq, name, value);
    }

    @Override
    public List<Square> getMoves(Board b) {
        Square[][] board = b.getSquareArray();
        int x = this.getSquare().getFile();
        int y = this.getSquare().getRank();

        return getDiagonalPcDistances(board, x, y);
    }
}
