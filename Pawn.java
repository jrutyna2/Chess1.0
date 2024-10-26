package Chess;
import java.util.*;
import Chess.*;

public class Pawn extends Piece {
    //private boolean hasMoved;

    public Pawn(int color, Square initSq, String name, int value) {
        super(color, initSq, name, value);
    }

    @Override
    public List<Square> getMoves(Board b) {
        LinkedList<Square> moves = new LinkedList<Square>();

        Square[][] board = b.getSquareArray();

        int x = this.getSquare().getFile();
        int y = this.getSquare().getRank();
        int c = this.getColor();

        // System.out.printf("\nPAWN\nrank:%d, file:%d, color:%d\n", y, x, c);

        if (c == 0) {
        //System.out.println("Pawn has moved= " + this.getHasMoved());
            if (!this.getHasMoved()) {
                if (!board[y+2][x].isOccupied() && !board[y+1][x].isOccupied()) {
                    moves.add(board[y+2][x]);
                }
            }

            if (y+1 <= 7) {
                if (!board[y+1][x].isOccupied()) {
                    moves.add(board[y+1][x]);
                }
            }

            if (x+1 <= 7 && y+1 <= 7) {
                if (board[y+1][x+1].isOccupied()) {
                    if (board[y+1][x+1].getPieceColor() != 0) {
                        moves.add(board[y+1][x+1]);
                    }
                }
            }

            if (x-1 >= 0 && y+1 <= 7) {
                if (board[y+1][x-1].isOccupied()) {
                    if (board[y+1][x-1].getPieceColor() != 0) {
                        moves.add(board[y+1][x-1]);
                    }
                }
            }
        }

        if (c == 1) {
            if (!this.getHasMoved()) {
                if (!board[y-2][x].isOccupied() && !board[y-1][x].isOccupied()) {
                    moves.add(board[y-2][x]);
                }
            }

            if (y-1 >= 0) {
                if (!board[y-1][x].isOccupied()) {
                    moves.add(board[y-1][x]);
                }
            }

            if (x-1 >= 0 && y-1 >= 0) {
                if (board[y-1][x-1].isOccupied()) {
                    if (board[y-1][x-1].getPieceColor() != 1) {
                        moves.add(board[y-1][x-1]);
                    }
                }
            }

            if (x+1 <= 7 && y-1 >= 0) {
                if (board[y-1][x+1].isOccupied()) {
                    if (board[y-1][x+1].getPieceColor() != 1) {
                        moves.add(board[y-1][x+1]);
                    }
                }
            }
        }

        return moves;
    }
}
