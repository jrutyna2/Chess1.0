package Chess;
import java.util.*;
import java.lang.Math;
import Chess.*;

public class Board {

    private final Square[][] board;

    public final LinkedList<Piece> Bpieces;
    public final LinkedList<Piece> Wpieces;
    public List<Square> movable;

    private King wk;
    private King bk;

    private Check mate;

    double posInf = Double.POSITIVE_INFINITY;
    double negInf = Double.NEGATIVE_INFINITY;

    private boolean wCheckmated = false;
    private boolean bCheckmated = false;

    // private Move bestMove = null;
    private Move bestMove;

    public Board() {
        board = new Square[8][8];
        Bpieces = new LinkedList<Piece>();
        Wpieces = new LinkedList<Piece>();
        bestMove = new Move(null, null);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                board[x][y] = new Square(this, x, y);
                this.board[x][y] = board[x][y];
            }
        }

    }

    public void wCM(Boolean v) {
        this.wCheckmated = v;
    }

    public void bCM(Boolean v) {
        this.bCheckmated = v;
    }

    public Move getBestMove() {
        return this.bestMove;
    }

    public void initializeBoard() {

        wk = new King(0, board[0][4], "king", 0);
        bk = new King(1, board[7][4], "king", 0);

        for (int x = 0; x < 8; x++) {
            board[1][x].put(new Pawn(0, board[1][x], "pawn", 1));
            board[6][x].put(new Pawn(1, board[6][x], "pawn", -1));
        }
        board[7][3].put(new Queen(1, board[7][3], "queen", -5));
        board[0][3].put(new Queen(0, board[0][3], "queen", 5));

        board[0][4].put(wk);
        board[7][4].put(bk);

        board[0][0].put(new Rook(0, board[0][0], "rook", 4));
        board[0][7].put(new Rook(0, board[0][7], "rook", 4));
        board[7][0].put(new Rook(1, board[7][0], "rook", -4));
        board[7][7].put(new Rook(1, board[7][7], "rook", -4));

        board[0][1].put(new Knight(0, board[0][1], "knight", 3));
        board[0][6].put(new Knight(0, board[0][6], "knight", 3));
        board[7][1].put(new Knight(1, board[7][1], "knight", -3));
        board[7][6].put(new Knight(1, board[7][6], "knight", -3));

        board[0][2].put(new Bishop(0, board[0][2], "bishop", 3));
        board[0][5].put(new Bishop(0, board[0][5], "bishop", 3));
        board[7][2].put(new Bishop(1, board[7][2], "bishop", -3));
        board[7][5].put(new Bishop(1, board[7][5], "bishop", -3));

        Wpieces.add(board[0][2].getPiece());
        Wpieces.add(board[1][4].getPiece());
        Wpieces.add(board[1][3].getPiece());
        Wpieces.add(board[0][6].getPiece());
        Wpieces.add(board[0][5].getPiece());
        Wpieces.add(board[0][1].getPiece());
        Wpieces.add(board[0][3].getPiece());
        Wpieces.add(board[1][2].getPiece());
        Wpieces.add(board[1][0].getPiece());
        Wpieces.add(board[1][7].getPiece());
        Wpieces.add(board[1][1].getPiece());
        Wpieces.add(board[0][0].getPiece());
        Wpieces.add(board[0][4].getPiece());
        Wpieces.add(board[0][7].getPiece());
        Wpieces.add(board[1][5].getPiece());
        Wpieces.add(board[1][6].getPiece());

        Bpieces.add(board[7][2].getPiece());
        Bpieces.add(board[6][4].getPiece());
        Bpieces.add(board[6][3].getPiece());
        Bpieces.add(board[7][6].getPiece());
        Bpieces.add(board[7][5].getPiece());
        Bpieces.add(board[7][1].getPiece());
        Bpieces.add(board[7][3].getPiece());
        Bpieces.add(board[6][2].getPiece());
        Bpieces.add(board[6][0].getPiece());
        Bpieces.add(board[6][7].getPiece());
        Bpieces.add(board[6][1].getPiece());
        Bpieces.add(board[7][0].getPiece());
        Bpieces.add(board[7][4].getPiece());
        Bpieces.add(board[7][7].getPiece());
        Bpieces.add(board[6][5].getPiece());
        Bpieces.add(board[6][6].getPiece());

        mate = new Check(this, Wpieces, Bpieces, wk, bk);
    }

    public Square[][] getSquareArray() {
        return this.board;
    }

    public Boolean isInBounds(int row, int col) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8)
            return true;
        return false;
    }

    public void checkPromotion() {
        for (int x = 0; x < 8; x++) {
            //check for white promotion
            if (board[0][x].isOccupied()) {
                if(board[0][x].getPieceColor() == 1 && board[0][x].getPieceName().equals("pawn")) {
                    board[0][x].put(new Queen(0, board[0][x], "queen", 5));
                    Wpieces.remove(board[1][x].getPiece());
                }
            }
            //check for black promotion
            if (board[7][x].isOccupied()) {
                if(board[7][x].getPieceColor() == 0 && board[7][x].getPieceName().equals("pawn")) {
                    board[7][x].put(new Queen(1, board[7][x], "queen", -5));
                    Bpieces.remove(board[7][x].getPiece());
                }
            }
        }
    }

    public void printBoard() {
        String files = "ABCDEFGH";

        for (int col = 7; col >= 0; col--) {
            System.out.printf(col+1+"\t");

            for (int row = 0; row < 8; row++) {

                if (board[col][row].isOccupied()) {
                    String name = board[col][row].getPieceName();
                    if (board[col][row].getPieceColor() == 0)
                        System.out.printf("w");
                    else
                        System.out.printf("b");
                    if (name.equals("pawn"))
                        System.out.printf("P  ");
                    else if (name.equals("queen"))
                        System.out.printf("Q  ");
                    else if (name.equals("king"))
                        System.out.printf("K  ");
                    else if (name.equals("rook"))
                        System.out.printf("R  ");
                    else if (name.equals("knight"))
                        System.out.printf("N  ");
                    else if (name.equals("bishop"))
                        System.out.printf("B  ");
                }
                else
                    System.out.printf(".   ");
            }
            System.out.printf("\n");
            if (col == 0) {
              System.out.printf("\n\t");
              for (int k = 0; k < 8; k++)
                  System.out.printf(files.charAt(k) + "   ");
            }
        }
        System.out.printf("\n");
    }

    public int castle(int color, int side, Square s, Square fin) {

        Piece k = s.getPiece();

        if (color == 0) {
            //queenside white
            if (side == 0) {
                Piece r = board[0][0].getPiece();
                if (k.move(fin) && r.move(board[0][3])) return 1;
            }
            //kingside white
            if (side == 1) {
                Piece r = board[0][7].getPiece();
                if (k.move(fin) && r.move(board[0][5])) return 1;
            }
        }
        if (color == 1) {
            //queenside black
            if (side == 0) {
                Piece r = board[7][0].getPiece();
                if (k.move(fin) && r.move(board[7][3])) return 1;
            }
            //kingside black
            if (side == 1) {
                Piece r = board[7][7].getPiece();
                if (k.move(fin) && r.move(board[7][5])) return 1;
            }
        }
        return 0;
    }

    public double minimax(Square[][] brd, int depth, double alpha, double beta, boolean color, int maxDepth) {

        Check boardEval = new Check(this, Wpieces, Bpieces, wk, bk);
        List<Move> moves = boardEval.getLegalMoves(!color);
        boolean hasmvd = false;
//Sept 28 2024
        System.out.printf("\nLegal Moves:\n");
        for (Move m : moves) {
            m.printMove();
        }
        System.out.printf("\n");
        //return board evaluation at bottom depth
        if (depth == 0 || moves.isEmpty())
            return boardEval();

        //minimax eval for white pieces
        if (!color) {
            double maxEval = negInf;
            for (Move m : moves) {
                Square s1 = m.getSquare1();
                Square s2 = m.getSquare2();
                Piece p1 = m.getPiece1();
                if (p1 == null) continue;
                Piece p2 = m.getPiece2();
                hasmvd = p1.getHasMoved();
                //save move that captures king
                if (m.containsKing()) {
                    maxEval = posInf;
                    continue;
                }
                //make move
                p1.moveTest(s2);

                // recursive call (test children of move)
                double eval = minimax(brd, depth - 1, alpha, beta,  true, maxDepth);
                if (depth == maxDepth && eval > maxEval)
                    bestMove = m;

                //put piece back
                p1.moveTest(s1);
                if (!hasmvd) p1.setHasMoved(false);
                // if move was a capture, put captured piece back
                if (p2 != null) {
                    s2.put(p2);
                }
                //alpha beta pruning
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        }
        else {
            double minEval = posInf;
            for (Move m : moves) {
                Square s1 = m.getSquare1();
                Square s2 = m.getSquare2();
                Piece p1 = m.getPiece1();
                if (p1 == null) continue;
                Piece p2 = m.getPiece2();
                hasmvd = p1.getHasMoved();
                //save move that captures king
                if (m.containsKing()) {
                  minEval = negInf;
                  continue;
                }
                //make move
                p1.moveTest(s2);


                // recursive call (test children of move)
                double eval = minimax(brd, depth - 1, alpha, beta, false, maxDepth);
                if (depth == maxDepth && eval < minEval)
                    bestMove = m;

                //put piece back
                p1.moveTest(s1);
                if (!hasmvd) p1.setHasMoved(false);

                //if move was a capture, return captured piece
                if (p2 != null) {
                    s2.put(p2);
                }
                //alpha beta pruning
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }
    //count pieces and return board evaluation
    public double boardEval() {
        Square[][] board = getSquareArray();
        double eval = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x][y].isOccupied()) {
                    eval += board[x][y].getPiece().getValue();
                }
            }
        }

        return eval;
    }

    public int cpuMove(Move m, int color) {
        Check checkDetect = new Check(this, Wpieces, Bpieces, wk, bk);
        Square s1 = m.getSquare1();
        Square s2 = m.getSquare2();
        Piece p1 = m.getPiece1();
        Piece p2 = m.getPiece2();

        List<Move> moves = checkDetect.getLegalMoves((color==0));

        if (moves.isEmpty()) {
          // System.out.println("HERE");
            if (wCheckmated) return 2;
            if (bCheckmated) return 3;
            else return 4;
        }

        //check if castling
        if (p1.getName().equals("king")) {
            if (Math.abs(s1.getFile() - s2.getFile()) == 2) {
                if (s2.getRank() == 0 && s2.getFile() == 2)
                    return (castle(0, 0, s1, s2));
                else if (s2.getRank() == 0 && s2.getFile() == 6)
                    return (castle(0, 1, s1, s2));
                else if (s2.getRank() == 7 && s2.getFile() == 2)
                    return (castle(1, 0, s1, s2));
                else if (s2.getRank() == 7 && s2.getFile() == 6)
                    return (castle(1, 1, s1, s2));
            }
        }

        //make move
        if (p1.move(s2)) {
            checkPromotion();
            return 1;
        }

        return 0;
    }

    //attempt to make move from input
    public int playerMove(int from[], int to[], int color) {
        if (!isInBounds(from[0], from[1]) || !isInBounds(to[0], to[1]))
            return 0;
        if (!board[from[0]][from[1]].isOccupied())
            return 0;

        Piece p2 = board[to[0]][to[1]].getPiece();
        Boolean capture = false, valid = false;
        Check checkDetect = new Check(this, Wpieces, Bpieces, wk, bk);

        List<Move> moves = checkDetect.getLegalMoves((color==0));

        if (moves.isEmpty()) {
            if (wCheckmated) return 2;
            if (bCheckmated) return 3;
            else return 4;
        }

        //loop through legal moves
        for (Move m : moves) {
            Square sq1 = m.getSquare1();
            Square sq2 = m.getSquare2();

            //check that move is valid
            if (from[0] == sq1.getRank() && from[1] == sq1.getFile())
                if (to[0] == sq2.getRank() && to[1] == sq2.getFile())
                    valid = true;
        }
        if (!valid) return 0;

        Piece p = board[from[0]][from[1]].getPiece();
        Square s1 = board[from[0]][from[1]];
        Square s2 = board[to[0]][to[1]];

        //check if castling
        if (p.getName().equals("king")) {
            if (Math.abs(s1.getFile() - s2.getFile()) == 2) {
                if (s2.getRank() == 0 && s2.getFile() == 2)
                    return (castle(0, 0, s1, s2));
                else if (s2.getRank() == 0 && s2.getFile() == 6)
                    return (castle(0, 1, s1, s2));
                else if (s2.getRank() == 7 && s2.getFile() == 2)
                    return (castle(1, 0, s1, s2));
                else if (s2.getRank() == 7 && s2.getFile() == 6)
                    return (castle(1, 1, s1, s2));
            }
        }

        //make move
        if (p.move(board[to[0]][to[1]])) {
            //check if mate
            Check mateDetect = new Check(this, Wpieces, Bpieces, wk, bk);
            moves = checkDetect.getLegalMoves((color==1));
            if (mateDetect.whiteCheckMated()) return 2;
            if (mateDetect.blackCheckMated()) return 3;
            checkPromotion();
            return 1;
        }

        return 0;
    }

  public void pieceTotals() {
      int Wsum = 0, Bsum = 0;

      for (Piece p : Wpieces)
          Wsum += p.getValue();
      for (Piece p : Bpieces)
          Bsum += p.getValue();

      System.out.println("White = " + Wsum + "     Black = " + (Bsum*-1));

  }

  public int[] getCoordinates(String input) {

      String files = "ABCDEFGH";
      char file = input.charAt(0);
      int rank = input.charAt(1) - '0' - 1;
      int[] coordinates = new int[]{-1, -1};

      for (int i = 0; i < 8; i++) {
          Character c = files.charAt(i);

          if (c.equals(Character.toUpperCase(file)))
              coordinates[1] = i;
          if (rank == i)
              coordinates[0] = i;
      }

      return coordinates;
  }


  public Character toFile(int col) {
      String files = "abcdefgh";

      for (int i = 0; i < 8; i++) {
          if (i == col)
              return files.charAt(i);
      }

      return ' ';
  }
}
