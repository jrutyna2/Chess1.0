package Chess;
import java.util.*;
import Chess.*;

public class Queen extends Piece {

    public Queen(int color, Square initSq, String name, int value) {
        super(color, initSq, name, value);
    }

    @Override
    public List<Square> getMoves(Board b) {
        LinkedList<Square> moves = new LinkedList<Square>();
        Square[][] board = b.getSquareArray();

        int x = this.getSquare().getFile();
        int y = this.getSquare().getRank();

        // System.out.printf("queen= file:%d, rank:%d\n", x, y);

        int[] vertDistances = getVerticalPcDistances(board, x, y);

        for (int i = vertDistances[1]; i <= vertDistances[0]; i++) {
            if (i != y) moves.add(board[i][x]);
        }

        for (int i = vertDistances[2]; i <= vertDistances[3]; i++) {
            if (i != x) moves.add(board[y][i]);
        }

        List<Square> bMoves = getDiagonalPcDistances(board, x, y);

        moves.addAll(bMoves);

        return moves;
    }

}
