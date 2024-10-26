package Chess;
import java.util.*;
import Chess.*;

public class test {
    private Board brd;
    private List<Piece> wPieces;
    private List<Piece> bPieces;
    private List<Piece> whitePieces;
    private List<Piece> blackPieces;
    private LinkedList<Square> legalMoves;
    private final LinkedList<Square> squares;
    private King bk;
    private King wk;
    private HashMap<Square,List<Piece>> wMoves;
    private HashMap<Piece, List<Square>> bPieceMoves;
    private HashMap<Piece, List<Square>> wPieceMoves;
    private HashMap<Piece, List<Square>> legalMoves2;
    private HashMap<Square,List<Piece>> bMoves;
    private HashMap<Square,List<Piece>> wAttacks;
    private HashMap<Square,List<Piece>> bAttacks;

    public test(Board brd, List<Piece> wPieces, List<Piece> bPieces, King wk, King bk) {
        this.brd = brd;
        this.wPieces = wPieces;
        this.bPieces = bPieces;
        this.bk = bk;
        this.wk = wk;

        // Initialize other fields
        whitePieces = new LinkedList<Piece>();
        blackPieces = new LinkedList<Piece>();
        legalMoves = new LinkedList<Square>();
        wMoves = new HashMap<Square,List<Piece>>();
        bMoves = new HashMap<Square,List<Piece>>();
        wAttacks = new HashMap<Square,List<Piece>>();
        bAttacks = new HashMap<Square,List<Piece>>();
        bPieceMoves = new HashMap<Piece,List<Square>>();
        wPieceMoves = new HashMap<Piece,List<Square>>();
        legalMoves2 = new HashMap<Piece,List<Square>>();

        Square[][] board = brd.getSquareArray();

        // add all squares to squares list and as hashmap keys
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[y][x].isOccupied()){
                    Piece p = board[y][x].getPiece();
                    if (p.getColor() == 0) {
                        whitePieces.add(p);
                        if (p.getName().equals("king"))
                            this.wk = (King)p;
                    }
                    else {
                        blackPieces.add(p);
                        if (p.getName().equals("king"))
                            this.bk = (King)p;
                    }

                }

                squares.add(board[y][x]);
                wMoves.put(board[y][x], new LinkedList<Piece>());
                bMoves.put(board[y][x], new LinkedList<Piece>());
                wAttacks.put(board[y][x], new LinkedList<Piece>());
                bAttacks.put(board[y][x], new LinkedList<Piece>());
            }
        }

        getWhiteMovesTest();
        getBlackMovesTest();

    }

    public void getWhiteMovesTest() {
        for (Piece p : whitePieces) {
            List<Square> pMoves = p.getMoves(brd);
            for (Square s : pMoves) {
                wMoves.get(s).add(p);
            }
        }

    }

    public void getBlackMovesTest() {
        for (Piece p : blackPieces) {
            List<Square> pMoves = p.getMoves(brd);
            for (Square s : pMoves) {
                bMoves.get(s).add(p);
            }
        }
    }

    public boolean whiteInCheckTest() {
        Square s = wk.getSquare();
        if (bMoves.get(s).isEmpty()) {
            //legalMoves.addAll(wMoves.keySet());
            return false;
        } else return true;
    }

    public boolean blackInCheckTest() {
        Square s = bk.getSquare();
        if (wMoves.get(s).isEmpty()) {
            //legalMoves.addAll(bMoves.keySet());
            return false;
        } else {System.out.println("white moves");return true;}
    }

}
