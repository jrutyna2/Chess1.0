package Chess;
import java.util.*;
import Chess.*;

public class King extends Piece {
    // private boolean hasCastled;
    // private boolean wCastleKS;
    // private boolean wCastleQS;
    // private boolean bCastleKS;
    // private boolean bCastleQS;

    public King(int color, Square initSq, String name, int value) {
        super(color, initSq, name, value);
    }

    @Override
    public List<Square> getMoves(Board b) {
        LinkedList<Square> moves = new LinkedList<Square>();

        Square[][] board = b.getSquareArray();

        int x = this.getSquare().getFile();
        int y = this.getSquare().getRank();
        int c = this.getColor();
        int i, j;
        int x_moves[] = {-1, -1, -1, 0, 1, 1, 1, 0};
        int y_moves[] = {-1, 0, +1, +1, 1, 0, -1, -1};

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
        // System.out.println("king color:"+this.getColor());
        // System.out.println("queen color:"+board[0][3].getPiece().getColor());

        // for (Square s : moves)
        //     System.out.println("*king " + s.getRank() + " "+s.getFile());

        // for (int i = 1; i > -2; i--) {
        //     for (int k = 1; k > -2; k--) {
        //         if(!(i == 0 && k == 0)) {
        //             try {
        //                 if(!board[y + k][x + i].isOccupied() ||
        //                         board[y + k][x + i].getPiece().getColor()
        //                         != this.getColor()) {
        //                     moves.add(board[y + k][x + i]);
        //                 }
        //             } catch (ArrayIndexOutOfBoundsException e) {
        //                 continue;
        //             }
        //         }
        //     }
        // }

        // System.out.println("in King: this.getColor = " + c);

        List<Square> castlingMoves = getCastlingRights(board, c);
        moves.addAll(castlingMoves);

        return moves;
    }

}
