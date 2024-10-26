package Chess;
import java.util.*;
import Chess.*;

public abstract class Piece {
    private final int color;
    private Square currentSquare;
    private String name;
    private final int value;
    private boolean hasMoved;

    // private boolean wCastleKS;
    // private boolean wCastleQS;
    // private boolean bCastleKS;
    // private boolean bCastleQS;

    public Piece(int color, Square initSq, String name, int value) {
        this.color = color;
        this.currentSquare = initSq;
        this.name = name;
        this.value = value;
    }

    public boolean move(Square fin) {
        Piece occup = fin.getPiece();

        if (occup != null) {
            if (occup.getColor() == this.color) return false;
            else fin.capture(this);
        }

        currentSquare.removePiece();
        this.currentSquare = fin;
        currentSquare.put(this);
        this.hasMoved = true;
        return true;
    }

    public boolean moveTest(Square s2) {
        Piece occup = s2.getPiece();

        if (occup != null) {
            if (occup.getColor() == this.color) return false;
            else s2.captureTest(this);
        }

        currentSquare.removePiece();
        this.currentSquare = s2;
        currentSquare.put(this);
        this.hasMoved = true;
        return true;
    }

    public Square getSquare() {
        return currentSquare;
    }

    public void setPosition(Square sq) {
        this.currentSquare = sq;
    }

