package Chess;
import java.util.*;
import Chess.*;

public class Check {
    private Board b;
    private List<Piece> wPieces;
    private List<Piece> bPieces;
    private LinkedList<Square> legalMoves;
    private final LinkedList<Square> squares;
    private King bk;
    private King wk;
    private HashMap<Square,List<Piece>> wMoves;
    private HashMap<Square,List<Piece>> bMoves;
    private HashMap<Square,List<Piece>> wAttacks;
    private HashMap<Square,List<Piece>> bAttacks;

    public Check(Board b, List<Piece> wPieces, List<Piece> bPieces, King wk, King bk) {
        this.b = b;
        this.wPieces = wPieces;
        this.bPieces = bPieces;
        this.bk = bk;
        this.wk = wk;

        // Initialize other fields
        squares = new LinkedList<Square>();
        legalMoves = new LinkedList<Square>();
        wMoves = new HashMap<Square,List<Piece>>();
        bMoves = new HashMap<Square,List<Piece>>();
        wAttacks = new HashMap<Square,List<Piece>>();
        bAttacks = new HashMap<Square,List<Piece>>();

        Square[][] board = b.getSquareArray();

        // add all squares to squares list and as hashmap keys
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                squares.add(board[y][x]);
                wMoves.put(board[y][x], new LinkedList<Piece>());
                bMoves.put(board[y][x], new LinkedList<Piece>());
                wAttacks.put(board[y][x], new LinkedList<Piece>());
                bAttacks.put(board[y][x], new LinkedList<Piece>());
            }
        }
        getWhiteMoves();
        getBlackMoves();
        getWhiteAttacks();
        getBlackAttacks();
        //printMap(wMoves);
        printMap(wAttacks, true);
        printMap(bAttacks, false);

        update();
    }

    public void getWhiteMoves() {
        for (Piece p : wPieces) {
            List<Square> pMoves = p.getMoves(b);
            if (pMoves.isEmpty()) continue;
            for (Square s : pMoves) {
                // if (!wMoves.get(s).contains(p))
                // if (p.getName().equals("king")) System.out.println("**king" + s.getRank()+" "+s.getFile());
                wMoves.get(s).add(p);
            }
        }
    }

    public void getBlackMoves() {
        for (Piece p : bPieces) {
            List<Square> pMoves = p.getMoves(b);
            for (Square s : pMoves) {
                bMoves.get(s).add(p);
            }
        }
    }

    public void getWhiteAttacks() {
        for (Square s : wMoves.keySet()) {
            for (Piece p : wMoves.get(s)) {
                if (s.isOccupied()) {
                    wAttacks.get(s).add(p);
                }
            }
        }
    }

    public void getBlackAttacks() {
        for (Square s : bMoves.keySet()) {
            for (Piece p : bMoves.get(s)) {
                if (s.isOccupied()) {
                    bAttacks.get(s).add(p);
                }
            }
        }
    }

    public boolean whiteInCheck() {
        update();
        Square s = wk.getSquare();
        if (bMoves.get(s).isEmpty()) {
            legalMoves.addAll(wMoves.keySet());
            return false;
        } else return true;
    }

    public boolean blackInCheck() {
        update();
        Square s = bk.getSquare();
        if (wMoves.get(s).isEmpty()) {
            legalMoves.addAll(bMoves.keySet());
            return false;
        } else return true;
    }

    public List<Square> getLegalMoves(boolean b) {
        legalMoves.removeAll(legalMoves);
        if (whiteInCheck()) {
            System.out.println("White's King is in check!");
            whiteCheckMated();
        } else if (blackInCheck()) {
            //legalMoves.clear();
            System.out.println("Black's King is in check!");
            printList(legalMoves);
            blackCheckMated();
            printList(legalMoves);
        }
        return legalMoves;
    }

    public boolean whiteCheckMated() {
        // Check if white is in check
        if (!this.whiteInCheck()) return false;

        // // If yes, check if king can evade
        // if (canEvade(bMoves, wk)) return false;

        // // If no, check if threat can be captured
        // List<Piece> threats = bMoves.get(wk.getSquare());
        // if (canCapture(wMoves, threats, wk)) return false;

        // // If no, check if threat can be blocked
        // if (canBlock(threats, wMoves, wk)) return false;

        // If no possible ways of removing check, checkmate occurred
        return false;
    }

    public boolean blackCheckMated() {
        if (!this.blackInCheck())
            return false;

        boolean checkmate = true;
        List<Piece> attacksOnKing = wMoves.get(bk.getSquare());

        if (canEvade(wMoves, bk)) checkmate = false;

        if (canCapture(bMoves, attacksOnKing, bk)) checkmate = false;

        if (canBlock(attacksOnKing, bMoves, bk)) checkmate = false;

        // If no possible ways of removing check, checkmate occurred
        return checkmate;
    }

    public boolean testMove(Piece p, Square fin) {
        Piece p2 = fin.getPiece();
        boolean success = true;
        Square curr = p.getSquare();

        p.move(fin);
        update();

        if (p.getColor() == 0 && whiteInCheck()) success = false;
        else if (p.getColor() == 1 && blackInCheck()) success = false;

        p.move(curr);
        if (p2 != null) fin.put(p2);

        update();

        legalMoves.addAll(squares);
        return success;
    }

    private boolean canEvade(Map<Square,List<Piece>> tMoves, King tKing) {
        boolean evade = false;
        List<Square> kingsMoves = tKing.getMoves(b);
        Iterator<Square> iterator = kingsMoves.iterator();

        // If king is not threatened at some square, it can evade
        while (iterator.hasNext()) {
            Square sq = iterator.next();
            if (!testMove(tKing, sq)) continue;
            if (tMoves.get(sq).isEmpty()) {
                legalMoves.add(sq);
                evade = true;
            }
        }

        return evade;
    }

    private boolean canCapture(Map<Square,List<Piece>> bMoves, List<Piece> attacksOnKing, King k) {
        boolean capture = false;

        if (attacksOnKing.size() == 1) {
            Square sq = attacksOnKing.get(0).getSquare();

            if (k.getMoves(b).contains(sq)) {
                legalMoves.add(sq);
                if (testMove(k, sq)) {
                    capture = true;
                }
                capture = true;
            }

            List<Piece> caps = bMoves.get(sq);
            LinkedList<Piece> capturers = new LinkedList<Piece>();
            capturers.addAll(caps);

            if (!capturers.isEmpty()) {
                legalMoves.add(sq);
                for (Piece p : capturers) {
                    if (testMove(p, sq)) {
                        capture = true;
                    }
                }
            }
        }

        return capture;
    }

    private boolean canCapture2(Map<Square,List<Piece>> poss, List<Piece> threats, King k) {

        boolean capture = false;
        if (threats.size() == 1) {
            Square sq = threats.get(0).getSquare();

            if (k.getMoves(b).contains(sq)) {
                legalMoves.add(sq);
                if (testMove(k, sq)) {
                    capture = true;
                }
            }

            List<Piece> caps = poss.get(sq);
            LinkedList<Piece> capturers = new LinkedList<Piece>();
            capturers.addAll(caps);

            if (!capturers.isEmpty()) {
                legalMoves.add(sq);
                for (Piece p : capturers) {
                    if (testMove(p, sq)) {
                        capture = true;
                    }
                }
            }
        }

        return capture;
    }
    public void printMap(HashMap<Square,List<Piece>> map, boolean color) {
        System.out.println("Created HashMap: " + ((color) ? "WHITE" : "BLACK"));
        Square[][] board = b.getSquareArray();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (map.get(board[x][y]).isEmpty())
                    continue;
                System.out.printf("Square: "+board[x][y].getRank() + " " + board[x][y].getFile() + "\tPiece(s): ");
                for (Piece p : map.get(board[x][y])) {
                    System.out.printf(p.getName()+" ");
                }
                System.out.printf("\n");
            }
        }
        //
        // Iterator<HashMap.Entry<Square, List<Piece>>> iter = map.entrySet().iterator();
        // while (iter.hasNext()) {
        //   HashMap.Entry<Square, List<Piece>> entry = iter.next();
        //   if (entry.getValue().isEmpty())
        //       continue;
        //   System.out.printf("Square: "+entry.getKey().getRank() + " " + entry.getKey().getFile() + "\tPiece(s): ");
        //
        //   for (Piece p : entry.getValue()) {
        //       System.out.printf(p.getName()+" ");
        //   }
        //   System.out.printf("\n");
        // }
    }

    public void printList(LinkedList<Square> legalMoves) {
        System.out.println("legalMoves:");
        for (Square s : legalMoves) {
            System.out.println("Square: " + s.getRank() + " " + s.getFile());
        }
    }
    // /*
    //  * Helper method to determine if check can be blocked by a piece.
    //  */
    private boolean canBlock(List<Piece> threats, Map <Square,List<Piece>> blockMoves, King k) {
        boolean blockable = false;

        // if (threats.size() == 1) {
        //     Square ts = threats.get(0).getSquare();
        //     Square ks = k.getSquare();
        //     Square[][] brdArray = b.getSquareArray();
        //
        //     if (ks.getFile() == ts.getFile()) {
        //         int max = Math.max(ks.getRank(), ts.getRank());
        //         int min = Math.min(ks.getRank(), ts.getRank());
        //
        //         for (int i = min + 1; i < max; i++) {
        //             List<Piece> blks = blockMoves.get(brdArray[i][ks.getFile()]);
        //             ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
        //             blockers.addAll(blks);
        //
        //             if (!blockers.isEmpty()) {
        //                 legalMoves.add(brdArray[i][ks.getFile()]);
        //
        //                 for (Piece p : blockers) {
        //                     if (testMove(p,brdArray[i][ks.getFile()])) {
        //                         blockable = true;
        //                     }
        //                 }
        //
        //             }
        //         }
        //     }
        //
        //     if (ks.getRank() == ts.getRank()) {
        //         int max = Math.max(ks.getFile(), ts.getFile());
        //         int min = Math.min(ks.getFile(), ts.getFile());
        //
        //         for (int i = min + 1; i < max; i++) {
        //             List<Piece> blks = blockMoves.get(brdArray[ks.getRank()][i]);
        //             ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
        //             blockers.addAll(blks);
        //
        //             if (!blockers.isEmpty()) {
        //
        //                 legalMoves.add(brdArray[ks.getRank()][i]);
        //
        //                 for (Piece p : blockers) {
        //                     if (testMove(p, brdArray[ks.getRank()][i])) {
        //                         blockable = true;
        //                     }
        //                 }
        //
        //             }
        //         }
        //     }
        //
        //     Class<? extends Piece> tC = threats.get(0).getClass();
        //
        //     if (tC.equals(Queen.class) || tC.equals(Bishop.class)) {
        //         int kX = ks.getFile();
        //         int kY = ks.getRank();
        //         int tX = ts.getFile();
        //         int tY = ts.getRank();
        //
        //         if (kX > tX && kY > tY) {
        //             for (int i = tX + 1; i < kX; i++) {
        //                 tY++;
        //                 List<Piece> blks = blockMoves.get(brdArray[tY][i]);
        //                 ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
        //                 blockers.addAll(blks);
        //
        //                 if (!blockers.isEmpty()) {
        //                     legalMoves.add(brdArray[tY][i]);
        //
        //                     for (Piece p : blockers) {
        //                         if (testMove(p, brdArray[tY][i])) {
        //                             blockable = true;
        //                         }
        //                     }
        //                 }
        //             }
        //         }
        //
        //         if (kX > tX && tY > kY) {
        //             for (int i = tX + 1; i < kX; i++) {
        //                 tY--;
        //                 List<Piece> blks = blockMoves.get(brdArray[tY][i]);
        //                 ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
        //                 blockers.addAll(blks);
        //
        //                 if (!blockers.isEmpty()) {
        //                     legalMoves.add(brdArray[tY][i]);
        //
        //                     for (Piece p : blockers) {
        //                         if (testMove(p, brdArray[tY][i])) {
        //                             blockable = true;
        //                         }
        //                     }
        //                 }
        //             }
        //         }
        //
        //         if (tX > kX && kY > tY) {
        //             for (int i = tX - 1; i > kX; i--) {
        //                 tY++;
        //                 List<Piece> blks = blockMoves.get(brdArray[tY][i]);
        //                 ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
        //                 blockers.addAll(blks);
        //
        //                 if (!blockers.isEmpty()) {
        //                     legalMoves.add(brdArray[tY][i]);
        //
        //                     for (Piece p : blockers) {
        //                         if (testMove(p, brdArray[tY][i])) {
        //                             blockable = true;
        //                         }
        //                     }
        //                 }
        //             }
        //         }
        //
        //         if (tX > kX && tY > kY) {
        //             for (int i = tX - 1; i > kX; i--) {
        //                 tY--;
        //                 List<Piece> blks = blockMoves.get(brdArray[tY][i]);
        //                 ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
        //                 blockers.addAll(blks);
        //
        //                 if (!blockers.isEmpty()) {
        //                     legalMoves.add(brdArray[tY][i]);
        //
        //                     for (Piece p : blockers) {
        //                         if (testMove(p, brdArray[tY][i])) {
        //                             blockable = true;
        //                         }
        //                     }
        //                 }
        //             }
        //         }
        //     }
        // }

        return blockable;
    }

    /**
     * Updates the object with the current situation of the game.
     */
    public void update() {
        // Iterators through pieces
        Iterator<Piece> wIter = wPieces.iterator();
        Iterator<Piece> bIter = bPieces.iterator();

        // empty moves and movable squares at each update
        for (List<Piece> pieces : wMoves.values()) {
            pieces.removeAll(pieces);
        }

        for (List<Piece> pieces : bMoves.values()) {
            pieces.removeAll(pieces);
        }

        legalMoves.removeAll(legalMoves);

        // Add each move white and black can make to map
        while (wIter.hasNext()) {
            Piece p = wIter.next();

            if (!p.getClass().equals(King.class)) {
                if (p.getSquare() == null) {
                    wIter.remove();
                    continue;
                }

                List<Square> mvs = p.getMoves(b);
                Iterator<Square> iter = mvs.iterator();
                while (iter.hasNext()) {
                    List<Piece> pieces = wMoves.get(iter.next());
                    pieces.add(p);
                }
            }
        }

        while (bIter.hasNext()) {
            Piece p = bIter.next();

            if (!p.getClass().equals(King.class)) {
                if (p.getSquare() == null) {
                    wIter.remove();
                    continue;
                }

                List<Square> mvs = p.getMoves(b);
                Iterator<Square> iter = mvs.iterator();
                while (iter.hasNext()) {
                    List<Piece> pieces = bMoves.get(iter.next());
                    pieces.add(p);
                }
            }
        }
    }

}
