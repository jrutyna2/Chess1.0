package Chess;
import java.util.*;
import Chess.*;

public class Knight extends Piece {

    public Knight(int color, Square initSq, String name, int value) {
        super(color, initSq, name, value);
    }

    @Override
    public List<Square> getMoves(Board b) {
        LinkedList<Square> moves = new LinkedList<Square>();
        Square[][] board = b.getSquareArray();

        int x = this.getSquare().getFile(); //x=6
        int y = this.getSquare().getRank(); //y=0
        int i, j;
        int x_moves[] = {-2, -2, -1, 1, 2, 2, 1, -1};
        int y_moves[] = {1, -1, -2, -2, -1, 1, 2, 2};

        //knight moves
        for (int k = 0; k < 8; k++) {
            i = x+x_moves[k];
            j = y+y_moves[k];
            if (b.isInBounds(i, j)) {
                if (board[j][i].isOccupied()) {
                    if (board[j][i].getPiece().getColor() == this.getColor())
                        continue;
                    else {
                      moves.add(board[j][i]);
                    }
                }
                else {
                    moves.add(board[j][i]);
                }
            }
        }

        // for (int i = 2; i > -3; i--) {
        //     for (int k = 2; k > -3; k--) {
        //         if(Math.abs(i) == 2 ^ Math.abs(k) == 2) {
        //             if (k != 0 && i != 0) {
        //                 try {
        //                     moves.add(board[y + k][x + i]);
        //                 } catch (ArrayIndexOutOfBoundsException e) {
        //                     continue;
        //                 }
        //             }
        //         }
        //     }
        // }

        return moves;
    }

}
