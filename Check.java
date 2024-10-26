package Chess;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import Chess.*;

public class Check {
    private Board b;
    private List<Piece> wPieces;
    private List<Piece> bPieces;
    private LinkedList<Square> legalSquares;
    private LinkedList<Move> whiteMoves;
    private LinkedList<Move> blackMoves;
    private LinkedList<Move> legalMoves;
    private King bk;
    private King wk;
    private HashMap<Square,List<Piece>> wMoves;
    private HashMap<Square,List<Piece>> bMoves;

    public Check(Board b, LinkedList<Piece> wPieces, LinkedList<Piece> bPieces, King wk, King bk) {
        this.b = b;
        this.wPieces = wPieces;
        this.bPieces = bPieces;
        this.bk = bk;
        this.wk = wk;

        // Initialize other fields
        legalSquares = new LinkedList<Square>();
        whiteMoves = new LinkedList<Move>();
        blackMoves = new LinkedList<Move>();
        legalMoves = new LinkedList<Move>();
        wMoves = new HashMap<Square,List<Piece>>();
        bMoves = new HashMap<Square,List<Piece>>();

        Square[][] board = b.getSquareArray();

        // add all squares to squares list and as hashmap keys
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                wMoves.put(board[y][x], new LinkedList<Piece>());
                bMoves.put(board[y][x], new LinkedList<Piece>());
            }
        }

        getWhiteMoves();
        getBlackMoves();
        checkPawns();

    }

    public void checkPawns() {
        Square[][] board = b.getSquareArray();
        for (int x = 0; x < 8; x++) {
            //set black pawns hasMoved
            if (board[6][x].isOccupied()) {
                if(board[6][x].getPieceColor() == 1 && board[6][x].getPieceName().equals("pawn")) {
                    board[6][x].getPiece().setHasMoved(false);
                }
            }
            //set white pawns hasMoved
            if (board[1][x].isOccupied()) {
                if(board[1][x].getPieceColor() == 0 && board[1][x].getPieceName().equals("pawn")) {
                    board[1][x].getPiece().setHasMoved(false);
                }
            }
        }
        if (board[0][4].isOccupied()) {
            if (board[0][4].getPiece().getName().equals("king"))
                board[0][4].getPiece().setHasMoved(false);
        }
        if (board[0][7].isOccupied()) {
            if (board[0][7].getPiece().getName().equals("rook"))
                board[0][7].getPiece().setHasMoved(false);
        }
        if (board[0][0].isOccupied()) {
            if (board[0][0].getPiece().getName().equals("rook"))
                board[0][0].getPiece().setHasMoved(false);
        }
        if (board[7][4].isOccupied()) {
            if (board[7][4].getPiece().getName().equals("king"))
                board[7][4].getPiece().setHasMoved(false);
        }
        if (board[7][0].isOccupied()) {
            if (board[7][0].getPiece().getName().equals("rook"))
                board[7][0].getPiece().setHasMoved(false);
        }
        if (board[7][7].isOccupied()) {
            if (board[7][7].getPiece().getName().equals("rook"))
                board[7][7].getPiece().setHasMoved(false);
        }

    }

    public void getWhiteMoves() {
        Iterator<Piece> wIter = wPieces.iterator();

        // empty moves list
        for (List<Piece> pieces : wMoves.values()) {
            pieces.removeAll(pieces);
        }
        if (!whiteMoves.isEmpty()) whiteMoves.clear();
        // Add white moves to map
        while (wIter.hasNext()) {
            Piece p = wIter.next();
            if (!p.getName().equals("king")) {
                Square s1 = p.getSquare();
                if (s1 == null) {
                    wIter.remove();
                    continue;
                }

                List<Square> moves = p.getMoves(b);
                Iterator<Square> iter = moves.iterator();
                while (iter.hasNext()) {
                    Square s2 = iter.next();
                    Move m = new Move(s1, s2);
                    whiteMoves.add(m);
                    List<Piece> pieces = wMoves.get(s2);
                    pieces.add(p);
                }
            }
        }
    }

    public void getBlackMoves() {
        Iterator<Piece> bIter = bPieces.iterator();
        for (List<Piece> pieces : bMoves.values()) {
            pieces.removeAll(pieces);
        }
        if (!blackMoves.isEmpty()) blackMoves.clear();
        while (bIter.hasNext()) {
            Piece p = bIter.next();
            if (!p.getName().equals("king")) {
                Square s1 = p.getSquare();
                if (s1 == null) {
                    bIter.remove();
                    continue;
                }
                List<Square> moves = p.getMoves(b);
                Iterator<Square> iter = moves.iterator();
                while (iter.hasNext()) {
                    Square s2 = iter.next();
                    Move m = new Move(s1, s2);
                    blackMoves.add(m);
                    List<Piece> pieces = bMoves.get(s2);
                    pieces.add(p);
                }
            }
        }
    }

    public boolean whiteInCheck() {
        getBlackMoves();
        getWhiteMoves();
        Square s = wk.getSquare();
        if (bMoves.get(s).isEmpty()) {
            return false;
        } else return true;
    }

    public boolean blackInCheck() {
        getBlackMoves();
        getWhiteMoves();
        Square s = bk.getSquare();
        if (wMoves.get(s).isEmpty()) {
            return false;
        } else {return true;}
    }

    //get legal squares that can be moved to
    public List<Square> getLegalSquares(boolean b) {
        if (b) legalSquares.addAll(wMoves.keySet());
        else legalSquares.addAll(bMoves.keySet());

        if (whiteInCheck()) {
            whiteCheckMated();
        } else if (blackInCheck()) {
            blackCheckMated();
        }
        return legalSquares;
    }

    //get confirmed legal moves
    public List<Move> getLegalMoves(boolean b) {
        if (b) {
          legalMoves.addAll(whiteMoves);
          getCastlingMoves(bMoves, wk, true);
        }
        else {
          legalMoves.addAll(blackMoves);
          getCastlingMoves(wMoves, bk, false);
        }

        //if whites in check, check for mate
        if (whiteInCheck()) {
            legalMoves.clear();
            if (whiteCheckMated()) this.b.wCM(true);
        }
        //if blacks in check, check for mate
        else if (blackInCheck()) {
            legalMoves.clear();
            if (blackCheckMated()) this.b.bCM(true);
        }

        return legalMoves;
    }

    public boolean whiteCheckMated() {
        boolean checkmate = true;
        // Check if white is in check
        if (!this.whiteInCheck()) return false;

        // see if white king can evade the attack
        if (canEscapeCheck(bMoves, wk)) checkmate = false;

        // see if white king or other white pcs can capture attacker
        List<Piece> attackers = bMoves.get(wk.getSquare());
        if (canCaptureAttacker(wMoves, attackers, wk)) checkmate = false;

        // see if any pieces can block the attack
        if (canBlockCheck(attackers, wMoves, wk)) checkmate = false;

        // If king cant escape check we have a checkmate
        return checkmate;
    }

    public boolean blackCheckMated() {
        if (!this.blackInCheck())
            return false;

        boolean checkmate = true;
        List<Piece> attacksOnKing = wMoves.get(bk.getSquare());

        if (canEscapeCheck(wMoves, bk)) checkmate = false;
        if (canCaptureAttacker(bMoves, attacksOnKing, bk)) checkmate = false;
        if (canBlockCheck(attacksOnKing, bMoves, bk)) checkmate = false;
        return checkmate;
    }

    //tests for the checkmate "can's"
    public boolean testBlockMove(Piece p, Square s2) {
        Piece p2 = s2.getPiece();
        boolean success = true;
        Square curr = p.getSquare();

        p.moveTest(s2);

        getBlackMoves();
        getWhiteMoves();

        if (p.getColor() == 0 && whiteInCheck()) success = false;
        if (p.getColor() == 1 && blackInCheck()) success = false;

        p.moveTest(curr);

        if (p2 != null) s2.put(p2);

        getBlackMoves();
        getWhiteMoves();

        return success;
    }

    public void getCastlingMoves(HashMap<Square,List<Piece>> oppMoves, King k, boolean c) {

        if (k.getHasMoved()) return;

        List<Square> kingsMoves = k.getMoves(b);
        Square[][] brd = b.getSquareArray();

        for (Square s : kingsMoves) {
          // white castling moves
            if (c) {
                if (s.getRank() == 0 && s.getFile() == 2) {
                    if (oppMoves.get(brd[0][2]).isEmpty() && oppMoves.get(brd[0][3]).isEmpty()) {
                        Move m = new Move(k.getSquare(), brd[0][2]);
                        legalMoves.add(m);
                    }
                }
                if (s.getRank() == 0 && s.getFile() == 5) {
                    if (oppMoves.get(brd[0][5]).isEmpty() && oppMoves.get(brd[0][6]).isEmpty()) {
                        Move m = new Move(k.getSquare(), brd[0][6]);
                        legalMoves.add(m);
                    }
                }
            }
            // black castling moves
            if (!c) {
                if (s.getRank() == 7 && s.getFile() == 2) {
                    if (oppMoves.get(brd[7][2]).isEmpty() && oppMoves.get(brd[7][3]).isEmpty()) {

                        Move m = new Move(k.getSquare(), brd[7][2]);
                        legalMoves.add(m);
                    }
                }
                if (s.getRank() == 7 && s.getFile() == 5) {

                    if (oppMoves.get(brd[7][5]).isEmpty() && oppMoves.get(brd[7][6]).isEmpty()) {

                        Move m = new Move(k.getSquare(), brd[7][6]);
                        legalMoves.add(m);
                    }
                }
            }
            else {
                Move m = new Move(k.getSquare(), s);
                legalMoves.add(m);
            }
        }
    }

    //see if king can move to a square that is free of check
    private boolean canEscapeCheck(HashMap<Square,List<Piece>> oppMoves, King k) {
        boolean evade = false;

        List<Square> kingsSquares = k.getMoves(b);
        Iterator<Square> iterator = kingsSquares.iterator();

        //Loop through kings potential moves

        while (iterator.hasNext()) {
            Square s = iterator.next();
            //Test if move results in check
            if (testBlockMove(k, s)) {
                if (oppMoves.get(s).isEmpty()) {
                    Move m = new Move(k.getSquare(), s);
                    legalMoves.add(m);
                    evade = true;
                }
            }
        }

        return evade;
    }

    //see if king or other pc can capture checking pc
    private boolean canCaptureAttacker(HashMap<Square,List<Piece>> movesMap, List<Piece> attackers, King k) {

        boolean capture = false;

        //if attackers > 1 we cannot capture both pieces
        if (attackers.size() == 1) {
            Square atckrsq = attackers.get(0).getSquare();

            //If king can capture piece; check if attackers square exists in kings moves
            if (k.getMoves(b).contains(atckrsq)) {
                //Test that move doesn't result in check

                if (testBlockMove(k, atckrsq)) {
                    //add move to legalMoves
                    Move m = new Move(k.getSquare(), atckrsq);
                    legalMoves.add(m);
                    capture = true;
                }
            }
            //list of pieces that can capture attacker
            List<Piece> capturers1 = movesMap.get(atckrsq);
            ConcurrentLinkedDeque<Piece> capturers = new ConcurrentLinkedDeque<Piece>();
            capturers.addAll(capturers1);

            if (!capturers.isEmpty()) {

                for (Piece p : capturers) {

                    Move m = new Move(p.getSquare(), atckrsq);
                    legalMoves.add(m);
                    if (testBlockMove(p, atckrsq)) {

                        capture = true;
                    }
                }
            }
        }

        return capture;
    }

    private boolean canBlockCheck(List<Piece> attackers, HashMap <Square,List<Piece>> movesMap, King k) {
        boolean block = false;

        //if attackers > 1 we cannot block the attack
        if (attackers.size() == 1) {
            //get square of attacker, king, and the board array
            Square atckrsq = attackers.get(0).getSquare();
            Square kingsq = k.getSquare();
            Square[][] brd = b.getSquareArray();

            //if attacker is a rook or queen (vertical attack)
            if (kingsq.getFile() == atckrsq.getFile()) {

                //get highest & lowest rank values of king and attacker
                int max = Math.max(kingsq.getRank(), atckrsq.getRank());
                int min = Math.min(kingsq.getRank(), atckrsq.getRank());

                //loop through each square between king and attacker
                for (int i = min + 1; i < max; i++) {
                    //get all pieces that can move to this square (and block attack)
                    List<Piece> blockers1 = movesMap.get(brd[i][kingsq.getFile()]);
                    ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                    blockers.addAll(blockers1);

                    if (blockers.isEmpty()) continue;

                    //loop through all potential blocking pieces
                    for (Piece p : blockers) {

                        //test that move doesn't result in check
                        if (testBlockMove(p,brd[i][kingsq.getFile()])) {

                            //add move to legalMoves
                            Move m = new Move(p.getSquare(), brd[i][kingsq.getFile()]);
                            legalMoves.add(m);
                            block = true;
                        }
                    }
                }
            }
            else if (!atckrsq.isOccupied()) return false;
            //if attacker is a rook or queen (horizontal attack)
            else if (kingsq.getRank() == atckrsq.getRank()) {
                //get highest & lowest rank values of king and attacker
                int max = Math.max(kingsq.getFile(), atckrsq.getFile());
                int min = Math.min(kingsq.getFile(), atckrsq.getFile());

                //loop through each square between king and attacker
                for (int j = min + 1; j < max; j++) {
                    //get all pieces that can move to this square (and block attack)
                    List<Piece> blockers1 = movesMap.get(brd[kingsq.getRank()][j]);
                    ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                    blockers.addAll(blockers1);
                    if (blockers.isEmpty()) continue;
                    //loop through all potential blocking pieces
                    for (Piece p : blockers) {
                        //test that move doesn't result in check
                        if (testBlockMove(p, brd[kingsq.getRank()][j])) {
                            //add move to legalMoves
                            Move m = new Move(p.getSquare(), brd[kingsq.getRank()][j]);
                            legalMoves.add(m);
                            block = true;
                        }
                    }
                }
            }
            else if (atckrsq.getPiece().getName().equals("bishop") || atckrsq.getPiece().getName().equals("queen")) {
                int kRank = kingsq.getRank();
                int kFile = kingsq.getFile();

                int aFile = atckrsq.getFile();
                int aRank = atckrsq.getRank();

                //southwest attack
                if (kFile > aFile && kRank > aRank) {
                    //loop through squares between king and attacker
                    for (int i = aFile + 1; i < kFile; i++) {
                        aRank++;
                        //get all pieces that can move to this square (and block attack)
                        List<Piece> blockers1 = movesMap.get(brd[aRank][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blockers1);

                        if (blockers.isEmpty()) continue;
                        //loop through all potential blocking pieces
                        for (Piece p : blockers) {
                            //test that move doesn't result in check
                            if (testBlockMove(p, brd[aRank][i])) {
                                //add move to legalMoves
                                Move m = new Move(p.getSquare(), brd[aRank][i]);
                                legalMoves.add(m);
                                block = true;
                            }
                        }
                    }
                }
                //northwest attack
                else if (kFile > aFile && kRank < aRank ) {
                    for (int i = aFile + 1; i < kFile; i++) {
                        aRank--;
                        //get all pieces that can move to this square (and block attack)
                        List<Piece> blockers1 = movesMap.get(brd[aRank][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blockers1);

                        if (blockers.isEmpty()) continue;
                        //loop through all potential blocking pieces
                        for (Piece p : blockers) {
                            //test that move doesn't result in check
                            if (testBlockMove(p, brd[aRank][i])) {
                                //add move to legalMoves
                                Move m = new Move(p.getSquare(), brd[aRank][i]);
                                legalMoves.add(m);
                                block = true;
                            }
                        }
                    }
                }
                //southeast attack
                else if (kFile < aFile && kRank > aRank) {
                    for (int i = aFile - 1; i > kFile; i--) {
                        aRank++;
                        //get all pieces that can move to this square (and block attack)
                        List<Piece> blockers1 = movesMap.get(brd[aRank][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blockers1);

                        if (blockers.isEmpty()) continue;
                        //loop through all potential blocking pieces
                        for (Piece p : blockers) {
                            //test that move doesn't result in check
                            if (testBlockMove(p, brd[aRank][i])) {
                                //add move to legalMoves
                                Move m = new Move(p.getSquare(), brd[aRank][i]);
                                legalMoves.add(m);
                                block = true;
                            }
                        }
                    }
                }
                //northeast attack
                else if (kFile < aFile && kRank < aRank) {
                    for (int i = aFile - 1; i > kFile; i--) {
                        aRank--;
                        //get all pieces that can move to this square (and block attack)
                        List<Piece> blockers1 = movesMap.get(brd[aRank][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blockers1);

                        if (blockers.isEmpty()) continue;
                        //loop through all potential blocking pieces
                        for (Piece p : blockers) {
                            //test that move doesn't result in check
                            if (testBlockMove(p, brd[aRank][i])) {
                                Move m = new Move(p.getSquare(), brd[aRank][i]);
                                legalMoves.add(m);
                                block = true;
                            }
                        }
                    }
                }
            }
        }
        // System.out.println("Block =" + block);
        return block;
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
    }

    public void printList(LinkedList<Square> legalSquares) {
        System.out.println("legalSquares:");
        for (Square s : legalSquares) {
            System.out.println("Square: " + s.getRank() + " " + s.getFile());
        }
    }
    public void printSquares(List<Square> legalSquares) {
        // System.out.println("legalSquares:");
        for (Square s : legalSquares) {
            System.out.println("Square: " + s.getRank() + " " + s.getFile());
        }
    }

    public void printMoves(List<Move> legalMoves) {
        System.out.println("legalMoves:");
        for (Move m : legalMoves) {
            m.printMove();
        }
    }

    public void printListp(List<Piece> pcs) {
        System.out.println("pieces:");
        for (Piece p : pcs) {
            System.out.println("Piece: " + p.getName() + " " + p.getSquare().getRank()+" "+p.getSquare().getFile());
        }
    }

}
