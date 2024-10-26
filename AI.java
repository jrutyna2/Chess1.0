package Chess;
import java.util.*;
import Chess.*;

public class AI {

    private Board b;
    private final Square[][] board;
    private final boolean color;

    public final LinkedList<Piece> Bpieces;
    public final LinkedList<Piece> Wpieces;
    public List<Square> moves;
    private King wk;
    private King bk;
    private Check mate;

    public AI(Square[][] board, boolean color) {
        this.board = board;
        this.color = color;
        Wpieces = new LinkedList<Piece>();
        Bpieces = new LinkedList<Piece>();
        moves = new LinkedList<Square>();

        getPieces();

        b = new Board(board, Wpieces, Bpieces);
        mate = new Check(b, Wpieces, Bpieces, wk, bk);
        // if (wk != null && bk != null)
        //     mate = new Check(this.b, Wpieces, Bpieces, wk, bk);
    }

    public void getPieces() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x][y].isOccupied()) {
                    if (board[x][y].getPieceColor() == 0) {
                        Wpieces.add(board[x][y].getPiece());
                        if (board[x][y].getPiece().getName().equals("king"))
                            wk = (King)board[x][y].getPiece();
                    }
                    if (board[x][y].getPieceColor() == 1)  {
                        Bpieces.add(board[x][y].getPiece());
                        if (board[x][y].getPiece().getName().equals("king"))
                            bk = (King)board[x][y].getPiece();
                    }
                }
            }
        }
    }

    public LinkedList<Piece> getPieces2(Square[][] board2, boolean color) {
        LinkedList<Piece> pieces = new LinkedList<Piece>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board2[x][y].isOccupied())
                    // if (board2[x][y].getPieceColor() == 0) {
                        pieces.add(board2[x][y].getPiece());
                        // if (board2[x][y].getPiece().getName().equals("king"))
                        //     wk = (King)board2[x][y].getPiece();
            //         }
            //         if (board2[x][y].getPieceColor() == 1)  {
            //             pieces.add(board2[x][y].getPiece());
            //             // if (board2[x][y].getPiece().getName().equals("king"))
            //             //     bk = (King)board2[x][y].getPiece();
            //         }
            }
        }
        return pieces;
    }

    public List<Move> getLegalMoves(boolean color) {
        if (mate == null)
            return null;
        else
            return mate.getLegalMoves(color);
    }

    public int boardEval(Square[][] board2, boolean color) {
        LinkedList<Piece> pieces = getPieces2(board2, color);
        int eval = 0;
        for (Piece p : Wpieces)
            eval += p.getValue();
        for (Piece p : Bpieces)
            eval += p.getValue();

        return eval;
    }

    public Square[][] testBoard(Move m) {
        boolean success = true;
        Square s1 = m.getSquare1();
        Square s2 = m.getSquare2();
        Piece p1 = m.getPiece1();
        Piece p2 = m.getPiece2();

        b.printBoard();
        p1.move(s2);
        b.printBoard();

        if (p1.getColor() == 0 && mate.whiteInCheck()) success = false;
        if (p1.getColor() == 1 && mate.blackInCheck()){System.out.println("blackInCheck = true");success = false;}
        else {System.out.println("blackInCheck = false");}
        // else if (p.getColor() == 1 && newTest.blackInCheckTest()){System.out.println("blackInCheck = true");success = false;}

        // p1.move(s1);
        // b.printBoard();
        // if (p2 != null) s2.put(p2);
        // b.printBoard();

        return board;
    }

}