    public int getColor() {
        return this.color;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public Boolean getHasMoved() {
        return this.hasMoved;
    }

    public void setHasMoved(Boolean moved) {
        this.hasMoved = moved;
    }

    public int[] getVerticalPcDistances(Square[][] board, int x, int y) {
        int yBelow = 0;
        int xLeft = 0;
        int yAbove = 7;
        int xRight = 7;

        //closest piece above
        for (int i = 7; i > y; i--) {
            if (board[i][x].isOccupied()) {
                if (board[i][x].getPiece().getColor() != this.color) {
                    yAbove = i;
                }
                else
                    yAbove = i - 1;
            }
        }
        //closest piece below
        for (int i = 0; i < y; i++) {
            if (board[i][x].isOccupied()) {
                // System.out.println("isOccupied: i=" + i);
                if (board[i][x].getPiece().getColor() != this.color) {
                    yBelow = i;
                }
                else
                    yBelow = i + 1;
            }
        }
        //closest piece to left
        for (int i = 0; i < x; i++) {
            if (board[y][i].isOccupied()) {
                if (board[y][i].getPiece().getColor() != this.color) {
                    xLeft = i;
                }
                else
                    xLeft = i + 1;
            }
        }
        //closest piece to right
        for (int i = 7; i > x; i--) {
            if (board[y][i].isOccupied()) {
                if (board[y][i].getPiece().getColor() != this.color) {
                    xRight = i;
                }
                else
                    xRight = i - 1;
            }
        }

        int[] vertDistances = {yAbove, yBelow, xLeft, xRight,};

        return vertDistances;
    }

    public List<Square> getDiagonalPcDistances(Square[][] board, int x, int y) {
        LinkedList<Square> diagDistances = new LinkedList<Square>();

        int xUL = x - 1;
        int xDL = x - 1;
        int xUR = x + 1;
        int xDR = x + 1;
        int yUL = y + 1;
        int yDL = y - 1;
        int yUR = y + 1;
        int yDR = y - 1;

        while (xUL >= 0 && yUL <= 7) {
            if (board[yUL][xUL].isOccupied()) {
                if (board[yUL][xUL].getPiece().getColor() == this.color) {
                    break;
                } else {
                    diagDistances.add(board[yUL][xUL]);
                    break;
                }
            } else {
                diagDistances.add(board[yUL][xUL]);
                xUL--;
                yUL++;
            }
            // System.out.println("UL");
        }

        while (xDL >= 0 && yDL >= 0) {
            if (board[yDL][xDL].isOccupied()) {
                if (board[yDL][xDL].getPiece().getColor() == this.color) {
                    break;
                } else {
                    diagDistances.add(board[yDL][xDL]);
                    break;
                }
            } else {
                diagDistances.add(board[yDL][xDL]);
                xDL--;
                yDL--;
            }
            // System.out.println("DL");
        }

        while (xDR <= 7 && yDR >= 0) {
            if (board[yDR][xDR].isOccupied()) {
                if (board[yDR][xDR].getPiece().getColor() == this.color) {
                    break;
                } else {
                    diagDistances.add(board[yDR][xDR]);
                    break;
                }
            } else {
                diagDistances.add(board[yDR][xDR]);
                xDR++;
                yDR--;
            }
            // System.out.println("DR");
        }

        while (xUR <= 7 && yUR <= 7) {
            if (board[yUR][xUR].isOccupied()) {
                if (board[yUR][xUR].getPiece().getColor() == this.color) {
                    break;
                } else {
                    diagDistances.add(board[yUR][xUR]);
                    break;
                }
            } else {
                diagDistances.add(board[yUR][xUR]);
                xUR++;
                yUR++;
            }
            // System.out.println("UR");
        }
        // System.out.printf("\n"+this.name+" "+this.currentSquare.getRank()+" "+this.currentSquare.getFile()+"\n");
        // System.out.printf("diag= ");
        // for (Square s : diagDistances)
        //     System.out.printf("(%d,%d) ", s.getRank(), s.getFile());
        // System.out.printf("\n");

        return diagDistances;
    }

    public List<Square> getCastlingRights(Square[][] board, int c) {
        LinkedList<Square> castleMoves = new LinkedList<Square>();

        if (!this.getHasMoved()) {
             if (c == 0) {
                  // System.out.println("true c==0:");
                  if (board[0][7].isOccupied() && board[0][7].getPieceName().equals("rook") && board[0][7].getPieceColor() == 0) {
                    // System.out.println("true c==0:2");
                      Piece r1 = board[0][7].getPiece();
                      if (!r1.getHasMoved()) {
                          // System.out.println("true c==0:3");
                          if (!board[0][6].isOccupied() && !board[0][5].isOccupied()) {
                              castleMoves.add(board[0][6]);
                              // wCastleKS = true;
                          }
                      }
                  }
                  if (board[0][0].isOccupied() && board[0][0].getPieceName().equals("rook") && board[0][0].getPieceColor() == 0) {
                      Piece r2 = board[0][0].getPiece();
                      if (!r2.getHasMoved()) {
                          if (!board[0][1].isOccupied() && !board[0][2].isOccupied() && !board[0][3].isOccupied()) {
                              castleMoves.add(board[0][2]);
                              // wCastleQS = true;
                          }
                      }
                  }
              }
              // if (r1.getName().equals("rook"))
              if (c == 1) {
                // System.out.println("true c==1:");
                 if (board[7][7].isOccupied() && board[7][7].getPieceName().equals("rook") && board[7][7].getPieceColor() == 1) {
                   // System.out.println("true c==1:  1");
                     Piece r1 = board[7][7].getPiece();
                     if (!r1.getHasMoved()) {
                     // System.out.println("true c==1:  2");
                         if (!board[7][6].isOccupied() && !board[7][5].isOccupied()) {
                             castleMoves.add(board[7][6]);
                             // bCastleKS = true;
                         }
                     }
                 }
                 if (board[7][0].isOccupied() && board[7][0].getPieceName().equals("rook") && board[7][0].getPieceColor() == 1) {
                   // System.out.println("true c==1:  3");
                     Piece r2 = board[7][0].getPiece();
                     if (!r2.getHasMoved()) {
                     // System.out.println("true c==1:  4");
                         if (!board[7][1].isOccupied() && !board[7][2].isOccupied() && !board[7][3].isOccupied()) {
                             castleMoves.add(board[7][2]);
                             // bCastleQS = true;
                          }
                     }
                 }
                }
            }

        return castleMoves;

    }

    // implemented by each piece subclass
    public abstract List<Square> getMoves(Board b);
}
